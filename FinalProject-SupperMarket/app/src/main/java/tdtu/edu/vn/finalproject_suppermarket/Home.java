package tdtu.edu.vn.finalproject_suppermarket;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tdtu.edu.vn.finalproject_suppermarket.Notification.DisplayNotification;
import tdtu.edu.vn.finalproject_suppermarket.Products.DisplayMainProduct;
import tdtu.edu.vn.finalproject_suppermarket.Promotion.PromotionMainDisplay;

public class Home extends AppCompatActivity {
    BottomNavigationView navbar;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Google Signin
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount != null) {
            register(googleSignInAccount.getEmail());
            saveSession(googleSignInAccount.getEmail());
        }

        setContentView(R.layout.home);
        navbar = findViewById(R.id.navBar);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        DisplayMainProduct home = new DisplayMainProduct();
        fragmentTransaction.add(R.id.mainDisplay, home);
        fragmentTransaction.commit();

        navbar.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.userAccount:
                        addNewFragment(new UserInformation());
                        break;
                    case R.id.homepage:
                        addNewFragment(new DisplayMainProduct());
                        break;
                    case R.id.notification:
                        addNewFragment(new DisplayNotification());
                        break;
                    case R.id.scan:
                        addNewFragment(new QRCode());
                        break;
                    case R.id.promotion:
                        addNewFragment(new PromotionMainDisplay());
                        break;
                }
                return true;
            }
        });
    }

    public void saveSession(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String dateLogin = dtf.format(now);
            editor.putString("username", username);
            editor.putString("dateLogin", dateLogin);
            editor.commit();
        }
    }

    public void register(String username) {
        OkHttpClient client = new OkHttpClient();
        String LOGIN_ENDPOINT = "https://suppermarket-api.fly.dev/user/register";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("first_name", "");
            jsonObject.put("last_name", "");
            jsonObject.put("phone_number", "");
            jsonObject.put("password", "");
            jsonObject.put("gender", "");
            jsonObject.put("city", "");
            jsonObject.put("district", "");
            jsonObject.put("ward", "");
            jsonObject.put("address", "");
            jsonObject.put("type_of_address", "");
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(responseData);
                            boolean isRegistered = json.getBoolean("status");
                            if (isRegistered) {
                                Toast.makeText(Home.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
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
    protected void onStop() {
        super.onStop();
        googleSignInClient.signOut();
    }

    private void addNewFragment(Fragment fragment) {
        fragmentManager.popBackStack();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainDisplay, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}