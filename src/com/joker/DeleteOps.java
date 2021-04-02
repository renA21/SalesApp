package com.joker;

import javax.swing.*;
import java.util.ArrayList;

public class DeleteOps extends JFrame {
    private JLabel deleteTitle;
    private JLabel quantityLabel;
    private JTextField quantityField;
    private JLabel priceLabel;
    private JLabel locationLabel;
    private JLabel stockLabel;
    private JLabel supplierLabel;
    private JLabel nameLabel;
    private JLabel recordLabel;
    private JTextField nameField;
    private JTextField supplierField;
    private JTextField stockField;
    private JTextField priceField;
    private JLabel payLabel;
    private JLabel cardLabel;
    private JTextField cardField;
    private JTextField amountField;
    private JLabel amountLabel;
    private JLabel idLabel;
    private JTextField idField;
    private JComboBox recComboBox;
    private JButton deleteButton;
    private JButton cancelButton;
    private JTextField locationField;
    private JTextField payTypeField;
    private JPanel mainPanel;
    private JLabel deleteLabel;

    public DeleteOps(String title,
                     String menuType,
                     ArrayList<DataTypes> inputArray0,
                     ArrayList<DataTypes> inputArray1)
    {
        super(title);
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);

        // set visibility of components based on the menu
        switch (menuType) {
            case ("Inventory"):
                idLabel.setVisible(true);
                idField.setVisible(true);
                nameLabel.setVisible(true);
                nameField.setVisible(true);
                supplierLabel.setVisible(true);
                supplierField.setVisible(true);
                stockLabel.setVisible(true);
                stockField.setVisible(true);
                locationLabel.setVisible(true);
                locationField.setVisible(true);
                priceLabel.setVisible(true);
                priceField.setVisible(true);
                quantityLabel.setVisible(false);
                quantityField.setVisible(false);
                payLabel.setVisible(false);
                payTypeField.setVisible(false);
                cardLabel.setVisible(false);
                cardField.setVisible(false);
                amountLabel.setVisible(false);
                amountField.setVisible(false);
                break;
            case ("Sales"):
                idLabel.setVisible(true);
                idField.setVisible(true);
                nameLabel.setVisible(true);
                nameField.setVisible(true);
                supplierLabel.setVisible(true);
                supplierField.setVisible(true);
                stockLabel.setVisible(false);
                stockField.setVisible(false);
                locationLabel.setVisible(false);
                locationField.setVisible(false);
                priceLabel.setVisible(false);
                priceField.setVisible(false);
                quantityLabel.setVisible(true);
                quantityField.setVisible(true);
                payLabel.setVisible(false);
                payTypeField.setVisible(false);
                cardLabel.setVisible(false);
                cardField.setVisible(false);
                amountLabel.setText("Amount Earned:");
                amountLabel.setVisible(true);
                amountField.setVisible(true);
                break;
            case ("Transactions"):
                idLabel.setVisible(true);
                idField.setVisible(true);
                nameLabel.setVisible(true);
                nameField.setVisible(true);
                supplierLabel.setVisible(false);
                supplierField.setVisible(false);
                stockLabel.setVisible(false);
                stockField.setVisible(false);
                locationLabel.setVisible(false);
                locationField.setVisible(false);
                priceLabel.setVisible(false);
                priceField.setVisible(false);
                quantityLabel.setVisible(false);
                quantityField.setVisible(false);
                payLabel.setVisible(true);
                payTypeField.setVisible(true);
                cardLabel.setVisible(true);
                cardField.setVisible(true);
                amountLabel.setVisible(true);
                amountField.setVisible(true);
                break;
            default:
                break;
        }

        // Panel Title/Records are not deleted in Sales, rather, cleared.
        if (menuType.equals("Sales")) {
            deleteTitle.setText("Clear record in " + menuType);
            deleteLabel.setText("Please choose the Record # you wish to clear:");
        } else {
            deleteTitle.setText("Delete record in " + menuType);
        }

        // List number of records present in the table
        for (int i = 0; i < inputArray0.size(); i++) {
            recComboBox.addItem(i);
        }

