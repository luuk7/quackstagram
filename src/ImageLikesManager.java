import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ImageLikesManager {

    private static final String likesFilePath = "data/likes.txt";

    // Method to like an image
    public static void likeImage(String username, String imageID) throws IOException {
        Map<String, Set<String>> likesMap = readLikes();
        if (!likesMap.containsKey(imageID)) {
            likesMap.put(imageID, new HashSet<>());
        }
        Set<String> users = likesMap.get(imageID);
        if (!users.contains(username)) { // Only add and save if the user hasn't already liked the image
            Path detailsPath = Paths.get("img", "image_details.txt");
            String imageOwner = "";
            try (BufferedReader reader = Files.newBufferedReader(detailsPath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("ImageID: " + imageID)) {
                        String[] parts = line.split(", ");
                        imageOwner = parts[1].split(": ")[1];
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            users.add(username); 
            // Record the like in notifications.txt
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String notification = String.format("%s; %s; %s; %s\n", imageOwner, User.getCurrentUser().getUsername(), imageID, timestamp);
            try (BufferedWriter notificationWriter = Files.newBufferedWriter(Paths.get("data", "notifications.txt"), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                notificationWriter.write(notification);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            users.remove(username);
        }
        saveLikes(likesMap);
    }

    // Method to read likes from file
    private static Map<String, Set<String>> readLikes() throws IOException {
        Map<String, Set<String>> likesMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(likesFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String imageID = parts[0];
                Set<String> users = (parts.length > 1 && !parts[1].isEmpty())
                ? Arrays.stream(parts[1].split(",")).collect(Collectors.toSet())
                : new HashSet<>();
                likesMap.put(imageID, users);
            }
        }
        return likesMap;
    }

    public static int getLikesCount(String imageID){
        int likes = 0;
        try {
            Map<String, Set<String>> likesMap = readLikes();
            System.out.println("id: " + imageID);
            System.out.println(likesMap);
            System.out.println(likesMap.get(imageID));
            likes = likesMap.get(imageID).size();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            likes = 0;
        }
        return likes;
    }

    // Method to save likes to file
    private static void saveLikes(Map<String, Set<String>> likesMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(likesFilePath, false))) {
            for (Map.Entry<String, Set<String>> entry : likesMap.entrySet()) {
                String line = entry.getKey() + ":" + String.join(",", entry.getValue());
                writer.write(line);
                writer.newLine();
            }
        }
    }

}
