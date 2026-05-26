package com.automation.tests.hybrid;

import com.automation.base.BaseTest;
import com.automation.api.ApiClient;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;

import java.time.Duration;

public class HybridUserOrderTest extends BaseTest {

    @Test(description = "Verify user exists via API then login via UI")
    public void testHybridLoginWithApiValidation() {
        String UI_URL = System.getProperty("ui.url", "https://www.saucedemo.com");

        // --- API STEP 1: Pre-flight GET check ---
        extentTest.log(Status.INFO, "[API] Pre-flight GET /users/2");
        Response apiResp = ApiClient.get("/users/2");
        Assert.assertEquals(apiResp.getStatusCode(), 200);
        extentTest.log(Status.INFO, "[API] Data: " + apiResp.jsonPath().getString("data.first_name"));

        // --- UI STEP 2: Login ---
        driver.get(UI_URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")))
                .sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.urlContains("inventory"));
        extentTest.log(Status.INFO, "[UI] Logged in");

        // --- UI STEP 3: Add to cart ---
        driver.findElement(By.cssSelector(".inventory_item:first-child button")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        wait.until(ExpectedConditions.urlContains("cart"));
        extentTest.log(Status.INFO, "[UI] On cart page");

        // --- UI STEP 4: Begin checkout ---
        driver.findElement(By.id("checkout")).click();
        wait.until(ExpectedConditions.urlContains("checkout-step-one"));
        driver.findElement(By.id("first-name")).sendKeys("Pavan");
        driver.findElement(By.id("last-name")).sendKeys("Kumar");
        driver.findElement(By.id("postal-code")).sendKeys("560001");
        driver.findElement(By.id("continue")).click();
        wait.until(ExpectedConditions.urlContains("checkout-step-two"));
        extentTest.log(Status.INFO, "[UI] Order summary displayed");

        // --- API STEP 5: Final backend state check ---
        Response finalResp = ApiClient.get("/users?page=1");
        Assert.assertEquals(finalResp.getStatusCode(), 200);
        extentTest.log(Status.PASS, "[Hybrid] API + UI integration test passed");
    }
}
