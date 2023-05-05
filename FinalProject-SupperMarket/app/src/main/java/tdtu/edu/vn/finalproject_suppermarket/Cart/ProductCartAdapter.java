package tdtu.edu.vn.finalproject_suppermarket.Cart;

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

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<ProductCart> data;

    public ProductCartAdapter(Context context, ArrayList<ProductCart> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    public void updateData(ArrayList<ProductCart> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cartItem = inflater.inflate(R.layout.cart_item, parent, false);
        ProductCartViewHolder productCartViewHolder = new ProductCartViewHolder(cartItem);
        return productCartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCartViewHolder holder, int position) {
        ProductCart productCart = (ProductCart) data.get(position);
        holder.tvProductName.setText(productCart.getProductName());
        holder.tvProductId.setText(productCart.getProductName());
        String priceFormat = NumberFormat.getCurrencyInstance(new Locale("vn", "VN")).format(Integer.parseInt(productCart.getProductPrice()));
        holder.tvProductPrice.setText(priceFormat);
        holder.tvQuantity.setText(String.valueOf(productCart.getQuantity()));

        byte[] decodedString = Base64.decode(productCart.getProductImage(), Base64.DEFAULT);
        Bitmap imageProductCart = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imvProductImage.setImageBitmap(imageProductCart);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
