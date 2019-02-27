package com.mario.homely.retrofit.services;

import com.mario.homely.responses.GetOneContainer;
import com.mario.homely.responses.PropertyResponse;
import com.mario.homely.responses.ResponseContainer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PropertyService {
    String BASE_URL = "/properties";

    @GET(BASE_URL)
    Call<ResponseContainer<PropertyResponse>> listProperties(@Query("near") String latlng, @Query("max_distance") int maxDistance);

    @GET(BASE_URL)
    Call<ResponseContainer<PropertyResponse>> listProperties();

    @GET(BASE_URL + "/auth")
    Call<ResponseContainer<PropertyResponse>> listPropertiesAuth();

    @GET(BASE_URL + "/{id}")
    Call<GetOneContainer<PropertyResponse>> getProperty(@Path("id") String id);

    @GET(BASE_URL + "/mine")
    Call<PropertyResponse> getMyProperties();

    @GET(BASE_URL + "/fav")
    Call<PropertyResponse> getMyFavs();

}