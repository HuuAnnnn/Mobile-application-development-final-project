package tdtu.edu.vn.finalproject_suppermarket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import tdtu.edu.vn.finalproject_suppermarket.ProductHistory.ProductHistoryActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserInformation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserInformation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout btnSignOut;
    private TextView tvInforUsername;
    private TextView tvInforFullname;

    private TextView tvHistoryList;
    private TextView tvChangePassword;
    private TextView tvUserInfor;
    private ImageView imgvAvatar;

    public UserInformation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserInformation.
     */
    // TODO: Rename and change types and number of parameters
    public static UserInformation newInstance(String param1, String param2) {
        UserInformation fragment = new UserInformation();
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
        return inflater.inflate(R.layout.fragment_user_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSignOut = view.findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        displayInformation();

        //change password
        tvChangePassword = view.findViewById(R.id.tvChangePassword);
        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePassword.class);
                startActivity(intent);
            }
        });

        //display history list
        tvHistoryList = view.findViewById(R.id.tvHistoryList);
        tvHistoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductHistoryActivity.class);
                startActivity(intent);
            }
        });

        tvUserInfor = view.findViewById(R.id.tvInformation);
        tvUserInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserInformationActivity.class);
                startActivity(intent);
            }
        });

        imgvAvatar = view.findViewById(R.id.imgvAvatar);
        imgvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeAvatarActivity.class);
                startActivity(intent);

            }
        });
    }


    public void displayInformation() {
        tvInforUsername = getView().findViewById(R.id.tvInforUsername);
        tvInforFullname = getView().findViewById(R.id.tvInforfullname);
        imgvAvatar = getView().findViewById(R.id.imgvAvatar);
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
                                Log.d("test", responseData);
                                JSONObject jsonObject = new JSONObject(responseData);
                                JSONObject jsonArray = jsonObject.getJSONObject("data");
                                String firstName = jsonArray.getString("first_name");
                                String lastName = jsonArray.getString("last_name");
                                String balance = jsonArray.getString("balance");
                                String fullName = (firstName + " " + lastName).trim();
                                if (fullName.equals("")) {
                                    fullName = jsonArray.getString("username");
                                }
                                tvInforFullname.setText("Số dư: " + balance + "đ");
                                tvInforUsername.setText(fullName);

                                String encodedImage = jsonArray.getString("image");
                                if (!encodedImage.equals("")) {
                                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    imgvAvatar.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 75, 75, false));
                                }
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

    public void signOut() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            editor.putString("username", "");
            editor.putString("dateLogin", "");
            editor.commit();

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}