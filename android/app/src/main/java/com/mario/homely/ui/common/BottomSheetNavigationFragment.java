package com.mario.homely.ui.common;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.mario.homely.R;
import com.mario.homely.util.UtilToken;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * Created by sonu on 17:07, 10/01/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class BottomSheetNavigationFragment extends BottomSheetDialogFragment {


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

        if (UtilToken.getToken(getContext()) == null) {
            contentView.findViewById(R.id.user_email).setVisibility(View.GONE);
            contentView.findViewById(R.id.user_name).setVisibility(View.GONE);
            contentView.findViewById(R.id.profile_image).setVisibility(View.GONE);
            contentView.findViewById(R.id.divider_one).setVisibility(View.GONE);
            navigationView.getMenu().findItem(R.id.bottomdrawer_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.bottomdrawer_signup).setVisible(true);
        } else {
            navigationView.getMenu().findItem(R.id.bottomdrawer_my_properties).setVisible(true);
            navigationView.getMenu().findItem(R.id.bottomdrawer_favourites).setVisible(true);
            navigationView.getMenu().findItem(R.id.bottomdrawer_my_profile).setVisible(true);
            navigationView.getMenu().findItem(R.id.bottomdrawer_logout).setVisible(true);
        }

        //implement navigation menu item click event
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottomdrawer_login:
                        Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                        loginIntent.putExtra("isLogin", true);
                        startActivity(loginIntent);
                        break;
                    case R.id.bottomdrawer_signup:
                        Intent signupIntent = new Intent(getContext(), LoginActivity.class);
                        signupIntent.putExtra("isLogin", false);
                        startActivity(signupIntent);
                        break;
                    case R.id.bottomdrawer_my_properties:
                        break;
                    case R.id.bottomdrawer_favourites:
                        break;
                    case R.id.bottomdrawer_my_profile:
                        break;
                    case R.id.bottomdrawer_logout:
                        break;
                }
                return false;
            }
        });
        closeButton = contentView.findViewById(R.id.close_image_view);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dismiss bottom sheet
                dismiss();
            }
        });

        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        //Set callback
        if (behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

}
