import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Represents a user on Quackstagram
class User {
    private String username;
    private String bio;
    private String password;
    private int postsCount;
    private int followersCount;
    private int followingCount;
    private List<PictureProxy> pictures;

    public User(String username, String bio, String password) {
        this.username = username;
        this.bio = bio;
        this.password = password;
        this.pictures = new ArrayList<>();
        // Initialize counts to 0
        this.postsCount = 0;
        this.followersCount = 0;
        this.followingCount = 0;
    }

    public User(String username) {
        this.username = username;
    }

    public static User getCurrentUser(){
        // Open InstagramProfileUI frame
        String loggedInUsername = "";
        // Read the logged-in user's username from users.txt
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                loggedInUsername = line.split(":")[0].trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = new User(loggedInUsername);
        return user;
    }

    // Add a picture to the user's profile
    public void addPicture(PictureProxy picture) {
        pictures.add(picture);
        postsCount++;
    }

    // Getter methods for user details
    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    // Setter methods for followers and following counts
    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public List<PictureProxy> getPictures() {
        return pictures;
    }

    public void setPostCount(int postCount) {
        this.postsCount = postCount;
    }

    // Implement the toString method for saving user information
    @Override
    public String toString() {
        return username + ":" + bio + ":" + password; // Format as needed
    }

}