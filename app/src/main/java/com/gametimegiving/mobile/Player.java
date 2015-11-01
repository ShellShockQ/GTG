package com.gametimegiving.mobile;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gametimegiving.mobile.Application.BaseApplication;
import com.gametimegiving.mobile.Utils.Constant;
import com.gametimegiving.mobile.Utils.Log;
import com.gametimegiving.mobile.Utils.Utilities;

public class Player extends BaseApplication {
    private static final String mApiServerUrl = BaseApplication.getInstance().getMetaData(BaseApplication.META_DATA_API_SERVER_URL);

    private static final String TAG = "Player";
    public Activity mActivity;
    public Utilities utilities = new Utilities();
    private int player_id;
    private int myteam_id;
    private int myTotalPledgeAmount;
    private int myLastPledgeAmount;
    private Charity[] myCharities;
    private Team[] myTeams;

    public static boolean isRegisteredPlayer(String userName, String pwd, Context context) {
        Boolean registeredPlayer = false;
        String method = "login";
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = String.format("%s/api/%s", mApiServerUrl, method);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response is: " + response.substring(0, 500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "That didn't work!");
            }
        });
        return registeredPlayer;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id() {
        this.player_id = 2;
    }

    public int getMyteam_id() {
        return myteam_id;
    }

    public void setMyteam_id(int myteam_id) {
        this.myteam_id = myteam_id;
    }

    public int getMyTotalPledgeAmount(Activity activity) {
        if (myTotalPledgeAmount == 0) {
            return utilities.ReadSharedPref(Constant.MYTOTALPLEDGEDAMOUNT, activity);
        } else {
            return myTotalPledgeAmount;
        }

    }

    public void setMyTotalPledgeAmount(int myTotalPledgeAmount) {
        this.myTotalPledgeAmount = myTotalPledgeAmount;
    }

    public Charity[] getMyCharities() {
        return myCharities;
    }

    public void setMyCharities(Charity[] myCharities) {
        this.myCharities = myCharities;
    }

    public Team[] getMyTeams() {
        return myTeams;
    }

    public void setMyTeams(Team[] myTeams) {
        this.myTeams = myTeams;
    }

    public int getMyLastPledgeAmount() {
        return myLastPledgeAmount;
    }

    public void setMyLastPledgeAmount(int myLastPledgeAmount) {
        this.myLastPledgeAmount = myLastPledgeAmount;
    }
}
