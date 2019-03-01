package com.mario.homely.ui.properties;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.mario.homely.R;
import com.mario.homely.responses.PropertyResponse;
import com.mario.homely.responses.ResponseContainer;
import com.mario.homely.retrofit.generator.AuthType;
import com.mario.homely.retrofit.generator.ServiceGenerator;
import com.mario.homely.retrofit.services.PropertyService;
//import com.mario.homely.ui.properties.detail.PropertyDetailsActivity;
import com.mario.homely.util.UtilToken;

import java.util.Objects;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

public class PropertiesMapFragment extends Fragment implements OnMapReadyCallback {
    private static final int DEFAULT_ZOOM = 16;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final LatLng mDefaultLocation = new LatLng(37.3866245, -5.9942548);
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private String jwt;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        jwt = UtilToken.getToken(Objects.requireNonNull(getContext()));
        getLocationPermissions();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        checkDeviceLocation();

        View v = inflater.inflate(R.layout.content_main, container, false);
        v.findViewById(R.id.btn_show_myloc).setOnClickListener(view -> {
            if (checkDeviceLocation()) getDeviceLocation();
            else enableDeviceLocation();
        });
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));
        ((SupportMapFragment) Objects.requireNonNull(getChildFragmentManager().findFragmentById(R.id.map))).getMapAsync(this);
//        btnGetLocation();

        return v;
    }

    /**
     * Actions done when the map is loaded
     **/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getDeviceLocation();
        mapUIConfig();
    }

    /**
     * Check if location permissions are granted. If not, ask for it.
     **/
    private void getLocationPermissions() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(this.getActivity()),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }

    /**
     * Check if GPS is enabled
     **/
    private boolean checkDeviceLocation() {
        LocationManager service;
        service = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(LOCATION_SERVICE);
        return service.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Ask the user to activate GPS
     **/
    private void enableDeviceLocation() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        alertDialogBuilder.setTitle(R.string.gps_requirement)
                .setMessage(R.string.gps_requirement_title)
                .setPositiveButton(R.string.accept, (dialog, which) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Actions when click location Button. If GPS isn't activated, it won't give it.
     **/
    private void btnGetLocation() {
        Objects.requireNonNull(getView()).findViewById(R.id.btn_show_myloc).setOnClickListener(view -> {
            if (checkDeviceLocation()) getDeviceLocation();
            else enableDeviceLocation();
        });
    }

    /**
     * Try to get your mobile location.
     * If is enabled, show yours.
     * If not:
     * * Show your last location
     * * Show the default one.
     **/
    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(Objects.requireNonNull(this.getActivity()), task -> {
                    if (task.getResult() != null) {
                        mMap.setMyLocationEnabled(true);
                        mLastKnownLocation = task.getResult();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        showNearbyLocations(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                    } else if (mLastKnownLocation != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        showNearbyLocations(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                    } else {
                        mMap.setMyLocationEnabled(false);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        listProperties();
                    }
                });
            } else if (mLastKnownLocation != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(mLastKnownLocation.getLatitude(),
                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                showNearbyLocations(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                listProperties();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void listProperties() {
        if (jwt == null) {
            PropertyService service = ServiceGenerator.createService(PropertyService.class);
            Call<ResponseContainer<PropertyResponse>> call = service.listProperties();
            call.enqueue(new Callback<ResponseContainer<PropertyResponse>>() {
                @Override
                public void onResponse(Call<ResponseContainer<PropertyResponse>> call, Response<ResponseContainer<PropertyResponse>> response) {
                    if (response.code() != 200) {
                        Toast.makeText(getActivity(), "Request Error", Toast.LENGTH_SHORT).show();
                    } else {
                        mMap.clear();
                        for (PropertyResponse property : Objects.requireNonNull(response.body()).getRows()) {
                            if (!property.getLoc().isEmpty()) {
                                float lng = Float.parseFloat(property.getLoc().split(",")[0]);
                                float lat = Float.parseFloat(property.getLoc().split(",")[1]);
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lng, lat))
                                        .title(property.getTitle())
                                        .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_custom_marker))
                                ).setTag(property.getId());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseContainer<PropertyResponse>> call, Throwable t) {
                    Log.e("Network Failure", t.getMessage());
                    Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            PropertyService service = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);
            Call<ResponseContainer<PropertyResponse>> call = service.listPropertiesAuth();
            call.enqueue(new Callback<ResponseContainer<PropertyResponse>>() {
                @Override
                public void onResponse(Call<ResponseContainer<PropertyResponse>> call, Response<ResponseContainer<PropertyResponse>> response) {
                    if (response.code() != 200) {
                        Toast.makeText(getActivity(), "Request Error", Toast.LENGTH_SHORT).show();
                    } else {
                        mMap.clear();
                        for (PropertyResponse property : Objects.requireNonNull(response.body()).getRows()) {
                            if (!property.getLoc().isEmpty()) {
                                float lng = Float.parseFloat(property.getLoc().split(",")[0]);
                                float lat = Float.parseFloat(property.getLoc().split(",")[1]);
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lng, lat))
                                        .title(property.getTitle())
                                        .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_custom_marker))
                                ).setTag(property.getId());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseContainer<PropertyResponse>> call, Throwable t) {
                    Log.e("Network Failure", t.getMessage());
                    Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showNearbyLocations(double latitude, double longitude) {
        PropertyService service = ServiceGenerator.createService(PropertyService.class);
        String coords = longitude + "," + latitude;
        Call<ResponseContainer<PropertyResponse>> call = service.listPropertiesNearby(coords, 5000);

        call.enqueue(new Callback<ResponseContainer<PropertyResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseContainer<PropertyResponse>> call, @NonNull Response<ResponseContainer<PropertyResponse>> response) {
                if (response.code() != 200) {
                    Toast.makeText(getActivity(), "Request Error", Toast.LENGTH_SHORT).show();
                } else {
                    mMap.clear();
                    for (PropertyResponse property : Objects.requireNonNull(response.body()).getRows()) {
                        if (!property.getLoc().isEmpty()) {
                            float lng = Float.parseFloat(property.getLoc().split(",")[0]);
                            float lat = Float.parseFloat(property.getLoc().split(",")[1]);
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lng, lat))
                                    .title(property.getTitle())
                                    .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_custom_marker))
                            ).setTag(property.getId());
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseContainer<PropertyResponse>> call, @NonNull Throwable t) {
                Log.e("Network Failure", t.getMessage());
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Set the config of map Interface
     **/
    private void mapUIConfig() {
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Objects.requireNonNull(getContext()), R.raw.property_map_style));

        mMap.setOnMarkerClickListener(marker -> {
//            Intent propertyDetails = new Intent(this.getContext(), PropertyDetailsActivity.class);
//            propertyDetails.putExtra("propertyId", marker.getTag().toString());
//            startActivity(propertyDetails);
            return false;
        });
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
