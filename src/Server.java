import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static boolean serverExists = false;

    // Using ArrayList for simplicity, with synchronized access
    public static List<ClientHandler> UserList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8080);
        System.out.println("Started, waiting for clients...");
        serverExists = true;

        while (true) {
            try {
                Socket clientSocket = server.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Create a ClientHandler for the connected client
                ClientHandler handler = new ClientHandler(clientSocket);
                Thread clientHandlerThread = new Thread(handler);
                clientHandlerThread.start();

                // Add the ClientHandler to UserList
                synchronized (UserList) {
                    UserList.add(handler);
                    printUserList();
                }

            } catch (IOException e) {
                e.printStackTrace();
                // Handle exceptions, possibly close server or log the error
            }
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
    public static void printUserList(){
        synchronized (UserList) {
            for (ClientHandler client : UserList) {
                System.out.println(client.toString());
            }
        }
    }



}