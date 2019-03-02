package com.mario.homely.ui.properties.myProperties;

import android.view.View;

import com.mario.homely.responses.PropertyResponse;

public interface MyPropertiesListListener {
    void onPropertyClick(View v, PropertyResponse property);
}
