package com.mario.homely.ui.common;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.mario.homely.R;
import com.mario.homely.responses.LoginResponse;
import com.mario.homely.retrofit.generator.ServiceGenerator;
import com.mario.homely.retrofit.services.LoginService;
import com.mario.homely.util.UtilToken;

import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Response;


public class LoginFragment extends Fragment {
    TextInputLayout email_input, password_input;
    Button btn_login;
    private Context ctx;
    private LoginListener mListener;


    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ctx = view.getContext();
        email_input = getActivity().findViewById(R.id.email_input);
        password_input = getActivity().findViewById(R.id.password_input);
        btn_login = getActivity().findViewById(R.id.btn_login_submit);

//        btn_login.setOnClickListener( (hey) -> {
//            String username_txt = email_input.getEditText().getText().toString();
//            String password_txt = password_input.getEditText().getText().toString();
//            final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
//
//            if (username_txt.equals("") || password_txt.equals("")) {
//                Toast.makeText(ctx, "Fields can't be empty!", Toast.LENGTH_LONG).show();
//            } else if (!EMAIL_REGEX.matcher(username_txt).matches()) {
//                Toast.makeText(ctx, "You need to use a correct email!", Toast.LENGTH_LONG).show();
//            } else if (password_txt.length() < 6) {
//                Toast.makeText(ctx, "Password must be at least 6 characters!", Toast.LENGTH_LONG).show();
//            } else {
//                String credentials = Credentials.basic(username_txt, password_txt);
//                LoginService service = ServiceGenerator.createService(LoginService.class);
//                Call<LoginResponse> call = service.doLogin(credentials);
//
//                call.enqueue(new retrofit2.Callback<LoginResponse>() {
//                    @Override
//                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                        if (response.code() != 201) {
//                            // error
//                            Log.e("Request Error", response.message());
//                            Toast.makeText(view.getContext(), "Error trying to login", Toast.LENGTH_SHORT).show();
//                        } else {
//                            // exito
//                            UtilToken.setToken(view.getContext(), response.body().getToken());
//                            UtilToken.setId(view.getContext(), response.body().getUser().get_id());
//                            startActivity(new Intent(view.getContext(), MainActivity.class));
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<LoginResponse> call, Throwable t) {
//                        Log.e("Network Failure", t.getMessage());
//                        Toast.makeText(view.getContext(), "Error. Can't connect to server", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginListener) {
            mListener = (LoginListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LoginListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
