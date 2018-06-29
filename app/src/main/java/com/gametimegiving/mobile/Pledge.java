package com.gametimegiving.mobile;

import android.app.Activity;

import com.gametimegiving.mobile.Application.BaseApplication;
import com.gametimegiving.mobile.Parse.RequestPackage;
import com.gametimegiving.mobile.Utils.Constant;
import com.gametimegiving.mobile.Utils.Log;
import com.gametimegiving.mobile.Utils.Utilities;

import java.util.Date;

public class Pledge extends BaseApplication {
    private final static String TAG = "PLEDGE";
    private int game_id;
    private int team_id;
    private int charity_id;
    private int amount;
    private int user;
    private Date timeOfPledge;
    private int preferredCharity_id;
    private Utilities utilities = new Utilities();
    private Activity mContext;


    public Pledge(Activity context) {
        mContext = context;
    }

    //Adding to the file
    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public int getTeam_id() {
        return team_id;
    }

    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }

    public int getCharity_id() {
        return charity_id;
    }

    public void setCharity_id(int charity_id) {
        this.charity_id = charity_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public Date getTimeOfPledge() {
        return timeOfPledge;
    }

    public void setTimeOfPledge(Date timeOfPledge) {
        this.timeOfPledge = timeOfPledge;
    }

    public int getPreferredCharity_id() {
        return preferredCharity_id;
    }

    public void setPreferredCharity_id() {
        this.preferredCharity_id = 1;
    }

    public int SubmitPledge() {
        int rtnVal = 0;
        String method = "pledge";
        try {
            final RequestPackage p = new RequestPackage();
            p.setMethod("POST");
            p.setUri(String.format("%s/api/%s", Constant.APISERVERURL, method));
            p.setParam("token", null);
            p.setParam("game_id", Integer.toString(getGame_id()));
            p.setParam("team_id", Integer.toString(getTeam_id()));
            p.setParam("charity_id", Integer.toString(getCharity_id()));
            p.setParam("preferredCharity_id", Integer.toString(getPreferredCharity_id()));
            p.setParam("amount", Integer.toString(getAmount()));
            p.setParam("user", Integer.toString(getUser()));
            String args = p.getEncodedParams();

        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
        return rtnVal;
///

    }
//    public int SubmitPledge() {
//        int rtnVal = 0;
//        String method = "pledge";
//        try {
//            RequestPackage p = new RequestPackage();
//            p.setMethod("POST");
//            p.setUri(String.format("%s/api/%s", mApiServerUrl, method));
//            p.setParam("token", null);
//            p.setParam("game_id", Integer.toString(getGame_id()));
//            p.setParam("team_id", Integer.toString(getTeam_id()));
//            p.setParam("charity_id", Integer.toString(getCharity_id()));
//            p.setParam("preferredCharity_id", Integer.toString(getPreferredCharity_id()));
//            p.setParam("amount", Integer.toString(getAmount()));
//            p.setParam("user", Integer.toString(getUser()));
//            RecordPledge task = new RecordPledge();
//            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p);
//        } catch (Exception exc) {
//            Log.e(TAG, exc.toString());
//        }
//
//        //get the return value
//
//        return rtnVal;
//    }

//    private class RecordPledge extends AsyncTask<RequestPackage, Void, Integer> {
//
//        @Override
//        protected Integer doInBackground(RequestPackage... params) {
//            int result = 0;
//            String content = HttpManager.getData(params[0]);
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(Integer integer) {
//            super.onPostExecute(integer);
//
//
//        }
//    }
}
