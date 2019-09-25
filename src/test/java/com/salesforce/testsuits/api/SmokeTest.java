package com.salesforce.testsuits.api;

import com.salesforce.base.BaseClass;
import com.salesforce.base.JsonOutput;
import junit.framework.Assert;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.salesforce.utilities.GenericUtility.getRandomNumber;

public class SmokeTest extends BaseClass {
    private String id;

    @Test(dataProvider = "excelData", description = "Create a new account", priority = 0)
    public void createAccount(Map<String, String> tcData) {
        String targetURL = (String)props.getProperty("env.baseurl") + "/account";
        String requestParam = tcData.get("Request");
        requestParam = requestParam.replace("RANDOM_NUMBER", String.valueOf(getRandomNumber()));
        System.out.println("Request body: " + requestParam);
        String requestType = tcData.get("Request Type");
        JsonOutput response = makeRestCallUsingHttpClient(targetURL, requestType, requestParam, new HashMap<String, String>());
        System.out.println("Response: " + response.getJsonResponse());
        this.id = response.getJsonResponse().get("id").toString();
        boolean success = (boolean) response.getJsonResponse().get("success");
        Assert.assertTrue(success);
    }

    @Test(dataProvider = "excelData", description = "Edit the account created in previous test case ", priority = 1)
    public void getAccount(Map<String, String> tcData) {
        String targetURL = manifestJsonObject.get(tcData.get("URL")) + "/account/" + id;
        String requestType = tcData.get("Request Type");
        JsonOutput jsonResponse = makeRestCallUsingHttpClient(targetURL, requestType);
        System.out.println("Response:\n" + jsonResponse.getJsonResponse());
        String actualId = jsonResponse.getJsonResponse().get("Id").toString();
        Assert.assertEquals(id, actualId);
        /*
        // Below steps are for de-serializing the json response into an object using Gson library
        com.salesforce.base.JsonOutput jsonOutput = new Gson().fromJson(jsonResponse, com.salesforce.base.JsonOutput.class);
        System.out.println("ID: " + jsonOutput.getId() + "\nName: " + jsonOutput.getName());
        Assert.assertEquals(jsonOutput.getName(), "TestAccount1", "Name did not match the expected Value");
        */
    }

    @Test(dataProvider = "excelData", description = "Delete the account created in previous test case", priority = 2)
    public void deleteAccount(Map<String, String> tcData) {
        String targetURL = manifestJsonObject.get(tcData.get("URL")) + "/account/" + id;
        String requestType = tcData.get("Request Type");
        JsonOutput jsonResponse = makeRestCallUsingHttpClient(targetURL, requestType);
        System.out.println("Response:\n" + jsonResponse.getJsonResponse());
        Assert.assertTrue(jsonResponse.getJsonResponse().isEmpty());
    }

    @Test(dataProvider = "excelData", description = "Validate the account is deleted successfully", priority = 3)
    public void validateDeletedAccount(Map<String, String> tcData) {
        String targetURL = manifestJsonObject.get(tcData.get("URL")) + "/account/" + id;
        String requestType = tcData.get("Request Type");
        JsonOutput jsonResponse = makeRestCallUsingHttpClient(targetURL, requestType);
        String expectedErrorMessage = "NOT_FOUND";
        JSONArray actualJsonArrayResponse = jsonResponse.getJsonArrayResponse();
        JSONObject actualJsonResponse = (JSONObject) actualJsonArrayResponse.get(0);
        System.out.println("Response:\n" + actualJsonResponse);
        String actualErrorMessage = actualJsonResponse.get("errorCode").toString();
        Assert.assertEquals(actualErrorMessage, expectedErrorMessage);
    }
}
