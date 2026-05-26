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
 * API Test: User CRUD operations against ReqRes.in
 * Demonstrates GET, POST, PUT, DELETE with RestAssured + ExtentReport
 */
public class UserApiTest {

    private static final String BASE_URL = "https://reqres.in/api";
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

    @Test(description = "GET all users - expect 200 with non-empty list",
            groups = {"api", "smoke"}, priority = 1)
    public void testGetAllUsers() {
        extentTest = ExtentReportManager.createTest("testGetAllUsers", "UserApiTest");
        extentTest.log(Status.INFO, "Sending GET /users?page=1");
        Response response = ApiClient.get("/users?page=1");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.jsonPath().getList("data").size() > 0);
        extentTest.log(Status.PASS, "Users list returned: " + response.jsonPath().getList("data").size() + " users");
    }

    @Test(description = "GET single user - expect correct user data",
            groups = {"api", "regression"}, priority = 2)
    public void testGetSingleUser() {
        extentTest = ExtentReportManager.createTest("testGetSingleUser", "UserApiTest");
        extentTest.log(Status.INFO, "Sending GET /users/2");
        Response response = ApiClient.get("/users/2");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.id"), 2);
        String email = response.jsonPath().getString("data.email");
        Assert.assertTrue(email.contains("@"), "Expected valid email");
        extentTest.log(Status.PASS, "User 2 email: " + email);
    }

    @Test(description = "POST create user - expect 201 with ID",
            groups = {"api", "regression"}, priority = 3)
    public void testCreateUser() {
        extentTest = ExtentReportManager.createTest("testCreateUser", "UserApiTest");
        String body = "{\"name\": \"Pavan Kumar\", \"job\": \"QA Architect\"}";
        extentTest.log(Status.INFO, "POST /users body: " + body);
        Response response = ApiClient.post("/users", body);
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertNotNull(response.jsonPath().getString("id"));
        Assert.assertEquals(response.jsonPath().getString("name"), "Pavan Kumar");
        extentTest.log(Status.PASS, "User created ID: " + response.jsonPath().getString("id"));
    }

    @Test(description = "PUT update user - expect 200 with updated fields",
            groups = {"api", "regression"}, priority = 4)
    public void testUpdateUser() {
        extentTest = ExtentReportManager.createTest("testUpdateUser", "UserApiTest");
        String body = "{\"name\": \"Pavan H\", \"job\": \"Senior QA Architect\"}";
        Response response = ApiClient.put("/users/2", body);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("job"), "Senior QA Architect");
        extentTest.log(Status.PASS, "User updated at: " + response.jsonPath().getString("updatedAt"));
    }

    @Test(description = "DELETE user - expect 204 No Content",
            groups = {"api", "regression"}, priority = 5)
    public void testDeleteUser() {
        extentTest = ExtentReportManager.createTest("testDeleteUser", "UserApiTest");
        Response response = ApiClient.delete("/users/2");
        Assert.assertEquals(response.getStatusCode(), 204);
        extentTest.log(Status.PASS, "User deleted successfully - 204 received");
    }

    @Test(description = "GET non-existent user - expect 404",
            groups = {"api", "negative"}, priority = 6)
    public void testGetNonExistentUser() {
        extentTest = ExtentReportManager.createTest("testGetNonExistentUser", "UserApiTest");
        Response response = ApiClient.get("/users/9999");
        Assert.assertEquals(response.getStatusCode(), 404);
        extentTest.log(Status.PASS, "404 confirmed for non-existent user");
    }
}
