import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static ServerSocket server;
    public static List<ClientHandler> UserList = new ArrayList<ClientHandler>();
    public static void createServer() throws IOException {
        try {
            server = new ServerSocket(8080);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Started, waiting for clients...");
        while (true){
            Socket clientSocket = server.accept();
            System.out.println("Client found");
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            Thread clientHandlerThread = new Thread(clientHandler); // Create a new thread for the client handler
            clientHandlerThread.start(); // Start the thread
            UserList.add(clientHandler); // Add the client handler to the user list
        }
    }


    public static void SaveToChat(Text textObject, String message) throws IOException {


        File file = null;
        if (textObject.getSenderName().compareTo(textObject.getReceiverName())<0){file = new File(textObject.getSenderName()+textObject.getReceiverName());}
        else if (textObject.getSenderName().compareTo(textObject.getReceiverName())>0) { file = new File(textObject.getReceiverName()+textObject.getSenderName());}
        else {System.out.println("Error. Try again.");return;}
        FileWriter writer = new FileWriter(file, true);
        writer.write(textObject.getDatetime() + " " + textObject.getSenderName() + ": " + message + "\n");
        writer.close();
    }
}
