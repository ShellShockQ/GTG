package com.gametimegiving.mobile.Parse;

import org.json.JSONObject;

public interface BaseApiListener {
    void onLogin(String token);

    void onFbLogin(String token);

    void onApiError(int errorId, String errorText);

    void onNoResults(String message);

    void onGetEvents(JSONArray items);

    void onRegister(JSONObject data);

    void onSaveProfile(JSONObject data);

    void onGetProfile(JSONObject data);

    void onFeedback();

    void onUserFile(JSONObject data);

    void onGetLayout(JSONArray items);

    void onGetCategory(JSONArray items);

    void onGetData(JSONArray items);

    void onForgot(JSONObject data);

    void onGetCharity(JSONArray items);

    void onGetGame(JSONArray items);

    void onGetTeam(JSONArray items);

    void onPledge(JSONArray items);
}


