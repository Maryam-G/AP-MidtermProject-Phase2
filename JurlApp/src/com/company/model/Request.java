package com.company.model;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;

public class Request implements Serializable {

    private String requestName;

    private URL url;
    private String method;
    private HashMap<String, String> requestHeaders;
    private HashMap<String, String> requestBody;

    public Request(String requestName,URL url, String method, HashMap<String, String> requestHeaders, HashMap<String, String> requestBody){
        this.requestName = requestName;
        this.url = url;
        this.method = method;
        this.requestHeaders = requestHeaders;
        this.requestBody = requestBody;
    }

    public String getRequestName() {
        return requestName;
    }

    @Override
    public String toString() {
        return  " url: " + url.toString() + " | " +
                " method: " + method + " | " +
                " headers: " + requestHeaders.toString() + " | " +
                " body: " + requestBody.toString()
                ;
    }
}
