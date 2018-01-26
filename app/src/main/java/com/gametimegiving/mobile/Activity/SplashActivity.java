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
import android.widget.Toast;

import com.gametimegiving.mobile.DBOpenHelper;
import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Utils.Constant;
import com.gametimegiving.mobile.Utils.Utilities;

public class SplashActivity extends Activity {
    private final String TAG = getClass().getSimpleName();
    private static int SPLASH_TIME_OUT = 2000;
    private Utilities utilties = new Utilities();
    private Intent nextActionIntent = null;

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
                        Toast.makeText(getApplicationContext(), "Let's On Board You", Toast.LENGTH_SHORT).show();
                        nextActionIntent = new Intent(SplashActivity.this, OnBoardingActivity.class);

                        // Close the main Activity
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "You've Been OnBoarded already", Toast.LENGTH_SHORT).show();
                        nextActionIntent = new Intent(SplashActivity.this, GameBoardActivity.class);
                    }

                    startActivity(nextActionIntent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, SPLASH_TIME_OUT);
    }

}

