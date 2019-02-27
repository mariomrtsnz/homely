package com.mario.homely.ui.common.main;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.mario.homely.R;
import com.mario.homely.responses.UserLoginResponse;
import com.mario.homely.responses.UserResponse;
import com.mario.homely.ui.common.login.LoginActivity;
import com.mario.homely.ui.user.MyProfileFragment;
import com.mario.homely.util.UtilToken;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import okhttp3.internal.Util;

/**
 * Created by sonu on 17:07, 10/01/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class BottomSheetNavigationFragment extends BottomSheetDialogFragment {

    private TextView loggedUsername, loggedEmail;
    private UserLoginResponse userLogged;

    public static BottomSheetNavigationFragment newInstance() {

        Bundle args = new Bundle();

        BottomSheetNavigationFragment fragment = new BottomSheetNavigationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //Bottom Sheet Callback
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            //check the slide offset and change the visibility of close button
            if (slideOffset > 0.5) {
                closeButton.setVisibility(View.VISIBLE);
            } else {
                closeButton.setVisibility(View.GONE);
            }
        }
    };

    private ImageView closeButton;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        //Get the content View
        View contentView = View.inflate(getContext(), R.layout.bottom_navigation_drawer, null);
        dialog.setContentView(contentView);

        NavigationView navigationView = contentView.findViewById(R.id.navigation_view);

        loggedUsername = contentView.findViewById(R.id.user_name);
        loggedEmail = contentView.findViewById(R.id.user_email);

        if (UtilToken.getToken(getContext()) != null) {
            userLogged = new UserLoginResponse();
            getUserData();
            loggedEmail.setVisibility(View.VISIBLE);
            loggedEmail.setText(userLogged.getEmail());
            loggedUsername.setVisibility(View.VISIBLE);
            loggedUsername.setText(userLogged.getName());
            contentView.findViewById(R.id.profile_image).setVisibility(View.VISIBLE);
            contentView.findViewById(R.id.divider_one).setVisibility(View.VISIBLE);
            navigationView.getMenu().findItem(R.id.bottomdrawer_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.bottomdrawer_signup).setVisible(false);
            navigationView.getMenu().findItem(R.id.bottomdrawer_my_properties).setVisible(true);
            navigationView.getMenu().findItem(R.id.bottomdrawer_favourites).setVisible(true);
            navigationView.getMenu().findItem(R.id.bottomdrawer_my_profile).setVisible(true);
            navigationView.getMenu().findItem(R.id.bottomdrawer_logout).setVisible(true);
        }

        //implement navigation menu item click event
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottomdrawer_login:
                    Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                    loginIntent.putExtra("isLogin", true);
                    startActivity(loginIntent);
                    dismiss();
                    return true;
                case R.id.bottomdrawer_signup:
                    Intent signupIntent = new Intent(getContext(), LoginActivity.class);
                    signupIntent.putExtra("isLogin", false);
                    startActivity(signupIntent);
                    dismiss();
                    return true;
                case R.id.bottomdrawer_my_properties:
                    return true;
                case R.id.bottomdrawer_favourites:
                    return true;
                case R.id.bottomdrawer_my_profile:
                    getFragmentManager().beginTransaction().replace(R.id.contenedor, new MyProfileFragment()).commit();
                    dismiss();
                    return true;
                case R.id.bottomdrawer_logout:
                    logout();
                    return true;
            }
            return false;
        });
        closeButton = contentView.findViewById(R.id.close_image_view);
        closeButton.setOnClickListener(view -> {
            //dismiss bottom sheet
            dismiss();
        });

        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        //Set callback
        if (behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    private void logout() {
        UtilToken.setId(getContext(), null);
        UtilToken.setToken(getContext(), null);
        UtilToken.setUserLoggedData(getContext(), null);
        Intent logoutIntent = new Intent(getContext(), LoginActivity.class);
        logoutIntent.putExtra("isLogin", true);
        startActivity(logoutIntent);
        dismiss();
    }

    private void getUserData() {
        userLogged.setName(UtilToken.getName(getContext()));
        userLogged.setEmail(UtilToken.getEmail(getContext()));
    }

}
