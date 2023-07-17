package com.android.restaurentuserapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.restaurentuserapp.activities.MainActivity;

public class BaseActivity extends AppCompatActivity
{
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public final void startNewActivity(Context context, Activity activity) {
        this.startActivity(new Intent(context, activity.getClass()));
    }

    public final void showToast( Context context,  String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public final void logoutFromApp(Activity context)
    {
        try{
            context.finishAffinity();
            new PrefHelper(context).putBoolean(Constants.LOGIN_STATUS , false);
            Intent intent =new  Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        catch (Exception exp){}
    }
}