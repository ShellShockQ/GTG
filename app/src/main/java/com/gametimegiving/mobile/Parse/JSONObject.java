package com.gametimegiving.mobile.Parse;

import com.gametimegiving.mobile.Utils.Log;

import org.json.JSONException;

public class JSONObject extends org.json.JSONObject {
    private static String TAG = "JSONObject";

    public JSONObject(org.json.JSONObject o) throws JSONException {
        super(o.toString());
    }

    public JSONObject() {
        super();
    }

    public JSONObject(String json) throws JSONException {
        super(json);
    }

    public static JSONObject fromString(String s) {
        JSONObject result = null;
        try {
            result = new JSONObject(s);
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
        return result;
    }

    @Override
    public org.json.JSONObject put(String key, Object value) {
        try {
            super.put(key, value);
        } catch (Exception exc) {
            Log.e(TAG, exc);
        }
        return this;
    }

    @Override
    public org.json.JSONObject put(String key, int value) {
        try {
            super.put(key, value);
        } catch (Exception exc) {
            Log.e(TAG, exc);
        }
        return this;
    }

    @Override
    public org.json.JSONObject put(String key, double value) {
        try {
            super.put(key, value);
        } catch (Exception exc) {
            Log.e(TAG, exc);
        }
        return this;
    }

    @Override
    public org.json.JSONObject put(String key, long value) {
        try {
            super.put(key, value);
        } catch (Exception exc) {
            Log.e(TAG, exc);
        }
        return this;
    }

    @Override
    public org.json.JSONObject put(String key, boolean value) {
        try {
            super.put(key, value);
        } catch (Exception exc) {
            Log.e(TAG, exc);
        }
        return this;
    }

    @Override
    public Object get(String key) {
        Object retVal = null;
        if (super.has(key)) {
            try {
                retVal = super.get(key);
                if (retVal.equals(null)) retVal = null;
            } catch (Exception exc) {
                Log.e(TAG, exc);
            }
        }

        return retVal;
    }

    @Override
    public String getString(String key) {
        String retVal = null;
        if (super.has(key)) {
            try {
                Object value = super.get(key);
                if (!value.equals(null)) {
                    retVal = super.getString(key);
                }
            } catch (Exception exc) {
                Log.e(TAG, exc);
            }
        }
        return retVal;
    }

    @Override
    public boolean getBoolean(String key) {
        boolean retVal = false;
        if (super.has(key)) {
            try {
                Object value = super.get(key);
                if (!value.equals(null)) {
                    retVal = super.getBoolean(key);
                }
            } catch (Exception exc) {
                Log.e(TAG, exc);
            }
        }
        return retVal;
    }

    @Override
    public int getInt(String key) {
        int retVal = 0;
        if (super.has(key)) {
            try {
                Object value = super.get(key);
                if (!value.equals(null)) {
                    retVal = super.getInt(key);
                }
            } catch (Exception exc) {
                Log.e(TAG, exc);
            }
        }
        return retVal;
    }

    @Override
    public double getDouble(String key) {
        double retVal = 0;
        if (super.has(key)) {
            try {
                Object value = super.get(key);
                if (!value.equals(null)) {
                    retVal = super.getDouble(key);
                }
            } catch (Exception exc) {
                Log.e(TAG, exc);
            }
        }
        return retVal;
    }

    @Override
    public long getLong(String key) {
        long retVal = 0;
        if (super.has(key)) {
            try {
                Object value = super.get(key);
                if (!value.equals(null)) {
                    retVal = super.getLong(key);
                }
            } catch (Exception exc) {
                Log.e(TAG, exc);
            }
        }
        return retVal;
    }

    @Override
    public JSONObject getJSONObject(String key) {
        JSONObject retVal = null;
        if (super.has(key)) {
            try {
                Object value = super.get(key);
                if (!value.equals(null)) {
                    org.json.JSONObject obj = super.getJSONObject(key);
                    retVal = new JSONObject(obj.toString());
                }
            } catch (Exception exc) {
                Log.e(TAG, exc);
            }
        }
        return retVal;
    }

    @Override
    public JSONArray getJSONArray(String key) {
        JSONArray retVal = null;
        if (super.has(key)) {
            try {
                Object value = super.get(key);
                if (!value.equals(null)) {
                    org.json.JSONArray obj = super.getJSONArray(key);
                    retVal = new JSONArray(obj.toString());
                }
            } catch (Exception exc) {
                Log.e(TAG, exc);
            }
        }
        return retVal;
    }
}
