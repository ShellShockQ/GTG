package com.gametimegiving.mobile.Parse;

import android.util.Log;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RequestPackage {
    private static final String TAG = "RequestPackage";
    private String uri;
    private String method = "GET";
    private Map<String, String> params = new HashMap<>();

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setParam(String key, String value) {
        params.put(key, value);

    }

    public String getEncodedParams() {
        StringBuilder sb = new StringBuilder();
        String value = null;
        for (String key : params.keySet()) {

            try {
                value = URLEncoder.encode(params.get(key), "UTF-8");

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key + "=" + value);

        }
        return sb.toString();
    }

    public String getEncodedParams(String json) {
        StringBuilder sb = new StringBuilder();
        String value = null;
        sb.append("{");
        int cnt = 0;
        int paramCnt = params.size();
        for (String key : params.keySet()) {
            sb.append(String.format("\"%s\":\"%s\"", key, params.get(key)));
            if (cnt < paramCnt - 1) sb.append(",");
            cnt++;
        }
        sb.append("}");
        Log.d(TAG, "JSONString: " + sb.toString());
        try {
            value = URLEncoder.encode(sb.toString(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "args=" + value);
        return "args=" + value;
    }
//    public String getEncodedParams(String Json) {
//        StringBuilder sb = new StringBuilder();
//        String value = null;
//        String args ="";
//        String json;
//        String arguments="{\"token\":\"null\",\"page\":\"0\",\"game_id\":\"257\"}";
//       try {
//     args = URLEncoder.encode(arguments, "UTF-8");
//       }catch (Exception exc) {
//        Log.e(TAG, exc.toString());
//    }
//        for (String key : params.keySet()) {
//            try {
//                sb.append(key + ":" + value);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return "args="+args;
//    }

}
