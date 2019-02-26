package com.mario.homely.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mario.homely.R;
import com.mario.homely.responses.UserLoginResponse;
import com.mario.homely.responses.UserResponse;
import com.mario.homely.retrofit.generator.ServiceGenerator;
import com.mario.homely.retrofit.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UtilToken {
    public static void setToken(Context mContext, String token) {
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(
                        mContext.getString(R.string.sharedpreferences_filename),
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(mContext.getString(R.string.jwt_key), token);
        editor.commit();
    }

    public static String getId(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                mContext.getString(R.string.sharedpreferences_filename),
                Context.MODE_PRIVATE
        );

        String id = sharedPreferences
                .getString(mContext.getString(R.string.userId), null);

        return id;
    }

    public static void setId(Context mContext, String id) {
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(
                        mContext.getString(R.string.sharedpreferences_filename),
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(mContext.getString(R.string.userId), id);
        editor.commit();
    }

    public static void setUserLoggedData(Context mContext, UserLoginResponse loggedUser) {
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(
                        mContext.getString(R.string.sharedpreferences_filename),
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (loggedUser != null) {
            editor.putString(mContext.getString(R.string.userName), loggedUser.getName());
            editor.putString(mContext.getString(R.string.userEmail), loggedUser.getEmail());
        } else {
            editor.putString(mContext.getString(R.string.userName), "");
            editor.putString(mContext.getString(R.string.userEmail), "");
        }
        editor.commit();
    }

    public static String getEmail(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                mContext.getString(R.string.sharedpreferences_filename),
                Context.MODE_PRIVATE
        );

        String userEmail = sharedPreferences
                .getString(mContext.getString(R.string.userEmail), null);

        return userEmail;
    }

    public static String getName(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                mContext.getString(R.string.sharedpreferences_filename),
                Context.MODE_PRIVATE
        );

        String userName = sharedPreferences
                .getString(mContext.getString(R.string.userName), null);

        return userName;
    }

    public static String getToken(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                mContext.getString(R.string.sharedpreferences_filename),
                Context.MODE_PRIVATE
        );

        String jwt = sharedPreferences
                .getString(mContext.getString(R.string.jwt_key), null);

        return jwt;
    }
}
