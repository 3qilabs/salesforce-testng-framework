package com.salesforce.testsuits.api;

import com.salesforce.base.BaseClass;
import com.salesforce.data.SalesForceDataBean;
import io.restassured.response.Response;
import junit.framework.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;

public class SmokeTestRestAssured extends BaseClass {

    @DataProvider(name = "dataBeanDataProvider")
    public Object[][] dataBeanData() {
        SalesForceDataBean dataBean = new SalesForceDataBean();
        dataBean.account.fillRandomData();
        return new Object[][]{{dataBean}};
    }

    private String id;

    @Test(dataProvider = "dataBeanDataProvider", description = "Create a new account", priority = 0)
    public void createAccountUsingDataBean(SalesForceDataBean tcData) {
        String baseUrl = (String)props.getProperty("env.baseurl");
        String endPoint = (String)props.getProperty("accounts.endpoint");
        String requestType = "POST";
        Response response = makeRestCallUsingRestAssured(baseUrl, endPoint, requestType, tcData.account, new HashMap<String, String>());
        System.out.println("Response Code: " + response.getStatusCode());
        this.id = response.jsonPath().get("id").toString();
        boolean success = (boolean) response.jsonPath().get("success");
        Assert.assertTrue(success);
        System.out.println("Test Passed!");
    }
}
