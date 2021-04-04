package com.joker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Reports extends JFrame {
    private static final DecimalFormat df = new DecimalFormat("#0.00"); // Decimal formatting for currency
    private static final FileOps invFile = new FileOps();
    private static final FileOps salesFile = new FileOps(); // FileOps object
    private static final FileOps transactionsFile  = new FileOps();
    private static DataTypes data;
    private static final ArrayList<DataTypes> invArray = new ArrayList<>();
    private static final ArrayList<DataTypes> salesArray = new ArrayList<>();
    private static final ArrayList<DataTypes> inputSalesArray = new ArrayList<>();
    private static final ArrayList<DataTypes> transactionsArray = new ArrayList<>();
    private static final boolean saved = true; // flag that determines whether changes have been made to the table
    private static final DefaultTableModel tableData0 = new DefaultTableModel();
    private static final DefaultTableModel tableData1 = new DefaultTableModel();
    private static final Object[] salesRow = new Object[7];
    private static final Object[] transactionsRow = new Object[6];
    private static int totalQuantity = 0;
    private static double totalPrice = 0;
    private static double totalRevenue = 0;
    private static LocalDate currentDate;
    private static String fileDate;
    private static String selectedYear;
    private static String selectedMonth;
    private static int prevYearCount = 0;
    private static int nextYearCount = 0;
    private static int prevMonthCount = 0;
    private static int nextMonthCount = 0;

    private JButton menuButton;
    private JLabel dateLabel;
    private JPanel mainPanel;
    private JTable salesTable;
    private JTable transactionsTable;
    private JLabel tableDateLabel;
    private JButton previousMonthButton;
    private JButton nextMonthButton;
    private JScrollPane salesScrollPane;
    private JScrollPane transactionsScrollPane;
    private JButton nextYearButton;
    private JButton previousYearButton;
    private JButton todayButton;
    private JLabel totalQuantityLabel;
    private JLabel totalPriceLabel;
    private JLabel totalRevenueLabel;

    public Reports(String title) {
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
            SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss a dd/MM/yyyy");
            dateLabel.setText(formatTime.format(d));
        }).start();

        // Show current table date
        tableDateLabel.setText("Date: " + fileDate);

        // Clear columns of JTable beforehand
        tableData0.setColumnCount(0);
        tableData1.setColumnCount(0);

        // JTable columns for Sales
        tableData0.addColumn("Record #");
        tableData0.addColumn("ID");
        tableData0.addColumn("Name");
        tableData0.addColumn("Supplier");
        tableData0.addColumn("Price");
        tableData0.addColumn("Quantity");
        tableData0.addColumn("Amount Earned");

        salesTable.setModel(tableData0);
        salesScrollPane.setViewportView(salesTable);

        // JTable columns for Transactions
        tableData1.addColumn("Record #");
        tableData1.addColumn("ID");
        tableData1.addColumn("Client Name");
        tableData1.addColumn("Payment Type");
        tableData1.addColumn("Credit Card Number");
        tableData1.addColumn("Amount");

        transactionsTable.setModel(tableData1);
        transactionsScrollPane.setViewportView(transactionsTable);

        // Clear contents of JTable beforehand
        tableData0.setRowCount(0);
        tableData1.setRowCount(0);
        fetchTableData();

        // Labels
        totalQuantityLabel.setText("Total quantity sold: " + totalQuantity);
        totalPriceLabel.setText("Total items price: " + totalPrice);
        totalRevenueLabel.setText("Total revenue: " + totalRevenue);

        menuButton.addActionListener(e -> {
            try {
                System.out.println("INFO: Entered Main Menu.");
                MainMenu.launchUI();
                // Clear all arrays
                invFile.getBuffer().clear();
                invArray.clear();
                salesFile.getBuffer().clear();
                salesArray.clear();
                transactionsFile.getBuffer().clear();
                transactionsArray.clear();
                totalQuantity = 0;
                totalPrice = 0;
                totalRevenue = 0;
                dispose();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        todayButton.addActionListener(e -> {
            nextYearCount = 0;
            prevYearCount = 0;
            nextMonthCount = 0;
            prevMonthCount = 0;
            selectedYear = String.valueOf(currentDate.getYear());
            selectedMonth = String.valueOf(currentDate.getMonthValue());
            fileDate = selectedYear + "-" + selectedMonth;
            tableDateLabel.setText("Date: " + fileDate);
            try {
                // Clear all arrays
                invFile.getBuffer().clear();
                invArray.clear();
                salesFile.getBuffer().clear();
                salesArray.clear();
                transactionsFile.getBuffer().clear();
                transactionsArray.clear();
                reports();
                printSalesTable();
                printTransactionsTable();
                tableData0.setRowCount(0);
                tableData1.setRowCount(0);
                fetchTableData();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        previousYearButton.addActionListener(e -> {
            if (nextYearCount != 0) {
                nextYearCount--;
                selectedYear = String.valueOf(currentDate.plusYears(nextYearCount).getYear());
            } else {
                prevYearCount++;
                selectedYear = String.valueOf(currentDate.minusYears(prevYearCount).getYear());
            }
            fileDate = selectedYear + "-" + selectedMonth;
            tableDateLabel.setText("Date: " + fileDate);
            try {
                // Clear all arrays
                invFile.getBuffer().clear();
                invArray.clear();
                salesFile.getBuffer().clear();
                salesArray.clear();
                transactionsFile.getBuffer().clear();
                transactionsArray.clear();
                reports();
                printSalesTable();
                printTransactionsTable();
                tableData0.setRowCount(0);
                tableData1.setRowCount(0);
                fetchTableData();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        nextYearButton.addActionListener(e -> {
            nextYearButton.setEnabled(true);
            if (prevYearCount != 0) {
                prevYearCount--;
                selectedYear = String.valueOf(currentDate.minusYears(prevYearCount).getYear());
            } else {
                nextYearCount++;
                selectedYear = String.valueOf(currentDate.plusYears(nextYearCount).getYear());
            }
            fileDate = selectedYear + "-" + selectedMonth;
            tableDateLabel.setText("Date: " + fileDate);
            try {
                // Clear all arrays
                invFile.getBuffer().clear();
                invArray.clear();
                salesFile.getBuffer().clear();
                salesArray.clear();
                transactionsFile.getBuffer().clear();
                transactionsArray.clear();
                reports();
                printSalesTable();
                printTransactionsTable();
                tableData0.setRowCount(0);
                tableData1.setRowCount(0);
                fetchTableData();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        previousMonthButton.addActionListener(e -> {
            if (nextMonthCount != 0) {
                nextMonthCount--;
                selectedMonth = String.valueOf(currentDate.plusMonths(nextMonthCount).getMonthValue());
            } else {
                prevMonthCount++;
                selectedMonth = String.valueOf(currentDate.minusMonths(prevMonthCount).getMonthValue());
            }
            fileDate = selectedYear + "-" + selectedMonth;
            tableDateLabel.setText("Date: " + fileDate);
            try {
                // Clear all arrays
                invFile.getBuffer().clear();
                invArray.clear();
                salesFile.getBuffer().clear();
                salesArray.clear();
                transactionsFile.getBuffer().clear();
                transactionsArray.clear();
                reports();
                printSalesTable();
                printTransactionsTable();
                tableData0.setRowCount(0);
                tableData1.setRowCount(0);
                fetchTableData();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        nextMonthButton.addActionListener(e -> {
            if (prevMonthCount != 0) {
                prevMonthCount--;
                selectedMonth = String.valueOf(currentDate.minusMonths(prevMonthCount).getMonthValue());
            } else {
                nextMonthCount++;
                selectedMonth = String.valueOf(currentDate.plusMonths(nextMonthCount).getMonthValue());
            }
            fileDate = selectedYear + "-" + selectedMonth;
            tableDateLabel.setText("Date: " + fileDate);
            try {
                // Clear all arrays
                invFile.getBuffer().clear();
                invArray.clear();
                salesFile.getBuffer().clear();
                salesArray.clear();
                transactionsFile.getBuffer().clear();
                transactionsArray.clear();
                reports();
                printSalesTable();
                printTransactionsTable();
                tableData0.setRowCount(0);
                tableData1.setRowCount(0);
                fetchTableData();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    private static void fetchTableData() {
        for (int i = 0; i < invArray.size(); i++) {
            salesRow[0] = i;
            salesRow[1] = invArray.get(i).getID();
            salesRow[2] = invArray.get(i).getName();
            salesRow[3] = invArray.get(i).getSupplier();
            salesRow[4] = df.format(invArray.get(i).getPrice());
            salesRow[5] = salesArray.get(i).getQuantity();
            salesRow[6] = df.format(salesArray.get(i).getAmount());
            tableData0.addRow(salesRow);
        }

        for (int i = 0; i < transactionsArray.size(); i++) {
            transactionsRow[0] = i;
            transactionsRow[1] = transactionsArray.get(i).getID();
            transactionsRow[2] = transactionsArray.get(i).getName();
            transactionsRow[3] = transactionsArray.get(i).getPaymentType();
            transactionsRow[4] = transactionsArray.get(i).getCardNumber();
            transactionsRow[5] = df.format(transactionsArray.get(i).getAmount());
            tableData1.addRow(transactionsRow);
        }
    }

    private static void reports() throws IOException {
        invFile.setFile(Main.dataDir,"inventory.csv");

        // Remove first row if column headers exist
        if (!invFile.getBuffer().isEmpty() && invFile.getBuffer().get(0).get(0).equals("ID")) {
            invFile.getBuffer().remove(0);
        }

        System.out.println(invFile.getBuffer().get(0).get(5));

        for (int i = 0; i < invFile.getBuffer().size(); i++) {
            data = new DataTypes();
            data.setID(invFile.getBuffer().get(i).get(0));
            data.setName(invFile.getBuffer().get(i).get(1));
            data.setSupplier(invFile.getBuffer().get(i).get(2));
            data.setLocation(invFile.getBuffer().get(i).get(4));
            data.setPrice(Double.parseDouble(invFile.getBuffer().get(i).get(5)));
            invArray.add(data);
        }

        for (int i = 0; i < invArray.size(); i++) {
            data = new DataTypes();
            data.setQuantity(0);
            data.setAmount(0);
            salesArray.add(data);
        }

        for (int i = 0; i <= 31; i++) {
            if (i < 10) {
                salesFile.setFile(Main.salesDir,
                        // HACK: formatting 0 for selected months with single digits
                        "sales_" + selectedYear + "-0" + selectedMonth + "-0" + i + ".csv"
                );
            } else {
                salesFile.setFile(Main.salesDir,
                        "sales_" + selectedYear + "-0" + selectedMonth + "-" + i + ".csv"
                );
            }
            if (!salesFile.existence) {
                continue;
            }
            sales();
        }

        for (int i = 1; i <= 31; i++) {
            if (i < 10) {
                transactionsFile.setFile(Main.transactionsDir,
                        "transactions_" + selectedYear + "-0" + selectedMonth + "-0" + i + ".csv"
                );
            } else {
                transactionsFile.setFile(Main.transactionsDir,
                        "transactions_" + selectedYear + "-0" + selectedMonth + "-" + i + ".csv"
                );
            }
            transactions();
        }
    }

    private static void sales() {
        // Sales data handling
        // Remove first row if column headers exist.
        if (!salesFile.getBuffer().isEmpty() && salesFile.getBuffer().get(0).get(4).equals("Quantity")) {
            salesFile.getBuffer().remove(0);
        }
        for (int i = 0; i < invArray.size(); i++) {
            data = new DataTypes();
            // Set quantity to zero to fill in non-existent sales data
            if (i >= salesFile.getBuffer().size()) {
                data.setQuantity(0);
            } else {
                data.setQuantity(Integer.parseInt(salesFile.getBuffer().get(i).get(4)));
            }
            inputSalesArray.add(data);
        }
        salesFile.getBuffer().clear();
        for (int i = 0; i < invArray.size(); i++) {
            int temp = salesArray.get(i).getQuantity();
            salesArray.set(i, data = new DataTypes());
            data.setQuantity(temp + inputSalesArray.get(i).getQuantity());
            data.setAmount(invArray.get(i).getPrice() * salesArray.get(i).getQuantity());
        }
        inputSalesArray.clear();
    }

    private static void total() {
        for (DataTypes dataTypes : salesArray) {
            totalQuantity += dataTypes.getQuantity();
            totalPrice += dataTypes.getAmount();
        }

    }

    private static void transactions() {
        if (!transactionsFile.existence) {
            totalRevenue = totalRevenue + 0;
        } else {
            // Transactions data handling
            // Remove first row if column headers exist
            if (!transactionsFile.getBuffer().isEmpty() && transactionsFile.getBuffer().get(0).get(0).equals("ID")) {
                transactionsFile.getBuffer().remove(0);
            }
            for (int i = 0; i < transactionsFile.getBuffer().size(); i++) {
                if (transactionsFile.getBuffer().get(i).get(0).equals("ID")) {
                    continue;
                }
                DataTypes data1 = new DataTypes();
                data1.setID(transactionsFile.getBuffer().get(i).get(0));
                data1.setName(transactionsFile.getBuffer().get(i).get(1));
                data1.setPaymentType(transactionsFile.getBuffer().get(i).get(2));
                data1.setCardNumber(transactionsFile.getBuffer().get(i).get(3));
                data1.setAmount(Double.parseDouble(transactionsFile.getBuffer().get(i).get(4)));
                transactionsArray.add(data1);
                totalRevenue = totalRevenue + transactionsArray.get(i).getAmount();
            }
            // Clear buffer to prevent duplication of records from previously read databases
            transactionsFile.getBuffer().clear();
        }
    }

    private static void printSalesTable() {
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

    private static void printTransactionsTable() {
        // Table headers
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("| %10s | %10s | %10s | %11s | %10s | %10s |\n",
                "Record #", "ID", "Name", "Pay Type", "Card Num", "Amount"
        );
        System.out.println("--------------------------------------------------------------------------------");

        if (transactionsFile.getBuffer().isEmpty() && transactionsArray.isEmpty()) {

            System.out.println("WARNING: Transactions database is empty. There are no records in this database.");
            JFrame alert = new JFrame();
            JOptionPane.showMessageDialog(
                    alert,
                    "File not found or Transactions database is empty. There are no records in this database.",
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

    public static void launchUI() throws IOException {
        System.out.println("=====REPORTS MENU=====");
        // Default to today's time
        currentDate = LocalDate.now();
        fileDate = currentDate.getYear() + "-" + currentDate.getMonthValue();
        selectedYear = String.valueOf(currentDate.getYear());
        selectedMonth = String.valueOf(currentDate.getMonthValue());
        reports();
        printSalesTable();
        printTransactionsTable();
        total();
        JFrame frame = new Reports("SalesApp " + Main.version + " | Reports");
        frame.setVisible(true);
    }
}
