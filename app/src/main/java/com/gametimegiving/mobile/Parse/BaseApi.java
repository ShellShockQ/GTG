package com.gametimegiving.mobile.Parse;

import android.text.TextUtils;

import com.gametimegiving.mobile.Application.BaseApplication;
import com.gametimegiving.mobile.Utils.Log;
import com.handlerexploit.prime.utils.ImageManager;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class BaseApi implements HttpRequestListener {
    private static String TAG = "Api";
    private String mToken;
    private List<BaseApiListener> mListeners = null;
    private String mLastUserName = null;
    private String mLastPassword = null;
    private String mLastFacebookId = null;
    private String mLastProfileImage = null;
    private HttpRequest mLastRequest = null;
    private BaseApplication mApplication;
    private ImageManager mImageManager;


    public BaseApi(BaseApplication application) {
        mApplication = application;
        mListeners = new ArrayList<BaseApiListener>();
        mImageManager = ImageManager.getInstance(mApplication.getApplicationContext());
        mImageManager.drain();
    }

    public void drain() {
        mImageManager.drain();
    }

    public synchronized void setEventListener(BaseApiListener listener) {
        mListeners = new ArrayList<BaseApiListener>();
        mListeners.add(listener);
    }

    public synchronized void addEventListener(BaseApiListener listener) {
        mListeners.add(listener);
    }

    public synchronized void removeEventListener(BaseApiListener listener) {
        mListeners.remove(listener);
    }

    public void forgot(String email) {
        try {
            String json = String.format(java.util.Locale.ENGLISH, "{\"email\":\"%s\"}", email);
            String args = URLEncoder.encode(json, "UTF-8");
            String method = "forgot";


            HttpRequest request = new HttpRequest(method, args);
            request.addEventListener(this);
            request.execute();
        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void login(String username, String password) {
        try {
            String json = String.format(java.util.Locale.ENGLISH, "{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
            String args = URLEncoder.encode(json, "UTF-8");
            String method = "login";

            mLastUserName = username;
            mLastPassword = password;

            HttpRequest request = new HttpRequest(method, args);
            request.addEventListener(this);
            request.execute();
        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void fbLogin(String email, String facebook_id) {
        try {
            String json = String.format(java.util.Locale.ENGLISH, "{\"email\":\"%s\",\"facebook_id\":\"%s\"}", email, facebook_id);
            String args = URLEncoder.encode(json, "UTF-8");
            String method = "fb_login";

            mLastFacebookId = facebook_id;

            HttpRequest request = new HttpRequest(method, args);
            request.addEventListener(this);
            request.execute();

        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void feedback(String description) {
        try {

            ArrayList<String> list = new ArrayList<String>();
            if (mLastUserName != null)
                list.add(String.format(java.util.Locale.ENGLISH, "\"username\":\"%s\"", mLastUserName));
            if (mLastFacebookId != null)
                list.add(String.format(java.util.Locale.ENGLISH, "\"facebook_id\":\"%s\"", mLastFacebookId));
            if (description != null)
                list.add(String.format(java.util.Locale.ENGLISH, "\"description\":\"%s\"", description));

            String json = String.format(java.util.Locale.ENGLISH, "{\"token\":\"%s\"}", mToken);

            if (list.size() > 0) {
                String params = TextUtils.join(",", list.toArray());
                json = String.format(java.util.Locale.ENGLISH, "{\"token\":\"%s\",%s}", mToken, params);
            }

            String args = URLEncoder.encode(json, "UTF-8");
            String method = "feedback";

            HttpRequest request = new HttpRequest(method, args);
            request.addEventListener(this);
            request.execute();

        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void userFile(byte[] data) {
        userFile(data, null);
    }

    public void userFile(byte[] data, String user_file_type) {
        try {
            ArrayList<String> list = new ArrayList<String>();
            if (user_file_type != null)
                list.add(String.format(java.util.Locale.ENGLISH, "\"user_file_type\":\"%s\"", user_file_type));

            String json = String.format(java.util.Locale.ENGLISH, "{\"token\":\"%s\"}", mToken);

            if (list.size() > 0) {
                String params = TextUtils.join(",", list.toArray());
                json = String.format(java.util.Locale.ENGLISH, "{\"token\":\"%s\",%s}", mToken, params);
            }

            String args = URLEncoder.encode(json, "UTF-8");
            String method = "userfile";

            HashMap<Object, Object> map = new HashMap<Object, Object>();
            map.put("userfile", data);

            HttpRequest request = new HttpRequest(method, args, map);
            request.addEventListener(this);
            request.execute();
        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void saveProfile(String age, String gender, String zip_code) {
        try {
            String json = String.format(java.util.Locale.ENGLISH, "{\"token\":\"%s\",\"age\":\"%s\",\"gender\":\"%s\",\"zip_code\":\"%s\"}", mToken, age, gender, zip_code);

            String args = URLEncoder.encode(json, "UTF-8");
            String method = "profile-save";

            HttpRequest request = new HttpRequest(method, args);
            request.addEventListener(this);
            request.execute();

        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void getProfile() {
        try {
            String json = String.format(java.util.Locale.ENGLISH, "{\"token\":\"%s\"}", mToken);

            String args = URLEncoder.encode(json, "UTF-8");
            String method = "profile-get";

            HttpRequest request = new HttpRequest(method, args);
            request.addEventListener(this);
            request.execute();

        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void getEvents(int dtm, int page, int type, String genre, double lat, double lon, int range) {
        try {

            //lat = 29.7628;
            //lon = -95.3831;

            ArrayList<String> list = new ArrayList<String>();
            list.add(String.format(java.util.Locale.ENGLISH, "\"dtm\":\"%d\"", dtm));
            list.add(String.format(java.util.Locale.ENGLISH, "\"page\":\"%d\"", page));
            list.add(String.format(java.util.Locale.ENGLISH, "\"type\":\"%d\"", type));
            if (genre != null)
                list.add(String.format(java.util.Locale.ENGLISH, "\"genre\":\"%s\"", genre));
            list.add(String.format(java.util.Locale.ENGLISH, "\"lat\":\"%f\"", lat));
            list.add(String.format(java.util.Locale.ENGLISH, "\"lon\":\"%f\"", lon));
            list.add(String.format(java.util.Locale.ENGLISH, "\"range\":\"%d\"", range));

            String json = String.format(java.util.Locale.ENGLISH, "{\"token\":\"%s\"}", mToken);

            if (list.size() > 0) {
                String params = TextUtils.join(",", list.toArray());
                json = String.format(java.util.Locale.ENGLISH, "{\"token\":\"%s\",%s}", mToken, params);
            }
            String args = URLEncoder.encode(json, "UTF-8");
            String method = "event";

            HttpRequest request = new HttpRequest(method, args);
            request.addEventListener(this);
            request.execute();

        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void getLayout(int section_id) {
        try {

            HashMap<Object, Object> map = new HashMap<Object, Object>();
            map.put("section_id", section_id);
            String args = null;
            String method = "layout";

            HttpRequest request = new HttpRequest(method, args, map);
            request.addEventListener(this);
            request.execute();
        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void getCategory(int layout_id) {
        try {
            HashMap<Object, Object> map = new HashMap<Object, Object>();
            map.put("layout_id", layout_id);
            String args = null;
            String method = "category";

            HttpRequest request = new HttpRequest(method, args, map);
            request.addEventListener(this);
            request.execute();
        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void getData(int dtm, int page, int section_id, int layout_id, int category_id, int type, double lat, double lon, int range) {
        try {

            //lat = 29.7628;
            //lon = -95.3831;

            ArrayList<String> list = new ArrayList<String>();
            list.add(String.format(java.util.Locale.ENGLISH, "\"dtm\":\"%d\"", dtm));
            list.add(String.format(java.util.Locale.ENGLISH, "\"page\":\"%d\"", page));
            list.add(String.format(java.util.Locale.ENGLISH, "\"section_id\":\"%d\"", section_id));
            list.add(String.format(java.util.Locale.ENGLISH, "\"layout_id\":\"%d\"", layout_id));
            list.add(String.format(java.util.Locale.ENGLISH, "\"category_id\":\"%d\"", category_id));
            list.add(String.format(java.util.Locale.ENGLISH, "\"type\":\"%d\"", type));
            list.add(String.format(java.util.Locale.ENGLISH, "\"lat\":\"%f\"", lat));
            list.add(String.format(java.util.Locale.ENGLISH, "\"lon\":\"%f\"", lon));
            list.add(String.format(java.util.Locale.ENGLISH, "\"range\":\"%d\"", range));

            String json = String.format(java.util.Locale.ENGLISH, "{\"token\":\"%s\"}", mToken);

            if (list.size() > 0) {
                String params = TextUtils.join(",", list.toArray());
                json = String.format(java.util.Locale.ENGLISH, "{\"token\":\"%s\",%s}", mToken, params);
            }
            String args = URLEncoder.encode(json, "UTF-8");
            String method = "data";

            HttpRequest request = new HttpRequest(method, args);
            request.addEventListener(this);
            request.execute();

        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void getData(String filter, int type, int page) {
        try {
            HashMap<Object, Object> map = new HashMap<Object, Object>();
            map.put("type", type);
            map.put("page", page);

            String json = filter;
            String args = URLEncoder.encode(json, "UTF-8");
            String method = "data";

            HttpRequest request = new HttpRequest(method, args, map);
            request.addEventListener(this);
            request.execute();
        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void pledge(int game_id, int team_id, int charity_id, int amount) {
        try {
            String json = String.format(java.util.Locale.ENGLISH, "{\"token\":\"%s\",\"game_id\":\"%d\",\"team_id\":\"%d\",\"charity_id\":\"%d\",\"amount\":\"%d\"}", mToken, game_id, team_id, charity_id, amount);

            String args = URLEncoder.encode(json, "UTF-8");
            String method = "pledge";

            HttpRequest request = new HttpRequest(method, args);
            request.addEventListener(this);
            request.execute();
        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void getCharity(int page) {
        try {
            String json = String.format(java.util.Locale.ENGLISH, "{\"token\":\"%s\",\"page\":\"%d\"}", mToken, page);

            String args = URLEncoder.encode(json, "UTF-8");
            String method = "charity";

            HttpRequest request = new HttpRequest(method, args);
            request.addEventListener(this);
            request.execute();
        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void getTeam(int page) {
        try {
            String json = String.format(java.util.Locale.ENGLISH, "{\"token\":\"%s\",\"page\":\"%d\"}", mToken, page);

            String args = URLEncoder.encode(json, "UTF-8");
            String method = "team";

            HttpRequest request = new HttpRequest(method, args);
            request.addEventListener(this);
            request.execute();
        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void getGame(int page) {
        getGame(0, page);
    }

    public void getGame(int game_id, int page) {
        try {

            ArrayList<String> list = new ArrayList<String>();
            if (game_id != 0)
                list.add(String.format(java.util.Locale.ENGLISH, "\"game_id\":\"%d\"", game_id));

            String json = String.format(java.util.Locale.ENGLISH, "{\"token\":\"%s\",\"page\":\"%d\"}", mToken, page);

            if (list.size() > 0) {
                String params = TextUtils.join(",", list.toArray());
                json = String.format(java.util.Locale.ENGLISH, "{\"token\":\"%s\",\"page\":\"%d\",%s}", mToken, page, params);
            }

            String args = URLEncoder.encode(json, "UTF-8");
            String method = "game";

            HttpRequest request = new HttpRequest(method, args);
            request.addEventListener(this);
            request.execute();
        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void register(String name, String email, String username, String password) {
        try {
            String json = String.format(java.util.Locale.ENGLISH, "{\"name\":\"%s\",\"email\":\"%s\",\"username\":\"%s\",\"password\":\"%s\"}", name, email, username, password);
            String args = URLEncoder.encode(json, "UTF-8");
            String method = "register";

            mLastUserName = username;
            mLastPassword = password;

            HttpRequest request = new HttpRequest(method, args);
            request.addEventListener(this);
            request.execute();

        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public void logout() {
        mToken = null;
    }

    public String getLastUsername() {
        return mLastUserName;
    }

    public String getLastPassword() {
        return mLastUserName;
    }

    public String getLastProfileImage() {
        return mLastProfileImage;
    }

    public String getLastFacebookId() {
        return mLastFacebookId;
    }

    public boolean isAuthenticated() {
        return (mToken != null);
    }

    public void reExectute() {
        Log.d(TAG, "ReExecute");
        if (mLastRequest != null) {
            mLastRequest.execute();
        }
    }

    public void onResult(String method, String result) {
        //Log.d(TAG, String.format(java.util.Locale.ENGLISH,  "%s %s", method, result));
        JSONObject item = null;
        try {
            item = new JSONObject(result);
        } catch (Exception ex) {
            item = new JSONObject();
            item.put("result", "error");

            if (result.contains("SocketException")) {
                item.put("error_id", -9998);
                item.put("data", result);
            } else if (result.contains("UnknownHostException")) {
                item.put("error_id", -9997);
                item.put("data", "Internet access is not available");
            } else {
                item.put("error_id", -9999);
                item.put("data", "Could not load data");
            }
            Log.e(TAG, result);
        }
        String resultValue = item.getString("result");
        if (resultValue.equals("error")) {
            int errorId = -9999;
            if (item.has("error_id")) errorId = item.getInt("error_id");
            String errorText = "unknown error";
            if (item.has("data")) errorText = item.getString("data");
            Log.e(TAG, String.format(java.util.Locale.ENGLISH, "API ERROR : %d %s", errorId, errorText));

            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onApiError(errorId, errorText);
            }
        } else if (item.has("no_results")) {
            String no_results = item.getString("no_results");
            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onNoResults(no_results);
            }
        } else if (method.equals("login")) {
            mToken = item.getString("data");
            mApplication.setPreference("Username", mLastUserName);
            mApplication.setPreference("Password", mLastPassword);

            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onLogin(mToken);
            }
        } else if (method.equals("fb_login")) {
            mToken = item.getString("data");
            mApplication.setPreference("FacebookId", mLastFacebookId);

            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onFbLogin(mToken);
            }
        } else if (method.equals("event")) {
            JSONArray items = item.getJSONArray("items");
            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onGetEvents(items);
            }
        } else if (method.equals("layout")) {
            JSONArray items = item.getJSONArray("items");
            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onGetLayout(items);
            }
        } else if (method.equals("charity")) {
            JSONArray items = item.getJSONArray("items");
            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onGetCharity(items);
            }
        } else if (method.equals("game")) {
            JSONArray items = item.getJSONArray("items");
            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onGetGame(items);
            }
        } else if (method.equals("team")) {
            JSONArray items = item.getJSONArray("items");
            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onGetTeam(items);
            }
        } else if (method.equals("category")) {
            JSONArray items = item.getJSONArray("items");
            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onGetCategory(items);
            }
        } else if (method.equals("pledge")) {
            JSONArray items = item.getJSONArray("items");
            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onPledge(items);
            }
        } else if (method.equals("data")) {
            JSONArray items = item.getJSONArray("items");
            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onGetData(items);
            }
        } else if (method.equals("register")) {
            JSONObject data = item.getJSONObject("data");
            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onRegister(data);
            }
        } else if (method.equals("forgot")) {
            JSONObject data = item.getJSONObject("data");
            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onForgot(data);
            }
        } else if (method.equals("profile-get")) {
            JSONObject data = item.getJSONObject("data");
            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onGetProfile(data);
            }
        } else if (method.equals("profile-save")) {
            JSONObject data = item.getJSONObject("data");
            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onSaveProfile(data);
            }
        } else if (method.equals("feedback")) {
            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onFeedback();
            }
        } else if (method.equals("userfile")) {
            JSONObject data = item.getJSONObject("data");
            Iterator<BaseApiListener> i = mListeners.iterator();
            while (i.hasNext()) {
                i.next().onUserFile(data);
            }
        } else {
            Log.d(TAG, String.format(java.util.Locale.ENGLISH, "onResult method %s not implemented", method));
        }
    }

}