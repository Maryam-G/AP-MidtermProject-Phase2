package com.company.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * A class for saving request information for example url, method, headers and body of request.
 *
 * @author Maryam Goli
 */
public class Request implements Serializable {

    private String urlString;
    private String method;
    private HashMap<String, String> requestHeaders;
    private HashMap<String, String> requestBody;

    /**
     * constructor method
     * @param urlString url-address
     * @param method method of request
     * @param requestHeaders headers of request
     * @param requestBody body of request
     */
    public Request(String urlString, String method, HashMap<String, String> requestHeaders, HashMap<String, String> requestBody){
        this.urlString = urlString;
        this.method = method;
        this.requestHeaders = requestHeaders;
        this.requestBody = requestBody;
    }

    /**
     * get url string
     * @return urlString field
     */
    public String getUrlString() {
        return urlString;
    }

    /**
     * get method of request
     * @return method field
     */
    public String getMethod() {
        return method;
    }

    /**
     * get headers of request in a HashMap with key and value
     * @return requestHeaders field
     */
    public HashMap<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    /**
     * get body of request in a HashMap with key and value
     * @return requestBody field
     */
    public HashMap<String, String> getRequestBody() {
        return requestBody;
    }

    /**
     * get information about request in a String
     * @return request-information
     */
    @Override
    public String toString() {
        return  " url: " + urlString + " | " +
                " method: " + method + " | " +
                " headers: " + requestHeaders.toString() + " | " +
                " body: " + requestBody.toString()
                ;
    }

}
