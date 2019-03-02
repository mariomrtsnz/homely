package com.mario.homely.ui.properties.addOne;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.mario.homely.R;
import com.mario.homely.util.CustomGeocoder;

import java.io.IOException;

import androidx.fragment.app.Fragment;

public class AddOnePropertyFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnAdd;
    private EditText etTitle, etDescription, etPrice, etRooms, etSize, etCategoryId, etAddress, etZipcode, etCity, etProvince;
    private String title, description, categoryId, address, zipcode, city, province;
    private double price;
    private int rooms;
    private float size;
    private GoogleMap mMap;
    SupportPlaceAutocompleteFragment placeAutoComplete;


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
        etTitle = layout.findViewById(R.id.et_add_one_property_title);
        etDescription = layout.findViewById(R.id.et_add_one_property_description);
        etPrice = layout.findViewById(R.id.et_add_one_property_price);
        etRooms = layout.findViewById(R.id.et_add_one_property_rooms);
        etSize = layout.findViewById(R.id.et_add_one_property_size);
        btnAdd.setOnClickListener(v -> {
            title = etTitle.getText().toString();
            description = etDescription.getText().toString();
            price = Double.parseDouble(etPrice.getText().toString());
            rooms = Integer.parseInt(etRooms.getText().toString());
            size = Float.parseFloat(etSize.getText().toString());
            if (title.isEmpty() || description.isEmpty() ) {
                Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_LONG);
            } else
                mListener.onAddSubmit(title, description, price, rooms, size, categoryId, address, zipcode, city, province);
        });
        return layout;
    }

    private String geocode(String address) throws IOException {
        String loc = CustomGeocoder.getLoc(getContext(), address);
        return loc;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddPropertyListener) {
            mListener = (AddPropertyListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddPropertyListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
