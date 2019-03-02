package com.mario.homely.ui.properties.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mario.homely.R;
import com.mario.homely.ui.properties.listview.PropertiesListFragment;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.DialogFragment;

public class FilterDialogFragment extends DialogFragment {
    private EditText etRooms, etCity, etProvince, etZipcode, etMinSize, etMaxSize, etMinPrice, etMaxPrice, etAddress;
    private String city, province, zipcode, address, rooms, minSize, maxSize, minPrice, maxPrice;;
    private Button btnApplyFilters;
    Map<String, String> options = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter_dialog, container, false);
        btnApplyFilters = v.findViewById(R.id.btn_apply_filters);
        etRooms = v.findViewById(R.id.et_filters_rooms);
        etCity = v.findViewById(R.id.et_filters_city);
        etProvince = v.findViewById(R.id.et_filters_province);
        etZipcode = v.findViewById(R.id.et_filters_zipcode);
        etMinSize = v.findViewById(R.id.et_filters_minSize);
        etMaxSize = v.findViewById(R.id.et_filters_maxSize);
        etMinPrice = v.findViewById(R.id.et_filters_minPrice);
        etMaxPrice = v.findViewById(R.id.et_filters_maxPrice);
//        etAddress = v.findViewById(R.id.et_filters_address);
        btnApplyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rooms = etRooms.getText().toString();
                city = etCity.getText().toString();
                province = etProvince.getText().toString();
                zipcode = etZipcode.getText().toString();
                minSize = etMinSize.getText().toString();
                maxSize = etMaxSize.getText().toString();
                minPrice = etMinPrice.getText().toString();
                maxPrice = etMaxPrice.getText().toString();
                if (!rooms.isEmpty())
                    options.put("rooms", rooms);
                if (!city.isEmpty())
                    options.put("city", city);
                if (!province.isEmpty())
                    options.put("province", province);
                if (!zipcode.isEmpty())
                    options.put("zipcode", zipcode);
                if (!minSize.isEmpty())
                    options.put("min_size", minSize);
                if (!maxSize.isEmpty())
                    options.put("max_size", maxSize);
                if (!minPrice.isEmpty())
                    options.put("min_price", minPrice);
                if (!maxPrice.isEmpty())
                    options.put("max_price", maxPrice);

                getFragmentManager().beginTransaction().replace(R.id.contenedor, new PropertiesListFragment(options)).commit();
            }
        });
        return v;
    }
}
