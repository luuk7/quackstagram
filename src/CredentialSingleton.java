import java.io.*;

public class CredentialSingleton {
    private static CredentialSingleton credentials;
    private final String credentialsFilePath = "data/credentials.txt";

    private CredentialSingleton(){}
    public static synchronized CredentialSingleton getInstance(){
        if (credentials == null){
            credentials = new CredentialSingleton();
        }
        return credentials;
    }

    public void add(String name, String password, String bio){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/credentials.txt", true))) {
            writer.write(name + ":" + password + ":" + bio);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean existsUsername(String username){
        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username + ":")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
