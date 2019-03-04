package com.mario.homely.ui.properties.addOne;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.mario.homely.R;
import com.mario.homely.dto.PropertyDto;
import com.mario.homely.responses.CategoryResponse;
import com.mario.homely.responses.MyPropertiesResponse;
import com.mario.homely.responses.ResponseContainer;
import com.mario.homely.retrofit.generator.ServiceGenerator;
import com.mario.homely.retrofit.services.CategoryService;
import com.mario.homely.util.CustomGeocoder;
import com.mario.homely.util.UtilToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOnePropertyFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnAdd;
    private Spinner spinnerCategories;
    private EditText etTitle, etDescription, etPrice, etRooms, etSize, etCategoryId, etAddress, etZipcode, etCity, etProvince;
    private String title, description, categoryId, address, zipcode, city, province, loc;
    private double price;
    private int rooms;
    private float size;
    private GoogleMap mMap;
    SupportPlaceAutocompleteFragment placeAutoComplete;
    private MyPropertiesResponse myProperty;
    private List<CategoryResponse> categories = new ArrayList<>();
    private String jwt;
    private Context ctx;


    private AddPropertyListener mListener;

    public AddOnePropertyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddOnePropertyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddOnePropertyFragment newInstance(String param1, String param2) {
        AddOnePropertyFragment fragment = new AddOnePropertyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            myProperty = (MyPropertiesResponse) getArguments().getSerializable("myProperty");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_add_one_property, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.add_one_property_map);
        mapFragment.getMapAsync(this);
        btnAdd = layout.findViewById(R.id.btn_add_one_property_submit);
        if (myProperty != null)
            btnAdd.setText("Edit");
        etTitle = layout.findViewById(R.id.et_add_one_property_title);
        etDescription = layout.findViewById(R.id.et_add_one_property_description);
        etPrice = layout.findViewById(R.id.et_add_one_property_price);
        etRooms = layout.findViewById(R.id.et_add_one_property_rooms);
        etSize = layout.findViewById(R.id.et_add_one_property_size);
        etAddress = layout.findViewById(R.id.et_add_one_property_address);
        etZipcode = layout.findViewById(R.id.et_add_one_property_zipcode);
        etCity = layout.findViewById(R.id.et_add_one_property_city);
        etProvince = layout.findViewById(R.id.et_add_one_property_province);
        spinnerCategories = layout.findViewById(R.id.spinner_categories);
        loadCategories();
        if (myProperty != null) {
            etTitle.setText(myProperty.getTitle());
            etDescription.setText(myProperty.getDescription());
            etPrice.setText(String.valueOf(myProperty.getPrice()));
            etRooms.setText(String.valueOf(myProperty.getRooms()));
            etSize.setText(String.valueOf(myProperty.getSize()));
            etAddress.setText(myProperty.getAddress());
            etZipcode.setText(myProperty.getZipcode());
            etCity.setText(myProperty.getCity());
            etProvince.setText(myProperty.getProvince());
//            spinnerCategories.setSelection(myProperty.getCategoryId());
        }
        btnAdd.setOnClickListener(v -> addProperty());
        return layout;
    }

    private void addProperty() {
        title = etTitle.getText().toString();
        description = etDescription.getText().toString();
        address = etAddress.getText().toString();
        zipcode = etZipcode.getText().toString();
        city = etCity.getText().toString();
        province = etProvince.getText().toString();
        CategoryResponse selectedCategory = (CategoryResponse) spinnerCategories.getSelectedItem();
        categoryId = selectedCategory.getId();
        if (title.isEmpty() || description.isEmpty() || etPrice.getText().toString().isEmpty() || etRooms.getText().toString().isEmpty() || etSize.getText().toString().isEmpty() || address.isEmpty() || zipcode.isEmpty() || city.isEmpty() || province.isEmpty()) {
            Toast.makeText(ctx, "All fields are required!", Toast.LENGTH_LONG).show();
        } else {
            try {
                String fullAddress = address + "," + zipcode + "," + city + "," + province;
                loc = geocode(fullAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
            price = Double.parseDouble(etPrice.getText().toString());
            rooms = Integer.parseInt(etRooms.getText().toString());
            size = Float.parseFloat(etSize.getText().toString());
            if (myProperty != null) {
                PropertyDto propertyEditDto = new PropertyDto(title, description, price, rooms, size, categoryId, address, zipcode, city, province, loc);
                mListener.onEditSubmit(propertyEditDto, myProperty.getId());
            } else {
                PropertyDto propertyDto = new PropertyDto(title, description, price, rooms, size, categoryId, address, zipcode, city, province, loc);
                mListener.onAddSubmit(propertyDto);
            }
        }
    }

    private void loadCategories() {
        CategoryService categoryService = ServiceGenerator.createService(CategoryService.class);
        Call<ResponseContainer<CategoryResponse>> callC = categoryService.listCategories();

        callC.enqueue(new Callback<ResponseContainer<CategoryResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainer<CategoryResponse>> call, Response<ResponseContainer<CategoryResponse>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ctx, "Request Error", Toast.LENGTH_SHORT).show();
                } else {
                    categories = response.body().getRows();
                    ArrayAdapter<CategoryResponse> adapter = new ArrayAdapter<>(ctx, android.R.layout.simple_spinner_dropdown_item, categories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategories.setAdapter(adapter);
                    spinnerCategories.setSelection(categories.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<CategoryResponse>> call, Throwable t) {
                Toast.makeText(ctx, "Network Failure", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String geocode(String address) throws IOException {
        String loc = CustomGeocoder.getLoc(getContext(), address);
        return loc;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx = context;
        if (context instanceof AddPropertyListener) {
            mListener = (AddPropertyListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddPropertyListener");
        }
        jwt = UtilToken.getToken(context);
//        if (jwt == null) {
//            this.finalize();
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
