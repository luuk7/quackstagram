import javax.swing.*;

public class Components {
    private static JPanel navigationPanel;
    private static JPanel headerPanel;

    public Components(){
        initialize();
    }

    public static void initialize(){
        navigationPanel = NavigationPanel.initializeNavigationPanel();
        headerPanel = HeaderPanel.initializeHeaderPanel("");
    }

    public static JPanel getNavigationPanel(){
        return navigationPanel;
    }

    public static JPanel getHeaderPanel(String text){
        if(text == null || text.isEmpty()) return headerPanel;
        return HeaderPanel.initializeHeaderPanel(text);
    }
}
