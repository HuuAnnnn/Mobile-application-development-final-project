package tdtu.edu.vn.finalproject_suppermarket;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReigisterAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReigisterAddressFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SwitchFragmentInterface mCallback;

    public static int ID = 1;

    public ReigisterAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReigisterAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReigisterAddressFragment newInstance(String param1, String param2) {
        ReigisterAddressFragment fragment = new ReigisterAddressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ReigisterAddressFragment newInstance() {
        ReigisterAddressFragment fragment = new ReigisterAddressFragment();
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
        return inflater.inflate(R.layout.fragment_reigister_address, container, false);
    }

    private MaterialButton btnBack;
    private MaterialButton btnSkip;
    private MaterialButton btnContinue;
    private Spinner spnProvince;
    private Spinner spnDistricts;
    private Spinner spnWard;
    private ProvinceAdapter provinceAdapter;
    private DistrictsAdapter districtsAdapter;
    List<Province> listProvince = new ArrayList<>();
    List<Districts> listDistricts = new ArrayList<>();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnBack = view.findViewById(R.id.btnBack);
        btnSkip = view.findViewById(R.id.btnSkip);
        btnContinue = view.findViewById(R.id.btnContinue);
        View fragmentView = view;
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.switchFragment(RegisterFragment.ID);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.switchFragment(RegisterFailFragment.ID);
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.switchFragment(RegisterSuccessFragment.ID);
            }
        });

        //generate spinner province
        listProvince.add(new Province("111", "---Tỉnh---", "111"));
        getProvince();
        spnProvince = view.findViewById(R.id.spnProvince);
        spnDistricts = view.findViewById(R.id.spnDistrict);
        provinceAdapter = new ProvinceAdapter(this.getContext(), R.layout.item_address_selected, listProvince);
        spnProvince.setAdapter(provinceAdapter);
        spnProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String provinceName = provinceAdapter.getItem(position).getName_with_type();
                listDistricts.add(new Districts("111", "---Tỉnh---", "111","f"));
                districtsAdapter = new DistrictsAdapter(fragmentView.getContext(), R.layout.item_address_selected, listDistricts);
                spnProvince.setAdapter(provinceAdapter);
            }
        });
    }

    public void getProvince(){

        // Khởi tạo OkHttpClient để lấy dữ liệu.
        OkHttpClient client = new OkHttpClient();

        // Tạo request lên server.
        Request request = new Request.Builder()
                .url("https://vn-public-apis.fpo.vn/provinces/getAll?limit=-1")
                .get()
                .build();

        // Thực thi request.
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error", "Network Error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Lấy thông tin JSON trả về. Bạn có thể log lại biến json này để xem nó như thế nào.
                String json = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String nameWithType = item.getString("name_with_type");
                        String code = item.getString("code");
                        String _id = item.getString("_id");
                        listProvince.add(new Province(_id, nameWithType, code));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (SwitchFragmentInterface ) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getParentFragment().toString()
                    + " must implement MyInterface ");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}