package com.gametimegiving.mobile.Application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.gametimegiving.mobile.Parse.BaseApi;
import com.gametimegiving.mobile.Utils.Log;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Logger;
import com.google.analytics.tracking.android.Tracker;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

//import com.twitter.sdk.android.Twitter;
//import com.twitter.sdk.android.core.TwitterAuthConfig;

public class BaseApplication extends Application {

    public static final String META_DATA_API_SERVER_URL = "API_SERVER_URL";
    public static final String META_DATA_LOGO_BASE_URL = "LOGO_BASE_URL";
    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");
    private static final boolean GA_OPT_OUT = false;
    private static final boolean GA_IS_DRY_RUN = false;
    private static final Logger.LogLevel GA_LOG_VERBOSITY = Logger.LogLevel.VERBOSE;
    private static String TAG = "BaseApplication";
    private static String PREFS_NAME = "BasePreferences";
    private static BaseApplication mBaseApplication;
    private static GoogleAnalytics mGa;
    private static Tracker mTracker;
    public BaseApi Api;
    public int AndroidApiVersion;
    public boolean Debuggable;
    public SQLiteDatabase db;
    private boolean mCamera;
    private ApplicationInfo mApplicationInfo;
    private DisplayMetrics mDisplayMetrics;

    public static BaseApplication getInstance() {
        return mBaseApplication;
    }

    public static Context getContext() {
        return mBaseApplication.getApplicationContext();
    }

    public static Tracker getGaTracker() {
        return mTracker;
    }

    public static GoogleAnalytics getGaInstance() {
        return mGa;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        this.Debuggable = isDebuggable(this);


        mBaseApplication = this;

        this.Api = new BaseApi(this);
        this.AndroidApiVersion = android.os.Build.VERSION.SDK_INT;

        try {
            mApplicationInfo = this.getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }

        setCamera();

        mDisplayMetrics = getResources().getDisplayMetrics();
        Log.d(TAG, String.format(java.util.Locale.ENGLISH, "DisplayMetrics:%d %d %d", mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels, mDisplayMetrics.densityDpi));

        initializeGa();

    }

    @SuppressLint("NewApi")
    private void setCamera() {
        PackageManager mgr = getPackageManager();
        mCamera = mgr.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!mCamera && this.AndroidApiVersion >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            mCamera = mgr.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
            if (!mCamera && this.AndroidApiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mCamera = mgr.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
            }
        }
    }

    public boolean hasCamera() {
        return mCamera;
    }

    public void setPreference(String key, String value) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getPreference(String key) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(key, null);
    }

    public String getMetaData(String key) {
        String result = null;
        if (mApplicationInfo != null) {
            Bundle metaData = mApplicationInfo.metaData;
            if (metaData.containsKey(key)) {
                result = metaData.getString(key);
            }
        }
        return result;
    }

    private void initializeGa() {
        mGa = GoogleAnalytics.getInstance(this);
//    mTracker = mGa.getTracker(getString(R.string.ga_tracking_id));
        mGa.setDryRun(GA_IS_DRY_RUN);
        mGa.getLogger().setLogLevel(GA_LOG_VERBOSITY);
        GoogleAnalytics.getInstance(this).setAppOptOut(GA_OPT_OUT);
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    private boolean isDebuggable(Context ctx) {
        boolean debuggable = false;

        try {
            PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature signatures[] = pinfo.signatures;

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            for (int i = 0; i < signatures.length; i++) {
                ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable)
                    break;
            }
        } catch (PackageManager.NameNotFoundException e) {
            //debuggable variable will remain false
        } catch (CertificateException e) {
            //debuggable variable will remain false
        }
        return debuggable;
    }
}
