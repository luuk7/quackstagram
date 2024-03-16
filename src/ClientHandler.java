import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private Socket socket;
    private String name;
    private BufferedReader reader;
    private PrintWriter writer;
    private Scanner scanner;
    public static String currentName = null;
    public static boolean acceptUserFlagafterLogin = false;

    public ClientHandler(Socket clientSocket){
        this.socket = clientSocket;
    }
    @Override
    public void run() {
        try {

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);//write to server
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//read from server
            Scanner scanner = new Scanner(System.in);//read from user

            this.writer = writer;
            this.reader = reader;
            this.scanner = scanner;



            acceptUserFlagafterLogin = false;
            this.name = currentName;
            Server.UserList.add(this);
            currentName = null;

            String msg;


            while ((msg = reader.readLine())!= null){
                writer.println("You" + ": " + msg);
                Text text = new Text(this.getName(),msg, this);
                if (text.getReceiverClient() == null){
                    JOptionPane.showMessageDialog(null, "User is not online, try again some other time");
                }else {
                    text.sendMessage(text);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    public Scanner getScanner() {
        return scanner;
    }

    public PrintWriter getWriter() {
        return writer;
    }
    public BufferedReader getReader(){
        return reader;
    }

    public String getName() {
        return name;
    }
}
