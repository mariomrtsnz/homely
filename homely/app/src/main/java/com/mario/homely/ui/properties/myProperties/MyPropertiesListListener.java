package com.mario.homely.ui.properties.myProperties;

import android.view.View;

import com.mario.homely.responses.MyPropertiesResponse;
import com.mario.homely.responses.PropertyResponse;

public interface MyPropertiesListListener {
    void onPropertyClick(View v, MyPropertiesResponse property);
    void onPropertyDeleteClick(View v, MyPropertiesResponse property);
    void onPropertyEditClick(View v, MyPropertiesResponse property);
}
