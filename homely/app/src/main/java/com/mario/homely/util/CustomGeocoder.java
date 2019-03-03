package com.mario.homely.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;

public class CustomGeocoder {

    public static String getLoc(Context context, String address) throws IOException {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        double latitude = 0;
        double longitude = 0;
        addresses = geocoder.getFromLocationName(address, 1);
        if(addresses.size() > 0) {
            Address location = addresses.get(0);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        String loc = latitude +","+ longitude;
        return loc;

    }
}
