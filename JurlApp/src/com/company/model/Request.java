package com.company.model;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;

public class Request implements Serializable {

    private String urlString;
    private String method;
    private HashMap<String, String> requestHeaders;
    private HashMap<String, String> requestBody;

    public Request(String urlString, String method, HashMap<String, String> requestHeaders, HashMap<String, String> requestBody){
        this.urlString = urlString;
        this.method = method;
        this.requestHeaders = requestHeaders;
        this.requestBody = requestBody;
    }

    @Override
    public String toString() {
        return  " url: " + urlString + " | " +
                " method: " + method + " | " +
                " headers: " + requestHeaders.toString() + " | " +
                " body: " + requestBody.toString()
                ;
    }
}
