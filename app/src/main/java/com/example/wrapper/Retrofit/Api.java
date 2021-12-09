package com.example.wrapper.Retrofit;

import com.example.wrapper.LiveApiResponseModel.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Md.harun or rashid on 08,December,2021
 * BABL, Bangladesh,
 */
public interface Api {
    @FormUrlEncoded
    @POST(Constants.GET_LOGIN_RESPONSE)
    Call<LoginResponse> getLoginResponse(@Field("user_name")String emp_id, @Field("password")String role_code);
}
