package tdtu.edu.vn.finalproject_suppermarket.Products;

public class Product {
    private String id;
    private String name;
    private String origin;
    private String description;
    private String category;
    private int price;
    private String image;

    public Product(String id, String name, String origin, String description, String category, int price, String image) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.description = description;
        this.category = category;
        this.price = price;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
