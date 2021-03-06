package com.example.wrapper.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wrapper.Models.AssetModel;
import com.example.wrapper.Models.Pojo.Asset;
import com.example.wrapper.R;
import com.example.wrapper.Session.SessionManager;
import com.example.wrapper.SyncUtils.data.DBHandler;
import com.example.wrapper.Utils.PermissionUtils;
import com.example.wrapper.ViewModel.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn, exitBtn;
    private ImageView CompanyLogo, BablLogo;
    private TextView ComapnyName, BablTitile, CompanyTitle;
    private TextInputLayout userNameInputLayout, passwordInputLayout;
    private TextInputEditText usernameEt, passwordEt;

    private RelativeLayout colorlayout;

    //Variable for storing the value of Editext (user Input)
    String username, password;
    Asset asset;

    private DBHandler dbHandler;
    private SQLiteDatabase db;

    private SessionManager sessionManager;
    private View view;
    //private String[] PERMISSION={Manifest.permission.INTERNET,Manifest.permission.CAMERA};

    private LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createDataBase();

        init();

//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 1=wifi ------- 2=mobile -----0=not Connected
//                int connectivityStatus = NetworkUtils.getConnectivityStatus(getApplicationContext());
//                if(connectivityStatus!=0){
//                    boolean isValid = loginViewModel.isValid("harun","12345",usernameEt,passwordEt);
//                    if(isValid){
//                        loginViewModel.getLoginResponseMutableLiveData("harun","12345").observe(this, new Observer<LoginResponse>() {
//                            @Override
//                            public void onChanged(LoginResponse loginResponse) {
//                                Log.e("LoggedIn",new Gson().toJson(loginResponse));
//
//                                //do whatever you want
//                            }
//                        });
//                    }
//                }else{
//                    Toast.makeText(LoginActivity.this, "Please Turn On Mobile Data", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        loginButtonClicked();

        exitButtonClicked();

    }


    private void createDataBase() {
        dbHandler = new DBHandler(getApplicationContext());
        db = dbHandler.getWritableDatabase();
        dbHandler.createTable(db);
    }

    private void loadData(String response, Asset asset) {
        AssetModel assetModel = new AssetModel();
        asset = assetModel.getAssetData(response);
        setAssetToLoginView(asset);
    }

    private void setAssetToLoginView(Asset asset) {
        ComapnyName.setText(asset.getCompanyName());
        BablTitile.setText(asset.getBabl_title());
        CompanyTitle.setText(asset.getProjectName());

        Glide.with(LoginActivity.this).load(asset.getCompanyLogo()).into(CompanyLogo);
        Glide.with(LoginActivity.this).load(asset.getBabl_logo()).into(BablLogo);
        colorlayout.setBackgroundColor(Color.parseColor(asset.getColor()));

    }


    private boolean gettingInputTextValue() {
        username = usernameEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();

        boolean isValid = validate(username, password);

        return isValid;
    }

    private boolean validate(String username, String password) {
        userNameInputLayout.setError(null);
        passwordInputLayout.setError(null);

        AssetModel assetModel = new AssetModel();

        if (username.isEmpty() && password.isEmpty()) {
            userNameInputLayout.setError("Username can't be empty ");
            passwordInputLayout.setError("Password can't be empty ");
            return false;
        }
        if (username.isEmpty()) {
            userNameInputLayout.setError("Username can't be empty");
            return false;
        }
        if (password.isEmpty()) {
            passwordInputLayout.setError("Password can't be empty");
            return false;
        }
        return true;
    }

    private void exitButtonClicked() {
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loginButtonClicked() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = gettingInputTextValue();
                if (isValid == true) {
                    boolean isNetworkConnected = checkNetworkConnection();
                    if (isNetworkConnected) {
                        sessionManager.saveLoginSession("SR", "12345");

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("pageFrom","LoginPage");
                        startActivity(intent);




                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("You must on your mobile data....");
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog.setCancelable(true);
                    }

                }
            }
        });
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void init() {
        CompanyLogo = findViewById(R.id.CompanyLogo);
        ComapnyName = findViewById(R.id.CompanyName);
        CompanyTitle = findViewById(R.id.CompanyTitle);
        usernameEt = findViewById(R.id.userNameEt);
        passwordEt = findViewById(R.id.passwordEt);
        userNameInputLayout = findViewById(R.id.usernameInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        BablLogo = findViewById(R.id.bablLogo);
        BablTitile = findViewById(R.id.bablTitle);
        exitBtn = findViewById(R.id.exitBtn);
        loginBtn = findViewById(R.id.loginBtn);
        colorlayout = findViewById(R.id.CompanyColor);

        sessionManager = new SessionManager(getApplicationContext());
        loginViewModel = ViewModelProviders.of(LoginActivity.this).get(LoginViewModel.class);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!PermissionUtils.hasPermissions(LoginActivity.this, PermissionUtils.PERMISSION)) {
            int PERMISSION_ALL = 1;
            ActivityCompat.requestPermissions(LoginActivity.this, PermissionUtils.PERMISSION, PERMISSION_ALL);
        }
        boolean isUserLoggedIn = loginViewModel.isUserLoggedIn();
        if (isUserLoggedIn) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}