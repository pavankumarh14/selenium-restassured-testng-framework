package com.automation.base;

import com.automation.drivers.DriverManager;
import com.automation.reports.ExtentReportManager;
import com.automation.utils.ScreenshotUtil;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;

public class BaseTest {

    protected WebDriver driver;
    protected ExtentTest extentTest;

    @BeforeSuite(alwaysRun = true)
    public void initReport() {
        ExtentReportManager.initReports();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        String browser = System.getProperty("browser", "chrome");
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        driver = DriverManager.getDriver(browser, headless);
        extentTest = ExtentReportManager.createTest(
                method.getName(),
                method.getDeclaringClass().getSimpleName()
        );
        extentTest.log(Status.INFO, "Browser: " + browser + " | Headless: " + headless);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getName());
            extentTest.fail(result.getThrowable());
            extentTest.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            extentTest.pass("Test Passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.skip("Test Skipped: " + result.getThrowable());
        }
        DriverManager.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void flushReport() {
        ExtentReportManager.flushReports();
    }

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}
