package com.joker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Inventory extends JFrame {
    private static final FileOps invFile = new FileOps(); // FileOps object
    private static DataTypes data;
    private static final ArrayList<DataTypes> invArray = new ArrayList<>();
    private static boolean saved = true; // flag that determines whether changes have been made to the table
    private static final DefaultTableModel tableData = new DefaultTableModel();
    private static final Object[] row = new Object[7];

    private JPanel mainPanel;
    private JTable invTable;
    private JScrollPane invScrollPane;
    private JButton menuButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton saveButton;
    private JLabel readFile;
    private JLabel dateLabel;

    public Inventory(String title) {
        super(title);
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);

        JFrame confirmExit = new JFrame();

        // Exit confirmation when closing the window
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int option = JOptionPane.showConfirmDialog(confirmExit,
                        "Are you sure you want to exit? Any unsaved changes will be lost.",
                        "Exit", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.out.println("INFO: The program closed successfully.");
                    System.exit(0);
                }
            }
        });

        new Timer(0, (ActionEvent e) -> {
            Date d = new Date();
            SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss a yyyy-MM-dd");
            dateLabel.setText(formatTime.format(d));
        }).start();

        readFile.setText("Read File: inventory.csv");

        tableData.setColumnCount(0);

        // JTable columns
        tableData.addColumn("Record #");
        tableData.addColumn("ID");
        tableData.addColumn("Name");
        tableData.addColumn("Supplier");
        tableData.addColumn("Stock");
        tableData.addColumn("Location");
        tableData.addColumn("Price");

        tableData.setRowCount(0);
        fetchTableData();

        invTable.setModel(tableData);
        invScrollPane.setViewportView(invTable);

        menuButton.addActionListener(e -> {
            try {
                if (saved) {
                    System.out.println("INFO: Entered Main Menu.");
                    invFile.getBuffer().clear(); // clear buffer
                    invArray.clear();
                    MainMenu.launchUI();
                    dispose();
                } else {
                    int option = JOptionPane.showConfirmDialog(confirmExit,
                            "Changes were made to the database. Any unsaved data will be lost. Do you want to continue?",
                            "Unsaved Changes", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        System.out.println("WARNING: Changes in the table were not saved.");
                        System.out.println("INFO: Entered Main Menu.");
                        invFile.getBuffer().clear(); // clear buffer
                        invArray.clear();
                        saved = true;
                        MainMenu.launchUI();
                        dispose();
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        addButton.addActionListener(e -> {
            System.out.println("INFO: Opened AddOps.");
            AddOps.launchUI("Inventory");
        });

        editButton.addActionListener(e -> {
            if (invArray.isEmpty()) {
                JFrame alert = new JFrame();
                JOptionPane.showMessageDialog(
                        alert,
                        "There is no data available in the table to edit.",
                        "Edit",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                System.out.println("INFO: Opened EditOps.");
                EditOps.launchUI("Inventory", invArray, null);
            }
        });

        deleteButton.addActionListener(e -> {
            if (invArray.isEmpty()) {
                JFrame alert = new JFrame();
                JOptionPane.showMessageDialog(
                        alert,
                        "There is no data available in the table to delete.",
                        "Delete",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                System.out.println("INFO: Opened DeleteOps.");
                DeleteOps.launchUI("Inventory", invArray, null);
            }
        });

        saveButton.addActionListener(e -> {
            try {
                saveData();
                saved = true;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    private static void fetchTableData() {
        for (int i = 0; i < invArray.size(); i++) {
            row[0] = i;
            row[1] = invArray.get(i).getID();
            row[2] = invArray.get(i).getName();
            row[3] = invArray.get(i).getSupplier();
            row[4] = invArray.get(i).getStock();
            row[5] = invArray.get(i).getLocation();
            row[6] = invArray.get(i).getPrice();
            tableData.addRow(row);
        }

        // just for reference
        /* for (int i = 0; i < invArray.size(); i++) {
            System.out.println(invArray.get(i).getID());
        } */
    }

    private static void inventory() throws IOException {
        invFile.setFile(Main.dataDir,"inventory.csv");

        // Remove first row if column headers exist
        if (!invFile.getBuffer().isEmpty() && invFile.getBuffer().get(0).get(0).equals("ID")) {
            invFile.getBuffer().remove(0);
        }

        for (int i = 0; i < invFile.getBuffer().size(); i++) {
            data = new DataTypes();
            data.setID(invFile.getBuffer().get(i).get(0));
            data.setName(invFile.getBuffer().get(i).get(1));
            data.setSupplier(invFile.getBuffer().get(i).get(2));
            data.setStock(Integer.parseInt(invFile.getBuffer().get(i).get(3)));
            data.setLocation(invFile.getBuffer().get(i).get(4));
            data.setPrice(Double.parseDouble(invFile.getBuffer().get(i).get(5)));
            invArray.add(data);
        }
    }

    private static void printTable() {
        // Table headers
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.printf("| %10s | %10s | %10s | %10s | %10s | %10s | %10s |\n",
                "Record #", "ID", "Name", "Supplier", "Stock", "Location", "Price"
        );
        System.out.println("--------------------------------------------------------------------------------------------");

        if (invFile.getBuffer().isEmpty() && invArray.isEmpty()) {

            System.out.println("WARNING: Database is empty. There are no records in this database.");
            JFrame alert = new JFrame();
            JOptionPane.showMessageDialog(
                    alert,
                    "File not found or database is empty. There are no records in this database.",
                    "Alert", JOptionPane.WARNING_MESSAGE
            );
        } else {
            for (int i = 0; i < invArray.size(); i++) {

                System.out.printf("| %10s | %10s | %10s | %10s | %10s | %10s | %10.2f |\n",
                        i,
                        invArray.get(i).getID(),
                        invArray.get(i).getName(),
                        invArray.get(i).getSupplier(),
                        invArray.get(i).getStock(),
                        invArray.get(i).getLocation(),
                        invArray.get(i).getPrice()
                );
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    public static void addData(
            String id,
            String name,
            String supplier,
            int stock,
            String location,
            double price
    ) throws IOException {
        invArray.add(data = new DataTypes());
        data.setID(id);
        data.setName(name);
        data.setSupplier(supplier);
        data.setStock(stock);
        data.setLocation(location);
        data.setPrice(price);
        printTable();
        tableData.setRowCount(0); //clear table
        fetchTableData();
        saved = false;
    }

    public static void editData(
            int index,
            String id,
            String name,
            String supplier,
            int stock,
            String location,
            double price
    ) throws IOException {
        invArray.set(index, data = new DataTypes());
        data.setID(id);
        data.setName(name);
        data.setSupplier(supplier);
        data.setStock(stock);
        data.setLocation(location);
        data.setPrice(price);
        printTable();
        tableData.setRowCount(0); //clear table
        fetchTableData();
        saved = false;
    }

    public static void deleteData(int index) {
        invArray.remove(index);
        printTable();
        tableData.setRowCount(0); //clear table
        fetchTableData();
        saved = false;
    }

    private static void saveData() throws IOException {

        FileWriter csvWriter = new FileWriter(new File(Main.dataDir,"inventory.csv"));
        csvWriter.append("ID,Name,Supplier,Stock,Location,Price\n");
        for (DataTypes dataTypes : invArray) {
            csvWriter
                    .append(dataTypes.getID())
                    .append(",")
                    .append(dataTypes.getName())
                    .append(",")
                    .append(dataTypes.getSupplier())
                    .append(",")
                    .append(Integer.toString(dataTypes.getStock()))
                    .append(",")
                    .append(dataTypes.getLocation())
                    .append(",")
                    .append(Double.toString(dataTypes.getPrice()))
                    .append("\n");
        }
        System.out.println("Saving changes to inventory.csv ...");
        csvWriter.flush();
        csvWriter.close();
        System.out.println("Changes saved!");
        JFrame message = new JFrame();
        JOptionPane.showMessageDialog(message, "The data in the table was saved successfully.");
    }

    public static void launchUI() throws IOException {
        System.out.println("=====INVENTORY MENU=====");
        inventory();
        printTable();
        JFrame frame = new Inventory("SalesApp " + Main.version + " | Inventory");
        frame.setVisible(true);
    }
}