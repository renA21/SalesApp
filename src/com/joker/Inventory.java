package com.joker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Inventory GUI
 */
public class Inventory extends JFrame {
    // Decimal formatting for currency
    private static final DecimalFormat df = new DecimalFormat("#0.00");
    private static final FileOps invFile = new FileOps();
    private static DataTypes data;
    private static final ArrayList<DataTypes> invArray = new ArrayList<>();
    // Flag that determines whether changes have been made to the table.
    private static boolean saved = true;
    private static final DefaultTableModel tableData = new DefaultTableModel();
    private static final Object[] row = new Object[7];
    // 0 = Inventory, 1 = Sales, 2 = Transactions.
    private static final int menuType = 0;

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

    /**
     * Creates the GUI and its declared Swing components.
     * @param title Title name for the window.
     */
    public Inventory(String title) {
        super(title);
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);

        // Exit confirmation when closing the window
        JFrame confirmExit = new JFrame();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int option = JOptionPane.showConfirmDialog(confirmExit,
                        "Are you sure you want to exit? " +
                                "Any unsaved changes will be lost.",
                        "Exit", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.out.println("INFO: The program closed successfully.");
                    System.exit(0);
                }
            }
        });

        // Show current time and date
        new Timer(0, (ActionEvent e) -> {
            Date d = new Date();
            SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss a yyyy-MM-dd");
            dateLabel.setText(formatTime.format(d));
        }).start();

        // Current working file
        readFile.setText("File: inventory.csv");

        // Clear column headers of JTable beforehand
        tableData.setColumnCount(0);

        // JTable column headers
        tableData.addColumn("Record #");
        tableData.addColumn("ID");
        tableData.addColumn("Name");
        tableData.addColumn("Supplier");
        tableData.addColumn("Stock");
        tableData.addColumn("Location");
        tableData.addColumn("Price");

        tableData.setRowCount(0); // Clear table contents beforehand
        fetchTableData();

        // Set column headers
        invTable.setModel(tableData);
        invScrollPane.setViewportView(invTable);

        menuButton.addActionListener(e -> {
            if (saved) {
                System.out.println("INFO: Entered Main Menu.");
                invFile.getInputBuffer().clear(); // clear buffer
                invArray.clear();
                MainMenu.launchUI();
                dispose();
            } else {
                int option = JOptionPane.showConfirmDialog(confirmExit,
                        "Changes were made to the database. " +
                                "Any unsaved data will be lost. " +
                                "Do you want to continue?",
                        "Unsaved Changes", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.out.println("WARNING: Changes in the table were not saved.");
                    System.out.println("INFO: Entered Main Menu.");
                    invFile.getInputBuffer().clear(); // clear buffer
                    invArray.clear();
                    saved = true;
                    MainMenu.launchUI();
                    dispose();
                }
            }
        });

        addButton.addActionListener(e -> {
            System.out.println("INFO: Opened AddOps.");
            AddOps.launchUI(menuType);
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
                EditOps.launchUI(menuType, invArray, null);
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
                DeleteOps.launchUI(menuType, invArray, null);
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

    /**
     * Parses data into the table from invArray.
     */
    private static void fetchTableData() {
        for (int i = 0; i < invArray.size(); i++) {
            row[0] = i;
            row[1] = invArray.get(i).getID();
            row[2] = invArray.get(i).getName();
            row[3] = invArray.get(i).getSupplier();
            row[4] = invArray.get(i).getStock();
            row[5] = invArray.get(i).getLocation();
            row[6] = df.format(invArray.get(i).getPrice());
            tableData.addRow(row);
        }
    }

    /**
     * Sets input file as inventory.csv and parses the data from it to an ArrayList.
     * @throws IOException IO error handling
     */
    private static void inventory() throws IOException {
        invFile.setFile(Main.dataDir,"inventory.csv");

        // Remove first row if column headers exist
        if (!invFile.getInputBuffer().isEmpty() &&
                invFile.getInputBuffer().get(0).get(0).equals("ID")
        ) {
            invFile.getInputBuffer().remove(0);
        }

        // Parses data from the CSV file String List buffer to the ArrayList
        for (int i = 0; i < invFile.getInputBuffer().size(); i++) {
            data = new DataTypes();
            data.setID(invFile.getInputBuffer().get(i).get(0));
            data.setName(invFile.getInputBuffer().get(i).get(1));
            data.setSupplier(invFile.getInputBuffer().get(i).get(2));
            data.setStock(Integer.parseInt(invFile.getInputBuffer().get(i).get(3)));
            data.setLocation(invFile.getInputBuffer().get(i).get(4));
            data.setPrice(Double.parseDouble(invFile.getInputBuffer().get(i).get(5)));
            invArray.add(data);
        }
    }

    /**
     * Prints a CLI version of the Inventory table to the console/log for debugging purposes.
     */
    private static void printTable() {
        // Table column headers
        System.out.println(
                "---------------------------------------------" +
                        "-----------------------------------------------"
        );
        System.out.printf("| %10s | %10s | %10s | %10s | %10s | %10s | %10s |\n",
                "Record #", "ID", "Name", "Supplier", "Stock", "Location", "Price"
        );
        System.out.println(
                "---------------------------------------------" +
                        "-----------------------------------------------"
        );

        if (invFile.getInputBuffer().isEmpty() && invArray.isEmpty()) {
            System.out.println("WARNING: Database is empty. " +
                    "There are no records in this database.");
            JFrame alert = new JFrame();
            JOptionPane.showMessageDialog(
                    alert,
                    "File not found or database is empty. " +
                            "There are no records in this database.",
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
        System.out.println(
                "---------------------------------------------" +
                        "-----------------------------------------------"
        );
    }

    /**
     * Adds a new record of data to the table.
     * @param id input new ID data.
     * @param name input new Name data.
     * @param supplier input new Supplier data.
     * @param stock input new Stock data.
     * @param location input new Location data.
     * @param price input new Price data.
     */
    public static void addData(
            String id,
            String name,
            String supplier,
            int stock,
            String location,
            double price
    ) {
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

    /**
     * Edit/overwrites the data corresponding to the selected record # from the EditOps combo box.
     * @param index record # index from recComboBox.
     * @param id corresponding ID data from selected record #.
     * @param name corresponding Name data from selected record #.
     * @param supplier corresponding Supplier data from selected record #.
     * @param stock corresponding Stock data from selected record #.
     * @param location corresponding Location data from selected record #.
     * @param price corresponding Price data from selected record #.
     */
    public static void editData(
            int index,
            String id,
            String name,
            String supplier,
            int stock,
            String location,
            double price
    ) {
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

    /**
     * Deletes the selected record # data from the DeleteOps combo box.
     * @param index record # index from recComboBox.
     */
    public static void deleteData(int index) {
        invArray.remove(index);
        printTable();
        tableData.setRowCount(0); //clear table
        fetchTableData();
        saved = false;
    }

    /**
     * Saves the data from the table to a CSV database file.
     * @throws IOException IO error handling.
     */
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
                    .append(df.format(dataTypes.getPrice()))
                    .append("\n");
        }
        System.out.println("Saving changes to inventory.csv ...");
        csvWriter.flush();
        csvWriter.close();
        System.out.println("Changes saved!");
        JFrame message = new JFrame();
        JOptionPane.showMessageDialog(message,
                "The data in the table was saved successfully."
        );
    }

    /**
     * Initializes the Inventory GUI and other private methods within the class.
     * @throws IOException IO error handling.
     */
    public static void launchUI() throws IOException {
        System.out.println("=====INVENTORY MENU=====");
        inventory();
        printTable();
        JFrame frame = new Inventory("SalesApp " + Main.version + " | Inventory");
        frame.setVisible(true);
    }
}