package com.qtthai.app_ban_hang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.qtthai.app_ban_hang.Fragments.SignInFragment;
import com.qtthai.app_ban_hang.R;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new SignInFragment()).commit();
    }
}
