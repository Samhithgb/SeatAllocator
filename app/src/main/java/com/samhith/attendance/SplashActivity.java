package com.samhith.attendance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (isConnectingToInternet()) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    boolean isLoggedIn = pref.getBoolean("login", false);
                    //  If the activity has never started before...
                    if (isLoggedIn) {
                        Toast.makeText(SplashActivity.this,"Logged in as "+pref.getString("usn",""),Toast.LENGTH_LONG).show();
                        Intent i = new Intent(SplashActivity.this, LandingScreen.class);
                        i.putExtra("usn",pref.getString("usn",""));
                        startActivity(i);
                        finish();

                    } else {
                        Intent i = new Intent(SplashActivity.this, Login.class);
                        startActivity(i);
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);
        }
        else {
            Toast.makeText(SplashActivity.this,"No Internet! Please connect and try again!",Toast.LENGTH_LONG).show();
            finish();
        }
    }


    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) SplashActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) { //Returns True if Device has Internet Connection
                        return true;
                    }
        }
//Returns False if Device has No Internet Connection
        return false; }


}