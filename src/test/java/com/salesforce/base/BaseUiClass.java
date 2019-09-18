package com.salesforce.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * This is the base class with methods to make HTTP calls
 * This also has a TestNG Data Provider that reads data from an excel
 * The HTTP Calls made in these functions get a new access token for every call and credentials are read from /src/main/resources/manifest.json
 * */
public class BaseUiClass extends BaseClass {

    public static WebDriver driver;

    /**
     * reads browser name from 'manifest.json' file and returns corresponding driver
     * @return -
     */
    protected WebDriver getDriver(){
        WebDriver browser;
        String browserName = manifestJsonObject.get("browser").toString();

        switch (browserName){
            case "firefox":
                browser = new FirefoxDriver();
                break;
            case "safari":
                browser = new SafariDriver();
                break;
            case "ie":
                browser = new InternetExplorerDriver();
                break;
            default:
                //will disable notifications
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-notifications");
                browser = new ChromeDriver(options);
                break;

        }
        //maximise the window
        browser.manage().window().maximize();
        //wait for page to load
        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        browser.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        browser.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        return browser;
    }

    public String getBrowserStatus() {
        String  status;
        String string = manifestJsonObject.get("closeBrowser").toString();
        switch (string) {
            case "Test":
                status = "Test";
                break;
            case "Suite":
                status = "Suite";
                break;
            default:
                status = "Method";
                break;
        }
        return status;
    }

    @BeforeSuite(alwaysRun = true, dependsOnMethods = "readManifestJSON")
    public void initDriver(){
        if(getBrowserStatus().equals("Suite")){
            driver = getDriver();
        }
    }

    @BeforeMethod
    public void beforeMethod(){
        if(getBrowserStatus().equals("Method")){
            driver = getDriver();
        }
    }
    @BeforeTest
    public void beforeTest(){
        if(getBrowserStatus().equals("Test")){
            driver = getDriver();
        }
    }

    @AfterMethod
    public void afterMethod(){
        if(getBrowserStatus().equals("Method")){

            driver.quit();
        }
    }
    @AfterTest
    public void afterTest(){
        if(getBrowserStatus().equals("Test")){
            driver.quit();
        }
    }
    @AfterSuite
    public void afterSuite(){
        if(getBrowserStatus().equals("Suite")){
            driver.quit();
        }
    }
}
