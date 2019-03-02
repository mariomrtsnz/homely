package com.mario.homely.retrofit.services;

import com.mario.homely.responses.CategoryResponse;
import com.mario.homely.responses.ResponseContainer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryService {
    String BASE_URL = "/categories";

    @GET(BASE_URL)
    Call<ResponseContainer<CategoryResponse>> listCategories();

    @GET(BASE_URL+"/{id}")
    Call<CategoryResponse> getOne(@Path("id") String id);
}
