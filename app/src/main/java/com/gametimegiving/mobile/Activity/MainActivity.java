/***************************************************************************************
 * FileName : MainActivity.java
 * <p/>
 * Dependencies :BaseActivity, SharedPreferences
 * <p/>
 * Description : This is MainActivity where we design DrawerLayout.
 ***************************************************************************************/
package com.gametimegiving.mobile.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.gametimegiving.mobile.Fragment.ProfileFragment;
import com.gametimegiving.mobile.Fragment.SelectGameFragment;
import com.gametimegiving.mobile.Parse.JSONArray;
import com.gametimegiving.mobile.Parse.JSONObject;
import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Team;
import com.gametimegiving.mobile.Utils.CommanDialog;
import com.gametimegiving.mobile.Utils.Constant;

import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    public static String MyTeamIds;
    String[] arr_charty;
    String[] arr_team;
    List<Team> TeamList;

    public static boolean isNetworkStatusAvialable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if (netInfos != null)
                if (netInfos.isConnected())
                    return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!ProfileFragment.hasProfileUpdate)
            replaceActivityToFragmnet();
        else
            ProfileFragment.hasProfileUpdate = false;
    }

    /**
     * Activity Replace By Fragment
     */
    private void replaceActivityToFragmnet() {
        SharedPreferences sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        MyTeamIds = sharedpreferences.getString("myTeamIds", "");
        Log.d(TAG, String.format("Shared Preferences are %s", sharedpreferences.getAll().toString()));
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.clear().commit();

        if (!sharedpreferences.getBoolean(Constant.ISPROFILESUBMITTED, false)) {
            mToolbarTitle.setText("Profile");
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.ll_container, ProfileFragment.newInstance());
            ft.commit();
        } else {
            mToolbarTitle.setText("Select Game");
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            ft.add(R.id.ll_container, SelectGameFragment.newInstance());
            ft.commit();
        }
    }

    /*
    *
    * Get Charity List From Json
    * */
    @Override
    public void onGetCharity(JSONArray results) {
        int len = results.length();
        arr_charty = new String[len];
        if (len > 0) {
            for (int i = 0; i < results.length(); i++) {
                try {
                    JSONObject json = new JSONObject(results.getJSONObject(i));
                    String name = json.getString("name");

                    arr_charty[i] = name;
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }


        }


    }

    /*  *
      * Get Team List From Json
      * */
    @Override
    public void onGetTeam(JSONArray results) {
        int len = results.length();
        arr_team = new String[len];
        if (len > 0) {
            for (int i = 0; i < results.length(); i++) {
                try {
                    JSONObject json = new JSONObject(results.getJSONObject(i));
                    String name = json.getString("name");

                    arr_team[i] = name;
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onBackPressed() {
        CommanDialog.exitfromapplication(this);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            if (netInfo.getType() != ConnectivityManager.TYPE_WIFI) {
                Toast.makeText(MainActivity.this, "This app doesn't work without wifi", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }


}
