package com.salesforce.pages;

import com.salesforce.base.BaseClass;
import com.salesforce.utilities.GenericUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
 /**
 * This is an Accounts Page class where all locators of page are stored
 * The class also has Selenium @FindBy that locates elements
 * The class has 'innerclass' where 'New Account' page locators are stored
 */
public class AccountsPage extends BaseClass {

    /**
     * Declaring private object of WebDriver
     */
    private WebDriver driver;

    /**
     * Creating constructor for this class
     * @param driver
     */

    public AccountsPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//*[@class='emptyContent']/div")
    WebElement noItemsToDisplay;

    @FindBy(xpath = "//*[@*='uiVirtualDataTable']/tbody/tr/th")
    List<WebElement> listOfUserName;

    @FindBy(xpath = "//div[@class='slds-media__body slds-align-middle']//li/span")
    WebElement accountsLabel;

    @FindBy(xpath = "(//*[@data-aura-class='uiVirtualDataTable']//tbody//th//a)[1]")
    WebElement accountName;

    @FindBy(xpath = "(//*[@title='New'])[1]")
    WebElement createNewAccount;

    @FindBy(xpath = "(//a[contains(@class,'rowActionsPlaceHolder')])[1]")
    WebElement accountDetailsDropdown;

    @FindBy(xpath = "//a[@title='Delete']")
    WebElement deleteAccount;

    @FindBy(xpath = "//*[@title='Delete' and@type='button']")
    WebElement deleteBtn;

    @FindBy(xpath = "//a[@title='Edit']")
    WebElement editBtn;

    @FindBy(xpath = "(//*[contains(@id,'input-')])[2]")
    WebElement searchField;

    @FindBy(xpath = "(//*[@data-aura-class='uiButton forceHeaderButton' and @type='button'])[3]")
    WebElement viewProfileIcon;

    @FindBy(xpath = "//*[@class='profile-card-toplinks']/a[2]")
    WebElement logOutBtn;

    public WebElement getViewProfileIcon() {
         return viewProfileIcon;
    }
    public WebElement getLogOutBtn() {
         return logOutBtn;
    }

    public WebElement getNoItemsToDisplay() {
         return noItemsToDisplay;
    }

    public WebElement getEditBtn() {
         return editBtn;
    }

    public WebElement getSearchField() {
        return searchField;
    }

    public List<WebElement> getListOfUserName() {
        return listOfUserName;
    }

    public WebElement getAccountsLabel() {
        return accountsLabel;
    }

    public WebElement getAccountName() {
        return accountName;
    }

    public WebElement getDeleteBtn() {
        return deleteBtn;
    }

    public WebElement getAccountDetailsDropdown() {
        return accountDetailsDropdown;
    }

    public WebElement getDeleteAccount() {
        return deleteAccount;
    }

    public WebElement getCreateNewAccount() {
        return createNewAccount;
    }


     /**
      * This method will log out from salesforce
      */
    public void logOut() {
        GenericUtility.waitForPageToLoad();
        getViewProfileIcon().click();
        GenericUtility.waitForPageToLoad();
        getLogOutBtn().click();
        GenericUtility.waitForPageToLoad();
    }
     /**
      * This method will call openPage of its parent page and this hierarchy
      * helps us land on this page (AccountsPage)
      */
    public void openPage(){
        HomePage homePage = new HomePage(this.driver);
        homePage.openPage();
    }

    public class NewAccountPage {
        private WebDriver driver;
        public NewAccountPage(WebDriver driver){
          this.driver = driver;
          PageFactory.initElements(driver, this);
        }

        @FindBy(xpath = "//*[contains(@class,'input uiInput uiInputText uiInput--default uiInput--input')]")
        WebElement newAccountName;

        @FindBy(xpath="(//*[@type='tel'])[1]")
        WebElement newAccountPhone;

        @FindBy(xpath = "(//*[@type='tel'])[2]")
        WebElement newAccountFax;

        @FindBy(xpath = "(//*[@type='url'])[1]")
        WebElement newAccountWebSite;

        @FindBy(xpath = "(//*[@role='listitem']//input)[5]")
        WebElement newAccountNumber;

        @FindBy(xpath = "(//*[@role='listitem']//input)[7]")
        WebElement newAccountSite;

        @FindBy(xpath="//*[@title='Save']")
        WebElement saveBtn;

        public WebElement getNewAccountFax() {
            return newAccountFax;
        }

        public WebElement getNewAccountWebSite() {
            return newAccountWebSite;
        }

        public WebElement getNewAccountNumber() {
            return newAccountNumber;
        }

        public WebElement getNewAccountSite() {
            return newAccountSite;
        }

        public WebElement getNewAccountName() {
            return newAccountName;
        }

        public WebElement getNewAccountPhone() {
            return newAccountPhone;
        }

        public WebElement getSaveBtn() {
            return saveBtn;
        }
    }
}