package com.company;

import java.util.Scanner;

import com.company.jurl.Jurl;

/**
 * A class for testing JurlApp and get input
 *
 * @author Maryam Goli
 */
public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String newLine;
        while(true){
            newLine = scanner.nextLine();
            Jurl jurl = new Jurl(newLine);
        }

    }
}
