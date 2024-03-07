import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;

public class HeaderPanel {
    public static JPanel headerPanel;
    private static JLabel label;
    private static final int WIDTH = 300;

    public HeaderPanel(){
    }

    public static JPanel initializeHeaderPanel(String text){
        // Header with the Register label
        headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 0, 51)); // Set a darker background for the header
        label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE); // Set the text color to white
        headerPanel.add(label);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
        return headerPanel;
    }
}
