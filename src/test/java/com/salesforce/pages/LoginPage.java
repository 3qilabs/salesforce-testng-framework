package com.salesforce.pages;

import com.salesforce.base.BaseClass;
import com.salesforce.utilities.GenericUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * This is Login Page class where all locators of page are stored
 * This class also has Selenium @FindBy that locates elements
 * The class has login method, which will login to salesforce
 */
public class LoginPage extends BaseClass {
    /**
     * Declaring private object of WebDriver
     */
    private WebDriver driver;

    /**
     * Creating constructor for this class
     * @param driver
     */
    public LoginPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//*[@id='username']")
    WebElement username;

    @FindBy(xpath = "//*[@id='password']")
    WebElement password;

    @FindBy(xpath = "//*[@id='Login']")
    WebElement loginBtn;

    public WebElement getUsername() {
        return username;
    }

    public WebElement getPassword() {
        return password;
    }

    public WebElement getLoginBtn() {
        return loginBtn;
    }

    /**
     * This nethod will log in to salesforce
     * Getting url, username and password from '/src/main/resources/manifest.json'
     */
    protected void login(){
     /*  String url = manifestJsonObject.get("salesforce_url").toString();
       String usrname = manifestJsonObject.get("username").toString();
       String psw = manifestJsonObject.get("webPassword").toString();*/
     String url = props.getProperty("salesforce_url").toString();
     String usrname = props.getProperty("username").toString();
     String psw = props.getProperty("webPassword").toString();
     driver.get(url);
       getUsername().sendKeys(usrname);
       getPassword().sendKeys(psw);
       getLoginBtn().click();
       GenericUtility.waitForPageToLoad(3000);
    }
}