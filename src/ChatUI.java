import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

public class ChatUI extends JFrame {

    String username = User.getCurrentUser().getUsername();
    static String recipientUsername;
    File file;
    static String path;
    static Client client;
    JTextArea fileContentTextArea; // Declare fileContentTextArea as a class-level variable
    JTextArea messageTextArea; 
    ArrayList<JLabel> followerLabels = new ArrayList<>();


    public ChatUI() {
        setTitle("ChatRoom");
        setLayout(new BorderLayout());
        initializeUI();
        try{
            Client.createClient();
        }catch (IOException e){}
    }

    private void initializeUI() {
        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());


        // Initialize fileContentTextArea
        fileContentTextArea = new JTextArea();
        fileContentTextArea.setEditable(false); // Set the text area to be read-only

        JScrollPane scrollPane = new JScrollPane(fileContentTextArea);

        // Left panel to display names of people
        JPanel namesPanel = new JPanel();
        namesPanel.setLayout(new BoxLayout(namesPanel, BoxLayout.Y_AXIS));

        messageTextArea = new JTextArea();
        messageTextArea.setEditable(false); // Set the text area to be read-only

        // Read following.txt and extract names of people following the current user
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/following.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].trim().equals(User.getCurrentUser().getUsername())) {
                    String[] followers = parts[1].trim().split(";");
                    for (String follower : followers) {
                        JLabel followerLabel = new JLabel(follower.trim());
                        followerLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

                        followerLabel.setOpaque(true);
                        followerLabels.add(followerLabel);
                        followerLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                // Handle click event
                                String clickedName = followerLabel.getText();
                                setReceipant(clickedName, followerLabel);
                            }
                        });
                        namesPanel.add(followerLabel);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(followerLabels.size() > 0){
            setReceipant(followerLabels.get(0).getText(), followerLabels.get(0));
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new BorderLayout());
            JTextField inputField = new JTextField();
            JButton sendButton = new JButton("Send");
            sendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String message = inputField.getText();
                    if(message.isEmpty()){
                        return;
                    }
                    sendMessage(message);
                    inputField.setText(""); // Clear the input field after sending message
                }
            });
            inputPanel.add(inputField,BorderLayout.CENTER);
            inputPanel.add(sendButton, BorderLayout.EAST);



            // Right panel to display text messages and input text box
            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BorderLayout());

            // Text area to display messages

            JScrollPane messageScrollPane = new JScrollPane(messageTextArea);
            rightPanel.add(messageScrollPane, BorderLayout.CENTER);
            rightPanel.add(inputPanel, BorderLayout.SOUTH);

            // Create a split pane to divide the screen
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, namesPanel, rightPanel);
            splitPane.setResizeWeight(0.3); // Adjust the divider location

            // Add panels to the content panel

            contentPanel.add(splitPane, BorderLayout.CENTER);
        }else{
            contentPanel.add(new JLabel("You are not following anyone"), BorderLayout.CENTER);
        }
        // Add content panel to the frame
        add(contentPanel, BorderLayout.CENTER);

        add(Components.getNavigationPanel(), BorderLayout.SOUTH);
    }

    public void resetLabels(ArrayList<JLabel> labels) {
        for (JLabel label : labels) {
            label.setBackground(null);
        }
    }

    public void setReceipant(String clickedName, JLabel followerLabel){
        resetLabels(followerLabels);
        followerLabel.setBackground(Color.WHITE);
        System.out.println("Clicked: " + clickedName);
        recipientUsername = clickedName;
        if (username.compareTo(recipientUsername) < 0) {
            path = "chats/" + username + recipientUsername + ".txt";
        } else if (username.compareTo(recipientUsername) > 0) {
            path = "chats/" + recipientUsername + username + ".txt";
        } else {
            System.out.println("Error. Try again.");
            return;
        }
        file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        // Here you can perform actions when a name is clicked
        readFile(path, messageTextArea);
    }

    public void sendMessage(String message) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true)); // Open the file in append mode
            writer.write(username + ": " + message); // Write the message to the file
            writer.newLine(); // Write a newline character to the file
            writer.close(); // Close the file
            readFile(path, messageTextArea); // Read the file to update the chat
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  ClientHandler getClientFromName(String name) {
        // Synchronize access to UserList when searching for a client
        synchronized (Server.UserList) {
            System.out.println(Server.UserList.size());
            for (ClientHandler client : Server.UserList) {
                if (client.getName().equals(name)) {
                    return client;
                }
            }
        }
        System.out.println("Client could not be found");
        return null;
    }


    public void readFile(String path, JTextArea messageTextArea) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            StringBuilder fileContent = new StringBuilder(); // Use StringBuilder to efficiently concatenate strings
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            reader.close();
            // Set the text of messageTextArea to the content read from the file
            messageTextArea.setText(fileContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}