import org.example.P001_FacebookLoginPage;
import org.example.FacebookSignUpPage;
import org.example.NotificationPopupHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class FacebookTestSuite {
    private WebDriver driver;
    private FacebookSignUpPage signUpPage;
    private P001_FacebookLoginPage loginPage;
    private NotificationPopupHandler notificationPopupHandler;

    @BeforeTest
    public void setup() {
        // Create a map to store preferences to switch off browser notifications
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);  // 1: Allow, 2: Block

        // Create an instance of ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);

        // Initialize ChromeDriver with options
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(300));
        driver.manage().window().maximize();

        // Initialize NotificationPopupHandler
        notificationPopupHandler = new NotificationPopupHandler(driver);
    }

    @Test(priority = 1)
    public void testSignUp() throws InterruptedException {
        driver.get("https://www.facebook.com/r.php");
        notificationPopupHandler.handleNotificationPopup();
        signUpPage = new FacebookSignUpPage(driver);
        signUpPage.enterFirstName("Mohamed");
        signUpPage.enterLastName("Nasser");
        signUpPage.selectDay("11");
        signUpPage.selectMonth("Mar");
        signUpPage.selectYear("1996");
        signUpPage.selectGender();
        signUpPage.enterMobileEmail("01018127745");
        signUpPage.enterPassword("123456789");
        signUpPage.clickSignUpButton();
    }

    @Test(priority = 2)
    public void testSignUpButtonEnabled() {
        driver.get("https://www.facebook.com/r.php");
        notificationPopupHandler.handleNotificationPopup();

        signUpPage = new FacebookSignUpPage(driver);
        Assert.assertTrue(signUpPage.isSignUpButtonEnabled(), "Sign Up button should be enabled.");
    }


    @DataProvider(name = "firstNameTestData")
    public Object[][] firstNameTestData() {
        return new Object[][]{
                {"Mohamed", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", true}, // Valid first name
                {"", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Empty first name
                {"@#$%", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Special characters
                {" ", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Blank space only
                {"Mohamed Nasser", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", true}, // First name with space
                {"Mohamed    Nasser", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Multiple spaces
                {"محمد", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", true}, // Non-English first name
                {"A".repeat(51), "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Extremely long first name
                {"Mohamed", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", true}, // Mixed case
                {"😊Mohamed", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Emoji
                {"Mohamed123", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Numbers
                {"#Mohamed", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Starting with special character
                {"Mohamed$", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Ending with special character
                {"123Mohamed", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Starting with numbers
                {"Mohamed😊", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Emoji characters
                {"A".repeat(100), "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Excessive length
                {" John ", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Leading or trailing spaces
                {"!@#$%", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Special characters only
                {"-123Mohamed", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Negative numbers
                {"أحمد", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", true}, // Arabic characters
                {"Mohamed@Nasser", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Symbols in the middle
                {"Mohamedأحمد", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", true}, // Mixed Arabic and English
                {"Mo#hn", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Special characters in the middle
                {"Иван", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", true} // Non-Latin characters
        };
    }

    @Test(priority = 1, dataProvider = "firstNameTestData")
    public void testFirstName(String firstName, String lastName, String mobileEmail, String password, String day, String month, String year, boolean isButtonEnabled) throws InterruptedException {
        driver.get("https://www.facebook.com/r.php");
        notificationPopupHandler.handleNotificationPopup();

        signUpPage = new FacebookSignUpPage(driver);
        signUpPage.enterFirstName(firstName);
        signUpPage.enterLastName(lastName);
        signUpPage.selectDay(day);
        signUpPage.selectMonth(month);
        signUpPage.selectYear(year);
        signUpPage.selectGender();
        signUpPage.enterMobileEmail(mobileEmail);
        signUpPage.enterPassword(password);
        signUpPage.clickSignUpButton();

        // Assert the Sign Up button's enabled state based on the provided test data
        if (isButtonEnabled) {
            Assert.assertTrue(signUpPage.isSignUpButtonEnabled(), "Sign Up button should be enabled for the input: " + firstName);
        } else {
            Assert.assertFalse(signUpPage.isSignUpButtonEnabled(), "Sign Up button should be disabled for the input: " + firstName);
        }
    }


    @DataProvider(name = "lastNameTestData")
    public Object[][] lastNameTestData() {
        return new Object[][]{
                {"Mohamed", "Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", true}, // Valid last name
                {"Mohamed", "", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Empty last name
                {"Mohamed", "@#$%", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Special characters
                {"Mohamed", " ", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Blank space only
                {"Mohamed", "Nasser Mohamed", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", true}, // Last name with space
                {"Mohamed", "Nasser    Mohamed", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Multiple spaces
                {"Mohamed", "محمد", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", true}, // Non-English last name
                {"Mohamed", "A".repeat(51), "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Extremely long last name
                {"Mohamed", "NassEr", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", true}, // Mixed case
                {"Mohamed", "Nasser😊", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Emoji
                {"Mohamed", "Nasser123", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Numbers
                {"Mohamed", "#Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Starting with special character
                {"Mohamed", "Nasser$", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Ending with special character
                {"Mohamed", "123Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Starting with numbers
                {"Mohamed", "😊Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Emoji characters
                {"Mohamed", "A".repeat(100), "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Excessive length
                {"Mohamed", " Nasser ", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Leading or trailing spaces
                {"Mohamed", "!@#$%", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Special characters only
                {"Mohamed", "-123Nasser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Negative numbers
                {"Mohamed", "أحمد", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", true}, // Arabic characters
                {"Mohamed", "Nasser@Ahmed", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Symbols in the middle
                {"Mohamed", "Nasserأحمد", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", true}, // Mixed Arabic and English
                {"Mohamed", "Nas#ser", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", false}, // Special characters in the middle
                {"Mohamed", "Иван", "m.a.nasser113@gmail.com", "CorrectPassword", "11", "Mar", "1996", true} // Non-Latin characters
        };
    }

    @Test(priority = 1, dataProvider = "lastNameTestData")
    public void testLastName(String firstName, String lastName, String mobileEmail, String password, String day, String month, String year, boolean isButtonEnabled) throws InterruptedException {
        driver.get("https://www.facebook.com/r.php");
        notificationPopupHandler.handleNotificationPopup();

        signUpPage = new FacebookSignUpPage(driver);
        signUpPage.enterFirstName(firstName);
        signUpPage.enterLastName(lastName);
        signUpPage.selectDay(day);
        signUpPage.selectMonth(month);
        signUpPage.selectYear(year);
        signUpPage.selectGender();
        signUpPage.enterMobileEmail(mobileEmail);
        signUpPage.enterPassword(password);
        signUpPage.clickSignUpButton();

        // Assert the Sign Up button's enabled state based on the provided test data
        if (isButtonEnabled) {
            Assert.assertTrue(signUpPage.isSignUpButtonEnabled(), "Sign Up button should be enabled for the input: " + lastName);
        } else {
            Assert.assertFalse(signUpPage.isSignUpButtonEnabled(), "Sign Up button should be disabled for the input: " + lastName);
        }
    }

    @Test(priority = 3)
    public void testValidLogin() throws InterruptedException {
        driver.get("https://www.facebook.com/");
        notificationPopupHandler.handleNotificationPopup();
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("---");
        loginPage.enterPassword("------");
        loginPage.clickLoginButton();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php");
    }

    @Test(priority = 4)
    public void logout() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            WebElement profileIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Your profile']")));
            profileIcon.click();
            Thread.sleep(1500);
            WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Log Out']")));
            logoutButton.click();
            wait.until(ExpectedConditions.urlContains("https://www.facebook.com/login"));
        } catch (TimeoutException e) {
            System.out.println("Profile icon was not found: " + e.getMessage());
        }
    }


    // Test Case 006: Invalid Email Without Domain
    @Test(priority = 6)
    public void InvalidEmailWithoutDomain() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser113@");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for Invalid Email Without Domain.");    }

    // Test Case 003: Invalid Email With Invalid Characters
    @Test(priority = 7)
    public void InvalidEmailWithInvalidCharacters() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser113!@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for Invalid Characters.");    }

    // Test Case 002: Invalid Email Without Domain
    @Test(priority = 8)
    public void ValidEmail() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser113@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login Pass for Valid Email Format.");    }

    // Test Case 005: Valid Email With Numeric Characters
    @Test(priority = 9)
    public void ValidEmailWithNumericCharacters() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser1131@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }

    // Test Case 006: Invalid Email With Missing '@' Symbol
    @Test(priority = 10)
    public void InvalidEmailWithMissingAtSymbol() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser113gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");    }

    // Test Case 007: Invalid Email With Multiple '@' Characters
    @Test(priority = 11)
    public void InvalidEmailWithMultipleAtSymbols() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser@@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");    }

    // Test Case 008: Valid Email With Dots
    @Test(priority = 12)
    public void ValidEmailWithDots() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.n.a.s.s.e.r.113@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with dots.");
    }

    // Test Case 009: Invalid Email Starting With Special Character
    @Test(priority = 13)
    public void InvalidEmailStartingWithSpecialCharacter() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail(".m.a.nasser113@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");    }

    // Test Case 010: Valid Email With Hyphen
    @Test(priority = 14)
    public void ValidEmailWithHyphen() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a-nasser113@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with hyphen.");
    }

    // Test Case 011: Invalid Email With Spaces
    @Test(priority = 15)
    public void InvalidEmailWithSpaces() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser 113@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");    }

    // Test Case 012: Valid Email With Uppercase Letters
    @Test(priority = 16)
    public void ValidEmailWithUppercaseLetters() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("M.A.NASSER113@GMAIL.COM");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with uppercase letters.");
    }

    // Test Case 013: Invalid Email With Consecutive Dots
    @Test(priority = 17)
    public void InvalidEmailWithConsecutiveDots() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a..nasser113@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");    }

    // Test Case 014: Invalid Email With Non-Standard Domain
    @Test(priority = 18)
    public void InvalidEmailWithNonStandardDomain() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser113@invalid_domain");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");    }

    // Test Case 015: Invalid Email With No TLD
    @Test(priority = 19)
    public void InvalidEmailWithNoTLD() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser113@gmail");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");    }

    // Test Case 016: Invalid Email With Special Characters Before '@'
    @Test(priority = 20)
    public void InvalidEmailWithSpecialCharactersBeforeAt() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser113#@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");    }

    // Test Case 017: Valid Email With Long Domain Name
    @Test(priority = 21)
    public void ValidEmailWithLongDomainName() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser113@longdomainname.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with long domain name.");
    }

    // Test Case 018: Invalid Email With Short Domain Name
    @Test(priority = 22)
    public void InvalidEmailWithShortDomainName() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser113@a.co");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");    }

    // Test Case 019: Valid Email With Numbers Only
    @Test(priority = 23)
    public void ValidEmailWithNumbersOnly() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("1234567890@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numbers only.");
    }

    // Test Case 020: Invalid Email With Two '@' Signs
    @Test(priority = 24)
    public void InvalidEmailWithTwoAtSigns() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a@Nasser113@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }

    // Test Case 021: Valid Email With Mixed Case
    @Test(priority = 25)
    public void ValidEmailWithMixedCase() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("M.A.NASSER113@GMAIL.COM");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with mixed case.");
    }

    // Test Case 022: Invalid Email With Invalid Top-Level Domain
    @Test(priority = 26)
    public void InvalidEmailWithInvalidTopLevelDomain() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser113@gmail.invalid");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }

    // Test Case 023: Valid Email With Multiple Dots Before '@'
    @Test(priority = 27)
    public void ValidEmailWithMultipleDotsBeforeAt() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.n.a.s.s.e.r.113@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with multiple dots before '@'.");
    }

    // Test Case 024: Invalid Email With Spaces In Domain
    @Test(priority = 28)
    public void InvalidEmailWithSpacesInDomain() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser113@g mail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }
    // Test Case 025: Invalid Email With Exceeding Length
    @Test(priority = 29)
    public void InvalidEmailWithExceedingLength() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser113123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }

    // Test Case 026: Invalid Email With Missing Domain Name
    @Test(priority = 30)
    public void InvalidEmailWithMissingDomainName() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser113@.");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }

    // Test Case 027: Invalid Email With No Local Part
    @Test(priority = 31)
    public void InvalidEmailWithNoLocalPart() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }

    // Test Case 028: Invalid Email With Long Local Part
    @Test(priority = 32)
    public void InvalidEmailWithLongLocalPart() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }

    // Test Case 029: Invalid Email With Invalid Domain Characters
    @Test(priority = 33)
    public void InvalidEmailWithInvalidDomainCharacters() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("m.a.nasser113@domain!@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }

    // Test Case 034: Login with an empty password field
    @Test(priority = 34)
    public void EmptyPasswordField() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("01018127745");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }

    // Test Case 035: Login with a password below the minimum length requirement
    @Test(priority = 35)
    public void PasswordBelowMinimumLength() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("01018127745");
        loginPage.enterPassword("123");
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }

    // Test Case 036: Login with a password above the maximum allowed length
    @Test(priority = 36)
    public void PasswordAboveMaximumLength() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("01018127745");
        String longPassword = "a".repeat(129); // Password longer than 128 characters
        loginPage.enterPassword(longPassword);
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }

    // Test Case 037: Login with a valid password containing special characters
    @Test(priority = 37)
    public void ValidPasswordWithSpecialCharacters() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("01018127745");
        loginPage.enterPassword("Correct!@#$%^&*()"); // Password with special characters
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }

    // Test Case 038: Login with a password containing leading and trailing spaces
    @Test(priority = 38)
    public void PasswordWithSpaces() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("01018127745");
        loginPage.enterPassword("  CorrectPassword123  "); // Password with spaces
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }

    // Test Case 039: Login with a case-sensitive incorrect password
    @Test(priority = 39)
    public void CaseSensitivePassword() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("01018127745");
        loginPage.enterPassword("mohamed@nasser"); // Incorrect case-sensitive password
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }

    // Test Case 040: Toggle password visibility and verify it displays correctly
    @Test(priority = 40)
    public void PasswordVisibilityToggle() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("01018127745");
        loginPage.enterPassword("CorrectPassword123"); // Password to check visibility

        // Simulate password visibility toggle action
        loginPage.togglePasswordVisibility();

        // Verify if password is shown/hidden properly
        boolean isPasswordVisible = loginPage.isPasswordVisible();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }

    // Test Case 041: Login with invalid special characters in the password
    @Test(priority = 41)
    public void InvalidSpecialCharactersInPassword() throws InterruptedException {
        loginPage = new P001_FacebookLoginPage(driver);
        loginPage.enterEmail("01018127745");
        loginPage.enterPassword("Password123<>"); // Invalid special characters in password
        loginPage.clickLoginButton();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, "https://www.facebook.com/login.php", "Login failed for valid email with numeric characters.");
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}





