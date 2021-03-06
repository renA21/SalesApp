package com.joker;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * EditOps GUI
 * Separate small window that handles the editing of data in a table.
 */
public class EditOps extends JFrame {
    // Decimal formatting for currency
    private final DecimalFormat df = new DecimalFormat("#0.00");

    private JLabel editTitle;
    private JLabel quantityLabel;
    private JTextField quantityField;
    private JLabel priceLabel;
    private JLabel locationLabel;
    private JLabel stockLabel;
    private JLabel supplierLabel;
    private JLabel nameLabel;
    private JLabel idLabel;
    private JTextField nameField;
    private JTextField supplierField;
    private JTextField stockField;
    private JComboBox locComboBox;
    private JTextField priceField;
    private JLabel payLabel;
    private JComboBox payComboBox;
    private JLabel cardLabel;
    private JTextField cardField;
    private JTextField amountField;
    private JLabel amountLabel;
    private JButton editButton;
    private JButton cancelButton;
    private JPanel mainPanel;
    private JTextField idField;
    private JComboBox recComboBox;

    /**
     * Creates the GUI and its declared Swing components.
     * @param title Title name for the window.
     * @param menuType The type of menu where DeleteOps was instantiated from.
     * @param inputArray0 Setting the first ArrayList.
     * @param inputArray1 Setting the second Arraylist. Set to null when working with only one Arraylist.
     */
    public EditOps(
            String title,
            int menuType,
            ArrayList<DataTypes> inputArray0,
            ArrayList<DataTypes> inputArray1
    ) {
        super(title);
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);

        // Set visibility of components based on the menu
        switch (menuType) {
            case 0 -> {
                idLabel.setVisible(true);
                idField.setVisible(true);
                nameLabel.setVisible(true);
                nameField.setVisible(true);
                supplierLabel.setVisible(true);
                supplierField.setVisible(true);
                stockLabel.setVisible(true);
                stockField.setVisible(true);
                locationLabel.setVisible(true);
                locComboBox.setVisible(true);
                priceLabel.setVisible(true);
                priceField.setVisible(true);
                quantityLabel.setVisible(false);
                quantityField.setVisible(false);
                payLabel.setVisible(false);
                payComboBox.setVisible(false);
                cardLabel.setVisible(false);
                cardField.setVisible(false);
                amountLabel.setVisible(false);
                amountField.setVisible(false);
            }
            case 1 -> {
                idLabel.setVisible(true);
                idField.setVisible(true);
                nameLabel.setVisible(true);
                nameField.setVisible(true);
                supplierLabel.setVisible(true);
                supplierField.setVisible(true);
                stockLabel.setVisible(false);
                stockField.setVisible(false);
                locationLabel.setVisible(false);
                locComboBox.setVisible(false);
                priceLabel.setVisible(true);
                priceField.setVisible(true);
                quantityLabel.setVisible(true);
                quantityField.setVisible(true);
                payLabel.setVisible(false);
                payComboBox.setVisible(false);
                cardLabel.setVisible(false);
                cardField.setVisible(false);
                amountLabel.setText("Amount Earned:");
                amountLabel.setVisible(true);
                amountField.setVisible(true);
            }
            case 2 -> {
                idLabel.setVisible(true);
                idField.setVisible(true);
                nameLabel.setVisible(true);
                nameField.setVisible(true);
                supplierLabel.setVisible(false);
                supplierField.setVisible(false);
                stockLabel.setVisible(false);
                stockField.setVisible(false);
                locationLabel.setVisible(false);
                locComboBox.setVisible(false);
                priceLabel.setVisible(false);
                priceField.setVisible(false);
                quantityLabel.setVisible(false);
                quantityField.setVisible(false);
                payLabel.setVisible(true);
                payComboBox.setVisible(true);
                cardLabel.setVisible(true);
                cardField.setVisible(true);
                amountLabel.setVisible(true);
                amountField.setVisible(true);
            }
        }

        // Add location and payment type combo box options
        locComboBox.addItem("Shop");
        locComboBox.addItem("Warehouse");
        payComboBox.addItem("Credit Card");
        payComboBox.addItem("Cash");

        // Panel Title
        switch (menuType) {
            case 0 -> editTitle.setText("Edit record in Inventory");
            case 1 -> editTitle.setText("Edit record in Sales");
            case 2 -> editTitle.setText("Edit record in Transactions");
        }

        // List number of records present in the table
        for (int i = 0; i < inputArray0.size(); i++) {
            recComboBox.addItem(i);
        }

