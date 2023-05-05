package tdtu.edu.vn.finalproject_suppermarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ChooseAddressActivity extends AppCompatActivity {

    private MaterialButton btnBack;
    private MaterialButton btnSkip;
    private MaterialButton btnContinue;
    private Spinner spnProvince;
    private Spinner spnDistricts;
    private Spinner spnWard;
    private ProvinceSpinnerAdapter provinceSpinnerAdapter;
    private DistrictsSpinnerAdapter districtsSpinnerAdapter;
    private WardSpinnerAdapter wardSpinnerAdapter;
    List<Province> listProvince = new ArrayList<>();
    List<Districts> listDistricts = new ArrayList<>();
    List<Ward> listWard = new ArrayList<>();
    private EditText edtAddress;
    private RadioGroup group;
    private MaterialButton btnSaveAddress;
    private EditText editText;
    private String city;
    private String district;
    private String ward;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);
        edtAddress = findViewById(R.id.edtAddress);
        //generate spinner province
        listProvince.add(new Province("111", "---Tỉnh---", "111"));
        listDistricts.add(new Districts("111", "---Quận, Huyện---", "111", "f"));
        listWard.add(new Ward("---Phường/xã---"));

        getProvince(); // generate List province

        spnProvince = findViewById(R.id.spnProvince);
        spnDistricts = findViewById(R.id.spnDistrict);
        spnWard = findViewById(R.id.spnWard);

        provinceSpinnerAdapter = new ProvinceSpinnerAdapter(this, R.layout.item_address_selected, listProvince);
        spnProvince.setAdapter(provinceSpinnerAdapter);
        districtsSpinnerAdapter = new DistrictsSpinnerAdapter(this, R.layout.item_address_selected, listDistricts);
        spnDistricts.setAdapter(districtsSpinnerAdapter);
        wardSpinnerAdapter = new WardSpinnerAdapter(this, R.layout.item_address_selected, listWard);
        spnWard.setAdapter(wardSpinnerAdapter);
        spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String provinceName = provinceSpinnerAdapter.getItem(position).getName_with_type();
                if(position!=0) {
                    listDistricts = new ArrayList<>();
                    Province province = findProvince(provinceName);
                    listDistricts.add(new Districts("111", "---Quận, Huyện---", "111", "f"));
                    getDistricts(province.getCode());
                    districtsSpinnerAdapter = new DistrictsSpinnerAdapter(ChooseAddressActivity.this, R.layout.item_address_selected, listDistricts);
                    spnDistricts.setAdapter(districtsSpinnerAdapter);

                    spnDistricts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String districtsName = districtsSpinnerAdapter.getItem(position).getNameWithType();
                            if(position!=0) {
                                Districts districts = findDistricts(districtsName);
                                listWard = new ArrayList<>();
                                listWard.add(new Ward("---Phường/xã---"));
                                getWard(districts.getCode());
                                wardSpinnerAdapter = new WardSpinnerAdapter(ChooseAddressActivity.this, R.layout.item_address_selected, listWard);
                                spnWard.setAdapter(wardSpinnerAdapter);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        group = findViewById(R.id.rdgTypeHome);
        btnSaveAddress = findViewById(R.id.btnSaveAddress);
        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int genid = group.getCheckedRadioButtonId();
                String type = "";
                if(genid==0 || genid==1 || genid ==2) {
                    RadioButton radioButton = (RadioButton)findViewById(genid);
                    type = radioButton.getText().toString();

                }
                String city = spnProvince.getSelectedItem().toString();
                String districts = spnDistricts.getSelectedItem().toString();
                String ward = spnWard.getSelectedItem().toString();
                String address = edtAddress.getText().toString();
                changeAddress(city,districts,ward,address,type);
            }
        });

    }
    public Province findProvince(String nameProvince){
        Province province = null;
        for (int i = 0; i<listProvince.size(); i++){
            if(listProvince.get(i).getName_with_type().equals(nameProvince)){
                province = listProvince.get(i);
            }
        }
        return province;
    }

    public void getProvince(){

        // Khởi tạo OkHttpClient để lấy dữ liệu.
        OkHttpClient client = new OkHttpClient();

        // Tạo request lên server.
        Request request = new Request.Builder()
                .url("https://vapi.vnappmob.com/api/province")
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
                ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
                String json = responseBodyCopy.string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String nameWithType = item.getString("province_name");
                        String code = item.getString("province_id");
                        String _id = item.getString("province_type");
                        listProvince.add(new Province(_id.trim(), nameWithType.trim(), code.trim()));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public Districts findDistricts(String nameDistricts){
        Districts districts = null;
        for (int i = 0; i<listDistricts.size(); i++){
            if(listDistricts.get(i).getNameWithType().equals(nameDistricts)){
                districts = listDistricts.get(i);
            }
        }
        return districts;
    }
    public void getDistricts(String provinceCode){
        // Khởi tạo OkHttpClient để lấy dữ liệu.
        OkHttpClient client = new OkHttpClient();

        // Tạo request lên server.
        Request request = new Request.Builder()
                .url("https://vapi.vnappmob.com/api/province/district/"+provinceCode.trim())
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
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String nameWithType = item.getString("district_name");
                        String code = item.getString("district_id");
                        String _id = "1";
                        String parentCode = "1";
                        listDistricts.add(new Districts(_id, nameWithType, code,parentCode.trim()));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void getWard(String districtCode){
        // Khởi tạo OkHttpClient để lấy dữ liệu.
        OkHttpClient client = new OkHttpClient();

        // Tạo request lên server.
        Request request = new Request.Builder()
                .url("https://vapi.vnappmob.com/api/province/ward/"+districtCode.trim())
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
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String nameWithType = item.getString("ward_name");
                        listWard.add(new Ward(nameWithType));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void changeAddress(String city, String districs, String ward, String address, String typeAddress) {
        SharedPreferences sharedPreferences = ChooseAddressActivity.this.getSharedPreferences("SupperMarket", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        OkHttpClient client = new OkHttpClient();
        String LOGIN_ENDPOINT = "https://suppermarket-api.fly.dev/user/change-address";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("city", city);
            jsonObject.put("district", districs);
            jsonObject.put("ward", ward);
            jsonObject.put("address", address);
            jsonObject.put("type_of_address", typeAddress);
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
                    edtAddress.setText(isChanged.toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                if (isChanged) {
                    ChooseAddressActivity.this.finish();
                } else {
                    ChooseAddressActivity.this.finish();
                }
            }
        });
    }
}