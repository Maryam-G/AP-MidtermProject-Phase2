package com.company.jurl;

import com.company.model.Request;
import com.company.utils.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Jurl {

    private String input;
    private String[] partsOfInput;

    private String urlString;
    private String method;
    private HashMap<String, String> requestHeaders;
    private HashMap<String, String> requestBody;

    private boolean showResponseHeaders;
    private boolean followRedirect;

    public Jurl(String newInput){
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

    public void checkRequest(){
        if(input.toLowerCase().startsWith(">jurl")){
            if(partsOfInput[1].toLowerCase().equals("create")){
                createCollection();
            }else if(partsOfInput[1].toLowerCase().equals("list")){
                showList();
            }else if(partsOfInput[1].toLowerCase().equals("fire")){
                // TODO : fire
            }else{
                // --url :
                if(input.contains("--url")){
                    setUrlString();
                }else{
                    //TODO : err or exception
                    System.err.println("Invalid URL! -> --url ...");
                }

                // --method or -M :
                if(input.contains("--method") || input.contains("-M")){
                    setMethod();
                }

                // --headers or -H :
                if(input.contains("--headers") || input.contains("-H")){
                    setRequestHeaders();
                }

                // --data or-d :
                if(input.contains("--data") || input.contains("-d")){
                    setRequestBody();
                }

                // --save or -S :
                if(input.contains("--save") || input.contains("-S")){
                    saveRequestInFile(new Request(urlString, method, requestHeaders, requestBody));
                }

                // -i :

                // --output or -O :

                // --help or -h :
                if(input.contains("--help") || input.contains("-h")){
                    printHelpList();
                }
            }
        }else{
            //TODO : err or exception
            System.err.println("Invalid statement!");
        }
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
                "-d or --data         => set body [Form-data] of request with \"=\" and \"&\" (for example : \"key1=value1&key2=value2\" )" +
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

        // save request in one collection (name of this collection is in partsOfInput[index])
        String collectionName = partsOfInput[index];
        if (FileUtils.findDirectoryInRequestDirectories(collectionName)) {
            FileUtils.writeRequestInFile(newRequest, collectionName);
        }else{
            //TODO : Exception or System.err
            System.err.println("Invalid name for directory!");
        }
    }

    public void showList(){
        // when input is ">jurl list" or ">jurl list listName"
        if(partsOfInput.length == 2){
            // ">jurl list" : (show list of Collections in directory <AllCollections>)
            ArrayList<String> nameOfAllCollections = FileUtils.listOfAllCollections();
            // print list of collections:
            int count = 1;
            for (String currentName : nameOfAllCollections){
                System.out.println(count + ". " + currentName);
            }
        }else{
            // ">jurl list listName" : (show list of requests in directory <listName>)
            String directoryName = input.substring(11);
            if(FileUtils.findDirectoryInRequestDirectories(directoryName)){
                ArrayList<Request> requests = new ArrayList<>();
                requests = FileUtils.listOfAllRequestsInDirectory(directoryName);
                // print list of requests:
                int count = 1;
                for(Request currentRequest : requests){
                    System.out.println(count + ". " + currentRequest.toString());
                    count++;
                }
            }else{
                //TODO : Exception or System.err
                System.err.println("Invalid name for directory!");
            }
        }
    }

    public void createCollection(){
        // >jurl create newCollection
        if(partsOfInput.length == 3){
            FileUtils.createNewCollection(partsOfInput[2]);
        }else{
            //TODO : err or exception
            System.err.println("Invalid name for new collection!");
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
