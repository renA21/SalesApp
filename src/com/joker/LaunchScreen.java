package com.joker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LaunchScreen extends JFrame {
    private JPanel launchPanel;
    private JButton launchButton;
    private JButton exitButton;

    public LaunchScreen(String title) {
        super(title);
        this.setContentPane(launchPanel);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);

        JFrame confirmExit = new JFrame();

        // System theme
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Exit confirmation when closing the window
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int option = JOptionPane.showConfirmDialog(confirmExit,
                        "Are you sure you want to exit?",
                        "Exit", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.out.println("INFO: The program closed successfully.");
                    System.exit(0);
                }
            }
        });

        launchButton.addActionListener(e -> {
            try {
                System.out.println("INFO: Entered Main Menu.");
                MainMenu.launchUI();
                dispose();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        exitButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(
                    confirmExit,
                    "Are you sure you want to exit?",
                    "Exit", JOptionPane.YES_NO_OPTION
            );
            if (option == JOptionPane.YES_OPTION) {
                System.out.println("INFO: The program closed successfully.");
                System.exit(0);
            }
        });
    }

    public static void launchUI() {
        System.out.println("INFO: Opened Launch Screen window.");
        System.out.println("=====LAUNCH SCREEN=====");
        JFrame frame = new LaunchScreen("SalesApp " + Main.version + " | Launch Screen");
        frame.setVisible(true);
    }

}
