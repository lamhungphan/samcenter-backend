package com.samcenter.intergration.login;

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

public class LoginTestAdmin {
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
        driver.findElement(By.linkText("Đăng nhập")).click();
        driver.findElement(By.cssSelector(".mb-3:nth-child(1) > .form-control")).click();
        driver.findElement(By.cssSelector(".mb-3:nth-child(1) > .form-control")).sendKeys("director");
        driver.findElement(By.cssSelector(".mb-3:nth-child(2) > .form-control")).sendKeys("123");
        driver.findElement(By.cssSelector(".btn")).click();
    }
}
