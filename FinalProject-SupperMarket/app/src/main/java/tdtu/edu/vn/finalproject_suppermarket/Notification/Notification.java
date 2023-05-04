package tdtu.edu.vn.finalproject_suppermarket.Notification;

public class Notification {
    private String id;
    private String title;
    private String dateCreate;
    private String content;
    private String image;

    public Notification(String id, String title, String dateCreate, String content, String image) {
        this.id = id;
        this.title = title;
        this.dateCreate = dateCreate;
        this.content = content;
        this.image = image;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
