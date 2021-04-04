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

public class Transactions extends JFrame {
    private static final DecimalFormat df = new DecimalFormat("#0.00"); // Decimal formatting for currency
    private static final FileOps transactionsFile = new FileOps(); // FileOps object
    private static DataTypes data;
    private static final ArrayList<DataTypes> transactionsArray = new ArrayList<>();
    private static boolean saved = true; // flag that determines whether changes have been made to the table
    private static final DefaultTableModel tableData = new DefaultTableModel();
    private static final Object[] row = new Object[6];
    private static LocalDate currentDate;
    private static String fileDate;
    private static int prevDayCount = 0;
    private static int nextDayCount = 0;
    /**
     * menuType = 0 -> Inventory
     * menuType = 1 -> Sales
     * menuType = 2 -> Transactions
     */
    private static final int menuType = 2;

    private JButton menuButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton previousButton;
    private JButton nextButton;
    private JButton saveButton;
    private JScrollPane transactionsScrollPane;
    private JTable transactionsTable;
    private JLabel readFile;
    private JLabel dateLabel;
    private JPanel mainPanel;
    private JLabel tableDateLabel;
    private JButton todayButton;

    public Transactions(String title) {
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
        readFile.setText("Read File: transactions_" + fileDate + ".csv");

        // Clear columns of JTable beforehand
        tableData.setColumnCount(0);

        // JTable columns
        tableData.addColumn("Record #");
        tableData.addColumn("ID");
        tableData.addColumn("Client Name");
        tableData.addColumn("Payment Type");
        tableData.addColumn("Credit Card Number");
        tableData.addColumn("Amount");

        // Clear contents of JTable beforehand
        tableData.setRowCount(0);
        fetchTableData();

        transactionsTable.setModel(tableData);
        transactionsScrollPane.setViewportView(transactionsTable);

        menuButton.addActionListener(e -> {
            try {
                if (saved) {
                    System.out.println("INFO: Entered Main Menu.");
                    // Clear all arrays
                    transactionsFile.getBuffer().clear();
                    transactionsArray.clear();
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
                        transactionsFile.getBuffer().clear();
                        transactionsArray.clear();
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
            AddOps.launchUI(menuType);
        });

        editButton.addActionListener(e -> {
            if (transactionsArray.isEmpty()) {
                JFrame alert = new JFrame();
                JOptionPane.showMessageDialog(
                        alert,
                        "There is no data available in the table to edit.",
                        "Edit",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                System.out.println("INFO: Opened EditOps.");
                EditOps.launchUI(menuType, transactionsArray, null);
            }
        });

        deleteButton.addActionListener(e -> {
            if (transactionsArray.isEmpty()) {
                JFrame alert = new JFrame();
                JOptionPane.showMessageDialog(
                        alert,
                        "There is no data available in the table to delete.",
                        "Delete",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                System.out.println("INFO: Opened DeleteOps.");
                DeleteOps.launchUI(menuType, transactionsArray, null);
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
            readFile.setText("Read File: transactions_" + fileDate + ".csv");
            tableDateLabel.setText("Date: " + fileDate);
            try {
                transactionsFile.getBuffer().clear(); // clear buffer
                transactionsArray.clear();
                transactions();
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
            readFile.setText("Read File: transactions_" + fileDate + ".csv");
            tableDateLabel.setText("Date: " + fileDate);
            try {
                transactionsFile.getBuffer().clear(); // clear buffer
                transactionsArray.clear();
                transactions();
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
            readFile.setText("Read File: transactions_" + fileDate + ".csv");
            tableDateLabel.setText("Date: " + fileDate);
            try {
                transactionsFile.getBuffer().clear(); // clear buffer
                transactionsArray.clear();
                transactions();
                printTable();
                tableData.setRowCount(0);
                fetchTableData();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    private static void fetchTableData() {
        for (int i = 0; i < transactionsArray.size(); i++) {
            row[0] = i;
            row[1] = transactionsArray.get(i).getID();
            row[2] = transactionsArray.get(i).getName();
            row[3] = transactionsArray.get(i).getPaymentType();
            row[4] = transactionsArray.get(i).getCardNumber();
            row[5] = df.format(transactionsArray.get(i).getAmount());
            tableData.addRow(row);
        }
    }

    private static void transactions() throws IOException {
        transactionsFile.setFile(Main.transactionsDir,"transactions_" + fileDate + ".csv");

        // Remove first row if column headers exist
        if (!transactionsFile.getBuffer().isEmpty() && transactionsFile.getBuffer().get(0).get(0).equals("ID")) {
            transactionsFile.getBuffer().remove(0);
        }

        for (int i = 0; i < transactionsFile.getBuffer().size(); i++) {
            data = new DataTypes();
            data.setID(transactionsFile.getBuffer().get(i).get(0));
            data.setName(transactionsFile.getBuffer().get(i).get(1));
            data.setPaymentType(transactionsFile.getBuffer().get(i).get(2));
            data.setCardNumber(transactionsFile.getBuffer().get(i).get(3));
            data.setAmount(Double.parseDouble(transactionsFile.getBuffer().get(i).get(4)));
            transactionsArray.add(data);
        }
    }

    private static void printTable() {
        // Table headers
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("| %10s | %10s | %10s | %11s | %10s | %10s |\n",
                "Record #", "ID", "Name", "Pay Type", "Card Num", "Amount"
        );
        System.out.println("--------------------------------------------------------------------------------");

        if (transactionsFile.getBuffer().isEmpty() && transactionsArray.isEmpty()) {

            System.out.println("WARNING: Database is empty. There are no records in this database.");
            JFrame alert = new JFrame();
            JOptionPane.showMessageDialog(
                    alert,
                    "File not found or database is empty. There are no records in this database.",
                    "Alert", JOptionPane.WARNING_MESSAGE
            );
        } else {
            for (int i = 0; i < transactionsArray.size(); i++) {

                System.out.printf("| %10s | %10s | %10s | %11s | %10s | %10.2f |\n",
                        i,
                        transactionsArray.get(i).getID(),
                        transactionsArray.get(i).getName(),
                        transactionsArray.get(i).getPaymentType(),
                        transactionsArray.get(i).getCardNumber(),
                        transactionsArray.get(i).getAmount()
                );
            }
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    public static void addData(
            String id,
            String name,
            String paymentType,
            String cardNumber,
            double amount
    ) {
        transactionsArray.add(data = new DataTypes());
        data.setID(id);
        data.setName(name);
        data.setPaymentType(paymentType);
        data.setCardNumber(cardNumber);
        data.setAmount(amount);
        printTable();
        tableData.setRowCount(0); //clear table
        fetchTableData();
        saved = false;
    }

    public static void editData(
            int index,
            String id,
            String name,
            String paymentType,
            String cardNumber,
            double amount
    ) {
        transactionsArray.set(index, data = new DataTypes());
        data.setID(id);
        data.setName(name);
        data.setPaymentType(paymentType);
        data.setCardNumber(cardNumber);
        data.setAmount(amount);
        printTable();
        tableData.setRowCount(0); //clear table
        fetchTableData();
        saved = false;
    }

    public static void deleteData(int index) {
        transactionsArray.remove(index);
        printTable();
        tableData.setRowCount(0); //clear table
        fetchTableData();
        saved = false;
    }

    private static void saveData() throws IOException {

        FileWriter csvWriter = new FileWriter(
                new File(Main.transactionsDir,"transactions_" + fileDate + ".csv")
        );
        csvWriter.append("ID,Name,Payment Type,Credit Card Number,Amount\n");
        for (DataTypes dataTypes : transactionsArray) {
            csvWriter
                    .append(dataTypes.getID())
                    .append(",")
                    .append(dataTypes.getName())
                    .append(",")
                    .append(dataTypes.getPaymentType())
                    .append(",")
                    .append(dataTypes.getCardNumber())
                    .append(",")
                    .append(df.format(dataTypes.getAmount()))
                    .append("\n");
        }
        System.out.println("Saving changes to transactions_" + fileDate + ".csv ...");
        csvWriter.flush();
        csvWriter.close();
        System.out.println("Changes saved!");
        JFrame message = new JFrame();
        JOptionPane.showMessageDialog(message, "The data in the table was saved successfully.");
    }

    public static void launchUI() throws IOException {
        System.out.println("=====TRANSACTIONS MENU=====");
        // Making sure all arrays are cleared
        transactionsFile.getBuffer().clear();
        transactionsArray.clear();
        // Default to today's time
        currentDate = LocalDate.now();
        fileDate = String.valueOf(currentDate);
        transactions();
        printTable();
        JFrame frame = new Transactions("SalesApp " + Main.version + " | Transactions");
        frame.setVisible(true);
    }
}
