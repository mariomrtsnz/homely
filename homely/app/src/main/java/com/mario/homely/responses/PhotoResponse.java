package com.mario.homely.responses;

public class PhotoResponse {
    private String id;
    private PropertyResponse propertyId;
    private String imgurlink;
    private String deletehash;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PropertyResponse getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(PropertyResponse propertyId) {
        this.propertyId = propertyId;
    }

    public String getDeletehash() {
        return deletehash;
    }

    public void setDeletehash(String deletehash) {
        this.deletehash = deletehash;
    }

    public String getImgurlink() {
        return imgurlink;
    }

    public void setImgurlink(String imgurlink) {
        this.imgurlink = imgurlink;
    }

    @Override
    public String toString() {
        return "PhotoResponse{" +
                "id='" + id + '\'' +
                ", propertyId='" + propertyId + '\'' +
                ", deletehash='" + deletehash + '\'' +
                '}';
    }
}
