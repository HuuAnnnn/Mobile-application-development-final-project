package tdtu.edu.vn.finalproject_suppermarket.Cart;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tdtu.edu.vn.finalproject_suppermarket.R;

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<ProductCart> data;
    private Context context;
    private OnDataChangeListener onDataChangedListener;

    public ProductCartAdapter(Context context, ArrayList<ProductCart> data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setOnDataChangedListener(OnDataChangeListener listener) {
        onDataChangedListener = listener;
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
        holder.tvProductId.setText(productCart.getProductId());
        String priceFormat = NumberFormat.getCurrencyInstance(new Locale("vn", "VN")).format(Integer.parseInt(productCart.getProductPrice()));
        holder.tvProductPrice.setText(priceFormat);
        holder.tvQuantity.setText(String.valueOf(productCart.getQuantity()));

        byte[] decodedString = Base64.decode(productCart.getProductImage(), Base64.DEFAULT);
        Bitmap imageProductCart = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imvProductImage.setImageBitmap(imageProductCart);

        holder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
                if (quantity > 0) {
                    holder.tvSub.setEnabled(true);
                }

                holder.tvQuantity.setText(String.valueOf(quantity + 1));
                updateQuantity(holder.tvProductId.getText().toString().replace("ID: ", "").trim(), quantity + 1);
            }
        });

        holder.tvSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
                if (quantity == 0) {
                    holder.tvSub.setEnabled(false);
                } else {
                    holder.tvQuantity.setText(String.valueOf(quantity - 1));
                    updateQuantity(holder.tvProductId.getText().toString().replace("ID: ", "").trim(), quantity - 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateQuantity(String productId, int quantity) {
        final String GET_CART_ENDPOINTS = "https://suppermarket-api.fly.dev/cart/update-quantity";
        SharedPreferences sharedPreferences = context.getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("product_id", productId);
            jsonObject.put("quantity", quantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        // put your json here
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(GET_CART_ENDPOINTS)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("onFailure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String responseData = response.body().string();
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("TEST", responseData);
                            JSONObject json = new JSONObject(responseData);
                            boolean isSuccess = json.getBoolean("state");
                            if (isSuccess) {
                                loadCart();
                            }
                        } catch (JSONException e) {
                            Log.d("onResponse", e.getMessage());
                        }
                    }
                });
            }
        });
    }

    public void loadCart() {
        if (onDataChangedListener != null) {
            onDataChangedListener.startChange(true); // notify listener of data change
        }

        final String GET_CART_ENDPOINTS = "https://suppermarket-api.fly.dev/cart/own-cart";
        SharedPreferences sharedPreferences = context.getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        // put your json here
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(GET_CART_ENDPOINTS)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("onFailure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response)
                    throws IOException {
                String responseData = response.body().string();
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("TEST", responseData);
                            JSONObject json = new JSONObject(responseData);
                            boolean isNotEmpty = json.getBoolean("status");
                            Log.d("TEST", isNotEmpty + "");
                            String totalFormat = NumberFormat.getCurrencyInstance(new Locale("vn", "VN")).format(json.getInt("total"));
                            Log.d("TEST", onDataChangedListener + "");
                            if (onDataChangedListener != null) {
                                onDataChangedListener.startChange(false);
                                onDataChangedListener.endChange(true);
                                onDataChangedListener.onDataChanged(totalFormat); // notify listener of data change
                            }

                            if (isNotEmpty) {
                                ArrayList<ProductCart> productCartList = new ArrayList<ProductCart>();
                                JSONArray cart = json.getJSONArray("cart");
                                for (int i = 0; i < cart.length(); i++) {
                                    JSONObject cartDetail = cart.getJSONObject(i);
                                    JSONObject product = cartDetail.getJSONObject("product");
                                    productCartList.add(new ProductCart(username,
                                            product.getString("id"),
                                            product.getString("name"),
                                            cartDetail.getString("quantity"),
                                            product.getString("price"),
                                            product.getString("image")));
                                }
                                updateData(productCartList);
                            }
                        } catch (JSONException e) {
                            Log.d("onResponse", e.getMessage());
                        }
                    }
                });
            }
        });
    }
}
