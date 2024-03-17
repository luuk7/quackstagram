import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class ChatUI extends JFrame {

    String username = User.getCurrentUser().getUsername();
    static String recipientUsername;
    File file;
    static String path;
    static Client client;
    JTextArea fileContentTextArea; // Declare fileContentTextArea as a class-level variable

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

        JTextArea messageTextArea = new JTextArea();
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
                        followerLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                // Handle click event
                                String clickedName = followerLabel.getText();
                                System.out.println("Clicked: " + clickedName);
                                recipientUsername = clickedName;
                                if (username.compareTo(recipientUsername) < 0) {
                                    path = "chats/" + username + recipientUsername;
                                } else if (username.compareTo(recipientUsername) > 0) {
                                    path = "chats/" + recipientUsername + username;
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
                        });
                        namesPanel.add(followerLabel);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        JTextField inputField = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText();
                try {
                    sendMessage(message);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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

        // Add content panel to the frame
        add(contentPanel, BorderLayout.CENTER);
        add(Components.getNavigationPanel(), BorderLayout.SOUTH);
    }


<<<<<<< HEAD
    public void sendMessage(String message) throws IOException {
        ClientHandler sender = Server.getClientFromName(username);
        ClientHandler receiver = Server.getClientFromName(recipientUsername);
        Text text = new Text(username,recipientUsername, message, sender,receiver);
        text.sendMessage(text);
=======
                Client.createClient(username);
                ClientHandler clientHandler = new ClientHandler(client.clientSocket, username);
                Server.UserList.add(clientHandler);

            } catch (IOException e) {
                // If connection failed, attempt to start the server locally
                try {
                    Server.createServer(username);
                    // Wait a bit for the server to start before attempting to connect again
                    Thread.sleep(1000);
                    client = new Client();
                    Client.createClient(username);

                } catch (IOException | InterruptedException ex) {
                    // Handle exceptions
                    ex.printStackTrace();
                }
            }
        });
        clientInitThread.start();
    }

    private void sendMessage(String message, JTextArea messageTextArea) {
        ClientHandler userClient = findClient(username);
        ClientHandler receiverClient = findClient(recipientUsername);
        System.out.println(userClient);
        System.out.println(receiverClient);
        if (userClient == null || receiverClient == null) {
            System.out.println("Client not found.");
            return;
        }

        try {
            Text text = new Text(username, recipientUsername, message, userClient, receiverClient);
            Text.sendMessage(text);
            // Assuming you want to display the sent message in the messageTextArea
            messageTextArea.append(username + ": " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to send message.");
        }
>>>>>>> ba05ee56108eaa14e32db50fed23794936b19eb5
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
            // Set the text of fileContentTextArea to the content read from the file
            messageTextArea.setText(fileContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
