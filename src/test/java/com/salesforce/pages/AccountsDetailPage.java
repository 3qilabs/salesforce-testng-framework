package com.salesforce.pages;

import com.salesforce.base.BaseClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

 /**
 * This is an Accounts Detail Page class where all locators of page are stored
 * The class also has Selenium @FindBy that locates elements
 */
public class AccountsDetailPage extends BaseClass {

    /**
     * Declaring private object of
     */
    private WebDriver driver;

    /**
     * Creating constructor for this class
     * @param driver
     */

    public AccountsDetailPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

     @FindBy(xpath = "(//*[@class='tabHeader' and @title='Details'])[1]")
     WebElement accountsDetails;

     @FindBy(xpath="(//*[@class='slds-form-element__control slds-grid itemBody']//span)[8]")
     WebElement accountName;

     @FindBy(xpath = "(//*[@class='slds-form-element__control slds-grid itemBody']//span)[11]")
     WebElement accountPhone;

     @FindBy(xpath = "(//*[@class='slds-form-element__control slds-grid itemBody']//span)[16]")
     WebElement accountFax;

     @FindBy(xpath = "(//*[@class='slds-form-element__control slds-grid itemBody']//span)[19]")
     WebElement accountNumber;

     @FindBy(xpath = "(//*[@class='slds-form-element__control slds-grid itemBody']//span)[22]")
     WebElement accountWebsite;

     @FindBy(xpath = "(//*[@class='slds-form-element__control slds-grid itemBody']//span)[25]")
     WebElement accountSite;

     public WebElement getAccountsDetails() {
         return accountsDetails;
     }

     public WebElement getAccountName() {
         return accountName;
     }

     public WebElement getAccountPhone() {
         return accountPhone;
     }

     public WebElement getAccountFax() {
         return accountFax;
     }

     public WebElement getAccountNumber() {
         return accountNumber;
     }

     public WebElement getAccountWebsite() {
         return accountWebsite;
     }

     public WebElement getAccountSite() {
         return accountSite;
     }


}