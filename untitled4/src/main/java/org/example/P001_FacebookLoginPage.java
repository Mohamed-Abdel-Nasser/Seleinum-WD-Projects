package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class P001_FacebookLoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators for Login
    private By emailField = By.id("email");
    private By passwordField = By.id("pass");
    private By loginButton = By.name("login");
    private By togglePasswordVisibilityButton = By.id("togglePassword");

    public P001_FacebookLoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofMillis(500)); // Initialize WebDriverWait
    }

    public void enterEmail(String email) throws InterruptedException {
        waitForElement(emailField).sendKeys(email);
        Thread.sleep(500);
    }

    public void enterPassword(String password) throws InterruptedException {
        waitForElement(passwordField).sendKeys(password);
        Thread.sleep(500);
    }

    public void clickLoginButton() throws InterruptedException {
        waitForElement(loginButton).click();
        Thread.sleep(500);
    }

    private WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public String getErrorMessage() {
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'incorrect')]"))); // Adjust the locator
        return errorElement.getText();
    }

    public boolean isLoginSuccessful() {
        try {
            // Wait for the profile picture or any unique element visible after login
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@class='profile-pic']"))); // Adjust the locator
            return true; // Login is successful
        } catch (Exception e) {
            return false; // Login failed
        }
    }


    // Method to toggle the password visibility
    public void togglePasswordVisibility() {
        driver.findElement(togglePasswordVisibilityButton).click();
    }

    // Method to check if password is visible
    public boolean isPasswordVisible() {
        WebElement passwordInput = driver.findElement(passwordField);
        String passwordFieldType = passwordInput.getAttribute("type");
        return "text".equals(passwordFieldType); // Returns true if visible, false if hidden
    }


}
