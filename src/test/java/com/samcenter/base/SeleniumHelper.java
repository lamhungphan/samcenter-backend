package com.samcenter.base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class SeleniumHelper {
    private WebDriver driver;
    private WebDriverWait wait;

    public SeleniumHelper(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public WebElement findElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void click(By locator) {
        findElement(locator).click();
    }

    public void sendKeys(By locator, String text) {
        WebElement element = findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    public String getText(By locator) {
        return findElement(locator).getText();
    }

    public boolean isElementVisible(By locator, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Chụp màn hình sử dụng AShot với chiến lược viewportPasting
     *
     * @param outputSrc Đường dẫn lưu file ảnh (bao gồm tên file và đuôi .png)
     */
    public void takeScreenshot(String outputSrc) throws IOException {
        Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(1000))
                .takeScreenshot(driver);
        ImageIO.write(screenshot.getImage(), "PNG", new File(outputSrc));
    }
}
