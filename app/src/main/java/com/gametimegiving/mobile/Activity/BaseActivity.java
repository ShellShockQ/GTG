/***************************************************************************************
 * FileName : BaseActivity.java
 * <p/>
 * Dependencies :BaseApiListener, SharedPreferences
 * <p/>
 * Description : This is BaseActivity where DrawerListener  deside which fragment will open
 ***************************************************************************************/
package com.gametimegiving.mobile.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.gametimegiving.mobile.Adapter.CharityListAdapter;
import com.gametimegiving.mobile.Adapter.ExpandableListAdapter;
import com.gametimegiving.mobile.Adapter.TeamListAdapter;
import com.gametimegiving.mobile.Application.BaseApplication;
import com.gametimegiving.mobile.Fragment.AboutUsFragment;
import com.gametimegiving.mobile.Fragment.CharityFragment;
import com.gametimegiving.mobile.Fragment.MyStatusFragment;
import com.gametimegiving.mobile.Fragment.ProfileFragment;
import com.gametimegiving.mobile.Fragment.SelectGameFragment;
import com.gametimegiving.mobile.Fragment.SettingFragment;
import com.gametimegiving.mobile.Fragment.TeamStatusFragment;
import com.gametimegiving.mobile.Parse.BaseApi;
import com.gametimegiving.mobile.Parse.BaseApiListener;
import com.gametimegiving.mobile.Parse.JSONArray;
import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Twitter.TwitterSession;
import com.gametimegiving.mobile.Utils.Common;
import com.gametimegiving.mobile.Utils.Constant;
import com.gametimegiving.mobile.Utils.Log;

import org.json.JSONObject;

import java.lang.reflect.Field;

public class BaseActivity extends AppCompatActivity implements BaseApiListener {

    public static TextView mToolbarTitle;
    public static ActionBarDrawerToggle mDrawerToggle;
    public static int selectedDonationMethod = -1;
    public static Bitmap bitmap;
    protected BaseApi mApi;
    protected BaseApplication mApp;
    protected BaseActivity mActivity;
    protected DisplayMetrics mDisplayMetrics;
    protected AlertDialog mAlert;
    protected TextView mTitle;
    protected Toolbar mToolbar;
    private String TAG = "BaseActivity";
    private String mActivityName;
    //variable for naviagtion drawer
    private DrawerLayout mDrawerLayout;
    private ExpandableListView mExpandableListView;
    private Common mCommon;
    private ExpandableListAdapter mListAdapter;
    private int lastExpandedPosition = -1;
    // drawer listener
    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {

        @Override
        public void onDrawerStateChanged(int status) {

        }

        @Override
        public void onDrawerSlide(View view, float slideArg) {

        }

        @Override
        public void onDrawerOpened(View view) {
        }

        @Override
        public void onDrawerClosed(View view) {
        }
    };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mApp = (BaseApplication) getApplication();

        mActivity = this;
        mActivityName = mActivity.getClass().getName();
        mActivityName = mActivityName.substring(mActivityName.lastIndexOf(".") + 1);
        TAG = mActivityName;

        if (bundle == null) Log.d(TAG, "onCreate");
        else Log.d(TAG, "onCreate:bundle");


        mApi = mApp.Api;
        mApi.drain();

        setDisplayHomeAsUpEnabled();

        mTitle = (TextView) findViewById(R.id.title);
        if (mTitle != null) mTitle.setText(mActivityName);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getOverflowMenu();

