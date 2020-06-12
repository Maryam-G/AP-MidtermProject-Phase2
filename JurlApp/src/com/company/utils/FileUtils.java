package com.company.utils;

import com.company.model.Request;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileUtils {

    static {
        boolean directoryCreated0 = new File("./Requests/").mkdirs();
        boolean directoryCreated1 = new File("./Requests/AllCollections/").mkdirs();
        boolean directoryCreated2 = new File("./Responses/").mkdirs();
    }

    public static boolean findDirectoryInRequestDirectories(String directoryName){
        File directory = new File("./Requests/AllCollections");
        File[] fList = directory.listFiles();
        for(File currentFile : fList){
            if(currentFile.isDirectory()){
                if(currentFile.getName().equals(directoryName)){
                    return true;
                }
            }
        }
        return false;
    }

    public static void writeRequestInFile(Request newRequest, String directoryName){

        File file = new File("./Requests/AllCollections/" + directoryName + "/" + "Request-" + (listOfAllRequestsInDirectory(directoryName).size()+1) + ".txt");
        try (ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(file))){
            objectOutput.writeObject(newRequest);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Request readRequestFromFile(File file){
        try (ObjectInputStream objectInput = new ObjectInputStream(new FileInputStream(file))){
            Request request = (Request) objectInput.readObject();
            return request;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Request> listOfAllRequestsInDirectory(String directoryName){
        ArrayList<Request> requests = new ArrayList<>();
        File directory = new File("./Requests/AllCollections/" + directoryName );
        File[] fList = directory.listFiles();
        Request currentRequest;
        for(File currentFile : fList){
            if(currentFile.isFile()){
                currentRequest = readRequestFromFile(currentFile);
                requests.add(currentRequest);
            }
        }
        return requests;
    }

    public static ArrayList<String> listOfAllCollections(){
        ArrayList<String> nameOfAllCollections = new ArrayList<>();
        File mainDirectory = new File("./Requests/AllCollections");
        File[] fList = mainDirectory.listFiles();
        for(File currentFile : fList){
            if(currentFile.isDirectory()){
                nameOfAllCollections.add(currentFile.getName());
            }
        }
        return nameOfAllCollections;
    }

    public static void createNewCollection(String collectionName){
        boolean directoryCreated = new File("./Requests/AllCollections/" + collectionName + "/").mkdirs();
        System.out.println("\u2192 New directory created : " + directoryCreated);
    }

    public static void saveResponseBodyInFile(String responseBodyString, boolean hasName, String fileName){
        File file;
        if(hasName){
            file = new File("./Responses/" + fileName );
        }else{
            String currentDate = new SimpleDateFormat("[yyyy-MM-dd...HH;mm;ss]").format(new Date());
            file = new File("./Responses/output_" + currentDate + ".txt");
        }

        // write in file
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))){
            bufferedWriter.write(responseBodyString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
