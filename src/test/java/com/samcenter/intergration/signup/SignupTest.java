package com.samcenter.intergration.signup;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

public class SignupTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize(); // Consistent with login test
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSuccessfulSignup() {
        // Navigate to the application
        driver.get("http://localhost:5173/");

        // 1. Open dropdown menu
        WebElement iconPerson = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".bi-person")
        ));
        iconPerson.click();

        // 2. Click "Đăng ký" in dropdown
        WebElement signupButtonDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Đăng ký')]")
        ));
        signupButtonDropdown.click();

        // 3. Fill the signup form
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[placeholder='Tên người dùng']")
        ));
        usernameField.sendKeys("newMem" + System.currentTimeMillis()); // Unique username

        WebElement emailField = driver.findElement(
                By.cssSelector("input[placeholder='Email']")
        );
        emailField.sendKeys("newMem" + System.currentTimeMillis() + "@gmail.com"); // Unique email

        WebElement passwordField = driver.findElement(
                By.cssSelector("input[placeholder='Mật khẩu']")
        );
        passwordField.sendKeys("1234");

        WebElement confirmPasswordField = driver.findElement(
                By.cssSelector("input[placeholder='Nhập lại mật khẩu']")
        );
        confirmPasswordField.sendKeys("1234");

        // 4. Submit the form
        WebElement submitButton = driver.findElement(
                By.cssSelector("button.btn.btn-success[type='submit']")
        );
        submitButton.click();

        // 5. Validate successful signup
        // Option 1: Check if user is logged in (like login test)
        iconPerson = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".bi-person")
        ));
        iconPerson.click();

        WebElement logoutItem = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[contains(@class, 'dropdown-item') and contains(text(), 'Đăng xuất')]")
        ));
        assertTrue(logoutItem.isDisplayed(), "Signup failed: Logout option not displayed");

        // Option 2: Check if modal closed (alternative, comment out if using Option 1)
        /*
        boolean isModalClosed = wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".modal-overlay")
        ));
        assertTrue(isModalClosed, "Signup failed: Modal did not close");
        */
    }

    @Test
    public void testFailedSignupWithMismatchedPasswords() {
        // Navigate to the application
        driver.get("http://localhost:5173/");

        // 1. Open dropdown menu
        WebElement iconPerson = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".bi-person")
        ));
        iconPerson.click();

        // 2. Click "Đăng ký" in dropdown
        WebElement signupButtonDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Đăng ký')]")
        ));
        signupButtonDropdown.click();

        // 3. Fill the signup form with mismatched passwords
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[placeholder='Tên người dùng']")
        ));
        usernameField.sendKeys("newMem");

        WebElement emailField = driver.findElement(
                By.cssSelector("input[placeholder='Email']")
        );
        emailField.sendKeys("newMem@gmail.com");

        WebElement passwordField = driver.findElement(
                By.cssSelector("input[placeholder='Mật khẩu']")
        );
        passwordField.sendKeys("1234");

        WebElement confirmPasswordField = driver.findElement(
                By.cssSelector("input[placeholder='Nhập lại mật khẩu']")
        );
        confirmPasswordField.sendKeys("4321"); // Mismatched password

        // 4. Submit the form
        WebElement submitButton = driver.findElement(
                By.cssSelector("button.btn.btn-success[type='submit']")
        );
        submitButton.click();

        // 5. Validate error message
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("p.error")
        ));
        assertTrue(errorMessage.isDisplayed(), "Error message not displayed for mismatched passwords");
        // Optionally, verify the error text if you know the expected message
        // assertTrue(errorMessage.getText().contains("không khớp"), "Expected error message for mismatched passwords");
    }
}