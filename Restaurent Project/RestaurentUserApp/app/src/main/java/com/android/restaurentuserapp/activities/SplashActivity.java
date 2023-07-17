package com.android.restaurentuserapp.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import com.android.restaurentuserapp.R;
import com.android.restaurentuserapp.utils.BaseActivity;
import com.google.firebase.FirebaseApp;

public class SplashActivity extends BaseActivity
{
    private static final int SPLASH_SCREEN_TIME_OUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(SplashActivity.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startNewActivity(SplashActivity.this , new MainActivity());
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}
