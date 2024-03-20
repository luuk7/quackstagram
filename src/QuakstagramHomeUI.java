import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class QuakstagramHomeUI extends JFrame{
    private static final int IMAGE_WIDTH = FrameManager.FRAME_WIDTH - 100; // Width for the image posts
    private static final int IMAGE_HEIGHT = 150; // Height for the image posts
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel homePanel;
    private JPanel imageViewPanel;

    public QuakstagramHomeUI() {
        setTitle("Quackstagram Home");
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        homePanel = new JPanel(new BorderLayout());
        imageViewPanel = new JPanel(new BorderLayout());

        initializeUI();

        cardPanel.add(homePanel, "Home");
        cardPanel.add(imageViewPanel, "ImageView");

        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "Home"); // Start with the home view
        add(Components.getHeaderPanel("Quackstagram"), BorderLayout.NORTH);
        // Navigation Panel
        add(Components.getNavigationPanel(), BorderLayout.SOUTH);
    }

    private void initializeUI() {
        // Content Scroll Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical box layout
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Never allow horizontal scrolling
        String[][] sampleData = createSampleData();
        populateContentPanel(contentPanel, sampleData);
        add(scrollPane, BorderLayout.CENTER);

        // Set up the home panel
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        homePanel.add(scrollPane, BorderLayout.CENTER);

    }

    private void populateContentPanel(JPanel panel, String[][] sampleData) {

        for (String[] postData : sampleData) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setBackground(Color.WHITE); // Set the background color for the item panel
            itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            itemPanel.setAlignmentX(CENTER_ALIGNMENT);
            JLabel nameLabel = new JLabel(postData[0]);
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Crop the image to the fixed size
            JLabel imageLabel = new JLabel();
            imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to image label
            String imageID = new File(postData[3]).getName().split("\\.")[0];
            try {
                BufferedImage originalImage = ImageIO.read(new File(postData[3]));
                BufferedImage croppedImage = originalImage.getSubimage(0, 0, Math.min(originalImage.getWidth(), IMAGE_WIDTH), Math.min(originalImage.getHeight(), IMAGE_HEIGHT));
                ImageIcon imageIcon = new ImageIcon(croppedImage);
                imageLabel.setIcon(imageIcon);
            } catch (IOException ex) {
                // Handle exception: Image file not found or reading error
                imageLabel.setText("Image not found");
            }

            JLabel descriptionLabel = new JLabel(postData[1]);
            descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel likesLabel = new JLabel();
            likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            likesLabel.setText(postData[2]);

            itemPanel.add(nameLabel);
            itemPanel.add(imageLabel);
            itemPanel.add(descriptionLabel);
            itemPanel.add(likesLabel);
            itemPanel.add(getLikeButton(imageID, likesLabel));

            panel.add(itemPanel);

            // Make the image clickable
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    displayImage(postData); // Call a method to switch to the image view
                }
            });


            // Grey spacing panel
            JPanel spacingPanel = new JPanel();
            spacingPanel.setPreferredSize(new Dimension(FrameManager.FRAME_WIDTH-10, 5)); // Set the height for spacing
            spacingPanel.setBackground(new Color(230, 230, 230)); // Grey color for spacing
            panel.add(spacingPanel);
        }
    }

    private JButton getLikeButton(String imageID, JLabel likesLabel){
        JButton likeButton = new JButton("🐤");
        likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        likeButton.setBackground(Color.BLACK); // Set the background color for the like button
        likeButton.setOpaque(true);
        likeButton.setBorderPainted(false); // Remove border
        likeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLikeAction(imageID, likesLabel);
            }
        });
        return likeButton;
    }

    private void handleLikeAction(String imageID, JLabel likesLabel) {
        ImageLikesSingleton imageLikesSingleton = ImageLikesSingleton.getInstance();

        try {
            imageLikesSingleton.likeImage(User.getCurrentUser().getUsername(), imageID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update the UI
        likesLabel.setText("🐤 " + imageLikesSingleton.getLikesCount(imageID));
    }


    private String[][] createSampleData() {
        ImageLikesSingleton imageLikesSingleton = ImageLikesSingleton.getInstance();

        String currentUser = "";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                currentUser = line.split(":")[0].trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String followedUsers = "";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "following.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(currentUser + ":")) {
                    followedUsers = line.split(":")[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Temporary structure to hold the data
        String[][] tempData = new String[100][]; // Assuming a maximum of 100 posts for simplicity
        int count = 0;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("img", "image_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null && count < tempData.length) {
                String[] details = line.split(", ");
                String imagePoster = details[1].split(": ")[1];
                if (followedUsers.contains(imagePoster)) {
                    String imagePath = "img/uploaded/" + details[0].split(": ")[1] + ".png"; // Assuming PNG format
                    String description = details[2].split(": ")[1];
                    System.out.println(details[0].split(": ")[1]);
                    String likes = "🐤 " + imageLikesSingleton.getLikesCount(details[0].split(": ")[1]);
                    tempData[count++] = new String[]{imagePoster, description, likes, imagePath};
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Transfer the data to the final array
        String[][] sampleData = new String[count][];
        System.arraycopy(tempData, 0, sampleData, 0, count);

        return sampleData;
    }

    private void displayImage(String[] postData) {
        imageViewPanel.removeAll(); // Clear previous content
        String imageID = new File(postData[3]).getName().split("\\.")[0];
        JLabel likesLabel = new JLabel(postData[2]); // Update this line

        // Display the image
        JLabel fullSizeImageLabel = new JLabel();
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
        try {
            BufferedImage originalImage = ImageIO.read(new File(postData[3]));
            BufferedImage croppedImage = originalImage.getSubimage(0, 0, Math.min(originalImage.getWidth(), FrameManager.FRAME_WIDTH-20), Math.min(originalImage.getHeight(), FrameManager.FRAME_HEIGHT-40));
            ImageIcon imageIcon = new ImageIcon(croppedImage);
            fullSizeImageLabel.setIcon(imageIcon);
        } catch (IOException ex) {
            // Handle exception: Image file not found or reading error
            fullSizeImageLabel.setText("Image not found");
        }

        // User Info 
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel,BoxLayout.Y_AXIS));
        JLabel userName = new JLabel(postData[0]);
        userName.setFont(new Font("Arial", Font.BOLD, 18));
        userPanel.add(userName);//User Name

        // Information panel at the bottom
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel(postData[1])); // Description
        infoPanel.add(new JLabel(postData[2])); // Likes
        infoPanel.add(getLikeButton(imageID, likesLabel));

        imageViewPanel.add(fullSizeImageLabel, BorderLayout.CENTER);
        imageViewPanel.add(infoPanel, BorderLayout.SOUTH);
        imageViewPanel.add(userPanel,BorderLayout.NORTH);

        imageViewPanel.revalidate();
        imageViewPanel.repaint();


        cardLayout.show(cardPanel, "ImageView"); // Switch to the image view
    }

    private void refreshDisplayImage(String[] postData, String imageID) {
        // Read updated likes count from image_details.txt
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("img", "image_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("imageID: " + imageID)) {
                    String likes = line.split(", ")[4].split(": ")[1];
                    postData[2] = "🐤 " + likes;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call displayImage with updated postData
        displayImage(postData);
    }
}
