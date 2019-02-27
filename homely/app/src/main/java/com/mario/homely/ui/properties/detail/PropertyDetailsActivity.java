package com.mario.homely.ui.properties.detail;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mario.homely.R;
import com.mario.homely.responses.GetOneContainer;
import com.mario.homely.responses.PropertyResponse;
import com.mario.homely.retrofit.generator.ServiceGenerator;
import com.mario.homely.retrofit.services.PropertyService;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyDetailsActivity extends AppCompatActivity {
    private PropertyResponse selectedProperty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        loadPropertyData(getIntent().getStringExtra("propertyId"));
    }

    private void loadPropertyData(String propertyId) {
        PropertyService service = ServiceGenerator.createService(PropertyService.class);
        Call<GetOneContainer<PropertyResponse>> call = service.getProperty(propertyId);
        call.enqueue(new Callback<GetOneContainer<PropertyResponse>>() {
            @Override
            public void onResponse(Call<GetOneContainer<PropertyResponse>> call, Response<GetOneContainer<PropertyResponse>> response) {
                if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Request Error", Toast.LENGTH_SHORT).show();
                } else {
                    selectedProperty = response.body().getRows();
                }
            }

            @Override
            public void onFailure(Call<GetOneContainer<PropertyResponse>> call, Throwable t) {
                Log.e("Network Failure", t.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
