package com.joker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * Reports GUI
 */
public class Reports extends JFrame {
    // Decimal format for currency
    private static final DecimalFormat df = new DecimalFormat("#0.00");
    private static final DecimalFormat dayFormat = new DecimalFormat("00");
    private static final FileOps invFile = new FileOps();
    private static final FileOps salesFile = new FileOps();
    private static final FileOps transactionsFile  = new FileOps();
    private static DataTypes data;
    private static final ArrayList<DataTypes> invArray = new ArrayList<>();
    private static final ArrayList<DataTypes> salesArray = new ArrayList<>();
    private static final ArrayList<DataTypes> inputSalesArray = new ArrayList<>();
    private static final ArrayList<DataTypes> transactionsArray = new ArrayList<>();
    private static final DefaultTableModel tableData0 = new DefaultTableModel();
    private static final DefaultTableModel tableData1 = new DefaultTableModel();
    private static final Object[] salesRow = new Object[7];
    private static final Object[] transactionsRow = new Object[6];
    private static LocalDate currentDate;
    private static String fileDate;
    private static int selectedYear;
    private static int selectedMonth;
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

    /**
     * Creates the GUI and its declared Swing components.
     * @param title Title name for the window.
     */
    public Reports(String title) {
        super(title);
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);

        // Exit confirmation when closing the window
        JFrame confirmExit = new JFrame();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
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
            SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss a dd/MM/yyyy");
            dateLabel.setText(formatTime.format(d));
        }).start();

        // Show current table date
        tableDateLabel.setText("Date: " + fileDate);

        // Total labels
        totalQuantityLabel.setText("Total quantity sold: " + totalQuantity());
        totalPriceLabel.setText("Total items price: " + totalPrice());
        totalRevenueLabel.setText("Total revenue: " + totalRevenue());

        // Clear column headers of both JTables beforehand
        tableData0.setColumnCount(0);
        tableData1.setColumnCount(0);

        // JTable column headers for Sales
        tableData0.addColumn("Record #");
        tableData0.addColumn("ID");
        tableData0.addColumn("Name");
        tableData0.addColumn("Supplier");
        tableData0.addColumn("Price");
        tableData0.addColumn("Quantity");
        tableData0.addColumn("Amount Earned");

        // Set column headers
        salesTable.setModel(tableData0);
        salesScrollPane.setViewportView(salesTable);

        // JTable column headers for Transactions
        tableData1.addColumn("Record #");
        tableData1.addColumn("ID");
        tableData1.addColumn("Client Name");
        tableData1.addColumn("Payment Type");
        tableData1.addColumn("Credit Card Number");
        tableData1.addColumn("Amount");

        // Set column headers
        transactionsTable.setModel(tableData1);
        transactionsScrollPane.setViewportView(transactionsTable);

        // Clear contents of JTables beforehand
        tableData0.setRowCount(0);
        tableData1.setRowCount(0);

        fetchTableData();

        menuButton.addActionListener(e -> {
            System.out.println("INFO: Entered Main Menu.");
            MainMenu.launchUI();
            // Clear all arrays
            invFile.getInputBuffer().clear();
            invArray.clear();
            salesFile.getInputBuffer().clear();
            salesArray.clear();
            transactionsFile.getInputBuffer().clear();
            transactionsArray.clear();
            dispose();
        });

        todayButton.addActionListener(e -> {
            nextYearCount = 0;
            prevYearCount = 0;
            nextMonthCount = 0;
            prevMonthCount = 0;
            selectedYear = currentDate.getYear();
            selectedMonth = currentDate.getMonthValue();
            fileDate = selectedYear + "-" + dayFormat.format(selectedMonth);
            tableDateLabel.setText("Date: " + fileDate);
            try {
                // Clear all arrays
                invFile.getInputBuffer().clear();
                invArray.clear();
                salesFile.getInputBuffer().clear();
                salesArray.clear();
                transactionsFile.getInputBuffer().clear();
                transactionsArray.clear();
                reports();
                printSalesTable();
                printTransactionsTable();
                tableData0.setRowCount(0);
                tableData1.setRowCount(0);
                fetchTableData();
                // Total labels
                totalQuantityLabel.setText("Total quantity sold: " + totalQuantity());
                totalPriceLabel.setText("Total items price: " + totalPrice());
                totalRevenueLabel.setText("Total revenue: " + totalRevenue());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        previousYearButton.addActionListener(e -> {
            if (nextYearCount != 0) {
                nextYearCount--;
                selectedYear = currentDate.plusYears(nextYearCount).getYear();
            } else {
                prevYearCount++;
                selectedYear = currentDate.minusYears(prevYearCount).getYear();
            }
            fileDate = selectedYear + "-" + dayFormat.format(selectedMonth);
            tableDateLabel.setText("Date: " + fileDate);
            try {
                // Clear all arrays
                invFile.getInputBuffer().clear();
                invArray.clear();
                salesFile.getInputBuffer().clear();
                salesArray.clear();
                transactionsFile.getInputBuffer().clear();
                transactionsArray.clear();
                reports();
                printSalesTable();
                printTransactionsTable();
                tableData0.setRowCount(0);
                tableData1.setRowCount(0);
                fetchTableData();
                // Total labels
                totalQuantityLabel.setText("Total quantity sold: " + totalQuantity());
                totalPriceLabel.setText("Total items price: " + totalPrice());
                totalRevenueLabel.setText("Total revenue: " + totalRevenue());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        nextYearButton.addActionListener(e -> {
            nextYearButton.setEnabled(true);
            if (prevYearCount != 0) {
                prevYearCount--;
                selectedYear = currentDate.minusYears(prevYearCount).getYear();
            } else {
                nextYearCount++;
                selectedYear = currentDate.plusYears(nextYearCount).getYear();
            }
            fileDate = selectedYear + "-" + dayFormat.format(selectedMonth);
            tableDateLabel.setText("Date: " + fileDate);
            try {
                // Clear all arrays
                invFile.getInputBuffer().clear();
                invArray.clear();
                salesFile.getInputBuffer().clear();
                salesArray.clear();
                transactionsFile.getInputBuffer().clear();
                transactionsArray.clear();
                reports();
                printSalesTable();
                printTransactionsTable();
                tableData0.setRowCount(0);
                tableData1.setRowCount(0);
                fetchTableData();
                // Total labels
                totalQuantityLabel.setText("Total quantity sold: " + totalQuantity());
                totalPriceLabel.setText("Total items price: " + totalPrice());
                totalRevenueLabel.setText("Total revenue: " + totalRevenue());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        previousMonthButton.addActionListener(e -> {
            if (nextMonthCount != 0) {
                nextMonthCount--;
                selectedMonth = currentDate.plusMonths(nextMonthCount).getMonthValue();
            } else {
                prevMonthCount++;
                selectedMonth = currentDate.minusMonths(prevMonthCount).getMonthValue();
            }
            fileDate = selectedYear + "-" + dayFormat.format(selectedMonth);
            tableDateLabel.setText("Date: " + fileDate);
            try {
                // Clear all arrays
                invFile.getInputBuffer().clear();
                invArray.clear();
                salesFile.getInputBuffer().clear();
                salesArray.clear();
                transactionsFile.getInputBuffer().clear();
                transactionsArray.clear();
                reports();
                printSalesTable();
                printTransactionsTable();
                tableData0.setRowCount(0);
                tableData1.setRowCount(0);
                fetchTableData();
                // Total labels
                totalQuantityLabel.setText("Total quantity sold: " + totalQuantity());
                totalPriceLabel.setText("Total items price: " + totalPrice());
                totalRevenueLabel.setText("Total revenue: " + totalRevenue());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        nextMonthButton.addActionListener(e -> {
            if (prevMonthCount != 0) {
                prevMonthCount--;
                selectedMonth = currentDate.minusMonths(prevMonthCount).getMonthValue();
            } else {
                nextMonthCount++;
                selectedMonth = currentDate.plusMonths(nextMonthCount).getMonthValue();
            }
            fileDate = selectedYear + "-" + dayFormat.format(selectedMonth);
            tableDateLabel.setText("Date: " + fileDate);
            try {
                // Clear all arrays
                invFile.getInputBuffer().clear();
                invArray.clear();
                salesFile.getInputBuffer().clear();
                salesArray.clear();
                transactionsFile.getInputBuffer().clear();
                transactionsArray.clear();
                reports();
                printSalesTable();
                printTransactionsTable();
                tableData0.setRowCount(0);
                tableData1.setRowCount(0);
                fetchTableData();
                // Total labels
                totalQuantityLabel.setText("Total quantity sold: " + totalQuantity());
                totalPriceLabel.setText("Total items price: " + totalPrice());
                totalRevenueLabel.setText("Total revenue: " + totalRevenue());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    /**
     * Parses data into the table from invArray, salesArray and transactionsArray.
     */
    private static void fetchTableData() {
        // Parse Sales table
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

        // Parse Transactions table
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

    /**
     * Handles the input of both the Sales and Transactions data tables altogether.
     * @throws IOException IO error handling
     */
    private static void reports() throws IOException {
        invFile.setFile(Main.dataDir,"inventory.csv");

        if (!invFile.existence) {
            JFrame error = new JFrame();
            JOptionPane.showMessageDialog(error,
                    "Unable to generate a report. The Inventory database is missing." +
                            "\nTry to add some inventory records and try again.",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }

        // Remove first row if column headers exist
        if (!invFile.getInputBuffer().isEmpty() &&
                invFile.getInputBuffer().get(0).get(0).equals("ID")
        ) {
            invFile.getInputBuffer().remove(0);
        }

        for (int i = 0; i < invFile.getInputBuffer().size(); i++) {
            data = new DataTypes();
            data.setID(
                    invFile.getInputBuffer().get(i).get(0)
            );
            data.setName(
                    invFile.getInputBuffer().get(i).get(1)
            );
            data.setSupplier(
                    invFile.getInputBuffer().get(i).get(2)
            );
            data.setLocation(
                    invFile.getInputBuffer().get(i).get(4)
            );
            data.setPrice(
                    Double.parseDouble(invFile.getInputBuffer().get(i).get(5))
            );
            invArray.add(data);
        }

        for (int i = 0; i < invArray.size(); i++) {
            data = new DataTypes();
            data.setQuantity(0);
            data.setAmount(0);
            salesArray.add(data);
        }

        for (int i = 0; i <= 31; i++) {
            salesFile.setFile(Main.salesDir,
                    "sales_" +
                    selectedYear +
                    "-" +
                    dayFormat.format(selectedMonth) +
                    "-" +
                    dayFormat.format(i) +
                    ".csv"
            );
            if (!salesFile.existence) {
                continue;
            }
            sales();
        }

        for (int i = 1; i <= 31; i++) {
            transactionsFile.setFile(Main.transactionsDir,
                    "transactions_" +
                    selectedYear +
                    "-" +
                    dayFormat.format(selectedMonth) +
                    "-" +
                    dayFormat.format(i) +
                    ".csv"
            );
            transactions();
        }
    }

    /**
     * Sales table data handling from reports() method
     */
    private static void sales() {
        // Remove first row if column headers exist.
        if (!salesFile.getInputBuffer().isEmpty() &&
                salesFile.getInputBuffer().get(0).get(4).equals("Quantity")
        ) {
            salesFile.getInputBuffer().remove(0);
        }
        for (int i = 0; i < invArray.size(); i++) {
            data = new DataTypes();
            // Set quantity to zero to fill in non-existent sales data
            if (i >= salesFile.getInputBuffer().size()) {
                data.setQuantity(0);
            } else {
                data.setQuantity(
                        Integer.parseInt(salesFile.getInputBuffer().get(i).get(4))
                );
            }
            inputSalesArray.add(data);
        }
        salesFile.getInputBuffer().clear();
        for (int i = 0; i < invArray.size(); i++) {
            int temp = salesArray.get(i).getQuantity();
            salesArray.set(i, data = new DataTypes());
            data.setQuantity(temp + inputSalesArray.get(i).getQuantity());
            data.setAmount(invArray.get(i).getPrice() * salesArray.get(i).getQuantity());
        }
        inputSalesArray.clear();
    }

    /**
     * Calculates the total quantity sold within a month
     * @return total item quantity value
     */
    private int totalQuantity() {
        int total = 0;
        for (DataTypes dataTypes : Reports.salesArray) {
            total += dataTypes.getQuantity();
        }
        return total;
    }

    /**
     * Calculates the total price of the sold items within a month
     * @return total price value of all items
     */
    private double totalPrice() {
        double total = 0;
        for (DataTypes dataTypes : Reports.salesArray) {
            total += dataTypes.getAmount();
        }
        return total;
    }

    /**
     * Calculates the total revenue of transactions within a month
     * @return total transactions revenue value
     */
    private double totalRevenue() {
        double total = 0;
        for (DataTypes dataTypes : Reports.transactionsArray) {
            total += dataTypes.getAmount();
        }
        return total;
    }

    /**
     * Transactions table data handling from reports() method
     */
    private static void transactions() {
        // Remove first row if column headers exist
        if (!transactionsFile.getInputBuffer().isEmpty() &&
                transactionsFile.getInputBuffer().get(0).get(0).equals("ID")
        ) {
            transactionsFile.getInputBuffer().remove(0);
        }
        for (int i = 0; i < transactionsFile.getInputBuffer().size(); i++) {
            if (transactionsFile.getInputBuffer().get(i).get(0).equals("ID")) {
                continue;
            }
            DataTypes data1 = new DataTypes();
            data1.setID(
                    transactionsFile.getInputBuffer().get(i).get(0)
            );
            data1.setName(
                    transactionsFile.getInputBuffer().get(i).get(1)
            );
            data1.setPaymentType(
                    transactionsFile.getInputBuffer().get(i).get(2)
            );
            data1.setCardNumber(
                    transactionsFile.getInputBuffer().get(i).get(3)
            );
            data1.setAmount(
                    Double.parseDouble(transactionsFile.getInputBuffer().get(i).get(4))
            );
            transactionsArray.add(data1);
        }
        // Clear buffer to prevent duplication of records from previously read databases
        transactionsFile.getInputBuffer().clear();
    }

    /**
     * Prints a CLI version of the Sales table to the console/log for debugging purposes.
     */
    private static void printSalesTable() {
        // Table headers
        System.out.println(
                "---------------------------------------------" +
                        "-----------------------------------------------"
        );
        System.out.printf("| %10s | %10s | %10s | %10s | %10s | %10s | %10s |\n",
                "Record #", "ID", "Name", "Supplier", "Price", "Quantity", "Amount"
        );
        System.out.println(
                "---------------------------------------------" +
                        "-----------------------------------------------"
        );

        if (salesFile.getInputBuffer().isEmpty() && salesArray.isEmpty()) {
            System.out.println("WARNING: File not found or Sales database is empty. " +
                    "There are no records for quantity during the selected month/year.");
            JFrame alert = new JFrame();
            JOptionPane.showMessageDialog(
                    alert,
                    "File not found or Sales database is empty." +
                            "\nThere are no records for quantity " +
                            "during the selected month/year.",
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
        System.out.println(
                "---------------------------------------------" +
                        "-----------------------------------------------"
        );
    }

    /**
     * Prints a CLI version of the Transactions table to the console/log for debugging purposes.
     */
    private static void printTransactionsTable() {
        // Table headers
        System.out.println("----------------------------------" +
                "----------------------------------------------");
        System.out.printf("| %10s | %10s | %10s | %11s | %10s | %10s |\n",
                "Record #", "ID", "Name", "Pay Type", "Card Num", "Amount"
        );
        System.out.println("----------------------------------" +
                "----------------------------------------------");

        if (transactionsFile.getInputBuffer().isEmpty() && transactionsArray.isEmpty()) {
            System.out.println("WARNING: Transactions database is empty. " +
                    "There are no records during the selected month/year.");
            JFrame alert = new JFrame();
            JOptionPane.showMessageDialog(
                    alert,
                    "File not found or Transactions database is empty." +
                            "\nThere are no records during the selected month/year.",
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
        System.out.println("----------------------------------" +
                "----------------------------------------------");
    }

    /**
     * Initializes the Reports GUI and other private methods within the class.
     * Reset dates.
     * @throws IOException IO error handling
     */
    public static void launchUI() throws IOException {
        System.out.println("=====REPORTS MENU=====");
        // Default to today's time
        currentDate = LocalDate.now();
        fileDate = currentDate.getYear() +
                "-" + dayFormat.format(currentDate.getMonthValue());
        selectedYear = currentDate.getYear();
        selectedMonth = currentDate.getMonthValue();
        reports();
        printSalesTable();
        printTransactionsTable();
        JFrame frame = new Reports("SalesApp " + Main.version + " | Reports");
        frame.setVisible(true);
    }
}
