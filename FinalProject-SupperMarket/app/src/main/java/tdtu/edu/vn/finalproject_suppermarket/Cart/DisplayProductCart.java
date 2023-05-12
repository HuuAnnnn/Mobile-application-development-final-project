package tdtu.edu.vn.finalproject_suppermarket.Cart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayProductCart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayProductCart extends Fragment implements OnDataChangeListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView displayProductCart;
    private ProductCartAdapter productCartAdapter;
    private TextView tvTotalPrice;
    private ImageButton btnCartBack;
    private ProgressBar spinner;
    private Button placeOrderButton;

    public DisplayProductCart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayProductCart.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayProductCart newInstance(String param1, String param2) {
        DisplayProductCart fragment = new DisplayProductCart();
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
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayProductCart = view.findViewById(R.id.cartRecyclerView);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        btnCartBack = view.findViewById(R.id.btnCartBack);
        spinner = view.findViewById(R.id.cartProgressBar);
        spinner.bringToFront();

        placeOrderButton = view.findViewById(R.id.placeOrderButton);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkout();
            }
        });

        productCartAdapter = new ProductCartAdapter(getContext(), new ArrayList<ProductCart>());
        productCartAdapter.setOnDataChangedListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        displayProductCart.setLayoutManager(layoutManager);
        displayProductCart.setHasFixedSize(true);
        displayProductCart.setAdapter(productCartAdapter);
        btnCartBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    public void checkout() {
        spinner.setVisibility(View.VISIBLE);
        final String GET_CART_ENDPOINTS = "https://suppermarket-api.fly.dev/cart/checkout";
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            spinner.setVisibility(View.INVISIBLE);
                            Log.d("TEST", responseData);
                            JSONObject json = new JSONObject(responseData);
                            boolean isSuccess = json.getBoolean("status");
                            Intent intent;
                            if (isSuccess) {
                                intent = new Intent(getActivity(), CheckoutSuccess.class);
                            } else {
                                intent = new Intent(getActivity(), CheckoutFail.class);
                            }
                            startActivity(intent);
                        } catch (JSONException e) {
                            Log.d("onResponse", e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        final String GET_CART_ENDPOINTS = "https://suppermarket-api.fly.dev/cart/own-cart";
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
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
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            spinner.setVisibility(View.INVISIBLE);
                            Log.d("TEST", responseData);
                            JSONObject json = new JSONObject(responseData);
                            boolean isNotEmpty = json.getBoolean("status");
                            Log.d("TEST", isNotEmpty + "");
                            String totalFormat = NumberFormat.getCurrencyInstance(new Locale("vn", "VN")).format(json.getInt("total"));
                            tvTotalPrice.setText(totalFormat);
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

                                productCartAdapter.updateData(productCartList);
                            } else {
                                Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("onResponse", e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDataChanged(String data) {
        tvTotalPrice.setText(data);
    }

    @Override
    public void startChange(boolean state) {
        if (state) {
            spinner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void endChange(boolean state) {
        if (state) {
            spinner.setVisibility(View.INVISIBLE);
        }
    }
}