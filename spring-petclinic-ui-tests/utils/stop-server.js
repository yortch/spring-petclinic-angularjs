const { exec } = require('child_process');
const http = require('http');

async function stopServer() {
  console.log('Stopping Spring Boot server...');
  
  try {
    // Try to find and kill Spring Boot processes
    const isWindows = process.platform === 'win32';
    
    if (isWindows) {
      // Windows: Find and kill Java processes running Spring Boot
      exec('tasklist /FI "IMAGENAME eq java.exe" /FO CSV', (error, stdout) => {
        if (!error && stdout.includes('java.exe')) {
          console.log('Found Java processes, attempting to stop...');
          exec('taskkill /F /IM java.exe', (killError) => {
            if (killError) {
              console.error('Error stopping server:', killError);
            } else {
              console.log('Server stopped successfully.');
            }
          });
        } else {
          console.log('No Spring Boot server processes found.');
        }
      });
    } else {
      // Unix-like systems: Find and kill processes on port 8080
      exec('lsof -ti:8080', (error, stdout) => {
        if (!error && stdout.trim()) {
          const pids = stdout.trim().split('\\n');
          pids.forEach(pid => {
            exec(`kill -TERM ${pid}`, (killError) => {
              if (killError) {
                console.error(`Error killing process ${pid}:`, killError);
              } else {
                console.log(`Stopped process ${pid}`);
              }
            });
          });
        } else {
          console.log('No processes found running on port 8080.');
        }
      });
    }
  } catch (error) {
    console.error('Error stopping server:', error);
  }
}

// If called directly, stop the server
if (require.main === module) {
  stopServer();
}

module.exports = { stopServer };