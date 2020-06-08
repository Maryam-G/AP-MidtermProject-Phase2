package com.company.httpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {

    private HttpURLConnection connection;

    public HttpClient(String urlString, String method, HashMap<String, String> header, HashMap<String, String> body){
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        try {
            if(!urlString.startsWith("http://")){
                urlString = "http://" + urlString;
            }
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            //request setup:
            connection.setRequestMethod(method);
            for(Map.Entry<String, String> entry : header.entrySet()){
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            int status = connection.getResponseCode();

            if(status / 100 != 2){
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while((line = reader.readLine()) != null){
                    responseContent.append(line);
                }
                reader.close();
            }else{
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line = reader.readLine()) != null){
                    responseContent.append(line);
                }
                reader.close();
            }

            System.out.println(responseContent.toString());


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
