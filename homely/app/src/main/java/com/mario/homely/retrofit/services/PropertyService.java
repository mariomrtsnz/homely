package com.mario.homely.retrofit.services;

import com.mario.homely.responses.GetOneContainer;
import com.mario.homely.responses.PropertyResponse;
import com.mario.homely.responses.ResponseContainer;
import com.mario.homely.responses.UserResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PropertyService {
    String BASE_URL = "/properties";

    @GET(BASE_URL)
    Call<ResponseContainer<PropertyResponse>> listPropertiesNearby(@Query("near") String lnglat, @Query("max_distance") int maxDistance);

    @GET(BASE_URL)
    Call<ResponseContainer<PropertyResponse>> listProperties();

    @GET(BASE_URL + "/auth")
    Call<ResponseContainer<PropertyResponse>> listPropertiesAuth();

    @GET(BASE_URL + "/{id}")
    Call<GetOneContainer<PropertyResponse>> getProperty(@Path("id") String id);

    @GET(BASE_URL + "/mine")
    Call<ResponseContainer<PropertyResponse>> getMyProperties();

    @GET(BASE_URL + "/fav")
    Call<ResponseContainer<PropertyResponse>> getMyFavs();

    @DELETE(BASE_URL + "/id")
    Call<UserResponse> deleteProperty(@Path("id") String id);

    @POST(BASE_URL + "/fav/{id}")
    Call<UserResponse> addAsFav(@Path("id") String id);

    @DELETE(BASE_URL + "/fav/{id}")
    Call<UserResponse> deleteAsFav(@Path("id") String id);

}
