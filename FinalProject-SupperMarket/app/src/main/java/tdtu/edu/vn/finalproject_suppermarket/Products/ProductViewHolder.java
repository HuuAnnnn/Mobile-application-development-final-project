package tdtu.edu.vn.finalproject_suppermarket.Products;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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
        SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();
                String LOGIN_ENDPOINT = "https://suppermarket-api.fly.dev/cart/add-to-cart";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", sharedPreferences.getString("username", ""));
                    jsonObject.put("product_id", displayProductId.getText().toString().replace("ID: ", "").trim());
                    jsonObject.put("quantity", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                // put your json here
                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                Request request = new Request.Builder()
                        .url(LOGIN_ENDPOINT)
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
                        ((Activity) itemView.getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject json = new JSONObject(responseData);
                                    boolean isAdded = json.getBoolean("status");
                                    String message = "Thêm sản phẩm thành công";
                                    if (!isAdded) {
                                        message = "Thêm sản phẩm không thành công";
                                    }

                                    Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    Log.d("onResponse", e.getMessage());
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}