package tdtu.edu.vn.finalproject_suppermarket.Notification;

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
 * Use the {@link DisplayNotification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayNotification extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView displayNotification;
    private NotificationAdapter notificationAdapter;
    private ProgressBar spinner;

    public DisplayNotification() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayNotification.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayNotification newInstance(String param1, String param2) {
        DisplayNotification fragment = new DisplayNotification();
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
        return inflater.inflate(R.layout.fragment_display_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayNotification = view.findViewById(R.id.displayNotification);
        spinner = view.findViewById(R.id.progressBar);
        loadNotifications();
    }

    public void loadNotifications() {
        final String GET_NOTIFICATIONS_ENDPOINTS = "https://suppermarket-api.fly.dev/notification/notifications";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(GET_NOTIFICATIONS_ENDPOINTS).build();
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
                                Log.d("Test", responseData);
                                isSuccess = json.getBoolean("status");
                                if (!isSuccess) {
                                    Toast.makeText(getContext(), "Không thể tải thông báo! Vui lòng kiểm tra lại kết nối", Toast.LENGTH_SHORT).show();
                                } else {
                                    ArrayList<Notification> notifications = new ArrayList<Notification>();
                                    JSONArray data = json.getJSONArray("data");
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject jsonObject = data.getJSONObject(i);
                                        Notification notification = new Notification(
                                                jsonObject.getString("id"),
                                                jsonObject.getString("title"),
                                                jsonObject.getString("dateCreate"),
                                                jsonObject.getString("content"),
                                                jsonObject.getString("image")
                                        );

                                        notifications.add(notification);
                                    }
                                    notificationAdapter = new NotificationAdapter<Notification>(getActivity(), notifications);
                                    displayNotification.setAdapter(notificationAdapter);
                                    displayNotification.setLayoutManager(new LinearLayoutManager(getActivity()));
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