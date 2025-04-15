package com.samcenter.intergration.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginTestDemo {

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

    public void loginWithUser(String username) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button")));

        usernameInput.clear();
        passwordInput.clear();

        usernameInput.sendKeys(username);
        passwordInput.sendKeys("secret_sauce");
        loginBtn.click();
    }

    @Test
    public void testOneUser() {
        loginWithUser("standard_user");

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory"),
                "Login failed: Không chuyển đến trang inventory");

        WebElement cart = driver.findElement(By.id("shopping_cart_container"));
        Assert.assertTrue(cart.isDisplayed(), "Giỏ hàng không hiển thị");

        System.out.println("Login thành công với standard_user");
    }

    @Test
    public void testMultipleUsers() {
        String[] users = {
                "standard_user",
                "locked_out_user",
                "problem_user",
                "performance_glitch_user",
                "error_user",
                "visual_user"
        };

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

        for (String user : users) {
            // Truy cập lại trang đăng nhập trước mỗi lần test
            driver.get("https://www.saucedemo.com/");

            try {
                // Thực hiện đăng nhập
                loginWithUser(user);

                // Kiểm tra nếu đăng nhập thành công
                if (driver.getCurrentUrl().contains("inventory")) {
                    System.out.println(user + " login thành công");

                    // Mở menu để tìm nút đăng xuất
                    WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("react-burger-menu-btn")));
                    menuButton.click();

                    // Đợi nút đăng xuất xuất hiện và nhấp được
                    WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link")));
                    logoutButton.click();

                    // Đợi cho đến khi quay lại trang đăng nhập
                    wait.until(ExpectedConditions.urlContains("saucedemo.com"));
                    System.out.println(user + " đăng xuất thành công");
                } else {
                    // Kiểm tra thông báo lỗi khi đăng nhập thất bại
                    try {
                        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
                        System.out.println(user + " login thất bại: " + error.getText());
                    } catch (Exception e) {
                        System.out.println(user + " login thất bại nhưng không tìm thấy thông báo lỗi.");
                    }
                }
            } catch (Exception e) {
                System.out.println("Lỗi khi kiểm tra user " + user + ": " + e.getMessage());
            }
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
