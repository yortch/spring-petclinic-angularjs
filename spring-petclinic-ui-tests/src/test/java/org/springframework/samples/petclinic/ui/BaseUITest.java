package org.springframework.samples.petclinic.ui;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for all UI tests using Playwright.
 * Handles browser lifecycle and provides common test infrastructure.
 */
public abstract class BaseUITest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    // Base URL for the application - can be overridden via system property
    protected static final String BASE_URL = System.getProperty("app.url", "http://localhost:8080");

    @BeforeAll
    static void setupPlaywright() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(true) // Set to false for debugging
                .setSlowMo(0)); // Add delay between actions for debugging
    }

    @AfterAll
    static void teardownPlaywright() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @BeforeEach
    void setupTest() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1280, 720));
        page = context.newPage();
        
        // Enable request/response logging for debugging
        page.onRequest(request -> System.out.println("Request: " + request.method() + " " + request.url()));
        page.onResponse(response -> System.out.println("Response: " + response.status() + " " + response.url()));
    }

    @AfterEach
    void teardownTest() {
        if (context != null) {
            context.close();
        }
    }

    /**
     * Navigate to the application home page
     */
    protected void navigateToHomePage() {
        page.navigate(BASE_URL);
        page.waitForLoadState();
    }

    /**
     * Wait for Angular to finish loading (if needed)
     */
    protected void waitForAngular() {
        // Wait for Angular digest cycle to complete
        page.waitForFunction("() => window.angular && window.angular.getTestability(document.body).whenStable");
    }

    /**
     * Take a screenshot for debugging purposes
     */
    protected void takeScreenshot(String filename) {
        page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get("target/screenshots/" + filename + ".png")));
    }
}