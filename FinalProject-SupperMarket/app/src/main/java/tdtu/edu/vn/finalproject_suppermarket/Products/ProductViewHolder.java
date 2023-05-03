package tdtu.edu.vn.finalproject_suppermarket.Products;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tdtu.edu.vn.finalproject_suppermarket.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    TextView displayProductName;
    TextView displayProductId;
    TextView displayProductPrice;
    TextView displayProductOrigin;
    TextView displayProductDescription;
    ImageView displayProductImage;
    Button btnAddToCart;
    ImageButton btnLikeProduct;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        displayProductName = itemView.findViewById(R.id.displayProductName);
        displayProductId = itemView.findViewById(R.id.displayProductId);
        displayProductPrice = itemView.findViewById(R.id.displayProductPrice);
        displayProductOrigin = itemView.findViewById(R.id.displayProductOrigin);
        displayProductDescription = itemView.findViewById(R.id.displayProductDescription);
        displayProductImage = itemView.findViewById(R.id.displayProductImage);
        btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        btnLikeProduct = itemView.findViewById(R.id.btnLikeProduct);
    }
}