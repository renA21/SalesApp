package com.joker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Main class: creates required directories, log flag and defining the version of the program.
 */
public class Main {

    // Program version
    public static final String version =  "1.0";

    // Create directories for the application
    public static final File dataDir = new File("salesAppData/");
    public static final File salesDir = new File("salesAppData/sales/");
    public static final File transactionsDir = new File("salesAppData/transactions/");

    // Debug: Flag to enable/disable logging
    private static final boolean logging = false;

    // Print console output to log file
    private static PrintStream out;

    /**
     *  Print program title and version to the console
     */
    public static void programInfo() {
        System.out.println("SalesApp Version: " + version);
    }

    /**
     * Main method, the starting point of the program.
     * Creates directories to store the database files.
     * @param args Sets command line arguments.
     */
    public static void main(String[] args) {
        if (logging) {
            try {
                out = new PrintStream(new FileOutputStream("salesApp_log.txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.setOut(out);
        }
        programInfo();
        dataDir.mkdir();
        salesDir.mkdir();
        transactionsDir.mkdir();
        LaunchScreen.launchUI();
    }
}
