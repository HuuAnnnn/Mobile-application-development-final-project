package tdtu.edu.vn.finalproject_suppermarket.ProductHistory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import tdtu.edu.vn.finalproject_suppermarket.Cart.ProductCart;
import tdtu.edu.vn.finalproject_suppermarket.Products.ProductViewHolder;
import tdtu.edu.vn.finalproject_suppermarket.R;

public class ProductHistoryAdapter extends RecyclerView.Adapter<ProductHistoryViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<ProductHistory> data;

    public ProductHistoryAdapter(Context context, ArrayList<ProductHistory> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }
    public void updateData(ArrayList<ProductHistory> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productHistoryItem = inflater.inflate(R.layout.product_history_item, parent, false);
        ProductHistoryViewHolder productHistoryViewHolder = new ProductHistoryViewHolder(productHistoryItem);
        return productHistoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHistoryViewHolder holder, int position) {
        ProductHistory productHistory = (ProductHistory) data.get(position);
        holder.tvReceiptID.setText(productHistory.getReceiptID());
        holder.tvProductName.setText(productHistory.getProductName());
        holder.tvProductId.setText(productHistory.getProductId());
        String priceFormat = NumberFormat.getCurrencyInstance(new Locale("vn", "VN")).format(Integer.parseInt(productHistory.getProductPrice()));
        holder.tvProductPrice.setText(priceFormat);
        holder.tvQuantity.setText(String.valueOf(productHistory.getQuantity()));

        byte[] decodedString = Base64.decode(productHistory.getProductImage(), Base64.DEFAULT);
        Bitmap imageProductCart = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imvProductImage.setImageBitmap(imageProductCart);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
