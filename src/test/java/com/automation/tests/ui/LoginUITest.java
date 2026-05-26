package com.automation.tests.ui;

import com.automation.base.BaseTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * UI Test: Login functionality on SauceDemo
 * Uses Selenium WebDriver + WebDriverWait + ExtentReport logging
 */
public class LoginUITest extends BaseTest {

    private static final String BASE_URL = "https://www.saucedemo.com";

    @Test(description = "Verify successful login with valid credentials",
            groups = {"ui", "smoke", "regression"})
    public void testValidLogin() {
        extentTest.log(Status.INFO, "Navigating to SauceDemo: " + BASE_URL);
        driver.get(BASE_URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")))
                .sendKeys("standard_user");
        // DELIBERATE FAILURE: using wrong password to trigger screenshot-on-failure
        driver.findElement(By.id("password")).sendKeys("WRONG_PASSWORD_FOR_DEMO");
        driver.findElement(By.id("login-button")).click();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory"),
                "Login failed - Expected inventory URL but was: " + currentUrl);
        String pageTitle = driver.findElement(By.className("title")).getText();
        Assert.assertEquals(pageTitle, "Products", "Page title mismatch after login");
        extentTest.log(Status.PASS, "Login successful - Products page verified");
    }

    @Test(description = "Verify error shown with invalid credentials",
            groups = {"ui", "regression"})
    public void testInvalidLogin() {
        extentTest.log(Status.INFO, "Navigating to SauceDemo: " + BASE_URL);
        driver.get(BASE_URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")))
                .sendKeys("invalid_user");
        driver.findElement(By.id("password")).sendKeys("invalid_pass");
        driver.findElement(By.id("login-button")).click();
        WebElement errorMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']"))
        );
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not shown for invalid login");
        extentTest.log(Status.PASS, "Error message displayed as expected: " + errorMsg.getText());
    }

    @Test(description = "Verify locked out user cannot login",
            groups = {"ui", "regression"})
    public void testLockedOutUserLogin() {
        extentTest.log(Status.INFO, "Navigating to SauceDemo: " + BASE_URL);
        driver.get(BASE_URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")))
                .sendKeys("locked_out_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        WebElement errorMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']"))
        );
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not shown for locked out user");
        extentTest.log(Status.PASS, "Locked out user error displayed: " + errorMsg.getText());
    }
}
