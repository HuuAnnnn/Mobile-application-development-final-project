package tdtu.edu.vn.finalproject_suppermarket.Products;

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

import tdtu.edu.vn.finalproject_suppermarket.R;

public class ProductAdapter<E> extends RecyclerView.Adapter<ProductViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<E> data;

    public ProductAdapter(Context context, ArrayList<E> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    public void updateData(ArrayList<E> data, int flag) {
        if (flag == 0) { //append
            for (int i = 0; i < data.size(); i++) {
                this.data.add(data.get(i));
                notifyItemInserted(getItemCount());
                notifyDataSetChanged();
            }
        } else { //clear all
            this.data.clear();
            notifyDataSetChanged();
        }
    }

    public void updateData(ArrayList<E> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productCard = inflater.inflate(R.layout.product_component, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(productCard);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = (Product) data.get(position);
        holder.displayProductId.setText("ID: " + product.getId());
        holder.displayProductName.setText(product.getName());
        holder.displayProductOrigin.setText("Nguồn gốc: " + product.getOrigin());
        holder.displayProductDescription.setText(product.getDescription());
        String priceFormat = NumberFormat.getCurrencyInstance(new Locale("vn", "VN")).format(product.getPrice());
        holder.displayProductPrice.setText("Giá: " + priceFormat);

        byte[] decodedString = Base64.decode(product.getImage(), Base64.DEFAULT);
        Bitmap imageProduct = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.displayProductImage.setImageBitmap(imageProduct);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}