package com.salesforce.base;

import com.salesforce.utilities.GenericUtility;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.*;

import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
* This is the base class with methods to make HTTP calls
* This also has a TestNG Data Provider that reads data from an excel
* The HTTP Calls made in these functions get a new access token for every call and credentials are read from /src/main/resources/manifest.json
* */
public class BaseTest {

    public String pwd = System.getProperty("user.dir");

    /**
     * Field to store all the environment params from /src/main/resources/manifest.json
     */
    public JSONObject manifestJsonObject;
    public static WebDriver driver;
    public Properties appProp = new Properties();
    private  String url;
    private String psw;
    private String usrname;

    @BeforeSuite(alwaysRun = true)
    private void readAppProps(){
        try{
            InputStream inputStream = new FileInputStream(pwd + "../../../resources/application.properties");
            appProp.load(inputStream);
        }catch(IOException ex){

        }

    }
    /**
     * This is a TestNG Data Provider, it reads data from an excel at /src/main/resources/
     * @param testcaseName
     * @return A Hash Map for the test case being executed: {testcase_id => {"column_header" => "cell_value"}}
     */
    @DataProvider(name = "excelData")
    public Object[] readExcelData(Method testcaseName) {
        Map<String, Map<String, String>> excelData = new Hashtable<>();
        try {
            // Todo: parameterize the excel name
            File src = new File(pwd + "/resources/salesforce_dev_api.xlsx");
            FileInputStream fis = new FileInputStream(src);
            XSSFWorkbook file = new XSSFWorkbook(fis);
            int numOfSheets = file.getNumberOfSheets();
            for (int sheetNum = 0; sheetNum < numOfSheets; sheetNum++) {
                XSSFSheet sheet = file.getSheetAt(sheetNum);
                int totalRows = sheet.getPhysicalNumberOfRows();
                int totalColumns = sheet.getRow(0).getPhysicalNumberOfCells();
             /*
             Creating a hash
             {testcase_id => {"column_header" => "cell_value"}}
             */
                for (int rowNum = 1; rowNum < totalRows; rowNum++) {
                    Map<String, String> rowData = new Hashtable<String, String>();
                    for (int columNum = 0; columNum < totalColumns; columNum++) {
                        sheet.getRow(0).getCell(columNum).setCellType(Cell.CELL_TYPE_STRING);
                        String key = sheet.getRow(0).getCell(columNum).getStringCellValue();
                        sheet.getRow(rowNum).getCell(columNum).setCellType(Cell.CELL_TYPE_STRING);
                        String value = sheet.getRow(rowNum).getCell(columNum).getStringCellValue();
                        rowData.put(key, value);
                    }
                    excelData.put(sheet.getRow(rowNum).getCell(0).getStringCellValue(), rowData);
                }
            }
        } catch (IOException ex) {
            System.out.println("Exception in readExcelData: \n");
            ex.printStackTrace();
        }
        return new Object[]{excelData.get(testcaseName.getName())};
    }

