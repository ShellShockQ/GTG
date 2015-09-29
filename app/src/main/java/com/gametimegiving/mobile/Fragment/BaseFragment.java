package com.gametimegiving.mobile.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gametimegiving.mobile.Application.BaseApplication;
import com.gametimegiving.mobile.Parse.BaseApi;
import com.gametimegiving.mobile.Parse.BaseApiListener;
import com.gametimegiving.mobile.Parse.JSONArray;
import com.gametimegiving.mobile.Utils.Log;

import org.json.JSONObject;

public class BaseFragment extends Fragment implements BaseApiListener {

    private static final String TAG = "BaseFragment";

    protected Fragment mFragment;
    protected BaseApi mApi;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mFragment = this;
        mApi = BaseApplication.getInstance().Api;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        mApi.addEventListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        mApi.removeEventListener(this);
    }

    @Override
    public void onLogin(String token) {

    }

    @Override
    public void onFbLogin(String token) {

    }

    @Override
    public void onApiError(int errorId, String errorText) {

    }

    @Override
    public void onNoResults(String message) {

    }

    @Override
    public void onGetEvents(JSONArray items) {

    }

    @Override
    public void onRegister(JSONObject data) {

    }

    @Override
    public void onSaveProfile(JSONObject data) {

    }

    @Override
    public void onGetProfile(JSONObject data) {

    }

    @Override
    public void onFeedback() {

    }

    @Override
    public void onUserFile(JSONObject data) {

    }

    @Override
    public void onGetLayout(JSONArray items) {

    }

    @Override
    public void onGetCategory(JSONArray items) {

    }

    @Override
    public void onGetData(JSONArray items) {

    }

    @Override
    public void onPledge(JSONArray items) {

    }

    @Override
    public void onGetGame(JSONArray items) {

    }

    @Override
    public void onGetCharity(JSONArray items) {

    }

    @Override
    public void onGetTeam(JSONArray items) {

    }

    @Override
    public void onForgot(JSONObject data) {

    }
}
