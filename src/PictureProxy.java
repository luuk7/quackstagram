import java.util.List;

// Proxy for Picture class
class PictureProxy extends Picture {
    private Picture picture;

    public PictureProxy(String imagePath, String caption) {
        super(imagePath, caption);
    }

    private Picture getRealPicture() {
        if (picture == null) {
            picture = new Picture(super.getImagePath(), super.getCaption());
        }
        return picture;
    }

    @Override
    public void addComment(String comment) {
        getRealPicture().addComment(comment);
    }

    @Override
    public void like() {
        getRealPicture().like();
    }

    @Override
    public String getImagePath() {
        return getRealPicture().getImagePath();
    }

    @Override
    public String getCaption() {
        return getRealPicture().getCaption();
    }

    @Override
    public int getLikesCount() {
        return getRealPicture().getLikesCount();
    }

    @Override
    public List<String> getComments() {
        return getRealPicture().getComments();
    }
}
