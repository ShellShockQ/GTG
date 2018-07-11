package com.gametimegiving.mobile;

import android.graphics.Bitmap;

import com.gametimegiving.mobile.Parse.RequestPackage;
import com.gametimegiving.mobile.Utils.Constant;
import com.gametimegiving.mobile.sampledata.SampleData;

import java.util.Date;

public class Game {
    private final String TAG = getClass().getSimpleName();
    private static String GameStatus;
    private int GameId;
    private Team HomeTeam;
    private Team AwayTeam;
    private Team MyTeam;
    private int home_score;
    private int away_score;
    private String period;

    public Game() {
        setGameStatus(Constant.GAMEINPROGRESS);
        setTimeLeft("00:00");
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    private Date startdate;
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
        try {
            this.hometeam_pledge = hometeam_pledge;
        }catch (Exception ex){
            this.hometeam_pledge=0;
        }
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

    public void setGameStatus(String gameStatus) {
        GameStatus = gameStatus;
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
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
        setPeriod("1st");
        setPersonalPledgeAmt();
        setTimeLeft("0:00");
    }

    public void ClearGamePledgeTotals() {
        setHometeam_pledge(0);
        setVisitingteam_pledge(0);

    }

    public Team getAwayTeam() {
        return AwayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        AwayTeam = awayTeam;
    }

    public Team getHomeTeam() {
        return HomeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        HomeTeam = homeTeam;
    }

    public Team getMyTeam() {
        return MyTeam;
    }

    public void setMyTeam(Team myTeam) {
        MyTeam = myTeam;
    }
    public Game getCurrentGame() {
        int gameId = 1;
        //TODO: This class needs to determine the current game based on location, a link, demo, etc.
        //TODO: Make an ajax call to get live game data
        Game mGame = SampleData.getDemoGame();
        RequestPackage p = new RequestPackage();
//        try {
//            String url = String.format(java.util.Locale.ENGLISH, "%s/api/%s", Constant.APISERVERURL, "game");
//            String method = "game";
//            p.setMethod("POST");
//            p.setUri(url);
//            p.setParam("token", null);
//            p.setParam("page", Integer.toString(0));
//            p.setParam("game_id", Integer.toString(gameId));
//        } catch (Exception exc) {
//            Log.e(TAG, exc.toString());
//        }
        return mGame;
    }


}
