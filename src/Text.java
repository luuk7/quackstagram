import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text {

    private final String sender, receiver,text,datetime;
    private ClientHandler senderClient, receiverClient;


    public final static SimpleDateFormat timeformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


    public Text(String sender, String receiver, String text, ClientHandler senderClient, ClientHandler receiverClient){
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.senderClient = senderClient;
        this.receiverClient = receiverClient;
        this.datetime = timeformat.format(new Date());

    }


    public Text(String sender, String text, ClientHandler senderClient) throws FileNotFoundException {
        this.sender = sender;
        this.text = text;
        this.senderClient = senderClient;
        this.datetime = timeformat.format(new Date());
        this.receiver = DecodeReceiver(this.text);
        this.receiverClient = CheckIfOnline(getReceiverName());
    }

    public String DecodeReceiver(String text) throws FileNotFoundException {
        Pattern pattern = Pattern.compile("@\\w+");
        Matcher matcher = pattern.matcher(text);
        String comparedname;


        if (matcher.find()){
            String temp = matcher.group();
            temp = temp.substring(1);


            File names = new File("names");
            Scanner nameScanner = new Scanner(names);

            while (nameScanner.hasNextLine()){
                comparedname = nameScanner.nextLine();
                if (comparedname.equals(temp)){
                    nameScanner.close();
                    return temp;
                }
            }
            nameScanner.close();
        }
        return null;
    }

    public ClientHandler CheckIfOnline(String name){
        for (ClientHandler client: Server.UserList){
            if (client.getName().equals(name)){
                return client;
            }
        }
        return null;
    }


    public static void sendMessage(Text textObject) throws IOException {
        String msg = textObject.getText();
        String msgPruned = msg.replace("@" + textObject.getReceiverName(), "");
        if (textObject.getReceiverClient() != null && textObject.getReceiverClient().getWriter() != null) {
            textObject.getReceiverClient().getWriter().println(textObject.getSenderName() + ": " + msgPruned);
            Server.SaveToChat(textObject, msgPruned);
        } else {
            // Handle the case where the receiver is not online or receiver's writer is not available
            System.out.println("Receiver is not online.");
        }
    }


    public String getReceiverName() {
        return receiver;
    }

    public String getText() {
        return text;
    }

    public String getSenderName() {
        return sender;
    }

    public ClientHandler getReceiverClient() {
        return receiverClient;
    }

    public ClientHandler getSenderClient() {
        return senderClient;
    }

    public String getDatetime() {return datetime;}
}
