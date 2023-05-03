package tdtu.edu.vn.finalproject_suppermarket.Products;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tdtu.edu.vn.finalproject_suppermarket.R;

public class MainProduct extends AppCompatActivity {
    private static final String GET_PRODUCTS_ENDPOINTS = "https://suppermarket-api.fly.dev/product/products";
    private ArrayList<Product> products;
    private ProductAdapter<Product> productAdapter;
    private RecyclerView displayAllProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_display_products);
        displayAllProducts = findViewById(R.id.displayProducts);
        products = new ArrayList<Product>();
        productAdapter = new ProductAdapter<Product>(this, products);
        displayAllProducts.setAdapter(productAdapter);
        displayAllProducts.setLayoutManager(new LinearLayoutManager(this));
        loadProducts();
    }

    public void loadProducts() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(GET_PRODUCTS_ENDPOINTS).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("onFailure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response)
                    throws IOException {
                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            boolean isSuccess = false;
                            try {
                                isSuccess = json.getBoolean("status");
                                if (!isSuccess) {
                                    Toast.makeText(MainProduct.this, "Không thể tải sản phẩm! Vui lòng kiểm tra lại kết nối", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray data = json.getJSONArray("data");
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject jsonObject = data.getJSONObject(i);
                                        Product product = new Product(
                                                jsonObject.getString("id"),
                                                jsonObject.getString("name"),
                                                jsonObject.getString("origin"),
                                                jsonObject.getString("description"),
                                                jsonObject.getString("category"),
                                                jsonObject.getInt("price"),
                                                jsonObject.getString("image")
                                        );
                                        products.add(product);
                                    }

                                    productAdapter.updateData(products);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    Log.d("onResponse", e.getMessage());
                }
            }
        });
    }
}