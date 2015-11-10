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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gametimegiving.mobile.DBOpenHelper;
import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Utils.Constant;
import com.gametimegiving.mobile.Utils.Utilities;

public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";
    private static int SPLASH_TIME_OUT = 2000;
    private Utilities utilties = new Utilities();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase _db = helper.getWritableDatabase();

        try {
            PackageManager packageManager = getPackageManager();
            String packageName = getPackageName();
            PackageInfo pInfo = packageManager.getPackageInfo(packageName, 0);
            String versionName = pInfo.versionName;
            String version_value = String.format(java.util.Locale.ENGLISH, "Version %s", versionName);
            TextView tv_VersionOnSplashPage = (TextView) findViewById(R.id.versiontext);
            tv_VersionOnSplashPage.setText(version_value);
        } catch (Exception ignored) {
        }

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                try {
                    SharedPreferences sharedpreferences = SplashActivity.this.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
// Check if onboarding_complete is false
                    if (!sharedpreferences.getBoolean("onboarding_complete", false)) {
                        // Start the onboarding Activity
                        Intent onboarding = new Intent(SplashActivity.this, OnBoardingActivity.class);
                        startActivity(onboarding);

                        // Close the main Activity
                        finish();
                        return;
                    }
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

