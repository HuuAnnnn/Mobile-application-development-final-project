package tdtu.edu.vn.finalproject_suppermarket.ProductHistory;

public class ProductHistory {
    public ProductHistory(String receiptID, String productId, String productName, String quantity, String productPrice, String productImage) {
        this.receiptID = receiptID;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.productImage = productImage;
    }

    public String getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(String receiptID) {
        this.receiptID = receiptID;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    private String receiptID;
    private String productId;
    private String productName;
    private String quantity;
    private String productPrice;
    private String productImage;
}