        // Fill the text fields/combo boxes corresponding to the selected record #.
        idField.setText(
                inputArray0.get(recComboBox.getSelectedIndex()).getID()
        );
        nameField.setText(
                inputArray0.get(recComboBox.getSelectedIndex()).getName()
        );
        supplierField.setText(
                inputArray0.get(recComboBox.getSelectedIndex()).getSupplier()
        );
        stockField.setText(
                Integer.toString(inputArray0.get(recComboBox.getSelectedIndex()).getStock())
        );
        if (menuType == 0) {
            if (inputArray0.get(
                    recComboBox.getSelectedIndex()).getLocation().equals("Shop")
            ) {
                locComboBox.setSelectedIndex(0);
            } else {
                locComboBox.setSelectedIndex(1);
            }
        }
        priceField.setText(
                df.format(inputArray0.get(recComboBox.getSelectedIndex()).getPrice())
        );
        if (menuType == 2) {
            if (inputArray0.get(
                    recComboBox.getSelectedIndex())
                    .getPaymentType()
                    .equals("Credit Card")
            ) {
                payComboBox.setSelectedIndex(0);
            } else {
                payComboBox.setSelectedIndex(1);
            }
        }
        // Enable card number field if Credit Card is selected from the combo box.
        cardField.setText(
                inputArray0.get(recComboBox.getSelectedIndex()).getCardNumber()
        );
        if (menuType == 1) {
            if (!inputArray1.isEmpty()) {
                amountField.setText(
                        df.format(inputArray1.get(
                                recComboBox.getSelectedIndex()).getQuantity() *
                        inputArray0.get(recComboBox.getSelectedIndex()).getPrice())
                );
            }
        }
        if (menuType == 1) {
            quantityField.setText(
                    Integer.toString(inputArray1.get(
                            recComboBox.getSelectedIndex()).getQuantity()
                    )
            );
            amountField.setText(
                    df.format(inputArray1.get(recComboBox.getSelectedIndex()).getAmount())
            );
        } else {
            amountField.setText(
                    df.format(inputArray0.get(recComboBox.getSelectedIndex()).getAmount())
            );
        }
        // Disable text field editing of inventory data for Sales menu
        if (menuType == 1) {
            idField.setEditable(false);
            nameField.setEditable(false);
            supplierField.setEditable(false);
            priceField.setEditable(false);
            // Disable editing for items not present in the shop
            if (inputArray0.get(
                    recComboBox.getSelectedIndex()).getLocation().equals("Warehouse")
            ) {
                JFrame alert = new JFrame();
                JOptionPane.showMessageDialog(alert,
                        "Cannot edit the selected Record #." +
                                "The selected item is not present at the shop.",
                        "warning",
                        JOptionPane.WARNING_MESSAGE
                );
                quantityField.setEditable(false);
                amountField.setEditable(false);
                editButton.setEnabled(false);
            } else {
                quantityField.setEditable(true);
                amountField.setEditable(true);
                editButton.setEnabled(true);
            }
        }

        recComboBox.addActionListener(e -> {
            idField.setText(
                    inputArray0.get(recComboBox.getSelectedIndex()).getID()
            );
            nameField.setText(
                    inputArray0.get(recComboBox.getSelectedIndex()).getName()
            );
            supplierField.setText(
                    inputArray0.get(recComboBox.getSelectedIndex()).getSupplier()
            );
            stockField.setText(
                    Integer.toString(inputArray0.get(
                            recComboBox.getSelectedIndex()).getStock()
                    )
            );
            if (menuType == 0) {
                if (inputArray0.get(
                        recComboBox.getSelectedIndex()).getLocation().equals("Shop")
                ) {
                    locComboBox.setSelectedIndex(0);
                } else {
                    locComboBox.setSelectedIndex(1);
                }
            }
            priceField.setText(
                    df.format(inputArray0.get(recComboBox.getSelectedIndex()).getPrice())
            );
            if (menuType == 2) {
                if (inputArray0.get(
                        recComboBox.getSelectedIndex())
                        .getPaymentType()
                        .equals("Credit Card")
                ) {
                    payComboBox.setSelectedIndex(0);
                } else {
                    payComboBox.setSelectedIndex(1);
                }
            }
            // Enable card number field if Credit Card is selected from the combo box.
            cardField.setText(
                    inputArray0.get(recComboBox.getSelectedIndex()).getCardNumber()
            );
            if (menuType == 1) {
                if (!inputArray1.isEmpty()) {
                    amountField.setText(
                            df.format(inputArray1.get(
                                    recComboBox.getSelectedIndex()).getQuantity() *
                            inputArray0.get(recComboBox.getSelectedIndex()).getPrice())
                    );
                }
            }
            if (menuType == 1) {
                quantityField.setText(
                        Integer.toString(inputArray1.get(
                                recComboBox.getSelectedIndex()).getQuantity()
                        )
                );
                amountField.setText(
                        df.format(inputArray1.get(
                                recComboBox.getSelectedIndex()).getAmount()
                        )
                );
            } else {
                amountField.setText(
                        df.format(inputArray0.get(
                                recComboBox.getSelectedIndex()).getAmount()
                        )
                );
            }
            // Disable text field editing of inventory data for Sales menu
            if (menuType == 1) {
                idField.setEditable(false);
                nameField.setEditable(false);
                supplierField.setEditable(false);
                priceField.setEditable(false);
                // Disable editing for items not present in the shop
                if (inputArray0.get(
                        recComboBox.getSelectedIndex()).getLocation().equals("Warehouse")
                ) {
                    JFrame alert = new JFrame();
                    JOptionPane.showMessageDialog(alert,
                            "Cannot edit the selected Record #. " +
                                    "The selected item is not present at the shop.",
                            "Alert",
                            JOptionPane.WARNING_MESSAGE
                    );
                    quantityField.setEditable(false);
                    amountField.setEditable(false);
                    editButton.setEnabled(false);
                } else {
                    quantityField.setEditable(true);
                    amountField.setEditable(true);
                    editButton.setEnabled(true);
                }
            }
        });

