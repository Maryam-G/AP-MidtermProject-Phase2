package com.company.jurl;

import com.company.model.Request;
import com.company.utils.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Jurl2 {

    private String input;
    private String[] partsOfInput;

    private String urlString;
    private String method;
    private HashMap<String, String> requestHeaders;
    private HashMap<String, String> requestBody;

    private boolean showResponseHeaders;
    private boolean followRedirect;

    public Jurl2(String newInput){
        input = newInput;
        partsOfInput = input.split(" ");
        urlString = null;
        //method of request is GET by default
        method = "GET";
        requestHeaders = new HashMap<>();
        requestBody = new HashMap<>();

        showResponseHeaders = false;
        followRedirect = false;
    }

    public void setUrlString(){
        // when input contains argument "--url"
        int index = indexOfString("--url") + 1;
        urlString = partsOfInput[index];
    }

    public void setMethod(){
        // when input contains argument "--method" or "-M"
        int index;
        if(input.contains("--method"))
            index = indexOfString("--method") + 1;
        else
            index = indexOfString("-M") + 1;

        String currentMethod = partsOfInput[index].toUpperCase();
        if(currentMethod.equals("GET") || currentMethod.equals("POST") || currentMethod.equals("PUT") || currentMethod.equals("DELETE") || currentMethod.equals("PATCH")){
            method = partsOfInput[index].toUpperCase();
        }else{
            System.err.println("Invalid request method!");
            //TODO : Exception or System.err ???
        }
    }

    public void setRequestHeaders(){
        // when input contains "--headers" or "-H"

        int index;
        if(input.contains("--headers"))
            index = indexOfString("--headers") + 1;
        else
            index = indexOfString("-H") + 1;

        String headersString = partsOfInput[index].substring(1, partsOfInput[index].length()-1);
        //TODO : header ha ba ";" az ham goda mishan ya "&" ???
        //TODO : handel kardan e khata
        String[] allHeaders = headersString.split(";");
        String key, value;
        for(String currentHeader : allHeaders){
            //TODO : key , value ha ba ":" az ham goda mishan ya "=" ???
            //TODO : handel kardan e khata
            key = currentHeader.split(":")[0];
            value = currentHeader.split(":")[1];

            if(key != null && value != null){
                requestHeaders.put(key, value);
            }else{
                //TODO : Exception or system.err ???
                System.err.println("Null key or null value in request headers!");
            }
        }
    }

    public void setRequestBody(){
        // when input contains "--data" or "-d"

        int index;
        if(input.contains("--data"))
            index = indexOfString("--data") + 1;
        else
            index = indexOfString("-d") + 1;

        String bodyString = partsOfInput[index].substring(1, partsOfInput[index].length()-1);
        //TODO : header ha ba ";" az ham goda mishan ya "&" ???
        //TODO : handel kardan e khata
        String[] partsOfBody = bodyString.split("&");
        String key, value;
        for(String currentPart : partsOfBody){
            //TODO : key , value ha ba ":" az ham goda mishan ya "=" ???
            //TODO : handel kardan e khata
            key = currentPart.split("=")[0];
            value = currentPart.split("=")[1];

            if(key != null && value != null){
                requestBody.put(key, value);
            }else{
                //TODO : Exception or system.err ???
                System.err.println("Null key or null value in request body!");
            }
        }
    }

    public void setShowResponseHeaders(){
        // when input contains argument "-i"
        showResponseHeaders = true;
    }

    public void printHelpList (){
        System.out.println("List of arguments : " +
                "-h or --help         => showing list of arguments in jurl" +
                //TODO : PATCH hst ya na ???
                "-M or --methods      => method of request ( GET[by-default] - PUT - POST - DELETE - PATCH )\n" +
                //TODO : goda kardan e header ha ba ";" ya "&" ???
                "-H or --headers      => set headers of request with \":\" and \";\" (for example : \"key1:value1;key2:value2\" )" +
                //TODO : "=" ya ":" -- ";" ya "&" ???
                "-d or --data          => set body [Form-data] of request with \"=\" and \"&\" (for example : \"key1=value1&key2=value2\" )" +
                "-i                   => for showing headers of response"
                //TODO : kamel kardan e list
        );
    }

    public void setFollowRedirect(){
        // when input contains argument "-f"
        followRedirect = true;
    }

    public void saveRequestInFile(Request newRequest){
        // when input contains argument "--save" or "-S"
        int index;
        if(input.contains("--save"))
            index = indexOfString("--save") + 1;
        else
            index = indexOfString("-S") + 1;

        boolean saveFileInMainDirectory;
        if(partsOfInput[index].startsWith("-")){
            // save request in main directory (AllRequests)
            saveFileInMainDirectory = true;
            FileUtils.writeRequestInFile(newRequest, saveFileInMainDirectory, null);
        }else{
            // save request in another directory (name of this directory is in partsOfInput[index])
            String directoryName = partsOfInput[index];
            if (FileUtils.findDirectoryInRequestDirectories(directoryName)) {
                saveFileInMainDirectory = false;
                FileUtils.writeRequestInFile(newRequest, saveFileInMainDirectory, directoryName);
            }else{
                //TODO : Exception or System.err
                System.err.println("This directory doesn't exist!");
            }
        }
    }


    public int indexOfString (String string){
        int index = 0;
        for(String s : partsOfInput){
            if(s.equals(string)){
                return index;
            }
            index++;
        }
        return -1;
    }

}
