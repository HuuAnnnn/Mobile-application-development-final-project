package tdtu.edu.vn.finalproject_suppermarket.Promotion;

public class Promotion {
    private String title;
    private String code;
    private String dateExpired;

    public Promotion(String title, String code, String dateExpired) {
        this.title = title;
        this.code = code;
        this.dateExpired = dateExpired;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDateExpired() {
        return dateExpired;
    }

    public void setDateExpired(String dateExpired) {
        this.dateExpired = dateExpired;
    }
}
