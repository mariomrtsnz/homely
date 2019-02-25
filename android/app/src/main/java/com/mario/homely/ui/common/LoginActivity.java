package com.mario.homely.ui.common;

import android.os.Bundle;
import android.widget.TextView;

import com.mario.homely.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class LoginActivity extends AppCompatActivity implements LoginListener, SignupListener{
    FragmentTransaction fragmentChanger;
    private Fragment login, signup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = new LoginFragment();
        signup = new SignUpFragment();
        if (getIntent().getBooleanExtra("isLogin", true)) {
            fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.login_container, login);
            fragmentChanger.commit();
        } else {
            fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.login_container, signup);
            fragmentChanger.commit();
        }
    }

    @Override
    public void logInButtonPressed() {

    }

    @Override
    public void goToSignUpPressed() {

    }

    @Override
    public void signUpButtonPressed() {

    }

    @Override
    public void goToLoginPressed() {

    }
}
