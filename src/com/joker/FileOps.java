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
    private final List<List<String>> buffer = new ArrayList<>();

    /**
     * Sets the input file information for the readFile method
     * @param path input file path
     * @param fileName input file name
     * @throws IOException IO error handling
     */
    public void setFile(File path, String fileName) throws IOException {
        this.path = path;
        this.fileName = fileName;
        readFile();
    }

    /**
     * Parses the data into a 2D list and groups it by rows.
     * @throws IOException IO error handling
     */
    public void readFile() throws IOException {
        // Read CSV file
        csvFile = new File(path, fileName);
        System.out.println("INFO: Looking for input file: " + "\"" + csvFile.getName() + "\"");
        if (!csvFile.exists()) {
            existence = false;
            System.out.println("WARNING: File not found.");
        } else {
            existence = true;
            System.out.println("INFO: File found. Read file: " + csvFile.getName());
            BufferedReader csvReader = new BufferedReader(new FileReader(new File(path, fileName)));
            String line;
            while ((line = csvReader.readLine()) != null) {
                String[] data = line.split(",");
                buffer.add(Arrays.asList(data));
            }
            csvReader.close();
        }
    }

    /**
     * Getter for the buffer
     * @return buffer
     */
    public List<List<String>> getBuffer() {
        return buffer;
    }

    /**
     * Getter for the csvFile state
     * @return csvFile
     */
    public File getCsvFile() {
        return csvFile;
    }

    /**
     * Determines the input file's existence
     * @return csvFile's existence
     */
    public boolean getExistence() {
        return existence;
    }
}