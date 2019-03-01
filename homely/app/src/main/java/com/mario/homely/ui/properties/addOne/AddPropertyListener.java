package com.mario.homely.ui.properties.addOne;

public interface AddPropertyListener {
    void onAddSubmit(String title, String description, double price, int rooms, double size, String categoryId, String address, String zipcode, String city, String Province);
}
