package com.joker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Sales extends JFrame {
    private static final DecimalFormat df = new DecimalFormat("#0.00"); // Decimal formatting for currency
    private static final FileOps invFile = new FileOps(); // FileOps object
    private static final FileOps salesFile = new FileOps(); // FileOps object
    private static DataTypes data;
    private static final ArrayList<DataTypes> invArray = new ArrayList<>();
    private static final ArrayList<DataTypes> salesArray = new ArrayList<>();
    private static boolean saved = true; // flag that determines whether changes have been made to the table
    private static final DefaultTableModel tableData = new DefaultTableModel();
    private static final Object[] row = new Object[7];
    private static LocalDate currentDate = LocalDate.now();
    private static String fileDate;
    private static int prevDayCount = 0;
    private static int nextDayCount = 0;
    /**
     * menuType = 0 -> Inventory
     * menuType = 1 -> Sales
     * menuType = 2 -> Transactions
     */
    private static final int menuType = 1;

    private JPanel mainPanel;
    private JButton menuButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton previousButton;
    private JButton nextButton;
    private JButton saveButton;
    private JScrollPane salesScrollPane;
    private JTable salesTable;
    private JLabel readFile;
    private JLabel dateLabel;
    private JLabel tableDateLabel;
    private JButton todayButton;

    public Sales(String title) {
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

        // Show current table date
        tableDateLabel.setText("Date: " + fileDate);
        readFile.setText("Read File: sales_" + fileDate + ".csv");

        tableData.setColumnCount(0);

        // JTable columns
        tableData.addColumn("Record #");
        tableData.addColumn("ID");
        tableData.addColumn("Name");
        tableData.addColumn("Supplier");
        tableData.addColumn("Price");
        tableData.addColumn("Quantity");
        tableData.addColumn("Amount Earned");

        // Clear contents of JTable beforehand
        tableData.setRowCount(0);
        fetchTableData();

        salesTable.setModel(tableData);
        salesScrollPane.setViewportView(salesTable);

        menuButton.addActionListener(e -> {
            try {
                if (saved) {
                    System.out.println("INFO: Entered Main Menu.");
                    salesFile.getBuffer().clear(); // clear buffer
                    salesArray.clear();
                    MainMenu.launchUI();
                    dispose();
                } else {
                    int option = JOptionPane.showConfirmDialog(confirmExit,
                            "Changes were made to the database. Any unsaved data will be lost. Do you want to continue?",
                            "Unsaved Changes", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        System.out.println("WARNING: Changes in the table were not saved.");
                        System.out.println("INFO: Entered Main Menu.");
                        // Clear all arrays
                        invFile.getBuffer().clear();
                        salesFile.getBuffer().clear();
                        invArray.clear();
                        salesArray.clear();
                        prevDayCount = 0;
                        nextDayCount = 0;
                        saved = true;
                        MainMenu.launchUI();
                        dispose();
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        editButton.addActionListener(e -> {
            if (salesArray.isEmpty()) {
                JFrame alert = new JFrame();
                JOptionPane.showMessageDialog(
                        alert,
                        "There is no data available in the table to edit.",
                        "Edit",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                System.out.println("INFO: Opened EditOps.");
                EditOps.launchUI(menuType, invArray, salesArray);
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
                DeleteOps.launchUI(menuType, invArray, salesArray);
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

        todayButton.addActionListener(e -> {
            nextDayCount = 0;
            prevDayCount = 0;
            fileDate = String.valueOf(currentDate);
            readFile.setText("Read File: sales_" + fileDate + ".csv");
            tableDateLabel.setText("Date: " + fileDate);
            try {
                invFile.getBuffer().clear();
                salesFile.getBuffer().clear(); // clear buffer
                invArray.clear();
                salesArray.clear();
                sales();
                printTable();
                tableData.setRowCount(0);
                fetchTableData();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        previousButton.addActionListener(e -> {
            if (nextDayCount != 0) {
                nextDayCount--;
                fileDate = String.valueOf(currentDate.plusDays(nextDayCount));
            } else {
                prevDayCount++;
                fileDate = String.valueOf(currentDate.minusDays(prevDayCount));
            }
            readFile.setText("Read File: sales_" + fileDate + ".csv");
            tableDateLabel.setText("Date: " + fileDate);
            try {
                invFile.getBuffer().clear();
                salesFile.getBuffer().clear(); // clear buffer
                invArray.clear();
                salesArray.clear();
                sales();
                printTable();
                tableData.setRowCount(0);
                fetchTableData();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        nextButton.addActionListener(e -> {
            if (prevDayCount != 0) {
                prevDayCount--;
                fileDate = String.valueOf(currentDate.minusDays(prevDayCount));
            } else {
                nextDayCount++;
                fileDate = String.valueOf(currentDate.plusDays(nextDayCount));
            }
            readFile.setText("Read File: sales_" + fileDate + ".csv");
            tableDateLabel.setText("Date: " + fileDate);
            try {
                invFile.getBuffer().clear();
                salesFile.getBuffer().clear(); // clear buffer
                invArray.clear();
                salesArray.clear();
                sales();
                printTable();
                tableData.setRowCount(0);
                fetchTableData();
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
            row[4] = df.format(invArray.get(i).getPrice());
            row[5] = salesArray.get(i).getQuantity();
            row[6] = df.format(salesArray.get(i).getAmount());
            tableData.addRow(row);
        }
    }

    private static void sales() throws IOException {
        // Sales menu depends data from inventory.csv
        invFile.setFile(Main.dataDir,"inventory.csv");

        salesFile.setFile(Main.salesDir,"sales_" + fileDate + ".csv");

        // Remove first row if column headers exist.
        if (!salesFile.getBuffer().isEmpty() && salesFile.getBuffer().get(0).get(0).equals("ID")) {
            salesFile.getBuffer().remove(0);
        }
        if (!invFile.getBuffer().isEmpty() && invFile.getBuffer().get(0).get(0).equals("ID")) {
            invFile.getBuffer().remove(0);
        }

        for (int i = 0; i < invFile.getBuffer().size(); i++) {
            data = new DataTypes();
            data.setID(invFile.getBuffer().get(i).get(0));
            data.setName(invFile.getBuffer().get(i).get(1));
            data.setSupplier(invFile.getBuffer().get(i).get(2));
            data.setLocation(invFile.getBuffer().get(i).get(4));
            data.setPrice(Double.parseDouble(invFile.getBuffer().get(i).get(5)));
            invArray.add(data);
        }

        // Get array size from salesFile if it exists
        if (salesFile.getCsvFile().exists()) {
            for (int i = 0; i < salesFile.getBuffer().size(); i++) {
                data = new DataTypes();
                if (salesFile.getBuffer().isEmpty()) {
                    data.setQuantity(0);
                    data.setAmount(0);
                } else {
                    data.setQuantity(Integer.parseInt(salesFile.getBuffer().get(i).get(4)));
                    data.setAmount(Double.parseDouble(salesFile.getBuffer().get(i).get(5)));
                }
                salesArray.add(data);
            }
        } else {
            for (int i = 0; i < invFile.getBuffer().size(); i++) {
                data = new DataTypes();
                if (salesFile.getBuffer().isEmpty()) {
                    data.setQuantity(0);
                    data.setAmount(0);
                } else {
                    data.setQuantity(Integer.parseInt(salesFile.getBuffer().get(i).get(4)));
                    data.setAmount(Double.parseDouble(salesFile.getBuffer().get(i).get(5)));
                }
                salesArray.add(data);
            }
        }
    }

    private static void printTable() {
        // Table headers
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.printf("| %10s | %10s | %10s | %10s | %10s | %10s | %10s |\n",
                "Record #", "ID", "Name", "Supplier", "Price", "Quantity", "Amount"
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
                System.out.printf("| %10s | %10s | %10s | %10s | %10.2f |",
                        i,
                        invArray.get(i).getID(),
                        invArray.get(i).getName(),
                        invArray.get(i).getSupplier(),
                        invArray.get(i).getPrice()
                );

                System.out.printf(" %10s | %10.2f |\n",
                        salesArray.get(i).getQuantity(),
                        salesArray.get(i).getAmount()
                );
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    public static void editData(
            int index,
            String id,
            String name,
            String supplier,
            double price,
            int quantity,
            double amount
    ) {
        try {
            salesArray.set(index, data = new DataTypes());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        data.setID(id);
        data.setName(name);
        data.setSupplier(supplier);
        data.setPrice(price);
        data.setQuantity(quantity);
        data.setAmount(amount);
        printTable();
        tableData.setRowCount(0); //clear table
        fetchTableData();
        saved = false;
    }

    public static void deleteData(int index) {
        salesArray.set(index, data = new DataTypes());
        data.setQuantity(0);
        data.setAmount(0);
        printTable();
        tableData.setRowCount(0); //clear table
        fetchTableData();
        saved = false;
    }

    private static void saveData() throws IOException {

        FileWriter csvWriter = new FileWriter(
                new File(Main.salesDir,"sales_" + fileDate + ".csv")
        );
        csvWriter.append("ID,Name,Supplier,Price,Quantity,Amount\n");
        for (int i = 0; i < invArray.size(); i++) {
            csvWriter
                    .append(invArray.get(i).getID())
                    .append(",")
                    .append(invArray.get(i).getName())
                    .append(",")
                    .append(invArray.get(i).getSupplier())
                    .append(",")
                    .append(df.format(invArray.get(i).getPrice()))
                    .append(",")
                    .append(Integer.toString(salesArray.get(i).getQuantity()))
                    .append(",")
                    .append(df.format(salesArray.get(i).getAmount()))
                    .append("\n");
        }
        System.out.println("Saving changes to " + "sales_" + fileDate + ".csv ...");
        csvWriter.flush();
        csvWriter.close();
        System.out.println("Changes saved!");
        JFrame message = new JFrame();
        JOptionPane.showMessageDialog(message, "The data in the table was saved successfully.");
    }

    public static void launchUI() throws IOException {
        System.out.println("=====SALES MENU=====");
        // Making sure all arrays are cleared
        invFile.getBuffer().clear();
        salesFile.getBuffer().clear();
        invArray.clear();
        salesArray.clear();
        // Default to today's time
        currentDate = LocalDate.now();
        fileDate = String.valueOf(currentDate);
        sales();
        printTable();
        JFrame frame = new Sales("SalesApp " + Main.version + " | Sales");
        frame.setVisible(true);
    }
}