package com.mario.homely.ui.properties.favs;

import android.view.View;

import com.mario.homely.responses.PropertyResponse;

public interface MyFavsListListener {
    void onPropertyClick(View v, PropertyResponse property);
}
