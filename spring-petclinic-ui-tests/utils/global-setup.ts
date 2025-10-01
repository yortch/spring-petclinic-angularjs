import { chromium, FullConfig } from '@playwright/test';
const ServerManager = require('./start-server');

async function globalSetup(config: FullConfig) {
  console.log('Starting global setup...');
  
  const serverManager = new ServerManager();
  
  try {
    // Start the Spring Boot server
    await serverManager.startServer();
    console.log('Spring Boot server started successfully');
    
    // Store server manager instance for teardown
    (global as any).serverManager = serverManager;
    
    // Verify the application is accessible
    const browser = await chromium.launch();
    const page = await browser.newPage();
    
    try {
      await page.goto('http://localhost:8080', { waitUntil: 'networkidle' });
      console.log('Application is accessible');
    } catch (error) {
      console.error('Application not accessible:', error);
      throw error;
    } finally {
      await browser.close();
    }
    
  } catch (error) {
    console.error('Global setup failed:', error);
    // Clean up on failure
    if (serverManager) {
      await serverManager.stopServer();
    }
    throw error;
  }
}

export default globalSetup;