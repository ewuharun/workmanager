package com.example.workmanagerimplementation.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workmanagerimplementation.LiveApiResponseModel.LoginResponse;
import com.example.workmanagerimplementation.Repository.LoginRepository;
import com.example.workmanagerimplementation.Session.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Created by Md.harun or rashid on 08,December,2021
 * BABL, Bangladesh,
 */
public class LoginViewModel extends AndroidViewModel {

    MutableLiveData<LoginResponse> loginResponseMutableLiveData = new MutableLiveData<>();
    LoginRepository loginRepository;
    SessionManager sessionManager;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.sessionManager=new SessionManager(application);
    }

    public MutableLiveData<LoginResponse> getLoginResponseMutableLiveData(String userName,String password) {
        return loginRepository.getLoginApiMutableLiveData(userName,password);
    }

    public boolean isValid(String userName, String password, TextInputEditText usernameEt, TextInputEditText passwordEt) {

        boolean isValid = true;

        if(userName.equals("") && password.equals("")){
            usernameEt.setError("user name is empty");
            passwordEt.setError("password is empty");
            isValid = false;
        }

        return isValid;
    }

    public boolean isUserLoggedIn() {
        return sessionManager.getIsLoggedIn();
    }
}
