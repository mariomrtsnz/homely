package com.mario.homely.ui.properties.listview;

import android.view.View;

import com.mario.homely.responses.PropertyResponse;

public interface PropertiesListListener {
    void onPropertyClick(View v, PropertyResponse property);
}
