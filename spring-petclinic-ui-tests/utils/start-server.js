const { spawn, exec } = require('child_process');
const path = require('path');
const http = require('http');

class ServerManager {
  constructor() {
    this.serverProcess = null;
    this.serverPort = 8080;
    this.maxRetries = 30; // 30 seconds timeout
    this.retryInterval = 1000; // 1 second
  }

  async startServer() {
    console.log('Starting Spring Boot server...');
    
    // Change to server directory
    const serverDir = path.join(__dirname, '../../spring-petclinic-server');
    
    return new Promise((resolve, reject) => {
      // Use direct mvn command since wrapper has issues
      const command = 'mvn';
      const args = ['spring-boot:run', '-Dspring-boot.run.profiles=hsqldb,prod'];
      
      this.serverProcess = spawn(command, args, {
        cwd: serverDir,
        stdio: 'pipe',
        shell: true
      });

      this.serverProcess.stdout.on('data', (data) => {
        const output = data.toString();
        console.log(`Server: ${output}`);
        
        // Check if server has started successfully
        if (output.includes('Started PetClinicApplication') || 
            output.includes('Tomcat started on port(s): 8080')) {
          console.log('Server started successfully!');
          this.waitForServerReady().then(resolve).catch(reject);
        }
      });

      this.serverProcess.stderr.on('data', (data) => {
        console.error(`Server Error: ${data}`);
      });

      this.serverProcess.on('close', (code) => {
        console.log(`Server process exited with code ${code}`);
        if (code !== 0) {
          reject(new Error(`Server failed to start with exit code ${code}`));
        }
      });

      this.serverProcess.on('error', (error) => {
        console.error('Failed to start server:', error);
        reject(error);
      });

      // Timeout after 2 minutes
      setTimeout(() => {
        if (this.serverProcess && !this.serverProcess.killed) {
          reject(new Error('Server startup timeout'));
        }
      }, 120000);
    });
  }

  async waitForServerReady() {
    console.log('Waiting for server to be ready...');
    
    for (let i = 0; i < this.maxRetries; i++) {
      try {
        await this.checkServerHealth();
        console.log('Server is ready!');
        return;
      } catch (error) {
        console.log(`Attempt ${i + 1}/${this.maxRetries}: Server not ready yet...`);
        await new Promise(resolve => setTimeout(resolve, this.retryInterval));
      }
    }
    
    throw new Error('Server failed to become ready within timeout period');
  }

  checkServerHealth() {
    return new Promise((resolve, reject) => {
      const req = http.request({
        hostname: 'localhost',
        port: this.serverPort,
        path: '/',
        method: 'GET',
        timeout: 5000
      }, (res) => {
        if (res.statusCode === 200) {
          resolve();
        } else {
          reject(new Error(`Server returned status ${res.statusCode}`));
        }
      });

      req.on('error', reject);
      req.on('timeout', () => {
        req.destroy();
        reject(new Error('Health check timeout'));
      });
      
      req.end();
    });
  }

  stopServer() {
    return new Promise((resolve) => {
      if (this.serverProcess && !this.serverProcess.killed) {
        console.log('Stopping server...');
        
        let resolved = false;
        
        this.serverProcess.on('close', () => {
          if (!resolved) {
            console.log('Server stopped.');
            resolved = true;
            resolve();
          }
        });

        this.serverProcess.on('exit', () => {
          if (!resolved) {
            console.log('Server exited.');
            resolved = true;
            resolve();
          }
        });

        // For Windows/bash environment, try different termination methods
        try {
          if (process.platform === 'win32') {
            // On Windows, use taskkill to ensure process tree termination
            const { exec } = require('child_process');
            exec(`taskkill /pid ${this.serverProcess.pid} /T /F`, (error) => {
              if (error) {
                console.log('taskkill failed, trying SIGTERM...');
                try {
                  this.serverProcess.kill('SIGTERM');
                } catch (killError) {
                  console.log('SIGTERM failed, trying SIGKILL...');
                  this.serverProcess.kill('SIGKILL');
                }
              }
            });
          } else {
            // Send SIGTERM to gracefully shutdown
            this.serverProcess.kill('SIGTERM');
          }
        } catch (error) {
          console.log('Error during termination, trying force kill...', error);
          this.serverProcess.kill('SIGKILL');
        }
        
        // Force resolution after 15 seconds if process doesn't respond
        setTimeout(() => {
          if (!resolved) {
            console.log('Force resolving server stop after timeout...');
            resolved = true;
            resolve();
          }
        }, 15000);
      } else {
        resolve();
      }
    });
  }
}

module.exports = ServerManager;

// If called directly, start the server
if (require.main === module) {
  const serverManager = new ServerManager();
  
  serverManager.startServer()
    .then(() => {
      console.log('Server is running. Press Ctrl+C to stop.');
      
      // Handle graceful shutdown
      process.on('SIGINT', async () => {
        console.log('\\nShutting down...');
        await serverManager.stopServer();
        process.exit(0);
      });
    })
    .catch((error) => {
      console.error('Failed to start server:', error);
      process.exit(1);
    });
}