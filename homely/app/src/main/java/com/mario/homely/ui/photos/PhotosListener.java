package com.mario.homely.ui.photos;

import android.view.View;

public interface PhotosListener {
    void deleteImage(View v, String deletehash);
    void addImage(String propertyId);
}
