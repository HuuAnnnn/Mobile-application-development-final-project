package tdtu.edu.vn.finalproject_suppermarket.Cart;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tdtu.edu.vn.finalproject_suppermarket.R;

public class ProductCartViewHolder extends RecyclerView.ViewHolder {
     TextView tvProductName;
     TextView tvProductId;
     TextView tvQuantity;
     TextView tvAdd;
     TextView tvSub;
     TextView tvProductPrice;
     ImageView imvProductImage;
    public ProductCartViewHolder(@NonNull View itemView) {
        super(itemView);
        tvProductName = itemView.findViewById(R.id.tvProductName);
        tvProductId = itemView.findViewById(R.id.tvProductID);
        tvQuantity = itemView.findViewById(R.id.tvQuantity);
        tvAdd = itemView.findViewById(R.id.tvAdd);
        tvSub = itemView.findViewById(R.id.tvSub);
        tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        imvProductImage = itemView.findViewById(R.id.productImageView);
    }
}
