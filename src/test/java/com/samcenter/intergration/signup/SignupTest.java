package com.samcenter.intergration.signup;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class SignupTest {
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
    public void newTest() {
        driver.get("http://localhost:5173/");
        driver.manage().window().setSize(new Dimension(1343, 702));
        driver.findElement(By.cssSelector(".bi-person")).click();
        driver.findElement(By.linkText("Đăng ký")).click();
        driver.findElement(By.cssSelector(".mb-3:nth-child(1) > .form-control")).sendKeys("newMem");
        driver.findElement(By.cssSelector(".mb-3:nth-child(2) > .form-control")).sendKeys("newMem@gmail.com");
        driver.findElement(By.cssSelector(".mb-3:nth-child(3) > .form-control")).sendKeys("1234");
        driver.findElement(By.cssSelector(".mb-3:nth-child(4) > .form-control")).sendKeys("1234");
        driver.findElement(By.cssSelector(".btn")).click();

        //login
//        driver.findElement(By.cssSelector(".mb-3:nth-child(1) > .form-control")).sendKeys("newMem");
//        driver.findElement(By.cssSelector(".mb-3:nth-child(2) > .form-control")).sendKeys("1234");
//        driver.findElement(By.cssSelector(".btn")).click();
    }
}
