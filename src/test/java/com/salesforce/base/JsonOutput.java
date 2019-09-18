package com.salesforce.base;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonOutput {
    private int responseStatusCode;
    private org.json.JSONObject jsonResponse;
    private org.json.JSONArray jsonArrayResponse;
    private Header[] responseHeaders;

    public JsonOutput(){
        this(0, new JSONObject("{}"), new Header[]{});
    }

    public JsonOutput(int responseStatusCode, JSONObject jsonResponse, Header[] responseHeaders) {
        this.responseStatusCode = responseStatusCode;
        this.jsonResponse = jsonResponse;
        this.responseHeaders = responseHeaders;
    }

    public JsonOutput(int responseStatusCode, JSONArray jsonArrayResponse, Header[] responseHeaders) {
        this.responseStatusCode = responseStatusCode;
        this.jsonArrayResponse = jsonArrayResponse;
        this.responseHeaders = responseHeaders;
    }

    public int getResponseStatusCode() {
        return responseStatusCode;
    }

    public JSONObject getJsonResponse() {
        return jsonResponse;
    }

    public Header[] getResponseHeaders() {
        return responseHeaders;
    }

    public JSONArray getJsonArrayResponse() {
        return jsonArrayResponse;
    }

}