    /**
     * Read the manifest.json and store in manifestJsonObject
     */
    @BeforeSuite(alwaysRun = true)
    public void readManifestJSON() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(pwd + "/resources/manifest.json"));

            manifestJsonObject = (JSONObject) obj;

        } catch (FileNotFoundException e) {
            System.out.println("manifest file not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Exception in opening manifest file");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Exception in parsing manifest file");
            e.printStackTrace();
        }
    }

    /**
     * Make HTTP Call using java.net.HttpURLConnection
     * @param targetURL
     * @param requestType
     * @return org.json.JSONObject Response from HTTP call
     */
    protected org.json.JSONObject makeRestCall(String targetURL, String requestType) {
        /*
         * Overloaded method to make calls without request body, and headers
         * */
        byte[] requestParams = "".getBytes();
        return makeRestCall(targetURL, requestType, requestParams, new HashMap<String, String>());
    }

    /**
     * Make HTTP Call using java.net.HttpURLConnection
     * @param targetURL
     * @param requestType
     * @param requestParams
     * @return org.json.JSONObject Response from HTTP call
     */
    protected org.json.JSONObject makeRestCall(String targetURL, String requestType, byte[] requestParams) {
        /*
         * Overloaded method to make calls without
         * */
        return makeRestCall(targetURL, requestType, requestParams, new HashMap<String, String>());
    }

    /**
     * Make HTTP Call using java.net.HttpURLConnection
     * @param targetURL
     * @param requestType
     * @param requestParams
     * @param headers
     * @return org.json.JSONObject Response from HTTP call
     */
    protected org.json.JSONObject makeRestCall(String targetURL, String requestType, byte[] requestParams, HashMap<String, String> headers) {
        /*
         * Switch statement on request type
         * Prepare request headers
         * Add request body
         * Return the response as JSONObject
         * */
        try {
            URL url = new URL(targetURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            String accessToken = "";

            // If the caller is NOT "getAccessToken", then get the access token
            if (!(stackTraceElements[2].getMethodName().equals("getAccessToken"))) {
                System.out.println("********Getting Access Token********");
                accessToken = getAccessToken();
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            }
            if (!headers.isEmpty()) {
                for (HashMap.Entry<String, String> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    connection.setRequestProperty(key, value);
                }
            }
            System.out.println("********Making Rest Call******** \n" + requestType + ": " + "\nURL: " + targetURL);
            switch (requestType) {
                case "GET":
                    connection.setRequestMethod("GET");
                    break;
                case "DELETE":
                    connection.setRequestMethod("DELETE");
                    break;
                case "POST":
                    connection.setUseCaches(false);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    if (headers.isEmpty()) {
                        connection.setRequestProperty("Content-Type", "application/json");
                    }
                    try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                        wr.write(requestParams);
                    }
                    break;
            }
            InputStream inStream = null;
            int responseCode;
            try {
                inStream = connection.getInputStream();
                responseCode = connection.getResponseCode();
            } catch (FileNotFoundException e) {
                System.out.println("The resource you are looking for is not found");
                e.printStackTrace();
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(inStream));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            if (response.toString().isEmpty()) {
                return new org.json.JSONObject("{}");
            }
            return new org.json.JSONObject(response.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new org.json.JSONObject();
    }

    /**
     * Make HTTP Call using org.apache.http.client
     * @param targetURL
     * @param requestType
     * @param requestParams
     * @param headers
     * @return org.json.JSONObject Response from HTTP call
     */
    protected JsonOutput makeRestCallUsingHttpClient(String targetURL, String requestType, String requestParams, HashMap<String, String> headers) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpUriRequest request = null;

        switch (requestType) {
            case "GET":
               HttpGet getrequest = new HttpGet(targetURL);
                request = getrequest;
                break;
            case "DELETE":
                HttpDelete deleteRequest = new HttpDelete(targetURL);
                request = deleteRequest;
                break;
            case "POST":
                HttpPost postRequest = new HttpPost(targetURL);
                if (headers.isEmpty()) {
                    postRequest.setHeader("Content-Type", "application/json");
                }
                try {
                    StringEntity input = new StringEntity(requestParams);
                    postRequest.setEntity(input);
                } catch (UnsupportedEncodingException e) {
                    System.out.println("Unsupported Encoding in POST call");
                    e.printStackTrace();
                }
                request = postRequest;
                break;
        }

        /* Adding the headers at one place */
        if (!headers.isEmpty()) {
            try {
                for (HashMap.Entry<String, String> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    request.addHeader(key, value);
                }
            } catch (NullPointerException e) {
                System.out.println("Request is not initialized! Please check the Request Type column in the excel.");
                e.printStackTrace();
            }
        }

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String accessToken = "";

        // If the caller is NOT "getAccessToken", then get the access token
        if (!(stackTraceElements[2].getMethodName().equals("getAccessToken"))) {
            System.out.println("********Getting Access Token********");
            accessToken = getAccessToken();
            request.addHeader ("Authorization", "Bearer " + accessToken);
            System.out.println("********Making HTTP Call********");
        }

        HttpResponse response = null;
        String strResponse = null;

        try {
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if(entity != null){
                strResponse = GenericUtility.getStringFromInputStream(entity.getContent());
            }
            else{
                // Response doesn't have a body(DELETE call response in this case)
                strResponse = "{}";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.toString().isEmpty()) {
            return new JsonOutput();
        }
//        return new org.json.JSONObject(strResponse.toString());
        try{
            return new JsonOutput(response.getStatusLine().getStatusCode(), new org.json.JSONObject(strResponse),response.getAllHeaders());
        }
        catch (org.json.JSONException e){
            return new JsonOutput(response.getStatusLine().getStatusCode(), new org.json.JSONArray(strResponse),response.getAllHeaders());
        }
    }

    /**
     * Overloaded method for making HTTP Call with default headers
     * @param targetURL
     * @param requestType
     * @return
     */
    protected JsonOutput makeRestCallUsingHttpClient(String targetURL, String requestType){
        return makeRestCallUsingHttpClient(targetURL, requestType, "", new HashMap<>());
    }

    /**
     * verloaded method for making HTTP Call with default headers
     * @param baseURI
     * @param targetURL
     * @param requestType
     * @return
     */
    protected Response makeRestCallUsingRestAssured(String baseURI, String targetURL, String requestType) {
        return makeRestCallUsingRestAssured(baseURI, targetURL, requestType, " ", new HashMap<>());
    }

    /**
     * Make HTTP Call using rest-assured
     * @param baseURI
     * @param targetURL
     * @param requestType
     * @param requestParams
     * @param headers
     * @return
     */

    protected Response makeRestCallUsingRestAssured(String baseURI, String targetURL, String requestType, String requestParams, HashMap<String, String> headers) {
        try {
            RestAssured.baseURI = baseURI;
            RequestSpecification request = RestAssured.given();
            if (headers.isEmpty()) {
                request.header("Content-Type", "application/json");
            }
            if (!headers.isEmpty()) {
                for (HashMap.Entry<String, String> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    request.header(key, value);
                }
            }
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            String accessToken = "";

            // If the caller is NOT "getAccessToken", then get the access token
            if (!(stackTraceElements[2].getMethodName().equals("getAccessToken"))) {
                System.out.println("********Getting Access Token********");
                accessToken = getAccessToken();
                request.header("Authorization", "Bearer " + accessToken);
                System.out.println("********Making HTTP Call********");
            }

            request.body(requestParams);
            switch (requestType) {
                case "GET":
                    return request.get(targetURL);
                case "DELETE":
                    return request.delete(targetURL);
                case "POST":
                    return request.post(targetURL);
                default:
                    return null;
            }
        }catch (Exception e){
            e.printStackTrace();
                return null;
        }
    }
    /**
     * Get an access token by making a HTTP Call. Request params and credentials are read from /src/main/resources/manifest.json
     * @return Access token string
     */

    private String getAccessToken() {
        String TargetURL = manifestJsonObject.get("token_url").toString();
        String requestType = "POST";
        String requestParams = "grant_type" + "=" + manifestJsonObject.get("grant_type") + "&" + "client_id" + "=" + manifestJsonObject.get("client_id") +
                "&" + "client_secret" + "=" + manifestJsonObject.get("client_secret") + "&" + "username" + "=" + manifestJsonObject.get("username") + "&" +
                "password" + "=" + manifestJsonObject.get("password");
        int requestLength = requestParams.length();
        byte[] encodedRequestParams = requestParams.getBytes(StandardCharsets.UTF_8);
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
//        headers.put("Content-Length", Integer.toString(requestLength));
//        org.json.JSONObject response = makeRestCall(TargetURL, requestType, encodedRequestParams, headers);
        JsonOutput response = makeRestCallUsingHttpClient(TargetURL, requestType, requestParams, headers);
        return response.getJsonResponse().get("access_token").toString();
    }

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
         //will maximise the window
         browser.manage().window().maximize();
         //will wait for page to load
         browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
       return browser;
    }

    public String getBrowserStatus() {
        String  status;
        String string = manifestJsonObject.get("closeBrowser").toString();
        switch (string) {
            case "AfterTest":
                status = "AfterTest";
                break;
            case "AfterSuite":
                status = "AfterSuite";
                break;
            default:
                status = "AfterMethod";
                break;
        }
        return status;
    }

    @BeforeSuite(alwaysRun = true, dependsOnMethods = "readManifestJSON")
    public void initDriver(){
        if(getBrowserStatus().equals("AfterSuite")){
            driver = getDriver();
        }
    }

    @BeforeMethod
    public void beforeMethod(){
        if(getBrowserStatus().equals("AfterMethod")){
            driver = getDriver();
        }
    }
    @BeforeTest
    public void beforeTest(){
        if(getBrowserStatus().equals("AfterTest")){
            driver = getDriver();
        }
    }

    @AfterMethod
    public void afterMethod(){
        if(getBrowserStatus().equals("AfterMethod")){

            driver.quit();
        }
    }
    @AfterTest
    public void afterTest(){
        if(getBrowserStatus().equals("AfterTest")){
            driver.quit();
        }
    }
    @AfterSuite
    public void afterSuite(){
        if(getBrowserStatus().equals("AfterSuite")){
            driver.quit();
        }
    }

}
