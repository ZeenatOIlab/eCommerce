package com.e.e_commerce_app.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.SessionManager;

public class SplashActivity extends AppCompatActivity {
    SessionManager sessionManager;
    String device_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager=new SessionManager(SplashActivity.this);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (
                        ( (sessionManager.getString("mobile").equalsIgnoreCase("") || sessionManager.getString("mobile").isEmpty())
                                && (sessionManager.getString("password").equalsIgnoreCase("") || sessionManager.getString("password").isEmpty()))
                                &&
                                ((sessionManager.getString("email").equalsIgnoreCase("") || sessionManager.getString("email").isEmpty())
                                        && (sessionManager.getString("passworde").equalsIgnoreCase("") || sessionManager.getString("passworde").isEmpty())
                                )
                )                {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                    finish();
                }

            }
        },3000);
    }

}
