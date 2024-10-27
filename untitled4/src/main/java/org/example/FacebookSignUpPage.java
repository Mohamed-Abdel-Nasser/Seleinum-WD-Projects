package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FacebookSignUpPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators for Sign Up
    private By firstNameField = By.name("firstname");
    private By lastNameField = By.name("lastname");
    private By mobileEmailField = By.name("reg_email__");
    private By passwordField = By.id("password_step_input");
    private By dayDropdown = By.id("day");
    private By monthDropdown = By.id("month");
    private By yearDropdown = By.id("year");
    private By genderRadio = By.xpath("//input[@name='sex' and @value='2']");
    private By signUpButton = By.name("websubmit");

    public FacebookSignUpPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofMillis(500)); // Initialize WebDriverWait
    }

    public void enterFirstName(String firstName) throws InterruptedException {
        waitForElement(firstNameField).sendKeys(firstName);
        Thread.sleep(500);
    }

    public void enterLastName(String lastName) throws InterruptedException {
        waitForElement(lastNameField).sendKeys(lastName);
        Thread.sleep(500);
    }

    public void enterMobileEmail(String mobileEmail) throws InterruptedException {
        waitForElement(mobileEmailField).sendKeys(mobileEmail);
        Thread.sleep(500);
    }

    public void enterPassword(String password) throws InterruptedException {
        waitForElement(passwordField).sendKeys(password);
        Thread.sleep(500);
    }

    public void selectDay(String day) throws InterruptedException {
        new Select(waitForElement(dayDropdown)).selectByVisibleText(day);
        Thread.sleep(500);
    }

    public void selectMonth(String month) throws InterruptedException {
        new Select(waitForElement(monthDropdown)).selectByVisibleText(month);
        Thread.sleep(500);
    }

    public void selectYear(String year) throws InterruptedException {
        new Select(waitForElement(yearDropdown)).selectByVisibleText(year);
        Thread.sleep(500);
    }

    public void selectGender() throws InterruptedException {
        waitForElement(genderRadio).click();
        Thread.sleep(500);
    }

    public void clickSignUpButton() throws InterruptedException {
        waitForElement(signUpButton).click();
        Thread.sleep(500);
    }

    public boolean isSignUpButtonEnabled() {
        return waitForElement(signUpButton).isEnabled();
    }

    private WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
