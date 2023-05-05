package tdtu.edu.vn.finalproject_suppermarket;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static int ID = 0;
    SwitchFragmentInterface mCallback;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MaterialButton buttonContinue;
    private EditText edtUsername;
    private EditText edtLastname;
    private EditText edtFirstname;
    private EditText edtPhone;
    private EditText edtPassword;
    private EditText edtRePassword;
    private RadioGroup radioGroup;
    private ProgressBar spinner;


    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonContinue = view.findViewById(R.id.btnContinue);


        edtUsername = view.findViewById(R.id.edtUsername);
        edtFirstname = view.findViewById(R.id.edtFirstname);
        edtLastname = view.findViewById(R.id.edtLastname);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtPassword = view.findViewById(R.id.edtPassword);
        edtRePassword = view.findViewById(R.id.edtRePassword);
        radioGroup = view.findViewById(R.id.rdgGender);
        spinner = view.findViewById(R.id.progressBar);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View childView) {
                String username = edtUsername.getText().toString().trim();
                String firstname = edtFirstname.getText().toString().trim();
                String lastname = edtLastname.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String rePassword = edtRePassword.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                int genid = radioGroup.getCheckedRadioButtonId();
                String gender = "";
                if (genid == 0 || genid == 1 || genid == 2) {
                    RadioButton radioButton = (RadioButton) view.findViewById(genid);
                    gender = radioButton.getText().toString();
                }
                if (username.equals("") || firstname.equals("") || lastname.equals("") || password.equals("") || rePassword.equals("") || phone.equals("")) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Thông báo")
                            .setMessage("Vui lòng không để trống các trường")
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                } else {
                    if (!password.trim().equals(rePassword.trim())) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Thông báo")
                                .setMessage("Vui lòng nhập cùng mật khẩu")
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                        edtUsername.setText(password);
                        edtFirstname.setText(rePassword);
                    } else {
                        register(username, lastname, firstname, phone, password, gender);
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (SwitchFragmentInterface) getParentFragment();
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

    public void register(String username, String lastname, String firstname, String phone, String password, String gender) {
        OkHttpClient client = new OkHttpClient();
        String hashPassword = Utils.md5Hash(password);
        String LOGIN_ENDPOINT = "https://suppermarket-api.fly.dev/user/register";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("first_name", firstname);
            jsonObject.put("last_name", lastname);
            jsonObject.put("phone_number", phone);
            jsonObject.put("password", hashPassword);
            jsonObject.put("gender", gender);
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            spinner.setVisibility(View.INVISIBLE);
                            JSONObject json = new JSONObject(responseData);
                            boolean isRegisted = json.getBoolean("status");
                            if (isRegisted) {
                                mCallback.switchFragment(RegisterSuccessFragment.ID);
                            } else {
                                mCallback.switchFragment(RegisterFailFragment.ID);
                            }
                        } catch (JSONException e) {
                            Log.d("onResponse", e.getMessage());
                        }
                    }
                });
            }
        });
    }
}