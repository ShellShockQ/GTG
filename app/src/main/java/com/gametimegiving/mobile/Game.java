package com.gametimegiving.mobile;

import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gametimegiving.mobile.Application.BaseApplication;
import com.gametimegiving.mobile.Parse.RequestPackage;
import com.gametimegiving.mobile.Utils.Constant;

import org.json.JSONObject;

public class Game {
    private static final String TAG = "GAME";
    private static String GameStatus;
    private int GameId;
    private int home_Id;
    private int away_Id;
    private String home_LongName;
    private String away_LongName;
    private int home_score;
    private int away_score;
    private int period;
    private String starttime;
    private String timeLeft;
    private int hometeam_pledge;
    private int visitingteam_pledge;
    private int PersonalPledgeAmt;
    private String homeLogo;
    private String awayLogo;
    private int userteam_id;
    private Bitmap homeLogobitmap;
    private Bitmap awayLogobitmap;

    public int getHometeam_pledge() {
        return hometeam_pledge;
    }

    public void setHometeam_pledge(int hometeam_pledge) {
        this.hometeam_pledge = hometeam_pledge;
    }

    public int getVisitingteam_pledge() {
        return visitingteam_pledge;
    }

    public void setVisitingteam_pledge(int visitingteam_pledge) {
        this.visitingteam_pledge = visitingteam_pledge;
    }

    public String getGameStatus() {
        return GameStatus;
    }

    public void setGameStatus() {
        GameStatus = "NotStarted";
    }

    public int getPersonalPledgeAmt() {
        return PersonalPledgeAmt;
    }

    public void setPersonalPledgeAmt() {
        PersonalPledgeAmt = 0;
    }


    public int getGameId() {
        return GameId;
    }

    public void setGameId(int gameId) {
        GameId = gameId;
    }

    public int getHome_Id() {
        return home_Id;
    }

    public void setHome_Id(int home_Id) {
        this.home_Id = home_Id;
    }

    public int getAway_Id() {
        return away_Id;
    }

    public void setAway_Id(int away_Id) {
        this.away_Id = away_Id;
    }

    public String getHome_LongName() {
        return home_LongName;
    }

    public void setHome_LongName(String home_LongName) {
        this.home_LongName = home_LongName;
    }

    public String getAway_LongName() {
        return away_LongName;
    }

    public void setAway_LongName(String away_LongName) {
        this.away_LongName = away_LongName;
    }

    public int getHome_score() {
        return home_score;
    }

    public void setHome_score(int home_score) {
        this.home_score = home_score;
    }

    public int getAway_score() {
        return away_score;
    }

    public void setAway_score(int away_score) {
        this.away_score = away_score;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }

    public String getHomeLogo() {
        return homeLogo;
    }

    public void setHomeLogo(String homeLogo) {
        this.homeLogo = homeLogo;
    }

    public String getAwayLogo() {
        return awayLogo;
    }

    public void setAwayLogo(String awayLogo) {
        this.awayLogo = awayLogo;
    }

    public Bitmap getHomeLogobitmap() {
        return homeLogobitmap;
    }

    public void setHomeLogobitmap(Bitmap homeLogobitmap) {
        this.homeLogobitmap = homeLogobitmap;
    }

    public Bitmap getAwayLogobitmap() {
        return awayLogobitmap;
    }

    public void setAwayLogobitmap(Bitmap awayLogobitmap) {
        this.awayLogobitmap = awayLogobitmap;
    }

    public int getUserteam_id() {
        return userteam_id;
    }

    public void setUserteam_id(int userteam_id) {
        this.userteam_id = userteam_id;
    }

    public void ClearBoard() {
        setAway_score(0);
        setHome_score(0);
        setPeriod(0);
        setPersonalPledgeAmt();
        setTimeLeft("0:00");
    }

    public void ClearGamePledgeTotals() {
        setHometeam_pledge(0);
        setVisitingteam_pledge(0);

    }

    public Game getCurrentGame(Integer gameId) {
        Game mGame = null;
        RequestPackage p = new RequestPackage();
        try {
            String url = String.format(java.util.Locale.ENGLISH, "%s/api/%s", Constant.APISERVERURL, "game");
            String method = "game";


            p.setMethod("POST");
            p.setUri(url);
            p.setParam("token", null);
            p.setParam("page", Integer.toString(0));
            p.setParam("game_id", Integer.toString(gameId));
//            GetMyGame task = new GetMyGame();
//            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p);

        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }

        JsonObjectRequest request = new JsonObjectRequest(p.getUri(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, String.format("The volley response was %s", response.toString()));
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, String.format("The Error from volley response was %s", error.toString()));

                    }
                }
        );
        BaseApplication.getInstance().getRequestQueue().add(request);

        return mGame;
    }



}
