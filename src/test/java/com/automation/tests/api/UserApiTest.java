package com.automation.tests.api;

import com.automation.api.ApiClient;
import com.automation.reports.ExtentReportManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

/**
 * API Test: User CRUD operations against JSONPlaceholder
 * Demonstrates GET, POST, PUT, DELETE with RestAssured + ExtentReport
 * Uses https://jsonplaceholder.typicode.com - free, no auth required
 */
public class UserApiTest {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private ExtentTest extentTest;

    @BeforeSuite(alwaysRun = true)
    public void initReports() {
        ExtentReportManager.initReports();
    }

    @AfterSuite(alwaysRun = true)
    public void flushReports() {
        ExtentReportManager.flushReports();
    }

    @BeforeClass
    public void setup() {
        ApiClient.init(BASE_URL);
    }

    @Test(description = "GET all users returns 200")
    public void testGetAllUsers() {
        extentTest = ExtentReportManager.createTest("testGetAllUsers", "GET /users - returns 200");
        try {
            Response response = ApiClient.get("/users");
            Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
            Assert.assertFalse(response.jsonPath().getList("$").isEmpty(), "Users list should not be empty");
            extentTest.log(Status.PASS, "GET /users returned 200 with " + response.jsonPath().getList("$").size() + " users");
        } catch (AssertionError | Exception e) {
            extentTest.log(Status.FAIL, "Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "GET single user returns 200")
    public void testGetSingleUser() {
        extentTest = ExtentReportManager.createTest("testGetSingleUser", "GET /users/1 - returns 200");
        try {
            Response response = ApiClient.get("/users/1");
            Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
            Assert.assertEquals(response.jsonPath().getInt("id"), 1, "User ID should be 1");
            extentTest.log(Status.PASS, "GET /users/1 returned 200, user: " + response.jsonPath().getString("name"));
        } catch (AssertionError | Exception e) {
            extentTest.log(Status.FAIL, "Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "GET non-existent user returns 404")
    public void testGetNonExistentUser() {
        extentTest = ExtentReportManager.createTest("testGetNonExistentUser", "GET /users/9999 - returns 404");
        try {
            Response response = ApiClient.get("/users/9999");
            Assert.assertEquals(response.getStatusCode(), 404, "Status code should be 404");
            extentTest.log(Status.PASS, "GET /users/9999 returned expected 404");
        } catch (AssertionError | Exception e) {
            extentTest.log(Status.FAIL, "Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "POST create user returns 201")
    public void testCreateUser() {
        extentTest = ExtentReportManager.createTest("testCreateUser", "POST /posts - returns 201");
        try {
            String body = "{\"title\": \"Test Post\", \"body\": \"Test body content\", \"userId\": 1}";
            Response response = ApiClient.post("/posts", body);
            Assert.assertEquals(response.getStatusCode(), 201, "Status code should be 201");
            Assert.assertNotNull(response.jsonPath().get("id"), "Created resource should have an ID");
            extentTest.log(Status.PASS, "POST /posts created with ID: " + response.jsonPath().get("id"));
        } catch (AssertionError | Exception e) {
            extentTest.log(Status.FAIL, "Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "PUT update user returns 200")
    public void testUpdateUser() {
        extentTest = ExtentReportManager.createTest("testUpdateUser", "PUT /posts/1 - returns 200");
        try {
            String body = "{\"id\": 1, \"title\": \"Updated Title\", \"body\": \"Updated body\", \"userId\": 1}";
            Response response = ApiClient.put("/posts/1", body);
            Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
            extentTest.log(Status.PASS, "PUT /posts/1 updated successfully");
        } catch (AssertionError | Exception e) {
            extentTest.log(Status.FAIL, "Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "DELETE user returns 200")
    public void testDeleteUser() {
        extentTest = ExtentReportManager.createTest("testDeleteUser", "DELETE /posts/1 - returns 200");
        try {
            Response response = ApiClient.delete("/posts/1");
            Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
            extentTest.log(Status.PASS, "DELETE /posts/1 returned 200");
        } catch (AssertionError | Exception e) {
            extentTest.log(Status.FAIL, "Test failed: " + e.getMessage());
            throw e;
        }
    }
}
