package com.salesforce.testsuits.api;

import com.salesforce.base.BaseClass;
import com.salesforce.data.SalesForceDataBean;
import io.restassured.response.Response;
import junit.framework.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class SmokeTestRestAssured extends BaseClass {

    private String id;
    public Logger logger = Logger.getLogger(this.getClass());
    @Test(dataProvider = "excelData", description = "Create a new account", priority = 0)
    public void createAccountUsingDataBean(Map<String, String> excelData) {
        SalesForceDataBean salesforceData = new SalesForceDataBean();
        salesforceData.account.fillRandomData();
        salesforceData.account.fillDataFromDB("select name from accounts");

        String baseUrl = (String)props.getProperty("env.baseurl");
        String endPoint = (String)props.getProperty("accounts.endpoint");
        String requestType = "POST";
        Response response = makeRestCallUsingRestAssured(baseUrl, endPoint, requestType, salesforceData.account, new HashMap<String, String>());
        this.logger.info("Response Code: " + response.getStatusCode());
        this.id = response.jsonPath().get("id").toString();
        boolean success = (boolean) response.jsonPath().get("success");
        Assert.assertTrue(success);
        this.logger.info("Test Passed!");
    }
}
