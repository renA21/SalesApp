package com.joker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {

    // Program version
    public static final String version =  "1.0 - Release Candidate 1";

    // Create directories for the application
    public static final File dataDir = new File("salesAppData/");
    public static final File salesDir = new File("salesAppData/sales/");
    public static final File transactionsDir = new File("salesAppData/transactions/");

    // Debug: Toggle logging
    private static final boolean logging = false;

    // Print console output to log file
    private static PrintStream out;
    static {
        try {
            out = new PrintStream(new FileOutputStream("salesApp_log.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void programInfo() {
        System.out.println("SalesApp Version: " + version);
    }

    public static void main(String[] args) {
        if (logging) {
            System.setOut(out);
        }
        programInfo();
        dataDir.mkdir();
        salesDir.mkdir();
        transactionsDir.mkdir();
        LaunchScreen.launchUI();
    }
}
