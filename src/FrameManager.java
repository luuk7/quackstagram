import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.*;

public class FrameManager{
    public static final int FRAME_WIDTH = 300;
    public static final int FRAME_HEIGHT = 500;
    public static JFrame currentFrame;

    public static void openFrame(String type){
        JFrame newFrame = getFrame(type);
        if(currentFrame != null){
            currentFrame.dispose();
        }
        newFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        newFrame.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setVisible(true);
        currentFrame = newFrame;
        System.out.println("Opened frame: " + type);
    }

    public static JFrame getFrame(String type){
        switch (type) {
            case "SIGN_IN":
                return new SignInUI();
            case "SIGN_UP":
                return new SignUpUI();
            case "PROFILE":
                String loggedInUsername = "";
                // Read the logged-in user's username from users.txt
                try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
                    String line = reader.readLine();
                    if (line != null) {
                        loggedInUsername = line.split(":")[0].trim();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                User user = new User(loggedInUsername);
                return new InstagramProfileUI(user);
            case "IMAGE_UPLOAD":
                return new ImageUploadUI();
            case "NOTIFICATIONS":
                return new NotificationsUI();
            case "EXPLORE":
                return new ExploreUI();
            case "HOME":
                return new QuakstagramHomeUI();
            default:
                System.out.println("Invalid frame type");
                return null;
        }
    }
}
