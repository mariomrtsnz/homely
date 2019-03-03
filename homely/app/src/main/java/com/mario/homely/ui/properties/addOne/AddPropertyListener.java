package com.mario.homely.ui.properties.addOne;

import com.mario.homely.dto.PropertyDto;

public interface AddPropertyListener {
    void onAddSubmit(PropertyDto propertyDto);
    void onEditSubmit(PropertyDto propertyEditDto, String myPropertyEditId);
}
