# selenium-restassured-testng-framework

![Java](https://img.shields.io/badge/Java-17-orange) ![Maven](https://img.shields.io/badge/Maven-3.9-blue) ![TestNG](https://img.shields.io/badge/TestNG-7.8-green) ![Selenium](https://img.shields.io/badge/Selenium-4.x-brightgreen) ![RestAssured](https://img.shields.io/badge/RestAssured-5.x-yellow)

A production-grade **Hybrid Automation Framework** combining **Selenium UI tests**, **RestAssured API tests**, and **integrated Hybrid tests** — built with TestNG, Maven, Extent Reports, screenshot capture on failure, and Jenkins CI/CD pipeline support.

---

## Features

- **UI Testing** — Selenium WebDriver with multi-browser support (Chrome/Firefox/Edge)
- **API Testing** — RestAssured with fluent request/response validation
- **Hybrid Testing** — API preconditions + UI journeys + API state verification
- **Extent Reports** — Rich HTML reports with test logs and embedded screenshots
- **Screenshot on Failure** — Auto-captured and attached to Extent Report
- **Headless Mode** — Configurable for CI environments
- **Jenkins Pipeline** — Parameterized pipeline with suite selection, browser, and environment
- **Config-driven** — Centralized `config.properties` for URLs, timeouts, and test data

---

## Project Structure

```
selenium-restassured-testng-framework/
├── Jenkinsfile
├── pom.xml
└── src/
    ├── main/java/com/automation/
    │   ├── base/
    │   │   └── BaseTest.java           # TestNG hooks, driver setup, Extent Reports
    │   ├── drivers/
    │   │   └── DriverManager.java      # Multi-browser WebDriver factory
    │   ├── reports/
    │   │   └── ExtentReportManager.java # Extent HTML reporter config
    │   ├── utils/
    │   │   └── ScreenshotUtil.java     # Screenshot capture utility
    │   └── api/
    │       └── ApiClient.java          # RestAssured HTTP client wrapper
    └── test/
        ├── java/com/automation/tests/
        │   ├── ui/
        │   │   └── LoginUITest.java     # Selenium UI tests
        │   ├── api/
        │   │   └── UserApiTest.java     # RestAssured API tests
        │   └── hybrid/
        │       └── HybridUserOrderTest.java # UI + API integrated tests
        └── resources/
            ├── config.properties
            ├── testng-ui.xml
            ├── testng-api.xml
            ├── testng-hybrid.xml
            └── testng-all.xml
```

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java JDK | 17+ |
| Maven | 3.9+ |
| Chrome / Firefox / Edge | Latest |
| Jenkins (for CI) | 2.400+ |

---

## Setup

```bash
# Clone the repository
git clone https://github.com/pavankumarh14/selenium-restassured-testng-framework.git
cd selenium-restassured-testng-framework

# Install dependencies
mvn clean install -DskipTests
```

Update `src/test/resources/config.properties` with your target environment URLs.

---

## Running Tests Locally

### Run All Tests
```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-all.xml
```

### Run UI Tests Only
```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-ui.xml -Dbrowser=chrome
```

### Run API Tests Only
```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-api.xml
```

### Run Hybrid Tests Only
```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-hybrid.xml -Dbrowser=chrome -Dheadless=true
```

### Run with Custom Parameters
```bash
mvn test \
  -Dsurefire.suiteXmlFiles=src/test/resources/testng-all.xml \
  -Dbrowser=firefox \
  -Dheadless=true \
  -Denv=staging
```

---

## Reports

After test execution, Extent Reports are generated at:
```
target/extent-reports/ExtentReport.html
```

Screenshots on failure are saved at:
```
target/screenshots/<TestName>_<timestamp>.png
```

---

## Jenkins CI/CD

1. Create a new **Pipeline** job in Jenkins
2. Point it to this repository
3. Set **Script Path** to `Jenkinsfile`
4. Run the pipeline — it supports parameters:
   - `TEST_SUITE`: all / ui / api / hybrid
   - `BROWSER`: chrome / firefox / edge
   - `HEADLESS`: true / false
   - `ENV`: staging / production

The pipeline publishes the Extent Report HTML and archives screenshots as build artifacts.

---

## Configuration

Edit `src/test/resources/config.properties`:

```properties
base.url=https://reqres.in/api
ui.url=https://www.saucedemo.com
browser=chrome
headless=false
explicit.wait=20
page.load.timeout=30
report.dir=target/extent-reports
screenshot.dir=target/screenshots
```

---

## License

MIT License — see [LICENSE](LICENSE) for details.
