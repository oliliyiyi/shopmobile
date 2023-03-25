package com.ssv.appsalephone.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import com.ssv.appsalephone.Home;
import com.ssv.appsalephone.R;

public class LoginFragment extends Fragment {




    // Activity
    private Home home;

    // View
    private View mView;

    private Button btnLogin;
    private EditText usernameEditText;
    private EditText passwordEditText;
    // endregion Variable


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login, container, false);
        initItem();
        setValueItem();
        return mView;

    }

    // region Private menthod

    // Khởi tạo các item
    private void initItem(){
        btnLogin = mView.findViewById(R.id.loginbtn);
        home = (Home) getActivity();
        usernameEditText = mView.findViewById(R.id.username);
        passwordEditText = mView.findViewById(R.id.password);



    }
    private void setValueItem(){


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SignIn();

                if (usernameEditText.getText().length() > 0 && passwordEditText.getText().length() > 0) {
                    String toastMessage = "Username: " + usernameEditText.getText().toString() + ", Password: " + passwordEditText.getText().toString();
                    Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
                    home.toCartFragment();

                } else {
                    String toastMessage = "Username or Password are not populated";
                    Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();

                }
            }
        });


    }



}