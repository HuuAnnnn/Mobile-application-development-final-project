package tdtu.edu.vn.finalproject_suppermarket.ProductHistory;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tdtu.edu.vn.finalproject_suppermarket.R;

public class ProductHistoryViewHolder extends RecyclerView.ViewHolder {
    TextView tvReceiptID;
    TextView tvProductName;
    TextView tvProductId;
    TextView tvQuantity;
    TextView tvProductPrice;
    ImageView imvProductImage;
    public ProductHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        tvReceiptID = itemView.findViewById(R.id.tvReceiptID);
        tvProductName = itemView.findViewById(R.id.tvProductName);
        tvProductId = itemView.findViewById(R.id.tvProductID);
        tvQuantity = itemView.findViewById(R.id.tvQuantity);
        tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        imvProductImage = itemView.findViewById(R.id.productImageView);
    }
}
