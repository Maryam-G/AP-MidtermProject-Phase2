package com.company.jurl;

import com.company.httpClient.HttpClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Jurl {

    private String request;
    private String[] partsOfRequest;

    private String urlString;
    private String method;
    private HashMap<String, String> headers;
    private ArrayList<String> body;

    private boolean showResponseHeaders;
    private boolean followRedirect;

    public Jurl(String newRequest){
        request = newRequest;
        partsOfRequest = newRequest.split(" ");
        headers = new HashMap<>();
        checkRequest();
    }

    public void checkRequest(){
        int index;
        if (partsOfRequest[0].equals(">jurl")){
            if(request.contains("--url")){
                index = indexOfString("--url") + 1;
                urlString = partsOfRequest[index];
            }else{
                System.err.println("url nadare");
            }

            if(request.contains("--method") || request.contains("-M")){
                if(request.contains("--method"))
                    index = indexOfString("--method") + 1;
                else
                    index = indexOfString("-M") + 1;

                String currentMethod = partsOfRequest[index].toUpperCase();
                if(currentMethod.equals("GET") || currentMethod.equals("POST") || currentMethod.equals("PUT")){
                    method = partsOfRequest[index].toUpperCase();
                }else{
                    System.err.println("invalid method");
                }
            }else{
                //method of request is GET by default
                method = "GET";
            }

            if(request.contains("--headers") || request.contains("-H")){
                if(request.contains("--headers"))
                    index = indexOfString("--headers") + 1;
                else
                    index = indexOfString("-H") + 1;
                setHeaders(partsOfRequest[index].substring(1, partsOfRequest[index].length()-1));
            }

            if(request.contains("-i")){
                showResponseHeaders = true;
            }else {
                showResponseHeaders = false;
            }

            if(request.contains("-f")){
                followRedirect = true;
            }else{
                followRedirect = false;
            }

            HttpClient httpClient = new HttpClient(urlString, method, headers);


            //prints:
            System.out.println(urlString);
            System.out.println(method);
            System.out.println(headers.toString());
        }else{
            System.err.println(">jurl to naneveshti !");
        }
    }

    public void setHeaders(String headersString){
        String[] allHeaders= headersString.split(";");
        String key, value;
        for(String currentHeader : allHeaders){
            key = currentHeader.split(":")[0];
            value = currentHeader.split(":")[1];
            if(key != null && value != null){
                headers.put(key, value);
            }else{
                System.err.println("null key or null value");
            }
        }
    }

    public void printHelpList(){
        System.out.println("List of arguments : " +
                "-M or --methods      => method of request (GET(by-default) - PUT - POST\n" +
                "-H or --headers      => headers of request (for example : \"key1:value1;key2:value2\" )" +
                "-i                   => for showing headers of response" +
                "-h or --help         => showing list of arguments in jurl" +
                ""
                );
    }

    public int indexOfString (String string){
        int index = 0;
        for(String s : partsOfRequest){
            if(s.equals(string)){
                return index;
            }
            index++;
        }
        return -1;
    }
}