        // Disable clearing when quantity is zero.
        if (menuType.equals("Sales")) {
            if (inputArray1.get(recComboBox.getSelectedIndex()).getQuantity() == 0) {
                JFrame warning = new JFrame();
                JOptionPane.showMessageDialog(
                        warning,
                        "Cannot edit the selected record, Quantity is zero.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
                deleteButton.setEnabled(false);
            }

        }

        // Fill the text fields/combo boxes corresponding to the selected record #.
        idField.setText(inputArray0.get(recComboBox.getSelectedIndex()).getID());
        nameField.setText(inputArray0.get(recComboBox.getSelectedIndex()).getName());
        supplierField.setText(inputArray0.get(recComboBox.getSelectedIndex()).getSupplier());
        stockField.setText(Integer.toString(inputArray0.get(recComboBox.getSelectedIndex()).getStock()));
        locationField.setText(inputArray0.get(recComboBox.getSelectedIndex()).getLocation());
        priceField.setText(Double.toString(inputArray0.get(recComboBox.getSelectedIndex()).getPrice()));
        payTypeField.setText(inputArray0.get(recComboBox.getSelectedIndex()).getPaymentType());
        cardField.setText(inputArray0.get(recComboBox.getSelectedIndex()).getCardNumber());
        if (menuType.equals("Sales")) {
            quantityField.setText(Integer.toString(inputArray1.get(recComboBox.getSelectedIndex()).getQuantity()));
            amountField.setText(Double.toString(inputArray1.get(recComboBox.getSelectedIndex()).getAmount()));
        } else {
            amountField.setText(Double.toString(inputArray0.get(recComboBox.getSelectedIndex()).getAmount()));
        }

        recComboBox.addActionListener(e -> {
            if (menuType.equals("Sales")) {
                if (inputArray1.get(recComboBox.getSelectedIndex()).getQuantity() == 0) {
                    JFrame warning = new JFrame();
                    JOptionPane.showMessageDialog(
                            warning,
                            "Cannot edit the selected record, Quantity is zero.",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE
                    );
                    deleteButton.setEnabled(false);
                }
            }
            idField.setText(inputArray0.get(recComboBox.getSelectedIndex()).getID());
            nameField.setText(inputArray0.get(recComboBox.getSelectedIndex()).getName());
            supplierField.setText(inputArray0.get(recComboBox.getSelectedIndex()).getSupplier());
            stockField.setText(Integer.toString(inputArray0.get(recComboBox.getSelectedIndex()).getStock()));
            locationField.setText(inputArray0.get(recComboBox.getSelectedIndex()).getLocation());
            priceField.setText(Double.toString(inputArray0.get(recComboBox.getSelectedIndex()).getPrice()));
            payTypeField.setText(inputArray0.get(recComboBox.getSelectedIndex()).getPaymentType());
            cardField.setText(inputArray0.get(recComboBox.getSelectedIndex()).getCardNumber());
            if (menuType.equals("Sales")) {
                quantityField.setText(Integer.toString(inputArray1.get(recComboBox.getSelectedIndex()).getQuantity()));
                amountField.setText(Double.toString(inputArray1.get(recComboBox.getSelectedIndex()).getAmount()));
            } else {
                amountField.setText(Double.toString(inputArray0.get(recComboBox.getSelectedIndex()).getAmount()));
            }
        });

        if (menuType.equals("Sales")) {
            deleteButton.setText("Clear");
        }

        deleteButton.addActionListener(e -> {
            switch (menuType) {
                case "Inventory":
                    Inventory.deleteData(recComboBox.getSelectedIndex());
                    break;
                case "Sales":
                    Sales.deleteData(recComboBox.getSelectedIndex());
                    break;
                case "Transactions":
                    Transactions.deleteData(recComboBox.getSelectedIndex());
                    break;
            }
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());
    }

    public static void launchUI(String menuType, ArrayList<DataTypes> inputArray0, ArrayList<DataTypes> inputArray1) {
        JFrame frame = new DeleteOps("Delete", menuType, inputArray0, inputArray1);
        frame.setVisible(true);
    }
}
