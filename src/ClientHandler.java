import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private Socket socket;
    private String name;
    private BufferedReader reader;
    private PrintWriter writer;
    private Scanner scanner;

    public static boolean acceptUserFlagafterLogin = false;

    public ClientHandler(Socket clientSocket, String name) throws IOException {
        this.socket = clientSocket;
        this.name = name;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.scanner = new Scanner(System.in);
    }
    public ClientHandler(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
        this.name = name;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.scanner = new Scanner(System.in);
    }
    @Override
    public void run() {
        try {


            String name = JOptionPane.showInputDialog("What do you want to be known as?");
            JOptionPane.showMessageDialog(null,"You will be known as: " + name);
            synchronized (Server.UserList) {
                for (ClientHandler client : Server.UserList) {
                    if (client.getName() == null) {
                        client.name = name;
                    }
                }
            }



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
    public void setWriter(PrintWriter writer){
        this.writer = writer;
    }
    public void setReader(BufferedReader reader){this.reader = reader;}
    public void getScanner(Scanner scanner){this.scanner = scanner;}
    public String toString(){
        return "Name:" + this.getName() + "Writer: " + this.getWriter() + "Reader:" + this.getReader() + "Socket:" + this.socket;
    }
}