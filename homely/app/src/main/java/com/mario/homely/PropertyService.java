package com.mario.homely;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PropertyService {
    String BASE_URL = "/properties/";

    @GET(BASE_URL)
    Call<ResponseContainer<PropertyResponse>> listProperties();
}