        mDisplayMetrics = getResources().getDisplayMetrics();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Ok", null);
        mAlert = builder.create();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                if (!"EventActivity".equals(mActivityName)) {
                    finish();
                    result = true;
                }
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        initView();
        if (mToolbar != null) {

            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
        initDrawer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        mApi.setEventListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        mApi.removeEventListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle == null) Log.d(TAG, "onSaveInstanceState");
        else Log.d(TAG, "onSaveInstanceState:bundle");
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        if (bundle == null) Log.d(TAG, "onRestoreInstanceState");
        else Log.d(TAG, "onRestoreInstanceState:bundle");
    }

    @Override
    public void onLogin(String token) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onFbLogin(String token) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onApiError(int errorId, String errorText) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onNoResults(String message) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onGetEvents(JSONArray items) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onGetLayout(JSONArray items) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onGetCategory(JSONArray items) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onGetData(JSONArray items) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onGetCharity(JSONArray items) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onGetTeam(JSONArray items) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onGetGame(JSONArray items) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPledge(JSONArray items) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onRegister(JSONObject data) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onGetProfile(JSONObject data) {
    }

    @Override
    public void onForgot(JSONObject data) {
    }

    @Override
    public void onSaveProfile(JSONObject data) {
    }

    @Override
    public void onFeedback() {
    }

    @Override
    public void onUserFile(JSONObject data) {
    }

    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toast(int id, int length) {
        String message = getResources().getString(id);
        toast(message, length);
    }

    public void toast(String message, int length) {
        Toast toast = Toast.makeText(mActivity, message, length);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void setDisplayHomeAsUpEnabled() {

    }

//code for navigation drawaver

    public void applyFont(String fontName, View view) {
        String fontPath = String.format("fnt/%s", fontName);
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);
            textView.setTypeface(typeface);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childView = viewGroup.getChildAt(i);
                applyFont(fontName, childView);
            }
        }
    }

    /*
    * Navigation Drawer Initialization
    *
    * */
    private void initView() {
        mToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.setDrawerListener(mDrawerListener);

        mExpandableListView = (ExpandableListView) findViewById(R.id.navlist);
        mExpandableListView.setGroupIndicator(null);
        mCommon = new Common();
        mCommon.prepareListData();
        mListAdapter = new ExpandableListAdapter(this, mCommon.listDataHeader, mCommon.listDataChild);
        // setting list adapter
        mExpandableListView.setAdapter(mListAdapter);
        mDrawerLayout.closeDrawer(mExpandableListView);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setonClickListner();
    }

    /**
     * Navigation drawer items clciklistner
     */
    public void setonClickListner() {

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    mExpandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                switch (groupPosition) {
                    case 1:
                        mToolbarTitle.setText("Team Status");
                        ft.replace(R.id.ll_container, TeamStatusFragment.newInstance());
                        ft.commit();
                        mDrawerLayout.closeDrawers();

                        break;

                    case 2:
                        mToolbarTitle.setText("Charity");
                        ft.replace(R.id.ll_container, CharityFragment.newInstance());
                        ft.commit();
                        mDrawerLayout.closeDrawers();

                        break;
                }
                return false;
            }
        });
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                switch (groupPosition) {

                    case 0:
                        mToolbarTitle.setText("Select Game");
                        ft.replace(R.id.ll_container, SelectGameFragment.newInstance());
                        ft.commit();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 1:


                        break;
                    case 2:

                        break;
                    case 3:
                        mToolbarTitle.setText("Profile");
                        ft.replace(R.id.ll_container, ProfileFragment.newInstance());
                        ft.commit();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 4:
                        mToolbarTitle.setText("MyStatus");
                        ft.replace(R.id.ll_container, MyStatusFragment.newInstance());
                        ft.commit();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 5:
                        mToolbarTitle.setText("AboutUs");
                        ft.replace(R.id.ll_container, AboutUsFragment.newInstance());
                        ft.commit();
                        mDrawerLayout.closeDrawers();
                        break;

                    case 6:
                        mToolbarTitle.setText("Setting");
                        ft.replace(R.id.ll_container, SettingFragment.newInstance());
                        ft.commit();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 7:
                        UserLogout();
                        mDrawerLayout.closeDrawers();
                        break;
                }

                return false;
            }

        });
    }

    /*
    *
    * Navigation drawer Open-Close
    * */
    private void initDrawer() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);


            }

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);

            }
        };
        SharedPreferences sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(Constant.ISPROFILESUBMITTED, false))
            mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /*
   * LogOut From Twitter Facebok and other
   *
   * */
    private void UserLogout() {
        SharedPreferences sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        String isLoginFrom = sharedpreferences.getString(Constant.ISLOGINFROM, null);
        if (isLoginFrom.equals("facebook")) {
            try {
                FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isLoginFrom.equals("twitter")) {
            SharedPreferences sharedPref = getSharedPreferences(TwitterSession.SHARED, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(TwitterSession.TWEET_AUTH_KEY, null);
            editor.putString(TwitterSession.TWEET_AUTH_SECRET_KEY, null);
            editor.putString(TwitterSession.TWEET_USER_NAME, null);

            editor.commit();
        }
        selectedDonationMethod = -1;
        //   clearSharedPreferences();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    /*
       * Clear Save Data from SharedPreferences
       * */
    private void clearSharedPreferences() {
        SharedPreferences sharedpreferences = this.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constant.PROFILEID, null);
        editor.putString(Constant.PROFILENAME, null);
        editor.putString(Constant.PROFILEURL, null);
        editor.putString(Constant.EMAILADDRESS, null);
        editor.putString(Constant.PASSWORD, null);
        editor.putBoolean(Constant.ISLOGIN, false);
        editor.putBoolean(Constant.ISPROFILESUBMITTED, false);
        editor.commit();
        bitmap = null;
        if (CharityListAdapter.selectedCharity != null)
            CharityListAdapter.selectedCharity.clear();
        CharityListAdapter.selectedCharity = null;
        CharityListAdapter.selectedItems = null;
        TeamListAdapter.selectedItems = null;
        if (TeamListAdapter.selectedTeams != null)
            TeamListAdapter.selectedTeams.clear();
        TeamListAdapter.selectedTeams = null;

    }
}
