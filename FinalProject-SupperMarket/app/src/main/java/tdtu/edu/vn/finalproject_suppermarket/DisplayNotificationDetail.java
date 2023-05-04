package tdtu.edu.vn.finalproject_suppermarket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tdtu.edu.vn.finalproject_suppermarket.Notification.Notification;

public class DisplayNotificationDetail extends AppCompatActivity {
    ImageButton btnBack;
    TextView displayNotificationTitle;
    TextView displayNotificationDateCreate;
    TextView displayNotificationContent;
    ImageView displayImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notification_detail);
        btnBack = findViewById(R.id.btnNotificationBack);
        displayNotificationContent = findViewById(R.id.displayContent);
        displayNotificationDateCreate = findViewById(R.id.displayDateCreate);
        displayNotificationTitle = findViewById(R.id.displayTile);
        displayImage = findViewById(R.id.detailImage);
        loadNotification();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void loadNotification() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("notificationID");
        final String GET_NOTIFICATION_ENDPOINTS = "https://suppermarket-api.fly.dev/notification/get-notification?notificationID=" + id;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(GET_NOTIFICATION_ENDPOINTS)
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
                    JSONObject json = new JSONObject(responseData);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            boolean isSuccess = false;
                            try {
                                Log.d("Test", responseData);
                                isSuccess = json.getBoolean("status");
                                if (!isSuccess) {
                                    Toast.makeText(DisplayNotificationDetail.this, "Không thể tải thông báo! Vui lòng kiểm tra lại kết nối", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONObject data = json.getJSONObject("data");
                                    Notification notification = new Notification(
                                            data.getString("id"),
                                            data.getString("title"),
                                            data.getString("dateCreate"),
                                            data.getString("content"),
                                            data.getString("image")
                                    );
                                    displayNotificationContent.setText(notification.getContent());
                                    displayNotificationDateCreate.setText(notification.getDateCreate());
                                    displayNotificationTitle.setText(notification.getTitle());

                                    byte[] decodedString = Base64.decode(notification.getImage(), Base64.DEFAULT);
                                    Bitmap imageNotification = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    displayImage.setImageBitmap(imageNotification);
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