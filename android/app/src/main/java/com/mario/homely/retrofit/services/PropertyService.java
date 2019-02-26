package com.mario.homely.retrofit.services;

import com.mario.homely.responses.PropertyResponse;
import com.mario.homely.responses.ResponseContainer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PropertyService {
    String BASE_URL = "/properties/";

    @GET(BASE_URL)
    Call<ResponseContainer<PropertyResponse>> listProperties(@Query("near") String latlng, @Query("max_distance") int maxDistance);

    @GET(BASE_URL)
    Call<ResponseContainer<PropertyResponse>> listProperties();

    @GET(BASE_URL + "/{id}")
    Call<PropertyResponse> getProperty(@Path("id") String id);

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
