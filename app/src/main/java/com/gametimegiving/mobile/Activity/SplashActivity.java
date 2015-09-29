/***************************************************************************************
 * FileName : SplashActivity.java
 * <p/>
 * Dependencies : SharedPreferences
 * <p/>
 * Description : This is SplashActivity where splash screen will show 2 seconds after that app will show login or main screen.
 ***************************************************************************************/
package com.gametimegiving.mobile.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Utils.Constant;

public class SplashActivity extends Activity {
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                try {
                    SharedPreferences sharedpreferences = SplashActivity.this.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
                    if (sharedpreferences.getBoolean(Constant.ISLOGIN, false)) {
                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        SplashActivity.this.startActivity(mainIntent);
                        SplashActivity.this.finish();

                    } else {
                     /* Create an Intent that will start the Login-Activity. */
                        Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        SplashActivity.this.startActivity(mainIntent);
                        SplashActivity.this.finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, SPLASH_TIME_OUT);
    }

}

