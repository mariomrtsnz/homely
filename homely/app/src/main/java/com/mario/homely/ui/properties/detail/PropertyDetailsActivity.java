package com.mario.homely.ui.properties.detail;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mario.homely.R;
import com.mario.homely.responses.GetOneContainer;
import com.mario.homely.responses.PropertyResponse;
import com.mario.homely.responses.UserResponse;
import com.mario.homely.retrofit.generator.AuthType;
import com.mario.homely.retrofit.generator.ServiceGenerator;
import com.mario.homely.retrofit.services.PropertyService;
import com.mario.homely.util.UtilToken;

import java.util.List;
import java.util.Objects;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyDetailsActivity extends Activity implements PropertyDetailsListener {
    private PropertyResponse selectedProperty;
    private ImageView goBackArrow, coverImage;
    private TextView description, direction, rooms, size, price, location;
    private RecyclerView recyclerViewGallery;
    private FloatingActionButton fabFav, rentNow;
    private Toolbar title;
    private List<String> arrayPhotos;
    private PropertyDetailsAdapter adapter;
    private PropertyService propertyService;
    private PropertiesDetailsListener listener;
    private boolean isFav = false;
    private GoogleMap mMap;
    GoogleMapOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        loadPropertyData(getIntent().getStringExtra("propertyId"));
//        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
//        new OnMapAndViewReadyListener(mapFragment, this);

        options = new GoogleMapOptions().liteMode(true);
//        goBackArrow = findViewById(R.id.iv_property_details_goback);
        coverImage = findViewById(R.id.iv_property_details_coverImage);
        title = findViewById(R.id.tv_property_details_title);
        fabFav = findViewById(R.id.fab_property_details_fav);
        fabFav.setOnClickListener(v -> {
            updateFav(v);
        });
        recyclerViewGallery = findViewById(R.id.property_details_recycler_gallery);
        recyclerViewGallery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new PropertyDetailsAdapter(this, arrayPhotos);
        price = findViewById(R.id.tv_property_details_price);
        rooms = findViewById(R.id.tv_property_details_rooms);
        size = findViewById(R.id.tv_property_details_size);
        location = findViewById(R.id.tv_property_details_location);
        description = findViewById(R.id.tv_property_details_description);
//        direction = findViewById(R.id.tv_property_details_rooms);

//        goBackArrow.setOnClickListener(v -> {
//            finish();
//        });
    }

    private void loadPropertyData(String propertyId) {
        propertyService = ServiceGenerator.createService(PropertyService.class);
        Call<GetOneContainer<PropertyResponse>> call = propertyService.getProperty(propertyId);
        call.enqueue(new Callback<GetOneContainer<PropertyResponse>>() {
            @Override
            public void onResponse(Call<GetOneContainer<PropertyResponse>> call, Response<GetOneContainer<PropertyResponse>> response) {
                if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Request Error", Toast.LENGTH_SHORT).show();
                } else {
                    selectedProperty = response.body().getRows();
                    title.setTitle(selectedProperty.getTitle());
                    price.setText(String.valueOf(selectedProperty.getPrice()));
                    rooms.setText(String.valueOf(selectedProperty.getRooms()));
                    size.setText(String.valueOf(selectedProperty.getSize()));
                    description.setText(selectedProperty.getDescription());
                    location.setText(selectedProperty.getAddress());
                    arrayPhotos = selectedProperty.getPhotos();
                    float lng = Float.parseFloat(selectedProperty.getLoc().split(",")[0]);
                    float lat = Float.parseFloat(selectedProperty.getLoc().split(",")[1]);
                    options.camera(new CameraPosition(new LatLng(lng, lat), 0, 0, 0));
                    if (selectedProperty.isFav()) {
                        fabFav.setImageResource(R.drawable.ic_favorite_black_24dp);
                        isFav = true;
                    }
                    if (arrayPhotos.size() > 0)
                        Glide.with(getBaseContext()).load(arrayPhotos.get(arrayPhotos.size()-1)).into(coverImage);
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
    public void updateFav(View v) {
        propertyService = ServiceGenerator.createService(PropertyService.class, UtilToken.getToken(this), AuthType.JWT);
        if (selectedProperty.isFav()) {
            Call<UserResponse> call = propertyService.deleteAsFav(getIntent().getStringExtra("propertyId"));
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.code() != 200) {
                        Toast.makeText(getApplication(), "Request Error", Toast.LENGTH_SHORT).show();
                    } else {
                        fabFav.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                        isFav = !isFav;
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Toast.makeText(getApplication(), "Network Failure", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Call<UserResponse> call = propertyService.addAsFav(getIntent().getStringExtra("propertyId"));
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.code() != 200) {
                        Toast.makeText(getApplication(), "Request Error", Toast.LENGTH_SHORT).show();
                    } else {
                        fabFav.setImageResource(R.drawable.ic_favorite_black_24dp);
                        isFav = !isFav;
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Toast.makeText(getApplication(), "Network Failure", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
