package com.salesforce.pages;

import com.salesforce.base.BaseClass;
import com.salesforce.utilities.GenericUtility;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * This is Home Page class where all locators of page are stored
 * This class also has Selenium @FindBy that locates elements
 * The class has 'openAccountPage' method which will navigate user to 'Accounts' Page
 */

public class HomePage extends BaseClass {

    /**
     * Declaring private object of WebDriver
     */
    private WebDriver driver;

    /**
     * Creating constructor for this class
     * @param driver
     */
    public HomePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "(//*[@class='onesetupBreadcrumbs']//span)[2]")
    WebElement homeLabel;

    @FindBy(xpath = "//*[@class='slds-icon-waffle']")
    WebElement setUpMenu;

    @FindBy(xpath = "(//*[@id='appTile'])[1]")
    WebElement serviceLink;

    @FindBy(xpath = "//*[@data-id='Account']/a")
    WebElement accountsTab;

    public WebElement getHomeLabel() {
        return homeLabel;
    }

    public WebElement getSetUpMenu() {
        return setUpMenu;
    }

    public WebElement getServiceLink() {
        return serviceLink;
    }

    public WebElement getAccountsTab() {
        return accountsTab;
    }

    /**
     * This method will navigate to accounts page when user logged in
     */
    protected void openAccountsPage(){
        getSetUpMenu().click();
        GenericUtility.waitForPageToLoad();
        getServiceLink().click();
        GenericUtility.waitForPageToLoad();
        WebElement element = getAccountsTab();
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
        GenericUtility.waitForPageToLoad();
    }

    /**
     * Creating object of 'Login page' and calling 'login' method
     * after calling 'openAccountsPage' method of  current class
     */
    public void openPage(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login();
        openAccountsPage();
    }

}