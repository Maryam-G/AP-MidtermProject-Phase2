package com.company.httpClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpClientHandler {

    private URL url;
    private String method;
    private HttpURLConnection connection;

    private HashMap<String , String> requestHeaders;

    private HashMap<String , String> responseHeaders;

    public HttpClientHandler(){

    }

    public void setUrl(String urlString) {
        if(!urlString.startsWith("http://")){
            urlString = "http://" + urlString;
        }
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setMethod(String method){
        this.method = method;
    }

    public void setRequestHeaders(HashMap<String, String> requestHeaders){
        this.requestHeaders = requestHeaders;
    }

    public void setConnection() {
        try {
            connection = (HttpURLConnection) url.openConnection();

            //request setup:
            connection.setRequestMethod(method);
//            for(Map.Entry<String, String> entry : requestHeaders)

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
