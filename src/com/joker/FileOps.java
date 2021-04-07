package com.joker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles requests for searching and parsing data from CSV database files
 */
public class FileOps {

    private File path;
    private String fileName;
    File csvFile;
    boolean existence;
    private final List<List<String>> inputBuffer = new ArrayList<>();

    /**
     * Sets the input file information for the readFile method
     * @param path sets the input file path
     * @param fileName sets the input filename
     * @throws IOException IO error handling
     */
    public void setFile(File path, String fileName) throws IOException {
        this.path = path;
        this.fileName = fileName;
        readFile();
    }

    /**
     * Parses the data from the input CSV file into a 2D list and groups it by rows.
     * @throws IOException IO error handling
     */
    public void readFile() throws IOException {
        csvFile = new File(path, fileName); // Defines input filename and its path.
        System.out.println("INFO: Looking for input file: " +
                "\"" + csvFile.getName() + "\"");
        if (!csvFile.exists()) {
            existence = false;
            System.out.println("WARNING: File not found.");
        } else {
            existence = true;
            System.out.println("INFO: File found. Read file: " + csvFile.getName());
            // Read text in the input CSV file by buffering characters.
            BufferedReader csvReader = new BufferedReader(
                    new FileReader(new File(path, fileName))
            );
            // Reads CSV file text per line.
            String line;
            while ((line = csvReader.readLine()) != null) {
                // Split strings within a line via commas as the delimiter.
                String[] input = line.split(",");
                // Sends data from the array to the inputBuffer 2D list.
                inputBuffer.add(Arrays.asList(input));
            }
            csvReader.close();
        }
    }

    /**
     * Getter for the inputBuffer
     * @return The inputBuffer 2D List
     */
    public List<List<String>> getInputBuffer() {
        return inputBuffer;
    }

    /**
     * Getter for csvFile
     * @return The csvFile variable
     */
    public File getCsvFile() {
        return csvFile;
    }

    /**
     * Getter for the input file's existence
     * @return The boolean for existence
     */
    public boolean getExistence() {
        return existence;
    }
}