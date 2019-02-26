package com.mario.homely.dto;

public class PhotoDto {
    private String id;
    private String propertyId;
    private String deletehash;

    public PhotoDto(String id, String propertyId, String deletehash) {
        this.id = id;
        this.propertyId = propertyId;
        this.deletehash = deletehash;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getDeletehash() {
        return deletehash;
    }

    public void setDeletehash(String deletehash) {
        this.deletehash = deletehash;
    }

    @Override
    public String toString() {
        return "PhotoDto{" +
                "id='" + id + '\'' +
                ", propertyId='" + propertyId + '\'' +
                ", deletehash='" + deletehash + '\'' +
                '}';
    }
}
