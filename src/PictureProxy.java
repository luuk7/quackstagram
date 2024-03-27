import java.util.List;

// Proxy for Picture class
class PictureProxy implements PictureInterface {
    private Picture picture;
    private String imagePath;
    private String caption;
    private int likesCount;
    private List<String> comments;

    public PictureProxy(String imagePath, String caption) {
        this.imagePath = imagePath;
        this.caption = caption;
    }

    private Picture getRealPicture() {
        if (picture == null) {
            picture = new Picture(getImagePath(), getCaption());
        }
        return picture;
    }

    public void addComment(String comment) {
        getRealPicture().addComment(comment);
    }

    public void like() {
        getRealPicture().like();
    }

    public String getImagePath() {
        return getRealPicture().getImagePath();
    }

    public String getCaption() {
        return getRealPicture().getCaption();
    }

    public int getLikesCount() {
        return getRealPicture().getLikesCount();
    }

    public List<String> getComments() {
        return getRealPicture().getComments();
    }
}
