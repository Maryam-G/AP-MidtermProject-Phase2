package com.company.jurl;

import com.company.httpClient.HttpClient;
import com.company.model.Request;
import com.company.utils.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jurl {
    //TODO : age url ro eshtebah bede

    private String input;
    private String[] partsOfInput;

    private String urlString;
    private String method;
    private HashMap<String, String> requestHeaders;
    private HashMap<String, String> requestBody;

    private boolean hasUrl;

    private boolean hasSaveArgument, saved;
    private boolean hasMethodArgument, validMethod;
    private boolean hasHeadersArgument, validHeaders;
    private boolean hasDataArguments, validData;

    public Jurl(String newInput){
        input = newInput;
        partsOfInput = input.split(" ");
        urlString = null;
        //method of request is GET by default
        method = "GET";
        requestHeaders = new HashMap<>();
        requestBody = new HashMap<>();

        hasUrl = false;

        hasSaveArgument = saved = false;
        hasMethodArgument = validMethod = false;
        hasHeadersArgument = validHeaders = false;
        hasDataArguments = validData = false;

        checkRequest();
    }

    public void checkRequest(){
        if(input.toLowerCase().startsWith(">jurl")){
            if(partsOfInput.length > 1){
                if(partsOfInput[1].toLowerCase().equals("create")){
                    createCollection();
                }else if(partsOfInput[1].toLowerCase().equals("list")){
                    showList();
                }else if(partsOfInput[1].toLowerCase().equals("fire")){
                    // TODO : fire
                    fire();
                }else{
                    //--help or -h :
                    if(findArgument("--help") || findArgument("-h")){
                        printHelpList();
                    }else {
                        // --url :
                        if (findArgument("--url")) {
                            setUrlString();
                        } else {
                            //TODO : err or exception
                            System.err.println("No URL!");
                        }

                        if (hasUrl) {
                            // --method or -M :
                            if (findArgument("--method") || findArgument("-M")) {
                                hasMethodArgument = true;
                                setMethod();
                            }

                            // --headers or -H :
                            if (findArgument("--headers") || findArgument("-H")) {
                                hasHeadersArgument = true;
                                setRequestHeaders();
                            }

                            // --data or-d :
                            if (findArgument("--data") || findArgument("-d")) {
                                hasDataArguments = true;
                                setRequestBody();
                            }

                            if((!(hasMethodArgument && !validMethod)) && (!(hasHeadersArgument && !validHeaders)) && (!(hasDataArguments && !hasDataArguments))){
                                // --save or -S :
                                if (findArgument("--save") || findArgument("-S")) {
                                    hasSaveArgument = true;
                                    saveRequest(new Request(urlString, method, requestHeaders, requestBody));
                                }

                                if(!(hasSaveArgument && !saved)){
                                    HttpClient httpClient = new HttpClient(urlString, method, requestHeaders, requestBody);

                                    printRequestInformation();
                                    System.out.println("\n" + httpClient.getResponseBody());

                                    // -i :
                                    if (findArgument("-i")) {
                                        showResponseHeaders(httpClient.getResponseHeaders());
                                    }

                                    // --output or -O :
                                    if (findArgument("--output") || findArgument("-O")) {
                                        saveResponseBody(httpClient.getResponseBody());
                                    }
                                }

                            }
                        }
                    }
                }
            }else{
                //TODO : err or exception
                System.err.println("Input for >jurl ?!");
            }
        }else{
            //TODO : err or exception
            System.err.println("Invalid statement!");
        }
    }

    public void setUrlString(){
        // when input contains argument "--url"
        int index = indexOfString("--url") + 1;
        if(index >= partsOfInput.length || partsOfInput[index].startsWith("-")){
            System.err.println("Unwritten URL! -> --url [...]");
        }else{
            urlString = partsOfInput[index];
            hasUrl = true;
        }
    }

    public void setMethod(){
        // when input contains argument "--method" or "-M"
        int index;
        if(input.contains("--method"))
            index = indexOfString("--method") + 1;
        else
            index = indexOfString("-M") + 1;
        if(index >= partsOfInput.length || partsOfInput[index].startsWith("-")){
            System.err.println("Unwritten method! -> --method [...]");
        }else{
            //TODO : PATCH hast ya na
            String currentMethod = partsOfInput[index].toUpperCase();
            if(currentMethod.equals("GET") || currentMethod.equals("POST") || currentMethod.equals("PUT") || currentMethod.equals("DELETE") || currentMethod.equals("PATCH")){
                method = partsOfInput[index].toUpperCase();
                validMethod = true;
            }else{
                //todo: patch hast ya na
                System.err.println("Invalid request method! (valid methods: GET, POST, PUT, DELETE, PATCH)");
                //TODO : Exception or System.err ???
            }
        }
    }

    public void setRequestHeaders(){
        // when input contains "--headers" or "-H"

        int index;
        if(input.contains("--headers"))
            index = indexOfString("--headers") + 1;
        else
            index = indexOfString("-H") + 1;

        if(index >= partsOfInput.length || partsOfInput[index].startsWith("-")){
            System.err.println("Unwritten headers! -> --headers [\"...\"]");
        }else{
            String headersString = partsOfInput[index].substring(1, partsOfInput[index].length()-1);
            //TODO : header ha ba ";" az ham goda mishan ya "&" ???
            //TODO : handel kardan e khata
            String[] allHeaders = headersString.split(";");
            String key, value;
            ArrayList<Boolean> booleanValues = new ArrayList<>();
            for(String currentHeader : allHeaders){
                //TODO : key , value ha ba ":" az ham goda mishan ya "=" ???
                //TODO : handel kardan e khata
                if(currentHeader.split(":").length == 2){
                    key = currentHeader.split(":")[0];
                    value = currentHeader.split(":")[1];

                    if(key != null && value != null){
                        requestHeaders.put(key, value);
                        booleanValues.add(true);
                    }else{
                        //TODO : Exception or system.err ???
                        System.err.println("Null key or null value in request headers!");
                        booleanValues.add(false);
                    }
                }else{
                    //todo :
                    //TODO : Exception or system.err ???
                    System.err.println("Null key or null value in request headers!");
                    booleanValues.add(false);
                }
            }

            //check validHeaders :
            boolean flag = true;
            for(Boolean b : booleanValues){
                if(b == false){
                    flag = false;
                    validHeaders = false;
                    break;
                }
            }
            if(flag){
                validHeaders = true;
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

        if(index >= partsOfInput.length || partsOfInput[index].startsWith("-")){
            System.err.println("Unwritten body! -> --data [\"...\"]");
        }else{
            String bodyStr = partsOfInput[index].substring(1, partsOfInput[index].length()-1);
            //TODO : header ha ba ";" az ham goda mishan ya "&" ???
            //TODO : handel kardan e khata
            String[] partsOfBody = bodyStr.split("&");
            String key, value;
            ArrayList<Boolean> booleanValues = new ArrayList<>();
            for(String currentPart : partsOfBody){
                //TODO : key , value ha ba ":" az ham goda mishan ya "=" ???
                //TODO : handel kardan e khata
                if(currentPart.split("=").length == 2){
                    key = currentPart.split("=")[0];
                    value = currentPart.split("=")[1];

                    if(key != null && value != null){
                        requestBody.put(key, value);
                        booleanValues.add(true);
                    }else{
                        //TODO : Exception or system.err ???
                        System.err.println("Null key or null value in request body!");
                        booleanValues.add(false);
                    }
                }else{
                    //TODO : Exception or system.err ???
                    System.err.println("Null key or null value in request body!");
                    booleanValues.add(false);
                }
            }

            //check validData :
            boolean flag = true;
            for(Boolean b : booleanValues){
                if(b == false){
                    flag = false;
                    validData = false;
                    break;
                }
            }
            if(flag){
                validData = true;
            }
        }
    }

    public void showResponseHeaders(Map<String, List<String>> responseHeaders){
        // when input contains argument "-i"
        System.out.println("\n");
        for (Map.Entry<String, List<String>> entry : responseHeaders.entrySet()) {
            if(entry.getKey() == null){
                System.out.println(entry.getValue());
                break;
            }
        }
        for (Map.Entry<String, List<String>> entry : responseHeaders.entrySet()) {
            if(entry.getKey() != null){
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    public void saveResponseBody(String responseBody){
        // when input contains argument "--output" or "-O"
        int index;
        if(input.contains("--output"))
            index = indexOfString("--output") + 1;
        else
            index = indexOfString("-O") + 1;

        boolean hasName;
        String fileName = null;
        if(index >= partsOfInput.length || partsOfInput[index].startsWith("-")){ // --output without name of file
            hasName = false;
        }else{ // --output with name of file
            hasName = true;
            fileName = partsOfInput[index];
        }

        FileUtils.saveResponseBodyInFile(responseBody, hasName, fileName);
        System.out.println("\u2192 Response body saved in file ...");
    }

    public void printHelpList (){
        System.out.println(
                "\u25B7 List of statements : " + "\n" +
                "   \u25B8 >jurl --help or >jurl -h  => show list of statements and arguments in jurl application" + "\n" +
                "   \u25B8 >jurl create [...]        => create new collection for requests                                     ([...] -> collection name)" + "\n" +
                "   \u25B8 >jurl list                => show list of collections" + "\n" +
                "   \u25B8 >jurl list [...]          => show list of requests in selected collection                           ([...] -> collection name)" + "\n" +
                "   \u25B8 >jurl fire [...] [...]    => resend saved requests                                                  ([...]-1 -> collection name , [...]-2 -> the numbers of saved requests)" + "\n" +
                "\u25B7 List of arguments : " + "\n" +
                "   \u25B8 --url [...]               => set URL of request                                                     ([...] -> url address)" + "\n" +
                //TODO : PATCH hst ya na ???
                "   \u25B8 -M or --methods [...]     => set method of request                                                  ([...] -> GET[by-default] - PUT - POST - DELETE - PATCH )" + "\n" +
                //TODO : goda kardan e header ha ba ";" ya "&" ???
                "   \u25B8 -H or --headers [...]     => set headers of request with \":\" and \";\"                                ([...] -> for example : \"key1:value1;key2:value2\" )" + "\n" +
                //TODO : "=" ya ":" -- ";" ya "&" ???
                "   \u25B8 -d or --data [...]        => set body [Form-data] of request with \"=\" and \"&\"                       ([...] -> for example : \"key1=value1&key2=value2\" )" + "\n" +
                "   \u25B8 -S or --save [...]        => save request (URL-method-headers-body) in selected collection          ([...] -> name of collection )" + "\n" +
                "   \u25B8 -i                        => showing headers of response" + "\n" +
                "   \u25B8 -O or --output ~[...]     => save response body in file                                             ([...] -> name of file [\"~\" means optional] )" + "\n"
                //TODO : kamel kardan e list
        );
    }

    public void saveRequest(Request newRequest){
        // when input contains argument "--save" or "-S"
        int index;
        if(input.contains("--save"))
            index = indexOfString("--save") + 1;
        else
            index = indexOfString("-S") + 1;

        // save request in one collection (name of this collection is in partsOfInput[index])
        if(index >= partsOfInput.length){
            //TODO : Exception or System.err
            System.err.println("Saving request in which collection?!");
        }else{
            String collectionName = partsOfInput[index];
            if (FileUtils.findDirectoryInRequestDirectories(collectionName)) {
                FileUtils.writeRequestInFile(newRequest, collectionName);
                saved = true;
            }else{
                //TODO : Exception or System.err
                System.err.println("Invalid name for directory!");
            }
        }
    }

    public void showList(){
        // when input is ">jurl list" or ">jurl list listName"
        if(partsOfInput.length == 2){
            // ">jurl list" : (show list of Collections in directory <AllCollections>)
            ArrayList<String> nameOfAllCollections = FileUtils.listOfAllCollections();
            if(nameOfAllCollections.size() == 0){
                System.out.println("No collection ...");
            }
            // print list of collections:
            int count = 1;
            for (String currentName : nameOfAllCollections){
                System.out.println(count + ". " + currentName);
                count++;
            }
        }else{
            // ">jurl list listName" : (show list of requests in directory <listName>)
            String directoryName = input.substring(11);
            if(FileUtils.findDirectoryInRequestDirectories(directoryName)){
                ArrayList<Request> requests = new ArrayList<>();
                requests = FileUtils.listOfAllRequestsInDirectory(directoryName);
                if(requests.size() == 0){
                    System.out.println("No request in this collection ...");
                }
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

    public void fire(){
        if(partsOfInput.length == 2){ // >jurl fire
            //TODO : Exception or System.err
            System.err.println("Name of collection?!");
        }else if(partsOfInput.length == 3){ // >jurl fire collectionName
            //TODO : Exception or System.err
            System.err.println("Request number in collection?!");
        }else if(partsOfInput.length > 3){
            String collectionName = partsOfInput[2];
            ArrayList<Integer> inputNumbers = new ArrayList<>();
            for(int i = 3; i < partsOfInput.length; i++){
                inputNumbers.add(Integer.parseInt(partsOfInput[i]));
            }

            if (FileUtils.findDirectoryInRequestDirectories(collectionName)) {
                ArrayList<Request> requestsInCollection = new ArrayList<>();
                requestsInCollection = FileUtils.listOfAllRequestsInDirectory(collectionName);
                for(int currentNumber : inputNumbers){
                    if((currentNumber >= 1) && (currentNumber <= requestsInCollection.size())){
                        Request currentRequest = requestsInCollection.get(currentNumber - 1);
                        HttpClient newHttpClient = new HttpClient(currentRequest.getUrlString(), currentRequest.getMethod(), currentRequest.getRequestHeaders(), currentRequest.getRequestBody());
                        //TODO : print request and response ...

                        System.out.println("\n\n" + "\u25CF Collection " + collectionName + " - Request " + currentNumber + " => " + currentRequest.toString());
                        showResponseHeaders(newHttpClient.getResponseHeaders());
                        System.out.println("\n" + newHttpClient.getResponseBody());
                    }else{
                        //TODO : Exception or System.err
                        System.err.println("Invalid number for request in collection!");
                    }
                }
            }else{
                //TODO : Exception or System.err
                System.err.println("Invalid name for collection!");
            }
        }
    }

    public void printRequestInformation(){
        // method :
        if(hasMethodArgument && validMethod){
            System.out.println("\u2192 Request method was set on " + method + "...");
        }else if(!hasMethodArgument){
            System.out.println("\u2192 Request method is \"GET\" by default...");
        }

        // headers :
        if(hasHeadersArgument && validHeaders){
            System.out.println("\u2192 Request headers were set...");
        }else if(!hasHeadersArgument){
            System.out.println("\u2192 Empty request headers...");
        }

        // body :
        if(hasDataArguments && validData){
            System.out.println("\u2192 Request body (multipart-formdata) was set...");
        }else if(!hasDataArguments){
            System.out.println("\u2192 Empty request body...");
        }

        // save request :
        if(hasSaveArgument && saved){
            System.out.println("\u2192 Request saved in collection ...");
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

    public boolean findArgument(String argument){
        for(String currentString : partsOfInput){
            if(currentString.equals(argument)){
                return true;
            }
        }
        return false;
    }
}
