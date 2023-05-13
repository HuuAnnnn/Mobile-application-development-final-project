package tdtu.edu.vn.finalproject_suppermarket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;

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

public class UserInformationActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtLasname;
    private EditText edtFirstname;
    private EditText edtPhone;
    private EditText edtCity;
    private EditText edtDistricts;
    private EditText edtWard;
    private EditText edtAddress;
    private EditText edtGender;
    private ImageButton btnUserInfor;

    private MaterialButton btnChange;
    private MaterialButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        edtAddress = findViewById(R.id.edtAddress);
        edtUsername = findViewById(R.id.edtUsername);
        edtLasname = findViewById(R.id.edtLastname);
        edtFirstname = findViewById(R.id.edtFirstname);
        edtPhone = findViewById(R.id.edtPhone);
        edtCity = findViewById(R.id.edtCity);
        edtDistricts = findViewById(R.id.edtDistricts);
        edtWard = findViewById(R.id.edtWard);
        edtGender = findViewById(R.id.edtGender);
        btnUserInfor = findViewById(R.id.btnUserInfor);
        btnUserInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInformationActivity.this.finish();
            }
        });
        displayInformation();

        btnChange = findViewById(R.id.btnChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAddress.setEnabled(true);
                edtUsername.setEnabled(true);
                edtLasname.setEnabled(true);
                edtFirstname.setEnabled(true);
                edtPhone.setEnabled(true);
                edtCity.setEnabled(true);
                edtDistricts.setEnabled(true);
                edtWard.setEnabled(true);
                edtGender.setEnabled(true);
            }
        });

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserInformationActivity.this)
                        .setTitle("Thông báo")
                        .setMessage("Bạn có chắc muốn thay đổi thông tin")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String address = edtAddress.getText().toString();
                                String lastname = edtLasname.getText().toString();
                                String firstName = edtFirstname.getText().toString();
                                String phone = edtPhone.getText().toString();
                                String city = edtCity.getText().toString();
                                String districts = edtDistricts.getText().toString();
                                String ward = edtWard.getText().toString();
                                String gender = edtGender.getText().toString();
                                updateInfor(lastname, firstName, phone, city, districts, ward, address, gender);
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
            }
        });
    }

    public void updateInfor(String lastName, String firstName, String phone, String city, String district, String ward, String address, String genger){
        SharedPreferences sharedPreferences = UserInformationActivity.this.getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        OkHttpClient client = new OkHttpClient();
        String LOGIN_ENDPOINT = "https://suppermarket-api.fly.dev/user/update-information";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("first_name", firstName);
            jsonObject.put("last_name", lastName);
            jsonObject.put("phone_number", phone);
            jsonObject.put("gender", genger);
            jsonObject.put("city", city);
            jsonObject.put("district", district);
            jsonObject.put("ward", ward);
            jsonObject.put("address", address);
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
                Boolean isChanged = false;
                try {
                    json = new JSONObject(responseData);
                    isChanged = json.getBoolean("status");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                if (isChanged) {
                    edtAddress.setEnabled(false);
                    edtUsername.setEnabled(false);
                    edtLasname.setEnabled(false);
                    edtFirstname.setEnabled(false);
                    edtPhone.setEnabled(false);
                    edtCity.setEnabled(false);
                    edtDistricts.setEnabled(false);
                    edtWard.setEnabled(false);
                    edtGender.setEnabled(false);
                } else {
                }
            }
        });
    }
    public void displayInformation() {
        SharedPreferences sharedPreferences = UserInformationActivity.this.getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
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

                    UserInformationActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(responseData);
                                JSONObject jsonArray = jsonObject.getJSONObject("data");
                                String lastname = jsonArray.getString("last_name");
                                String firstName = jsonArray.getString("first_name");
                                String phone = jsonArray.getString("phone_number");
                                String city = jsonArray.getString("city");
                                String districst = jsonArray.getString("district");
                                String ward = jsonArray.getString("ward");
                                String address = jsonArray.getString("address");
                                String gender = jsonArray.getString("gender");
                                edtAddress.setText(address);
                                edtUsername.setText(username);
                                edtLasname.setText(lastname);
                                edtFirstname.setText(firstName);
                                edtPhone.setText(phone);
                                edtCity.setText(city);
                                edtDistricts.setText(districst);
                                edtWard.setText(ward);
                                edtGender.setText(gender);
                                edtAddress.setEnabled(false);
                                edtUsername.setEnabled(false);
                                edtLasname.setEnabled(false);
                                edtFirstname.setEnabled(false);
                                edtPhone.setEnabled(false);
                                edtCity.setEnabled(false);
                                edtDistricts.setEnabled(false);
                                edtWard.setEnabled(false);
                                edtGender.setEnabled(false);
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
}