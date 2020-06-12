package com.company;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;

import com.company.jurl.Jurl;

public class Main {

    public static void main(String[] args) {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        Scanner scanner = new Scanner(System.in);
        String newLine;
        while(true){
            newLine = scanner.nextLine();
            Jurl jurl = new Jurl(newLine);
        }



    }
}
