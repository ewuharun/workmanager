package com.example.wrapper.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.wrapper.LiveApiResponseModel.LoginResponse;
import com.example.wrapper.Retrofit.ApiClient;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Md.harun or rashid on 08,December,2021
 * BABL, Bangladesh,
 */
public class LoginRepository {

    public MutableLiveData<LoginResponse> getLoginApiMutableLiveData(String userName, String password) {

        final MutableLiveData<LoginResponse> loginMutableLiveData = new MutableLiveData<>();

        ApiClient.getApiClient().getLoginResponse(userName, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Log.e("LoginResponse",new Gson().toJson(response.body()));

                    if(response!=null && response.code()==200){
                        Log.e("Success","LoginSuccess");
                    }else{
                        Log.e("Failed","Something went wrong !");
                    }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginResponseError",t.getLocalizedMessage());
            }
        });
        return loginMutableLiveData;
    }
}
