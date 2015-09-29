/**
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * <p/>
 * http://www.londatiga.net
 */
package com.gametimegiving.mobile.Twitter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import twitter4j.auth.AccessToken;

public class TwitterSession {
    public static final String TWEET_AUTH_KEY = "auth_key";
    public static final String TWEET_AUTH_SECRET_KEY = "auth_secret_key";
    public static final String TWEET_USER_NAME = "user_name";
    public static final String SHARED = "Twitter_Preferences";
    private static final String TWEET_USER_PROFILE_IMAGE = "user_profile_image";
    private SharedPreferences sharedPref;
    private Editor editor;


    public TwitterSession(Context context) {
        sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);

        editor = sharedPref.edit();
    }

    public void storeAccessToken(AccessToken accessToken, String username, String userProfileImage) {
        editor.putString(TWEET_AUTH_KEY, accessToken.getToken());
        editor.putString(TWEET_AUTH_SECRET_KEY, accessToken.getTokenSecret());
        editor.putString(TWEET_USER_NAME, username);
        editor.putString(TWEET_USER_PROFILE_IMAGE, userProfileImage);
        editor.commit();
    }

    public void resetAccessToken() {
        editor.putString(TWEET_AUTH_KEY, null);
        editor.putString(TWEET_AUTH_SECRET_KEY, null);
        editor.putString(TWEET_USER_NAME, null);
        editor.putString(TWEET_USER_PROFILE_IMAGE, null);
        editor.commit();
    }

    public String getUsername() {
        return sharedPref.getString(TWEET_USER_NAME, "");
    }

    public String getTweetUserProfileImage() {
        return sharedPref.getString(TWEET_USER_PROFILE_IMAGE, "");
    }

    public AccessToken getAccessToken() {
        String token = sharedPref.getString(TWEET_AUTH_KEY, null);
        String tokenSecret = sharedPref.getString(TWEET_AUTH_SECRET_KEY, null);

        if (token != null && tokenSecret != null)
            return new AccessToken(token, tokenSecret);
        else
            return null;
    }
}