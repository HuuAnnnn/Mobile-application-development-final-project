package tdtu.edu.vn.finalproject_suppermarket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangeAvatarActivity extends AppCompatActivity {

    private MaterialButton btnPickImage;
    private MaterialButton btnSave;
    private ImageView imgAvatar;
    private String img_str;
    private ImageView imgvAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);
        displayInformation();
        img_str = "";
        btnPickImage = findViewById(R.id.btnPickImage);
        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ChangeAvatarActivity.this)
                        .setTitle("Thông báo")
                        .setMessage("Bạn có muốn thay đổi ảnh đại diện")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateAvatar(img_str);
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
    private void pickImage() {
        ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (data != null) {
                // Lấy đường dẫn của hình ảnh từ Intent
                Uri imageUri = data.getData();
                imgAvatar = findViewById(R.id.imgvAvatar);
                imgAvatar.setImageURI(imageUri);
                imgAvatar.buildDrawingCache();
                Bitmap bitmap = imgAvatar.getDrawingCache();

                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] image=stream.toByteArray();

                img_str = Base64.encodeToString(image, 0);
            }
    }

    public void displayInformation() {
        imgvAvatar = findViewById(R.id.imgvAvatar);
        SharedPreferences sharedPreferences = ChangeAvatarActivity.this.getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
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

                    ChangeAvatarActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(responseData);
                                JSONObject jsonArray = jsonObject.getJSONObject("data");
                                String lastname = jsonArray.getString("image");

                                String encodedImage = jsonArray.getString("image");
                                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                imgvAvatar.setImageBitmap(decodedByte);
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
    public void updateAvatar(String img_str) {
        SharedPreferences sharedPreferences = ChangeAvatarActivity.this.getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        OkHttpClient client = new OkHttpClient();
        String LOGIN_ENDPOINT = "https://suppermarket-api.fly.dev/user/update-user-image";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("newImage", img_str);
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
                    ChangeAvatarActivity.this.finish();
                } else {
                }
            }
        });
    }
}