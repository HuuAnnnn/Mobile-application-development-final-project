package tdtu.edu.vn.finalproject_suppermarket.ProductHistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tdtu.edu.vn.finalproject_suppermarket.Cart.ProductCart;
import tdtu.edu.vn.finalproject_suppermarket.ChangePassword;
import tdtu.edu.vn.finalproject_suppermarket.Products.Product;
import tdtu.edu.vn.finalproject_suppermarket.Products.ProductAdapter;
import tdtu.edu.vn.finalproject_suppermarket.R;

public class ProductHistoryActivity extends AppCompatActivity {

    private ProductHistoryAdapter productHistoryAdapter;
    private ArrayList<ProductHistory> productArrayList;
    private RecyclerView displayAllProductsHistory;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_history);
        displayAllProductsHistory = findViewById(R.id.historyRecyclerView);
        loadProductsHistory();
    }

    public void loadProductsHistory() {
        SharedPreferences sharedPreferences = ProductHistoryActivity.this.getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        OkHttpClient client = new OkHttpClient();
        String LOGIN_ENDPOINT = "https://suppermarket-api.fly.dev/cart/history";
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
                JSONObject json = null;
                Boolean status = false;
                try {
                    json = new JSONObject(responseData);
                    status = json.getBoolean("status");
                    if (status) {
                        ArrayList<ProductHistory> productHistories = new ArrayList<ProductHistory>();
                        JSONArray history = json.getJSONArray("history");
                        for (int i = 0; i < history.length(); i++) {
                            JSONObject historyDetail = history.getJSONObject(i);
                            JSONObject product = historyDetail.getJSONObject("product");
                            productHistories.add(new ProductHistory(historyDetail.getString("receipt_id"),
                                    product.getString("id"),
                                    product.getString("name"),
                                    historyDetail.getString("quantity"),
                                    historyDetail.getString("price"),
                                    product.getString("image")));
                        }
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                productHistoryAdapter = new ProductHistoryAdapter(ProductHistoryActivity.this, productHistories);
                                displayAllProductsHistory.setAdapter(productHistoryAdapter);
                                displayAllProductsHistory.setLayoutManager(new LinearLayoutManager(ProductHistoryActivity.this));

                            }
                        });

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}