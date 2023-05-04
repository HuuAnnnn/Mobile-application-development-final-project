package tdtu.edu.vn.finalproject_suppermarket;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRCode#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QRCode extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CodeScannerView scannerView;
    private CodeScanner codeScanner;
    private Button scannerAddToCart;
    private TextView productID;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QRCode() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QRCode.
     */
    // TODO: Rename and change types and number of parameters
    public static QRCode newInstance(String param1, String param2) {
        QRCode fragment = new QRCode();
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
        return inflater.inflate(R.layout.fragment_q_r_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scannerView = view.findViewById(R.id.scanner_view);
        checkPermissions();
        scannerAddToCart = view.findViewById(R.id.scannerAddToCart);
        productID = view.findViewById(R.id.productID);
        codeScanner = new CodeScanner(getContext(), scannerView);
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setScanMode(ScanMode.SINGLE);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFlashEnabled(false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
        scannerAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();
                String LOGIN_ENDPOINT = "https://suppermarket-api.fly.dev/cart/add-to-cart";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", sharedPreferences.getString("username", ""));
                    jsonObject.put("product_id", productID.getText().toString());
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject json = new JSONObject(responseData);
                                    boolean isAdded = json.getBoolean("status");
                                    String message = "Thêm sản phẩm thành công";
                                    if (!isAdded) {
                                        message = "Thêm sản phẩm không thành công";
                                    }

                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    productID.setText("");
                                    scannerAddToCart.setVisibility(View.INVISIBLE);
                                    codeScanner.startPreview();
                                } catch (JSONException e) {
                                    Log.d("onResponse", e.getMessage());
                                }
                            }
                        });
                    }
                });
            }
        });

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scannerAddToCart.setVisibility(View.VISIBLE);
                        productID.setText(result.getText());
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
                scannerAddToCart.setVisibility(View.INVISIBLE);
                productID.setText("");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    public void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    public void checkPermissions() {
        int grantCode = -1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            grantCode = getActivity().checkSelfPermission(Manifest.permission.CAMERA);
        }
        String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (grantCode != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, 1000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "You need to accept the camera permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}