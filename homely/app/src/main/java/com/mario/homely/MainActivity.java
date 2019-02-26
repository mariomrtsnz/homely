package com.mario.homely;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    List<PropertyResponse> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listProperties();
    }

    public void listProperties() {
        PropertyService service = ServiceGenerator.createService(PropertyService.class);
        Call<ResponseContainer<PropertyResponse>> call = service.listProperties();
        call.enqueue(new Callback<ResponseContainer<PropertyResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainer<PropertyResponse>> call, Response<ResponseContainer<PropertyResponse>> response) {
                if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Request Error", Toast.LENGTH_SHORT).show();
                } else {
                    items = response.body().getRows();
                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<PropertyResponse>> call, Throwable t) {
                Log.e("Network Failure", t.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
