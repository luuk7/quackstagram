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
                    saveUserInformation(username, password, credentials[2]);
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveUserInformation(String username, String password, String bio) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/users.txt", false))) {
            writer.write(username + ":" + bio + ":" + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}