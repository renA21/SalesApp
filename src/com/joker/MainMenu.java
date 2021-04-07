package com.joker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Main Menu GUI
 */
public class MainMenu extends JFrame {
    private JButton inventoryButton;
    private JButton transactionsButton;
    private JButton salesButton;
    private JButton reportsButton;
    private JPanel mainMenuPanel;
    private JLabel dateLabel;

    /**
     * Creates the GUI and its declared Swing components.
     * @param title Title name for the window.
     */
    public MainMenu(String title) {
        super(title);
        this.setContentPane(mainMenuPanel);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);

        // Exit confirmation when closing the window
        JFrame confirmExit = new JFrame();
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

        // Show current time and date
        new Timer(0, (ActionEvent e) -> {
            Date d = new Date();
            SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss a yyyy-MM-dd");
            dateLabel.setText(formatTime.format(d));
        }).start();

        inventoryButton.addActionListener(e -> {
            try {
                System.out.println("INFO: Entered Inventory Menu.");
                Inventory.launchUI();
                dispose();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        transactionsButton.addActionListener(e -> {
            try {
                System.out.println("INFO: Entered Transactions Menu.");
                Transactions.launchUI();
                dispose();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        salesButton.addActionListener(e -> {
            try {
                System.out.println("INFO: Entered Sales Menu.");
                Sales.launchUI();
                dispose();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        reportsButton.addActionListener(e -> {
            try {
                System.out.println("INFO: Entered Reports Menu.");
                Reports.launchUI();
                dispose();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    /**
     * Initializes the MainMenu GUI and other private methods within the class.
     */
    public static void launchUI() {
        System.out.println("=====MAIN MENU=====");
        JFrame frame = new MainMenu("SalesApp " + Main.version + " | Main Menu");
        frame.setVisible(true);
    }
}
