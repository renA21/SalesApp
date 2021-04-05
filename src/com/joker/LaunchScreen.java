package com.joker;

import javax.swing.*;

/**
 * Launch Screen GUI
 */
public class LaunchScreen extends JFrame {
    private JPanel launchPanel;
    private JButton launchButton;
    private JButton exitButton;

    /**
     * Creates the GUI and its declared Swing components.
     * @param title set window title.
     */
    public LaunchScreen(String title) {
        super(title);
        this.setContentPane(launchPanel);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);

        // Set system theme
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                UnsupportedLookAndFeelException e
        ) {
            e.printStackTrace();
        }

        // Exit confirmation when closing the window
        JFrame confirmExit = new JFrame();
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
            System.out.println("INFO: Entered Main Menu.");
            MainMenu.launchUI();
            dispose();
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

    /**
     * Initializes the LaunchScreen GUI.
     */
    public static void launchUI() {
        System.out.println("INFO: Opened Launch Screen window.");
        System.out.println("=====LAUNCH SCREEN=====");
        JFrame frame = new LaunchScreen("SalesApp " + Main.version + " | Launch Screen");
        frame.setVisible(true);
    }

}
