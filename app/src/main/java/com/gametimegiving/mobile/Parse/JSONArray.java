package com.gametimegiving.mobile.Parse;

import com.gametimegiving.mobile.Utils.Log;

import org.json.JSONException;

public class JSONArray extends org.json.JSONArray {

    private static String TAG = "JSONArray";

    public JSONArray() {
        super();
    }

    public JSONArray(String json) throws JSONException {
        super(json);
    }

    public JSONArray(org.json.JSONArray o) throws JSONException {
        super(o.toString());
    }

    public static JSONArray fromString(String s) {
        JSONArray result = new JSONArray();
        try {
            result = new JSONArray(s);
        } catch (Exception ignored) {
        }
        return result;
    }

    public JSONObject getJSONObject(int index) {
        JSONObject retVal = null;
        try {
            org.json.JSONObject obj = super.getJSONObject(index);
            retVal = new JSONObject(obj);
        } catch (Exception exc) {
            Log.e(TAG, exc);
        }
        return retVal;
    }

}
