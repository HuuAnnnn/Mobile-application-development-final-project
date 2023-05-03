package tdtu.edu.vn.finalproject_suppermarket.Products;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayMainProduct#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayMainProduct extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String GET_PRODUCTS_ENDPOINTS = "https://suppermarket-api.fly.dev/product/products";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProductAdapter<Product> productAdapter;
    private RecyclerView displayAllProducts;
    private ProgressBar spinner;

    public DisplayMainProduct() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayMainProduct.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayMainProduct newInstance(String param1, String param2) {
        DisplayMainProduct fragment = new DisplayMainProduct();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_main_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayAllProducts = view.findViewById(R.id.displayProducts);
        spinner = view.findViewById(R.id.progressBar);
        spinner.bringToFront();
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            boolean isSuccess = false;
                            try {
                                isSuccess = json.getBoolean("status");
                                if (!isSuccess) {
                                    Toast.makeText(getContext(), "Không thể tải sản phẩm! Vui lòng kiểm tra lại kết nối", Toast.LENGTH_SHORT).show();
                                } else {
                                    ArrayList<Product> products = new ArrayList<Product>();
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
                                    productAdapter = new ProductAdapter<Product>(getActivity(), products);
                                    displayAllProducts.setAdapter(productAdapter);
                                    displayAllProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    spinner.setVisibility(View.INVISIBLE);
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