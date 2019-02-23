package com.mario.homely.retrofit.services;

import com.mario.homely.dto.RegisterDto;
import com.mario.homely.responses.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginService {
    @POST("/auth")
    Call<LoginResponse> doLogin(@Header("Authorization") String authorization);


    @POST("/users")
    Call<LoginResponse> doSignUp(@Body RegisterDto register);
}
