package com.mario.homely.ui.properties.detail;

import android.content.Context;

import java.util.List;

public class PropertyDetailsAdapter {
    private String[] photosArray;

    public PropertyDetailsAdapter(Context ctx, List<String> data, PropertiesDetailsListener mListener) {
        this.data = data;
        this.context = ctx;
        this.mListener = mListener;
    }
}
