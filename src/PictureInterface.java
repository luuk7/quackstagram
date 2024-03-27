import java.util.List;

public interface PictureInterface {

    void addComment(String Comment);
    void like();
    String getImagePath();
    String getCaption();
    int getLikesCount();
    List<String> getComments();
}
