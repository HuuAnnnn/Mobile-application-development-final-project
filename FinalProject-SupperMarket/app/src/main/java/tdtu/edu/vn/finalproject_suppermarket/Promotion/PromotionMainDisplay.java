package tdtu.edu.vn.finalproject_suppermarket.Promotion;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
 * Use the {@link PromotionMainDisplay#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PromotionMainDisplay extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView displayPromotion;
    private PromotionAdapter promotionAdapter;
    private ProgressBar spinner;

    public PromotionMainDisplay() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PromotionMainDisplay.
     */
    // TODO: Rename and change types and number of parameters
    public static PromotionMainDisplay newInstance(String param1, String param2) {
        PromotionMainDisplay fragment = new PromotionMainDisplay();
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
        return inflater.inflate(R.layout.fragment_promotion_main_display, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayPromotion = view.findViewById(R.id.displayPromotions);
        spinner = view.findViewById(R.id.progressBar);
        ArrayList<Promotion> promotionArrayList = new ArrayList<>();
        promotionAdapter = new PromotionAdapter(getContext(), promotionArrayList);
        displayPromotion.setAdapter(promotionAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        OkHttpClient client = new OkHttpClient();
        String PROMOTIONS_ENDPOINTS = "https://suppermarket-api.fly.dev/promotion/promotions";

        Request request = new Request.Builder().url(PROMOTIONS_ENDPOINTS).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("onFailure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response)
                    throws IOException {
                String responseData = response.body().string();
                Log.d("Test", responseData);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            spinner.setVisibility(View.INVISIBLE);
                            JSONObject jsonObject = new JSONObject(responseData);
                            ArrayList<Promotion> promotions = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                promotions.add(new Promotion(
                                        object.getString("title"),
                                        object.getString("id"),
                                        object.getString("expiredDate")
                                ));
                            }

                            promotionAdapter.updateData(promotions);
                        } catch (JSONException e) {
                            Log.d("onResponse", e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}