import java.awt.*;
import javax.swing.*;

public class NavigationPanel {
    
    private static final int NAV_ICON_SIZE = 20; // Corrected static size for bottom icons
    public static JPanel navigationPanel;

    public NavigationPanel(){

    }

    public static JPanel initializeNavigationPanel(){
        // Navigation Bar
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(new Color(249, 249, 249));
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        navigationPanel.add(createIconButton("img/icons/home.png", "home"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton("img/icons/search.png","explore"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton("img/icons/add.png","add"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton("img/icons/heart.png","notification"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton("img/icons/profile.png", "profile"));
        return navigationPanel;
    }

    private static JButton createIconButton(String iconPath, String buttonType) {
        ImageIcon iconOriginal = new ImageIcon(iconPath);
        Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(iconScaled));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);

        // Define actions based on button type
        if ("home".equals(buttonType)) {
            button.addActionListener(e -> openHomeUI());
        } else if ("profile".equals(buttonType)) {
            button.addActionListener(e -> openProfileUI());
        } else if ("notification".equals(buttonType)) {
            button.addActionListener(e -> notificationsUI());
        } else if ("explore".equals(buttonType)) {
            button.addActionListener(e -> exploreUI());
        } else if ("add".equals(buttonType)) {
            button.addActionListener(e -> ImageUploadUI());
        }
        return button;
    }

    private static void openProfileUI() {
        // Open InstagramProfileUI frame
        FrameManager.openFrame("PROFILE");
    }

    private static void ImageUploadUI() {
        // Open InstagramProfileUI frame
        FrameManager.openFrame("IMAGE_UPLOAD");
    }

    private static void notificationsUI() {
        FrameManager.openFrame("NOTIFICATIONS");
    }

    private static void openHomeUI() {
        FrameManager.openFrame("HOME");
    }

    private static void exploreUI() {
        FrameManager.openFrame("EXPLORE");
    }
}
