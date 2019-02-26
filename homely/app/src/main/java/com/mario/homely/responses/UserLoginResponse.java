package com.mario.homely.responses;

public class UserLoginResponse {
    private String id;
    private String email;
    private String name;
    private String picture;

    public String get_id() {
        return id;
    }

    public void set_id(String _id) {
        this.id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
