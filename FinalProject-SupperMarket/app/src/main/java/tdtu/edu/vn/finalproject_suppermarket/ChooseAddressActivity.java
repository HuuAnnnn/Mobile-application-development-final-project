package tdtu.edu.vn.finalproject_suppermarket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

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

    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);

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
                if(!provinceName.equals("---Tỉnh---")) {
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
                Log.e("TAG", json);
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
                .url("https://vn-public-apis.fpo.vn/districts/getByProvince?provinceCode="+provinceCode.trim()+"&limit=-1")
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
                        String parentCode = item.getString("parent_code");
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
                .url("https://vn-public-apis.fpo.vn/wards/getByDistrict?districtCode="+districtCode.trim()+"&limit=-1")
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
                        listWard.add(new Ward(nameWithType));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}