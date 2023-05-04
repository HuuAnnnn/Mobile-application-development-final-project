package tdtu.edu.vn.finalproject_suppermarket;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

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
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    public static final int ID = 0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    CallbackManager callbackManager;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText edtPassword;
    private ImageView showPassword;
    private EditText edtUsername;
    private TextView tvRegister;
    private Button btnLogin;
    private ProgressBar spinner;
    private View vGmail;
    private View vFacebook;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        showPassword = (ImageView) view.findViewById(R.id.btnShowPassword);
        edtUsername = view.findViewById(R.id.edtUsername);
        tvRegister = view.findViewById(R.id.tvRegister);
        btnLogin = view.findViewById(R.id.btnLogin);
        spinner = view.findViewById(R.id.progressBar);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View childView) {
                spinner.setVisibility(View.VISIBLE);
                tvRegister.setEnabled(false);
                btnLogin.setEnabled(false);
                edtUsername.setEnabled(false);
                edtPassword.setEnabled(false);
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                if (username.equals("") || password.equals("")) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Thông báo")
                            .setMessage("Vui lòng không để trống các trường")
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                } else {
                    login(username, password);
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPager mviewPager = (ViewPager) getActivity().findViewById(R.id.viewPager);
                mviewPager.setCurrentItem(RegisterFragment.ID);
            }
        });

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowHidePass(showPassword);
            }
        });

        //Login with Gmail
        vGmail = view.findViewById(R.id.vGmail);
        loginWithGmail();

        //Login with Facebook
        vFacebook = view.findViewById(R.id.vFacebook);
//        loginWithFacebook();
    }

    public void ShowHidePass(ImageView showPassword) {

        if (showPassword.getId() == R.id.btnShowPassword) {

            if (edtPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (showPassword)).setImageResource(R.drawable.show_password_icon);

                //Show Password
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                edtPassword.setSelection(edtPassword.getText().length());
            } else {
                ((ImageView) (showPassword)).setImageResource(R.drawable.hide_password_icon);

                //Hide Password
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                edtPassword.setSelection(edtPassword.getText().length());
            }
        }
    }

    public void login(String username, String password) {
        OkHttpClient client = new OkHttpClient();
        String LOGIN_ENDPOINT = "https://suppermarket-api.fly.dev/user/login";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
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
                            boolean isLogged = json.getBoolean("status");
                            if (isLogged) {
                                Intent intent = new Intent(getActivity(), Home.class);
                                startActivity(intent);
                                ((Activity) getActivity()).finish();
                            } else {
                                tvRegister.setEnabled(true);
                                btnLogin.setEnabled(true);
                                edtUsername.setEnabled(true);
                                edtPassword.setEnabled(true);
                                edtPassword.setText("");
                                String message = "Sai tài khoản hoặc khẩu";
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Thông báo")
                                        .setMessage(message)
                                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        }).show();
                            }
                        } catch (JSONException e) {
                            Log.d("onResponse", e.getMessage());
                        }
                    }
                });
            }
        });
    }

    public void loginWithGmail() {
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        if (googleSignInAccount != null) {
            navigateMainDisplayProducts();
        }

        vGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    public void loginWithFacebook() {
        callbackManager = CallbackManager.Factory.create();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && accessToken.isExpired() == false) {
            navigateMainDisplayProducts();
        }
        vFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), listOf("email", "public_profile"));
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(), "be cancel", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getContext(), "be error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                navigateMainDisplayProducts();
            } catch (ApiException e) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void navigateMainDisplayProducts() {
        Intent intent = new Intent(getActivity(), Home.class);
        startActivity(intent);
        getActivity().finish();
    }
}