package com.joker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileOps {

    private File path;
    private String fileName;
    File csvFile;
    boolean existence;
    private final List<List<String>> buffer = new ArrayList<>();

    public void setFile(File path, String fileName) throws IOException {
        this.path = path;
        this.fileName = fileName;
        readFile();
    }

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

    public List<List<String>> getBuffer() {
        return buffer;
    }

    public File getCsvFile() {
        return csvFile;
    }

    public boolean getExistence() {
        return existence;
    }
}