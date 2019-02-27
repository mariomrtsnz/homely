package com.mario.homely.ui.properties.detail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mario.homely.R;
import com.mario.homely.responses.GetOneContainer;
import com.mario.homely.responses.PropertyResponse;
import com.mario.homely.retrofit.generator.AuthType;
import com.mario.homely.retrofit.generator.ServiceGenerator;
import com.mario.homely.retrofit.services.PropertyService;
import com.mario.homely.util.UtilToken;

import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyDetailsActivity extends Activity implements PropertyDetailsListener {
    private PropertyResponse selectedProperty;
    private ImageView goBackArrow, coverImage;
    private TextView description, direction, rooms, size, price;
    private FloatingActionButton fabFav;
    private Toolbar title;
    private String[] arrayPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        loadPropertyData(getIntent().getStringExtra("propertyId"));
//        goBackArrow = findViewById(R.id.iv_property_details_goback);
        coverImage = findViewById(R.id.iv_property_details_coverImage);
        title = findViewById(R.id.tv_property_details_title);
        fabFav = findViewById(R.id.fab_property_details_fav);
        fabFav.setOnClickListener(v -> {
            addAsFav(v);
        });
//        price = findViewById(R.id.tv_property_details_price);
//        goBackArrow.setOnClickListener(v -> {
//            finish();
//        });
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
                    title.setTitle(selectedProperty.getTitle());
//                    price.setText(String.valueOf(selectedProperty.getPrice()));
                    arrayPhotos = selectedProperty.getPhotos();
                    if (arrayPhotos.length != 0)
                        Glide.with(getBaseContext()).load(arrayPhotos[arrayPhotos.length-1]).into(coverImage);
                }
            }

            @Override
            public void onFailure(Call<GetOneContainer<PropertyResponse>> call, Throwable t) {
                Log.e("Network Failure", t.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void addAsFav(View v) {
        PropertyService service = ServiceGenerator.createService(PropertyService.class, UtilToken.getToken(getApplicationContext()), AuthType.JWT);
//        Call<GetOneContainer<PropertyResponse>> call = service.addAsFav(selectedProperty.getId());
//        call.enqueue(new Callback<GetOneContainer<PropertyResponse>>() {
//            @Override
//            public void onResponse(Call<GetOneContainer<PropertyResponse>> call, Response<GetOneContainer<PropertyResponse>> response) {
//                if (response.code() != 200) {
//                    Toast.makeText(getApplicationContext(), "Request Error", Toast.LENGTH_SHORT).show();
//                } else {
//                    selectedProperty = response.body().getRows();
//                    title.setTitle(selectedProperty.getTitle());
////                    price.setText(String.valueOf(selectedProperty.getPrice()));
//                    arrayPhotos = selectedProperty.getPhotos();
//                    if (arrayPhotos.length != 0)
//                        Glide.with(getBaseContext()).load(arrayPhotos[arrayPhotos.length-1]).into(coverImage);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetOneContainer<PropertyResponse>> call, Throwable t) {
//                Log.e("Network Failure", t.getMessage());
//                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

}
