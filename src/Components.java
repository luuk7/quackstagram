import javax.swing.*;

public class Components {
    public static JPanel navigationPanel;

    public Components(){
        initialize();
    }

    public static void initialize(){
        navigationPanel = NavigationPanel.initializeNavigationPanel();
    }
}
