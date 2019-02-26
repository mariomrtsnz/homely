package com.mario.homely.retrofit.services;

import com.mario.homely.responses.ResponseContainer;
import com.mario.homely.responses.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {
    String BASE_URL = "/users";

    @GET(BASE_URL)
    Call<ResponseContainer<UserResponse>> listUsers();

    @GET(BASE_URL + "/{id}")
    Call<UserResponse> getUser(@Path("id") String id);

    @GET(BASE_URL + "/me")
    Call<UserResponse> getMe();

//    @PUT(BASE_URL + "/{id}")
//    Call<UserEditResponse> editUser(@Path("id") String id, @Body UserEditDto user);
//
//    @PUT(BASE_URL + "/{id}/password")
//    Call<UserResponse> editPassword(@Path("id") String id, @Body String password);
//
//    @Multipart
//    @POST(BASE_URL + "/uploadProfilePicture")
//    Call<MyProfileResponse> uploadPictureProfile(@Part MultipartBody.Part avatar,
//                                                 @Part("id") RequestBody id);
//
//    @PUT(BASE_URL + "/addPoiLike/{id}")
//    Call<UserResponse> ChangePoiFav(@Path("id") String id);
}
