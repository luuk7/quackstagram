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

    public static void openFrame(String type, User user){
        JFrame newFrame = getFrame(type, user);
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

    public static JFrame getFrame(String type, User user){
        switch (type) {
            case "SIGN_IN":
                FileBasedAuthentication fileBasedAuthentication = new FileBasedAuthentication();
                return new SignInUI(fileBasedAuthentication);
            case "SIGN_UP":
                return new SignUpUI();
            case "PROFILE":
                return new InstagramProfileUI(user);
            case "IMAGE_UPLOAD":
                return new ImageUploadUI();
            case "NOTIFICATIONS":
                return new NotificationsUI();
            case "EXPLORE":
                return new ExploreUI();
            case "HOME":
                return new QuakstagramHomeUI();
            case "CHAT":
                return new ChatUI();
            default:
                System.out.println("Invalid frame type");
                return null;
        }
    }
}
