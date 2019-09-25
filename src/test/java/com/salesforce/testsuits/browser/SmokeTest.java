package com.salesforce.testsuits.browser;

import com.salesforce.base.BaseUiClass;
import com.salesforce.pages.AccountsDetailPage;
import com.salesforce.pages.AccountsPage;
import com.salesforce.utilities.GenericUtility;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * This is SmokeTest class to execute smoke test
 * The class extends '/src/main/java/com/salesforce/base/BaseTest' class
 * This class also uses TestNG and Selenium to perform the testing and calls some
 * methods from '/src/main/java/com/salesforce/utilities/GenericUtility' class
 */
public class SmokeTest extends BaseUiClass {

    /**
     * Declaring private 'accountName' to assign account name
     * for testing purposes
     */
    private String accountName;

   @Test(description = "This test will create a new account", priority = 0)
   public void createAccount(){
       accountName = GenericUtility.getNewName();
       String accountPhone = GenericUtility.getPhoneNumber();
       String accountFax = GenericUtility.getPhoneNumber();
       String accountNumber = GenericUtility.getRandomNumber();
       String accountWebsite = GenericUtility.getNewWebsite();
       String accountSite = GenericUtility.getRandomString();

       AccountsPage accountsPage = new AccountsPage(driver);
       AccountsPage.NewAccountPage newAccountPage = accountsPage.new NewAccountPage(driver);
       AccountsDetailPage accountsDetailPage = new AccountsDetailPage(driver);
       accountsPage.openPage();
       accountsPage.getCreateNewAccount().click();
       GenericUtility.waitForPageToLoad();
       newAccountPage.getNewAccountName().sendKeys(accountName);
       newAccountPage.getNewAccountPhone().sendKeys(accountPhone);
       newAccountPage.getNewAccountFax().sendKeys(accountFax);
       newAccountPage.getNewAccountNumber().sendKeys(accountNumber);
       newAccountPage.getNewAccountWebSite().sendKeys(accountWebsite);
       newAccountPage.getNewAccountSite().sendKeys(accountSite);
       newAccountPage.getSaveBtn().click();
       GenericUtility.waitForPageToLoad();
       accountsDetailPage.getAccountsDetails().click();
       GenericUtility.waitForPageToLoad();
       String actualUserName = accountsDetailPage.getAccountName().getText();
       String actualAccountPhone = accountsDetailPage.getAccountPhone().getText();
       String actualAccountFax = accountsDetailPage.getAccountFax().getText();
       String actualAccountNumber = accountsDetailPage.getAccountNumber().getText();
       String actualAccountWebsite = accountsDetailPage.getAccountWebsite().getText();
       String actualAccountSite = accountsDetailPage.getAccountSite().getText();
       Assert.assertEquals(actualUserName, accountName);
       Assert.assertEquals(actualAccountPhone, accountPhone);
       Assert.assertEquals(actualAccountFax, accountFax);
       Assert.assertEquals(actualAccountNumber, accountNumber);
       Assert.assertEquals(actualAccountWebsite, accountWebsite);
       Assert.assertEquals(actualAccountSite, accountSite);
       accountsPage.logOut();
   }

    @Test(description = "This test will get created account", priority = 1)
    public void getAccount(){
        AccountsPage accountsPage = new AccountsPage(driver);
        accountsPage.openPage();
        GenericUtility.waitForPageToLoad();
        accountsPage.getSearchField().sendKeys(accountName, Keys.ENTER);
        GenericUtility.waitForPageToLoad();
        String actualAccountName = accountsPage.getAccountName().getText();
        Assert.assertEquals(actualAccountName, accountName);
        accountsPage.logOut();
    }
    @Test(description = "This test will update created account", priority = 2)
    public void updateAccount(){
        String accountPhone = GenericUtility.getPhoneNumber();
        String accountFax = GenericUtility.getPhoneNumber();
        String accountNumber = GenericUtility.getRandomNumber();

        AccountsPage accountsPage = new AccountsPage(driver);
        AccountsPage.NewAccountPage newAccountPage = accountsPage.new NewAccountPage(driver);
        AccountsDetailPage accountsDetailPage = new AccountsDetailPage(driver);
        accountsPage.openPage();
        accountsPage.getAccountDetailsDropdown().click();
        GenericUtility.waitForPageToLoad();
        accountsPage.getEditBtn().click();
        GenericUtility.waitForPageToLoad();

        newAccountPage.getNewAccountPhone().clear();
        newAccountPage.getNewAccountPhone().sendKeys(accountPhone);
        newAccountPage.getNewAccountFax().clear();
        newAccountPage.getNewAccountFax().sendKeys(accountFax);
        newAccountPage.getNewAccountNumber().clear();
        newAccountPage.getNewAccountNumber().sendKeys(accountNumber);
        newAccountPage.getSaveBtn().click();
        GenericUtility.waitForPageToLoad();
        accountsPage.getSearchField().sendKeys(accountName, Keys.ENTER);
        GenericUtility.waitForPageToLoad();
        accountsPage.getAccountName().click();
        GenericUtility.waitForPageToLoad();
        accountsDetailPage.getAccountsDetails().click();
        GenericUtility.waitForPageToLoad();

        String actualAccountPhone = accountsDetailPage.getAccountPhone().getText();
        String actualAccountFax = accountsDetailPage.getAccountFax().getText();
        String actualAccountNumber = accountsDetailPage.getAccountNumber().getText();

        Assert.assertEquals(actualAccountPhone, accountPhone);
        Assert.assertEquals(actualAccountFax, accountFax);
        Assert.assertEquals(actualAccountNumber, accountNumber);
        accountsPage.logOut();
    }

    @Test(description = "This test will delete created account", priority = 3)
    public void deleteAccount(){
        String expectedSearchResult = "No items to display.";
        AccountsPage accountsPage = new AccountsPage(driver);
        accountsPage.openPage();
        GenericUtility.waitForPageToLoad();
        accountsPage.getSearchField().sendKeys(accountName, Keys.ENTER);
        GenericUtility.waitForPageToLoad();
        accountsPage.getAccountDetailsDropdown().click();
        GenericUtility.waitForPageToLoad();
        accountsPage.getDeleteAccount().click();
        GenericUtility.waitForPageToLoad();
        accountsPage.getDeleteBtn().click();
        GenericUtility.waitForPageToLoad();
        accountsPage.getSearchField().clear();
        accountsPage.getSearchField().sendKeys(accountName, Keys.ENTER);
        GenericUtility.waitForPageToLoad();
        String actualSearchResult = accountsPage.getNoItemsToDisplay().getText();
        Assert.assertEquals(actualSearchResult, expectedSearchResult);
        accountsPage.logOut();
    }
}