        editButton.addActionListener(e -> {
            switch (menuType) {
                case 0 -> Inventory.editData(
                        recComboBox.getSelectedIndex(),
                        idField.getText(),
                        nameField.getText(),
                        supplierField.getText(),
                        Integer.parseInt(stockField.getText()),
                        String.valueOf(locComboBox.getSelectedItem()),
                        Double.parseDouble(priceField.getText())
                );
                case 1 -> Sales.editData(
                        recComboBox.getSelectedIndex(),
                        idField.getText(),
                        nameField.getText(),
                        supplierField.getText(),
                        Double.parseDouble(priceField.getText()),
                        Integer.parseInt(quantityField.getText()),
                        Double.parseDouble(amountField.getText())
                );
                case 2 -> Transactions.editData(
                        recComboBox.getSelectedIndex(),
                        idField.getText(),
                        nameField.getText(),
                        String.valueOf(payComboBox.getSelectedItem()),
                        cardField.getText(),
                        Double.parseDouble(amountField.getText())
                );
            }
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        // Accept only numbers
        stockField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                stockField.setEditable(
                        e.getKeyChar() >= '0' &&
                        e.getKeyChar() <= '9' ||
                        e.getKeyCode() == KeyEvent.VK_BACK_SPACE
                );
            }
        });

        priceField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                priceField.setEditable(
                        e.getKeyChar() >= '0' &&
                        e.getKeyChar() <= '9' ||
                        e.getKeyChar() == '.' ||
                        e.getKeyCode() == KeyEvent.VK_BACK_SPACE
                );
            }
        });

        quantityField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                quantityField.setEditable(
                        e.getKeyChar() >= '0' &&
                        e.getKeyChar() <= '9' ||
                        e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                        e.getKeyCode() == KeyEvent.VK_ENTER
                );
            }
        });

        cardField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                cardField.setEditable(
                        e.getKeyChar() >= '0' &&
                        e.getKeyChar() <= '9' ||
                        e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                        e.getKeyCode() == KeyEvent.VK_ENTER
                );
            }
        });

        amountField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                amountField.setEditable(
                        e.getKeyChar() >= '0' &&
                        e.getKeyChar() <= '9' ||
                        e.getKeyChar() == '.' ||
                        e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                        e.getKeyCode() == KeyEvent.VK_ENTER
                );
            }
        });

        quantityField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    double tempVal = Double.parseDouble(quantityField.getText()) *
                            inputArray0.get(recComboBox.getSelectedIndex()).getPrice();
                    amountField.setText(String.valueOf(df.format(tempVal)));
                }
            }
        });

        payComboBox.addActionListener(e -> {
            if (payComboBox.getSelectedIndex() == 1) {
                cardField.setText("N/A");
                cardField.setEditable(false);
            } else {
                cardField.setText(null);
                cardField.setEditable(true);
            }
        });
    }

    /**
     * Initializes the EditOps GUI
     * @param menuType input menu type
     * @param inputArray0 input first ArrayList
     * @param inputArray1 input second ArrayList
     */
    public static void launchUI(
            int menuType,
            ArrayList<DataTypes> inputArray0,
            ArrayList<DataTypes> inputArray1
    ) {
        JFrame frame = new EditOps("Edit", menuType, inputArray0, inputArray1);
        frame.setVisible(true);
    }
}