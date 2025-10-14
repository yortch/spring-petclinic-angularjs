#!/bin/bash

# Script to build and start the Spring PetClinic server for UI testing  
# This script ensures the application is built and running before tests execute

# Set script to exit on error
set -e

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Starting Spring PetClinic for UI testing...${NC}"

# Navigate to project root (two levels up from scripts directory)
cd "$(dirname "$0")/../.."

# Check if server is already running
if curl -s http://localhost:8080 > /dev/null 2>&1; then
    echo -e "${GREEN}Server is already running on port 8080${NC}"
    exit 0
fi

# Build the project if not already built
if [ ! -f "spring-petclinic-server/target/petclinic.jar" ]; then
    echo -e "${YELLOW}Building project...${NC}"
    ./mvnw clean install -DskipTests
fi

# Start the server in the background
echo -e "${YELLOW}Starting server...${NC}"
./mvnw spring-boot:run -pl spring-petclinic-server &
SERVER_PID=$!

# Save PID for cleanup
echo $SERVER_PID > spring-petclinic-ui-tests/.server.pid

# Wait for server to be ready
echo -e "${YELLOW}Waiting for server to start...${NC}"
for i in {1..60}; do
    if curl -s http://localhost:8080 > /dev/null 2>&1; then
        echo -e "${GREEN}Server is ready!${NC}"
        exit 0
    fi
    sleep 2
    echo -n "."
done

echo -e "\n${RED}Server failed to start within expected time${NC}"
exit 1
