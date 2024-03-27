import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

interface AuthenticationStrategy {
    boolean authenticate(String username, String password);
}

class FileBasedAuthentication implements AuthenticationStrategy {
    @Override
    public boolean authenticate(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/credentials.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(":");
                if (credentials[0].equals(username) && credentials[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}