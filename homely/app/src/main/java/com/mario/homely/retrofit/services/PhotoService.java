package com.mario.homely.retrofit.services;

import com.mario.homely.responses.PhotoResponse;
import com.mario.homely.responses.PhotoUploadResponse;
import com.mario.homely.responses.ResponseContainer;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface PhotoService {
    String BASE_URL = "/photos";

    @GET(BASE_URL)
    Call<ResponseContainer<PhotoResponse>> getAll();

    @GET(BASE_URL + "/{id}")
    Call<PhotoResponse> getOne(@Path("id") String id);

    @Multipart
    @POST(BASE_URL)
    Call<PhotoUploadResponse> upload(@Part MultipartBody.Part photo, @Part("propertyId") RequestBody propertyId);

    @DELETE(BASE_URL + "/{id}")
    Call<PhotoResponse> delete(@Path("id") String id);
}
