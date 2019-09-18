package com.salesforce.integrated;

import com.jayway.restassured.response.Response;
import com.salesforce.base.BaseUiClass;
import com.salesforce.base.JsonOutput;
import com.salesforce.pages.AccountsDetailPage;
import com.salesforce.pages.AccountsPage;
import com.salesforce.utilities.GenericUtility;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.salesforce.utilities.GenericUtility.getNewName;
import static com.salesforce.utilities.GenericUtility.getRandomNumber;

public class Integrated  extends BaseUiClass {
    private static String id;
    private static String accountName;

    @Test(dataProvider = "excelData", description = "Create a new account from API and Validate from UI", priority = 0)
    public void createNewAccount(Map<String, String> tcData) {
        String targetURL = manifestJsonObject.get(tcData.get("URL")) + "/account";
        String requestParam = tcData.get("Request");
        accountName = GenericUtility.getNewName();
        requestParam = requestParam.replace("getNewName", accountName);
        System.out.println("Request body: " + accountName);
        String requestType = tcData.get("Request Type");
        JsonOutput response = makeRestCallUsingHttpClient(targetURL, requestType, requestParam, new HashMap<String, String>());
        System.out.println("Response: " + response.getJsonResponse());
        id=response.getJsonResponse().get("id").toString();
        AccountsPage accountsPage = new AccountsPage(driver);
        accountsPage.openPage();
        GenericUtility.waitForPageToLoad();
        accountsPage.getSearchField().sendKeys(accountName, Keys.ENTER);
        GenericUtility.waitForPageToLoad();
        String actualAccountName = accountsPage.getAccountName().getText();
        Assert.assertEquals(actualAccountName, accountName);
        accountsPage.logOut();
    }

    @Test(dataProvider = "excelData", description = "Update created account from UI and validate from API", priority = 1)
    public void updatedAccount(Map<String, String> tcData){
        accountName = GenericUtility.getNewName();
        AccountsPage accountsPage = new AccountsPage(driver);
        AccountsPage.NewAccountPage newAccountPage = accountsPage.new NewAccountPage(driver);
        accountsPage.openPage();
        accountsPage.getAccountDetailsDropdown().click();
        GenericUtility.waitForPageToLoad();
        accountsPage.getEditBtn().click();
        GenericUtility.waitForPageToLoad();
        newAccountPage.getNewAccountName().clear();
        newAccountPage.getNewAccountName().sendKeys(accountName);
        newAccountPage.getSaveBtn().click();
        accountsPage.logOut();
        String targetURL = manifestJsonObject.get(tcData.get("URL")) + "/account/"+id;
        String requestType = tcData.get("Request Type");
        JsonOutput jsonResponse = makeRestCallUsingHttpClient(targetURL, requestType);
        System.out.println("Response:\n" + jsonResponse.getJsonResponse());
        String actualName = jsonResponse.getJsonResponse().get("Name").toString();
        Assert.assertEquals(actualName, accountName);
    }

    @Test(dataProvider = "excelData", description = "Delete account from API and validate from UI", priority = 2)
    public void deleteAccount(Map<String, String> tcData) {
        String targetURL = manifestJsonObject.get(tcData.get("URL")) + "/account/"+id;
        String requestType = tcData.get("Request Type");
        makeRestCallUsingHttpClient(targetURL, requestType);
        String expectedSearchResult = "No items to display.";
        AccountsPage accountsPage = new AccountsPage(driver);
        accountsPage.openPage();
        GenericUtility.waitForPageToLoad();
        accountsPage.getSearchField().sendKeys(accountName, Keys.ENTER);
        GenericUtility.waitForPageToLoad();
        String actualSearchResult = accountsPage.getNoItemsToDisplay().getText();
        Assert.assertEquals(actualSearchResult, expectedSearchResult);
        accountsPage.logOut();
    }
}
