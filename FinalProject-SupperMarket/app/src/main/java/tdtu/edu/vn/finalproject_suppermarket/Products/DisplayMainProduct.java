package tdtu.edu.vn.finalproject_suppermarket.Products;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tdtu.edu.vn.finalproject_suppermarket.Cart.ShoppingCart;
import tdtu.edu.vn.finalproject_suppermarket.ChooseAddressActivity;
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
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProductAdapter<Product> productAdapter;
    private ArrayList<Product> productArrayList = new ArrayList<Product>();
    private RecyclerView displayAllProducts;
    private ProgressBar spinner;
    private EditText inputSearch;
    private ImageButton btnShoppingCart;
    private TextView updateDeliveryAddress;

    private TextView tvAddressDelivery;
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
        productArrayList.add(new Product("1", "1", "1", "1", "1", 1, "1"));
        productAdapter = new ProductAdapter<>(getContext(), productArrayList);
        btnShoppingCart = view.findViewById(R.id.btnShoppingCart);
        inputSearch = view.findViewById(R.id.inputSearch);
        updateDeliveryAddress = view.findViewById(R.id.updateDeliveryAddress);

        tvAddressDelivery = view.findViewById(R.id.tvAddressDelivery);
        displayAddress();

        updateDeliveryAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChooseAddressActivity.class);
                startActivity(intent);
            }
        });
        loadProducts();
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = inputSearch.getText().toString().trim();
                if (!keyword.equals("")) {
                    searchProducts(keyword);
                }
            }
        });

        btnShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShoppingCart.class);
                getContext().startActivity(intent);
            }
        });
    }

    public void displayAddress() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        OkHttpClient client = new OkHttpClient();
        String GET_INFOR = "https://suppermarket-api.fly.dev/user/information";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username.trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        // put your json here
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(GET_INFOR)
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
                try {
                    String responseData = response.body().string();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(responseData);
                                JSONObject jsonArray = jsonObject.getJSONObject("data");
                                String city = jsonArray.getString("city");
                                String district = jsonArray.getString("district");
                                String ward = jsonArray.getString("ward");
                                String address = jsonArray.getString("address");
                                String fullAddress = "Địa chỉ giao hàng: "+address+", "+ward+", "+district+", "+city;
                                tvAddressDelivery.setText(fullAddress);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    });
                } catch (Exception e) {

                }
            }
        });
    }
    public void searchProducts(String keyword) {
        OkHttpClient client = new OkHttpClient();
        String findProductString = "https://suppermarket-api.fly.dev/product/search?keyword=" + keyword.replace(" ", "%20");
        Toast.makeText(getContext(), findProductString, Toast.LENGTH_SHORT).show();

        Request request = new Request.Builder().url(findProductString).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("onFailure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response)
                    throws IOException {
                String responseData = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            productArrayList = new ArrayList<Product>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                jsonObject = data.getJSONObject(i);
                                Product product = new Product(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("origin"),
                                        jsonObject.getString("description"),
                                        jsonObject.getString("category"),
                                        jsonObject.getInt("price"),
                                        jsonObject.getString("image")
                                );
                                productArrayList.add(product);
                            }
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    productAdapter.updateData(productArrayList, 0);

                                }
                            });
                            spinner.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            Log.d("onResponse", e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
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
                                    productArrayList = new ArrayList<Product>();
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

                                        productArrayList.add(product);
                                    }
                                    productAdapter = new ProductAdapter<Product>(getActivity(), productArrayList);
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