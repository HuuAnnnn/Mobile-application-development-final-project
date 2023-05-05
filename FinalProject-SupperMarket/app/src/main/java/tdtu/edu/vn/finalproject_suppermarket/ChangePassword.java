package tdtu.edu.vn.finalproject_suppermarket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangePassword extends AppCompatActivity {

    private EditText edtPassword;
    private EditText edtRePassword;
    private MaterialButton btnChangePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        edtPassword = findViewById(R.id.edtPassword);
        edtRePassword  = findViewById(R.id.edtRePassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        SharedPreferences sharedPreferences = ChangePassword.this.getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View childView) {
                String password = edtPassword.getText().toString().trim();
                String rePassword = edtRePassword.getText().toString().trim();
                if ( password.equals("")|| rePassword.equals("")) {
                    new AlertDialog.Builder(ChangePassword.this)
                            .setTitle("Thông báo")
                            .setMessage("Vui lòng không để trống các trường")
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                } else {
                    if(!password.trim().equals(rePassword.trim())){
                        new AlertDialog.Builder(ChangePassword.this)
                                .setTitle("Thông báo")
                                .setMessage("Vui lòng nhập cùng mật khẩu")
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                    }else {
                        changePassword(username,password);
                    }
                }
            }
        });
    }

    public void changePassword(String username, String password) {
        OkHttpClient client = new OkHttpClient();
        String LOGIN_ENDPOINT = "https://suppermarket-api.fly.dev/user/change-password";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("newPassword", password);
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
                    signOut();
                } else {
                }
            }
        });
    }
    public void signOut() {
        SharedPreferences sharedPreferences = ChangePassword.this.getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            editor.putString("username", "");
            editor.putString("dateLogin", "");
            editor.commit();

            Intent intent = new Intent(ChangePassword.this, MainActivity.class);
            startActivity(intent);
            ChangePassword.this.finish();
        }
    }
}