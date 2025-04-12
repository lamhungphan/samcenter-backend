package com.samcenter.intergration.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest {

    WebDriver driver;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @BeforeMethod
    public void login() {
        driver.get("https://www.saucedemo.com/");
    }

    public void loginWithUser(String username) throws InterruptedException {
        WebElement usernameInput = driver.findElement(By.id("user-name"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("login-button"));

        usernameInput.clear();
        passwordInput.clear();

        usernameInput.sendKeys(username);
        passwordInput.sendKeys("secret_sauce");
        loginBtn.click();

        Thread.sleep(2000);
    }

    @Test
    public void testOneUser() throws InterruptedException {
        loginWithUser("standard_user");

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory"),
                "Login failed: Không chuyển đến trang inventory");

        WebElement cart = driver.findElement(By.id("shopping_cart_container"));
        Assert.assertTrue(cart.isDisplayed(), "Giỏ hàng không hiển thị");

        System.out.println("Login thành công với standard_user");
    }

    @Test
    public void testMultipleUsers() throws InterruptedException {
        String[] users = {
                "standard_user",
                "locked_out_user",
                "problem_user",
                "performance_glitch_user",
                "error_user",
                "visual_user"
        };

        for (String user : users) {
            loginWithUser(user);

            if (driver.getCurrentUrl().contains("inventory")) {
                System.out.println(user + " login thành công");
            } else {
                WebElement error = driver.findElement(By.cssSelector("[data-test='error']"));
                System.out.println(user + " login thất bại: " + error.getText());
            }

            Thread.sleep(1000);
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
