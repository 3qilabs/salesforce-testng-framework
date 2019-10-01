package com.salesforce.base;

import com.salesforce.core.ConfigurationManager;
import com.salesforce.utilities.GenericUtility;
import com.salesforce.utilities.PropertyUtil;
import com.sun.xml.internal.rngom.parse.host.Base;
import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * This is the base class with methods to make HTTP calls
 * This also has a TestNG Data Provider that reads data from an excel
 * The HTTP Calls made in these functions get a new access token for every call and credentials are read from /src/test/resources/manifest.json
 */
public class BaseClass {

    public BaseClass(){
        this.logger = Logger.getLogger("BaseClass");
        PropertyConfigurator.configure("log4j.properties");

    }
    public Logger logger;
    public String pwd = System.getProperty("user.dir");

    /**
     * Field to store all the environment params from /src/test/resources/manifest.json
     */
    public static JSONObject manifestJsonObject;
    protected PropertyUtil props = ConfigurationManager.getBundle();

    /**
     * This is a TestNG Data Provider, it reads data from an excel at /src/test/resources/
     *
     * @param testcaseName
     * @return A Hash Map for the test case being executed: {testcase_id => {"column_header" => "cell_value"}}
     */
    @DataProvider(name = "excelData")
    public Object[][] readExcelData(Method testcaseName) {
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
                        if(sheet.getRow(rowNum).getCell(columNum) == null){
                            continue;
                        }
                        sheet.getRow(rowNum).getCell(columNum).setCellType(Cell.CELL_TYPE_STRING);
                        String value = sheet.getRow(rowNum).getCell(columNum).getStringCellValue();
                        rowData.put(key, value);
                    }
                    excelData.put(sheet.getRow(rowNum).getCell(0).getStringCellValue(), rowData);
                }
            }
        } catch (IOException ex) {
//            logger.info("Exception in readExcelData: \n");
            ex.printStackTrace();
        }
        return new Object[][]{{excelData.get(testcaseName.getName())}};
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
//            logger.info("manifest file not found");
            e.printStackTrace();
        } catch (IOException e) {
//            logger.info("Exception in opening manifest file");
            e.printStackTrace();
        } catch (ParseException e) {
//            logger.info("Exception in parsing manifest file");
            e.printStackTrace();
        }
    }

    /**
     * Make HTTP Call using java.net.HttpURLConnection
     *
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
     *
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
     *
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
//                logger.info("********Getting Access Token********");
                //accessToken = getAccessToken();
                getAccessToken();
                accessToken = props.getProperty("accessToken").toString();
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            }
            if (!headers.isEmpty()) {
                for (HashMap.Entry<String, String> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    connection.setRequestProperty(key, value);
                }
            }
//            logger.info("********Making Rest Call******** \n" + requestType + ": " + "\nURL: " + targetURL);
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
//                logger.info("The resource you are looking for is not found");
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
     *
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
//                    logger.info("Unsupported Encoding in POST call");
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
//                logger.info("Request is not initialized! Please check the Request Type column in the excel.");
                e.printStackTrace();
            }
        }

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String accessToken = "";

        // If the caller is NOT "getAccessToken", then get the access token
        if (!(stackTraceElements[2].getMethodName().equals("getAccessToken"))) {
//            logger.info("********Getting Access Token********");
           // accessToken = getAccessToken();
            getAccessToken();
            accessToken = props.getProperty("accessToken").toString();
            request.addHeader("Authorization", "Bearer " + accessToken);
//            logger.info("********Making HTTP Call********");
        }

        HttpResponse response = null;
        String strResponse = null;

        try {
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                strResponse = GenericUtility.getStringFromInputStream(entity.getContent());
            } else {
                // Response doesn't have a body(DELETE call response in this case)
                strResponse = "{}";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        httpClient.close();
        if (response.toString().isEmpty()) {
            return new JsonOutput();
        }
//        return new org.json.JSONObject(strResponse.toString());
        try {
            return new JsonOutput(response.getStatusLine().getStatusCode(), new org.json.JSONObject(strResponse), response.getAllHeaders());
        } catch (org.json.JSONException e) {
            return new JsonOutput(response.getStatusLine().getStatusCode(), new org.json.JSONArray(strResponse), response.getAllHeaders());
        }
    }

    /**
     * Overloaded method for making HTTP Call with default headers
     *
     * @param targetURL
     * @param requestType
     * @return
     */
    protected JsonOutput makeRestCallUsingHttpClient(String targetURL, String requestType) {
        return makeRestCallUsingHttpClient(targetURL, requestType, "", new HashMap<>());
    }

    /**
     * verloaded method for making HTTP Call with default headers
     *
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
     *
     * @param baseURI
     * @param endPoint
     * @param requestType
     * @param requestParams
     * @param headers
     * @return
     */

    protected Response makeRestCallUsingRestAssured(String baseURI, String endPoint, String requestType, Object requestParams, HashMap<String, String> headers) {
        try {
            try {
                RestAssured.baseURI = baseURI;
            } catch (NoClassDefFoundError e) {
                RestAssured.baseURI = baseURI;
            }
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
                this.logger.info("********Getting Access Token********");
                //accessToken = getAccessToken();
                getAccessToken();
                accessToken = props.getProperty("accessToken").toString();
                request.header("Authorization", "Bearer " + accessToken);
                this.logger.info("********Making HTTP Call********");
            }

            if (requestParams.getClass().equals("String")){
                request.body(requestParams);
            }else if (!requestParams.toString().isEmpty()) {
/*
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(requestParams);*/
                request.body(requestParams, ObjectMapperType.GSON);
            }
            JsonOutput jsonOutput = new JsonOutput();
            switch (requestType) {
                case "GET":
                    return request.get(endPoint);
                case "DELETE":
                    return request.delete(endPoint);
                case "POST":
                    return request.post(endPoint);
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get an access token by making a HTTP Call. Request params and credentials are read from /src/test/resources/manifest.json
     *
     * @return Access token string
     */
    private void getAccessToken() {
        String TargetURL = props.getProperty("token_url").toString();
        String requestType = "POST";
        String requestParams = "grant_type" + "=" + props.getProperty("grant_type") + "&" + "client_id" + "=" + props.getProperty("client_id") +
                "&" + "client_secret" + "=" + props.getProperty("client_secret") + "&" + "username" + "=" + props.getProperty("username") + "&" +
                "password" + "=" + props.getProperty("password");
        int requestLength = requestParams.length();
        byte[] encodedRequestParams = requestParams.getBytes(StandardCharsets.UTF_8);
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        LocalDateTime localDateTime = LocalDateTime.now();
        long currentAccessTokenTimeStamp = Long.parseLong(dateTimeFormatter.format(localDateTime));

        if(props.getProperty("accessTokenTimeStamp") == null){
          props.setProperty("accessTokenTimeStamp", 0);
        }
        if((currentAccessTokenTimeStamp - Long.parseLong(props.getProperty("accessTokenTimeStamp").toString()))>5){
            JsonOutput response = makeRestCallUsingHttpClient(TargetURL, requestType, requestParams, headers);
            props.setProperty("accessToken",response.getJsonResponse().get("access_token"));
            long tokenTimeStamp = Long.parseLong(dateTimeFormatter.format(localDateTime));
            props.setProperty("accessTokenTimeStamp", tokenTimeStamp);
        }
    }
}

