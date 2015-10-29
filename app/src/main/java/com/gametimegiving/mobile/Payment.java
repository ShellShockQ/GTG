package com.gametimegiving.mobile;

import android.os.AsyncTask;
import android.util.Log;

import com.gametimegiving.mobile.Application.BaseApplication;
import com.gametimegiving.mobile.Parse.HttpManager;
import com.gametimegiving.mobile.Parse.RequestPackage;

import org.json.JSONException;
import org.json.JSONObject;

public class Payment {
    private final static String TAG = "PAYMENT";
    private final static int REQUEST_CODE = 100;


    private final static String mApiServerUrl = BaseApplication.getInstance().getMetaData(BaseApplication.META_DATA_API_SERVER_URL);


    public void GenerateClientToken() {
        RequestPackage p = new RequestPackage();
        String method = "token";
        p.setMethod("GET");
        p.setUri(String.format("%s/api/%s", mApiServerUrl, method));
        GetClientTokenTask task = new GetClientTokenTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p);
    }

    public void SendNonceToBraintree() {
        RequestPackage p = new RequestPackage();
        String method = "braintreeClientToken";
        p.setMethod("GET");
        p.setUri(String.format("%s/api/%s", mApiServerUrl, method));
    }

    private class GetClientTokenTask extends AsyncTask<RequestPackage, Void, String> {

        @Override
        protected String doInBackground(RequestPackage... params) {

            return HttpManager.getData(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                //    ClientToken=jsonObject.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private class SendNonceToBrainTreeTask extends AsyncTask<RequestPackage, Void, String> {


        @Override
        protected String doInBackground(RequestPackage... params) {
            return HttpManager.getData(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, result);
        }
    }
}
