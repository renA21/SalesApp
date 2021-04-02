package com.joker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.security.Key;

public class AddOps extends JFrame {
    private JPanel mainPanel;
    private JButton addButton;
    private JButton cancelButton;
    private JTextField quantityField;
    private JTextField idField;
    private JTextField nameField;
    private JTextField supplierField;
    private JTextField stockField;
    private JComboBox locComboBox;
    private JTextField priceField;
    private JComboBox payComboBox;
    private JTextField cardField;
    private JTextField amountField;
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel supplierLabel;
    private JLabel stockLabel;
    private JLabel locationLabel;
    private JLabel priceLabel;
    private JLabel quantityLabel;
    private JLabel payLabel;
    private JLabel cardLabel;
    private JLabel amountLabel;
    private JLabel addTitle;

    public AddOps(String title, String menuType) {

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
                break;
        }

        // Add location and payment type combo box options
        locComboBox.addItem("Shop");
        locComboBox.addItem("Warehouse");
        payComboBox.addItem("Credit Card");
        payComboBox.addItem("Cash");

        // Panel Title
        addTitle.setText("New record in " + menuType);

        addButton.addActionListener(e -> {
            try {
                switch (menuType) {
                    case "Inventory":
                        Inventory.addData(
                                idField.getText(),
                                nameField.getText(),
                                supplierField.getText(),
                                Integer.parseInt(stockField.getText()),
                                String.valueOf(locComboBox.getSelectedItem()),
                                Double.parseDouble(priceField.getText())
                        );
                        break;
                    // might not be needed
                    /*case "Sales":
                        Sales.addData(
                                idField.getText(),
                                nameField.getText(),
                                supplierField.getText(),
                                Double.parseDouble(priceField.getText()),
                                Integer.parseInt(quantityField.getText()),
                                Double.parseDouble(amountField.getText())
                        );
                        break;*/
                    case "Transactions":
                        Transactions.addData(
                                idField.getText(),
                                nameField.getText(),
                                String.valueOf(payComboBox.getSelectedItem()),
                                cardField.getText(),
                                Double.parseDouble(amountField.getText())
                        );
                        break;
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        // Accept only numbers
        stockField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                stockField.setEditable(
                        e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || e.getKeyCode() == KeyEvent.VK_BACK_SPACE
                );
            }
        });

        priceField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                priceField.setEditable(
                        e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || e.getKeyChar() == '.' || e.getKeyCode() == KeyEvent.VK_BACK_SPACE
                );
            }
        });

        quantityField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                quantityField.setEditable(
                        e.getKeyChar() >= '0' && e.getKeyChar() <= '9' ||
                                e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                                e.getKeyCode() == KeyEvent.VK_ENTER
                );
            }
        });

        cardField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                cardField.setEditable(
                        e.getKeyChar() >= '0' && e.getKeyChar() <= '9' ||
                                e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                                e.getKeyCode() == KeyEvent.VK_ENTER
                );
            }
        });

        amountField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                amountField.setEditable(
                        e.getKeyChar() >= '0' && e.getKeyChar() <= '9' ||
                                e.getKeyChar() == '.' ||
                                e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                                e.getKeyCode() == KeyEvent.VK_ENTER
                );
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

    public static void launchUI(String menuType) {
        JFrame frame = new AddOps("Add", menuType);
        frame.setVisible(true);
    }
}