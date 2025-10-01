import { FullConfig } from '@playwright/test';

async function globalTeardown(config: FullConfig) {
  console.log('Starting global teardown...');
  
  // Get server manager from global setup
  const serverManager = (global as any).serverManager;
  
  if (serverManager) {
    try {
      await serverManager.stopServer();
      console.log('Spring Boot server stopped successfully');
    } catch (error) {
      console.error('Error stopping server:', error);
    }
  }
  
  console.log('Global teardown completed');
}

export default globalTeardown;