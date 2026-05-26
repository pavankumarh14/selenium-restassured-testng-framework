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
        String API_URL = "https://jsonplaceholder.typicode.com";

        // --- API STEP 1: Pre-flight GET check ---
        extentTest.log(Status.INFO, "[API] Pre-flight GET /users/2");
        ApiClient.init(API_URL);
        Response apiResp = ApiClient.get("/users/2");
        Assert.assertEquals(apiResp.getStatusCode(), 200,
                "[API] Pre-flight check failed: expected 200");
        String apiUserName = apiResp.jsonPath().getString("name");
        extentTest.log(Status.INFO, "[API] User verified: " + apiUserName);

        // --- UI STEP 2: Login ---
        driver.get(UI_URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")))
                .sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.urlContains("inventory"));
        extentTest.log(Status.INFO, "[UI] Login successful");

        // --- UI STEP 3: Add item to cart ---
        WebElement addToCart = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector(".btn_primary.btn_inventory")));
        addToCart.click();
        extentTest.log(Status.INFO, "[UI] Item added to cart");

        // --- UI STEP 4: Go to cart ---
        driver.findElement(By.className("shopping_cart_link")).click();
        wait.until(ExpectedConditions.urlContains("cart"));
        extentTest.log(Status.INFO, "[UI] Navigated to cart");

        // --- API STEP 5: Verify post-checkout via API ---
        extentTest.log(Status.INFO, "[API] Final check - verifying /posts/1");
        Response postResp = ApiClient.get("/posts/1");
        Assert.assertEquals(postResp.getStatusCode(), 200,
                "[API] Final check failed");
        extentTest.log(Status.PASS, "Hybrid test completed: API pre-check + UI flow + API final check");
    }
}
