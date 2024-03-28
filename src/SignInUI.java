import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SignInUI extends JFrame {
    private JTextField txtUsername;
    private JTextField txtPassword;
    private JButton btnSignIn, btnRegisterNow;
    private JLabel lblPhoto;
    private AuthenticationStrategy authenticationStrategy;

    public SignInUI(AuthenticationStrategy authenticationStrategy) {
        this.authenticationStrategy = authenticationStrategy; // Set the strategy
        setTitle("Quackstagram - Register");
        setLayout(new BorderLayout(10, 10));
        initializeUI();
    }

    private void initializeUI() {
        // Profile picture placeholder without border
        lblPhoto = new JLabel();
        lblPhoto.setPreferredSize(new Dimension(80, 80));
        lblPhoto.setHorizontalAlignment(JLabel.CENTER);
        lblPhoto.setVerticalAlignment(JLabel.CENTER);
        lblPhoto.setIcon(new ImageIcon(new ImageIcon("img/logos/DACS.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        JPanel photoPanel = new JPanel(); // Use a panel to center the photo label
        photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        photoPanel.add(lblPhoto);

        // Text fields panel
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        txtUsername = new JTextField("Username");
        txtPassword = new JTextField("Password");

        txtUsername.setForeground(Color.GRAY);
        txtPassword.setForeground(Color.GRAY);

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(photoPanel);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtUsername);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(Box.createVerticalStrut(10));


        // Register button with black text
        btnSignIn = new JButton("Sign-In");
        btnSignIn.addActionListener(this::onSignInClicked);
        btnSignIn.setBackground(new Color(255, 90, 95)); // Use a red color that matches the mockup
        btnSignIn.setForeground(Color.BLACK); // Set the text color to black
        btnSignIn.setFocusPainted(false);
        btnSignIn.setBorderPainted(false);
        btnSignIn.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel registerPanel = new JPanel(new BorderLayout()); // Panel to contain the register button
        registerPanel.setBackground(Color.WHITE); // Background for the panel
        registerPanel.add(btnSignIn, BorderLayout.CENTER);

        // Adding components to the frame
        add(Components.getHeaderPanel("Quackstagram"), BorderLayout.NORTH);
        add(fieldsPanel, BorderLayout.CENTER);
        add(registerPanel, BorderLayout.SOUTH);

        // New button for navigating to SignUpUI
        btnRegisterNow = new JButton("No Account? Register Now");
        btnRegisterNow.addActionListener(this::onRegisterNowClicked);
        btnRegisterNow.setBackground(Color.WHITE); // Set a different color for distinction
        btnRegisterNow.setForeground(Color.BLACK);
        btnRegisterNow.setFocusPainted(false);
        btnRegisterNow.setBorderPainted(false);

        // Panel to hold both buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // Grid layout with 1 row, 2 columns
        buttonPanel.setBackground(Color.white);
        buttonPanel.add(btnSignIn);
        buttonPanel.add(btnRegisterNow);
        btnSignIn.addActionListener(this::onSignInClicked);
        btnRegisterNow.addActionListener(this::onRegisterNowClicked);

        // Adding the button panel to the frame
        add(buttonPanel, BorderLayout.SOUTH);

    }

    private void onSignInClicked(ActionEvent event) {
        String enteredUsername = txtUsername.getText();
        String enteredPassword = txtPassword.getText();

        // Use the strategy to authenticate
        if (authenticationStrategy.authenticate(enteredUsername, enteredPassword)) {
            System.out.println("Authentication successfull: " + enteredUsername);
            FrameManager.openFrame("PROFILE", User.getCurrentUser());
            // Proceed with navigation or any other action upon successful authentication
        } else {
            System.out.println("Authentication failed");
            // Handle failed authentication
        }
    }

    private void onRegisterNowClicked(ActionEvent event) {
        // Go to the SignUpUI frame
        FrameManager.openFrame("SIGN_UP", null);
    }
}

