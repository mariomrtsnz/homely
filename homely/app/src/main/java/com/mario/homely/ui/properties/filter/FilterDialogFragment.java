package com.mario.homely.ui.properties.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mario.homely.R;

import androidx.fragment.app.DialogFragment;

public class FilterDialogFragment extends DialogFragment {
    private EditText etRooms, etCity, etProvince, etZipcode, etMinSize, etMaxSize, etMinPrice, etMaxPrice, etAddress;
    private int rooms, minSize, maxSize, minPrice, maxPrice;
    private String city, province, zipcode, address;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter_dialog, container, false);

        // Do all the stuff to initialize your custom view


        return v;
    }
}
