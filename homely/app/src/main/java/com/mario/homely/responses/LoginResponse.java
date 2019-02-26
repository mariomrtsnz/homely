package com.mario.homely.responses;

public class LoginResponse {
    String token;
    UserLoginResponse user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserLoginResponse getUser() {
        return user;
    }

    public void setUser(UserLoginResponse user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", user=" + user +
                '}';
    }
}
