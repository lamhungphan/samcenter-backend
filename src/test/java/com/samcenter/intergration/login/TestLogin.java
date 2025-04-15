package com.samcenter.intergration.login;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
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

public class TestLogin {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testUserLoginSuccess() throws InterruptedException {
        driver.get("http://localhost:5173/");
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 1. Mở dropdown account
        WebElement iconPerson = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".bi-person")));
        iconPerson.click();

        // 2. Click vào nút "Đăng nhập" trong dropdown
        WebElement loginButtonDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Đăng nhập')]")
        ));
        loginButtonDropdown.click();

        // 3. Đợi modal render ra và nhập thông tin
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[placeholder='Tên người dùng']")
        ));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[placeholder='Mật khẩu']"));
        WebElement loginSubmitButton = driver.findElement(By.id("login"));

        // 4. Điền form và submit
        usernameInput.sendKeys("user3");
        passwordInput.sendKeys("1234");
        loginSubmitButton.click();
        Thread.sleep(2000);

        iconPerson = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".bi-person")));
        iconPerson.click();

        // Kiểm tra có hiển thị nút Đăng xuất không
        WebElement logoutItem = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[contains(@class, 'dropdown-item') and contains(text(), 'Đăng xuất')]")
        ));

        // Xác nhận đăng nhập thành công
        Assertions.assertTrue(logoutItem.isDisplayed());
    }

    @Test
    public void testAdminLoginSuccess() throws InterruptedException {
        driver.get("http://localhost:5173/");
        driver.manage().window().maximize();

        // 1. Mở dropdown tài khoản
        WebElement iconPerson = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".bi-person")));
        iconPerson.click();

        // 2. Click vào nút "Đăng nhập"
        WebElement loginButtonDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Đăng nhập')]")
        ));
        loginButtonDropdown.click();

        // 3. Điền form đăng nhập
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[placeholder='Tên người dùng']")
        ));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[placeholder='Mật khẩu']"));
        WebElement loginSubmitButton = driver.findElement(By.id("login"));

        usernameInput.sendKeys("director");
        passwordInput.sendKeys("123");
        loginSubmitButton.click();

        // 4. Đợi Swal biến mất
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".swal2-container")
        ));


        // 6. Kiểm tra có hiển thị sidebar chứa chữ "Dashboard"
        WebElement dashboardTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[contains(@class, 'nav-link') and contains(text(), 'Dashboard')]")
        ));

        Assertions.assertTrue(dashboardTab.isDisplayed());
    }
}
