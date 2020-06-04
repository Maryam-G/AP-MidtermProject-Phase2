package com.company.utils;

import com.company.model.Request;

import java.io.*;

public class FileUtils {

    public static boolean findDirectoryInRequestDirectories(String directoryName){
        File directory = new File("./AllRequests");
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

    public static void writeRequestInFile(Request newRequest, Boolean saveFileInMainDirectory, String directoryName){
        File file;
        if(saveFileInMainDirectory == true){
            file = new File("./AllRequests/" + newRequest.getRequestName() + ".txt");
        }else{
            file = new File("./AllRequests/" + directoryName + "/" + newRequest.getRequestName() + ".txt");
        }

        try (ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(file))){
            objectOutput.writeObject(newRequest);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
