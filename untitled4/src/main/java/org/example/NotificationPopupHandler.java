package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NotificationPopupHandler {
    private WebDriver driver;
    private WebDriverWait wait;

    public NotificationPopupHandler(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(1)); // Initialize WebDriverWait
    }

    public void handleNotificationPopup() {
        // Wait for the notification pop-up to appear and click 'Block' or 'Allow' as needed
        try {
            // Use explicit wait to handle notification pop-up if it appears
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Block']")));
            driver.findElement(By.xpath("//button[text()='Block']")).click(); // Click 'Block' button
        } catch (Exception e) {
            System.out.println("No notification pop-up appeared.");
        }
    }
}
