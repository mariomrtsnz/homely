package com.mario.homely.ui.common.main;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mario.homely.R;
import com.mario.homely.dto.PropertyDto;
import com.mario.homely.responses.CreatedPropertyResponse;
import com.mario.homely.responses.MyPropertiesResponse;
import com.mario.homely.responses.PhotoUploadResponse;
import com.mario.homely.responses.PropertyResponse;
import com.mario.homely.responses.UserResponse;
import com.mario.homely.retrofit.generator.AuthType;
import com.mario.homely.retrofit.generator.ServiceGenerator;
import com.mario.homely.retrofit.services.PhotoService;
import com.mario.homely.retrofit.services.PropertyService;
import com.mario.homely.ui.photos.PhotosFragment;
import com.mario.homely.ui.photos.PhotosListener;
import com.mario.homely.ui.properties.PropertiesMapFragment;
import com.mario.homely.ui.properties.addOne.AddOnePropertyFragment;
import com.mario.homely.ui.properties.addOne.AddPropertyListener;
import com.mario.homely.ui.properties.detail.PropertyDetailsActivity;
import com.mario.homely.ui.properties.favs.MyFavsListListener;
import com.mario.homely.ui.properties.filter.FilterDialogFragment;
import com.mario.homely.ui.properties.listview.PropertiesListFragment;
import com.mario.homely.ui.properties.listview.PropertiesListListener;
import com.mario.homely.ui.properties.myProperties.MyPropertiesListFragment;
import com.mario.homely.ui.properties.myProperties.MyPropertiesListListener;
import com.mario.homely.ui.user.MyProfileListener;
import com.mario.homely.util.UtilToken;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PropertiesListListener, MyProfileListener, AddPropertyListener, MyFavsListListener, MyPropertiesListListener, PhotosListener {

    private BottomAppBar bottomAppBar;
    private boolean isInMap = true;
    FloatingActionButton fabMap;
    FragmentTransaction fragmentChanger;
    private static final int OPEN_DOCUMENT_CODE = 2;
    private CreatedPropertyResponse activeProperty;
    private String activePropertyId;
    private ProgressBar progressBar;
    private Uri imageUri;
    private ProgressDialog pgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpBottomAppBar();
        fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new PropertiesListFragment());
        fragmentChanger.commit();
        fabMap = findViewById(R.id.fab_map);
        fabMap.setOnClickListener(view -> {
            if (isInMap) {
                Objects.requireNonNull(getSupportFragmentManager()).beginTransaction()
                        .replace(R.id.contenedor, new PropertiesMapFragment())
                        .commit();
//                fabMap.animate().setDuration(500).rotationY(180);
                fabMap.setImageResource(R.drawable.ic_view_list_white_24dp);
                isInMap = !isInMap;
            } else {
                Objects.requireNonNull(getSupportFragmentManager()).beginTransaction()
                        .replace(R.id.contenedor, new PropertiesListFragment())
                        .commit();
//                fabMap.animate().setDuration(500).rotationY(0);
                fabMap.setImageResource(R.drawable.ic_map_white_24dp);
                isInMap = !isInMap;
            }
        });
    }

    private void setUpBottomAppBar() {
        //find id
        bottomAppBar = findViewById(R.id.bar);

        //set bottom bar to Action bar as it is similar like Toolbar
        setSupportActionBar(bottomAppBar);

        //click event over Bottom bar menu item
        bottomAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.btn_add:
                    Objects.requireNonNull(getSupportFragmentManager()).beginTransaction()
                            .replace(R.id.contenedor, new AddOnePropertyFragment()).addToBackStack(null)
                            .commit();
                    return true;
                case R.id.btn_filter:
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    DialogFragment dialogFragment = new FilterDialogFragment();
                    dialogFragment.show(ft, "dialog");
                    return true;
            }
            return false;
        });

        //click event over navigation menu like back arrow or hamburger icon
        bottomAppBar.setNavigationOnClickListener(view -> {
            //open bottom sheet
            BottomSheetDialogFragment bottomSheetDialogFragment = BottomSheetNavigationFragment.newInstance();
            bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
        });
    }

    //Inflate menu to bottom bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO: Comprobar que si el token está nulo no salga la opción de añadir una propiedad.
        if (UtilToken.getToken(this) != null)
            getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPropertyClick(View v, PropertyResponse property) {
        Intent i = new Intent(this, PropertyDetailsActivity.class);
        i.putExtra("propertyId", property.getId());
        i.putExtra("isFav", property.isFav());
        startActivity(i);
    }

    /**
     * MyPropertiesListener
     * @param v
     * @param property
     */

    @Override
    public void onPhotosClick(View v, MyPropertiesResponse property) {
        Fragment photosFragment = new PhotosFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("propertyPhotos", new ArrayList<>(property.getPhotos()));
        bundle.putString("propertyId", property.getId());
        photosFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, photosFragment).addToBackStack(null).commit();
    }

    @Override
    public void addImage(String propertyId) {
        activePropertyId = propertyId;
        getPhotoAndUpload();
    }

    @Override
    public void onPropertyClick(View v, MyPropertiesResponse property) {
        Intent i = new Intent(this, PropertyDetailsActivity.class);
        i.putExtra("propertyId", property.getId());
        startActivity(i);
    }

    @Override
    public void onPropertyDeleteClick(View v, MyPropertiesResponse property) {
        PropertyService propertyService = ServiceGenerator.createService(PropertyService.class, UtilToken.getToken(this), AuthType.JWT);
        Call<UserResponse> call = propertyService.deleteProperty(property.getId());
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() != 204) {
                    Toast.makeText(getApplicationContext(), "Request Error", Toast.LENGTH_SHORT).show();
                } else {
                    Objects.requireNonNull(getSupportFragmentManager()).beginTransaction()
                            .replace(R.id.contenedor, new MyPropertiesListFragment())
                            .commit();
                    Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPropertyEditClick(View v, MyPropertiesResponse property) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("myProperty", property);
        AddOnePropertyFragment editOneProperty = new AddOnePropertyFragment();
        editOneProperty.setArguments(bundle);
        Objects.requireNonNull(getSupportFragmentManager()).beginTransaction()
                .replace(R.id.contenedor, editOneProperty)
                .commit();
    }

    @Override
    public void onAddSubmit(PropertyDto propertyDto) {
        // TODO: From latlng of marker clicked on map get rest of data (reverse geocoding).
        // TODO: Do call
        PropertyService propertyService = ServiceGenerator.createService(PropertyService.class, UtilToken.getToken(this), AuthType.JWT);
        Call<CreatedPropertyResponse> call = propertyService.createProperty(propertyDto);
        call.enqueue(new Callback<CreatedPropertyResponse>() {
            @Override
            public void onResponse(Call<CreatedPropertyResponse> call, Response<CreatedPropertyResponse> response) {
                if (response.code() != 201) {
                    Toast.makeText(getApplicationContext(), "Request Error", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(getString(R.string.add_one_property_photos_alert_title));

                    String positiveText = getString(android.R.string.ok);
                    builder.setPositiveButton(positiveText, (dialog, which) -> {
                        activeProperty = response.body();
                        getPhotoAndUpload();
                        pgDialog = new ProgressDialog(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        pgDialog.setIndeterminate(true);
                        pgDialog.setCancelable(false);
                        pgDialog.setTitle("Creating data");
                        pgDialog.show();
                    });

                    String negativeText = getString(android.R.string.cancel);
                    builder.setNegativeButton(negativeText, (dialog, which) -> {
                        dialog.dismiss();
                        Objects.requireNonNull(getSupportFragmentManager()).beginTransaction()
                                .replace(R.id.contenedor, new MyPropertiesListFragment())
                                .commit();
                        Toast.makeText(getApplicationContext(), "Created Successfully", Toast.LENGTH_SHORT).show();
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<CreatedPropertyResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPhotoAndUpload() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_DOCUMENT_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == OPEN_DOCUMENT_CODE && resultCode == RESULT_OK) {
            if (resultData != null) {
                Uri imageUri = resultData.getData();
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    int cantBytes;
                    byte[] buffer = new byte[1024 * 4];
                    try {
                        while ((cantBytes = bufferedInputStream.read(buffer, 0, 1024 * 4)) != -1) {
                            baos.write(buffer, 0, cantBytes);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), baos.toByteArray());

                    MultipartBody.Part body = MultipartBody.Part.createFormData("photo", "photo", requestFile);

                    RequestBody propertyId;
                    if (activeProperty != null)
                        propertyId = RequestBody.create(MultipartBody.FORM, activeProperty.getId());
                    else
                        propertyId = RequestBody.create(MultipartBody.FORM, activePropertyId);

                    PhotoService servicePhoto = ServiceGenerator.createService(PhotoService.class, UtilToken.getToken(this), AuthType.JWT);
                    Call<PhotoUploadResponse> callPhoto = servicePhoto.upload(body, propertyId);
                    callPhoto.enqueue(new Callback<PhotoUploadResponse>() {
                        @Override
                        public void onResponse(Call<PhotoUploadResponse> call, Response<PhotoUploadResponse> response) {
                            if (response.isSuccessful()) {
//                                activeProperty.getPhotos().add(Objects.requireNonNull(response.body()).getId());
                                Objects.requireNonNull(getSupportFragmentManager()).beginTransaction()
                                        .replace(R.id.contenedor, new MyPropertiesListFragment())
                                        .commit();
                                if (pgDialog != null)
                                    pgDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Created With Photo Successfully", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<PhotoUploadResponse> call, Throwable t) {
                            Log.e("Upload error", t.getMessage());
                        }
                    });
            }
        }
    }

    @Override
    public void onEditSubmit(PropertyDto propertyEditDto, String myPropertyEditId) {
        // TODO: From latlng of marker clicked on map get rest of data (reverse geocoding).
        PropertyService propertyService = ServiceGenerator.createService(PropertyService.class, UtilToken.getToken(this), AuthType.JWT);
        Call<CreatedPropertyResponse> call = propertyService.editProperty(myPropertyEditId, propertyEditDto);
        call.enqueue(new Callback<CreatedPropertyResponse>() {
            @Override
            public void onResponse(Call<CreatedPropertyResponse> call, Response<CreatedPropertyResponse> response) {
                if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Request Error", Toast.LENGTH_SHORT).show();
                } else {
                    Objects.requireNonNull(getSupportFragmentManager()).beginTransaction()
                            .replace(R.id.contenedor, new MyPropertiesListFragment())
                            .commit();
                    Toast.makeText(getApplicationContext(), "Edited Successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreatedPropertyResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void deleteImage(View v, String deletehash) {

    }
}