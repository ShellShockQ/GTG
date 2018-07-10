package com.gametimegiving.mobile.sampledata;

import com.gametimegiving.mobile.Game;
import com.gametimegiving.mobile.Team;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Detrick on 1/25/2018.
 */

public  class SampleData {
    public static Date today = Calendar.getInstance().getTime();

    static public List<Game> GetListOfGamesFromSampleData() {
        String JSONGames= "{\"games\":[{\"game_id\":\"000\",\"home_long\":\"Dallas Cowboys\",\"away_long\":\"Pittsburgh Steelers\"},{\"game_id\":\"001\",\"home_long\":\"LA Clippers\",\"away_long\":\"Milwaukee Bucks\"}]}";

        List<Game> SampleGames = new ArrayList<>();
        Game game1 = new Game();
        game1.setGameId(0);
        game1.getHomeTeam().setTeamName("Dallas Cowboys");
        game1.getAwayTeam().setTeamName("Pittsburgh Steelers");
        game1.setStartdate(today);
        SampleGames.add(game1);
        Game game2 = new Game();
        game2.setGameId(1);
        game2.getHomeTeam().setTeamName("LA Clippers");
        game2.getAwayTeam().setTeamName("Milwaukee Bucks");
        game2.setStartdate(today);
        SampleGames.add(game2);
//        Game game3 = new Game();
//        SampleGames.add(game3);
//        Game game4 = new Game();
//        SampleGames.add(game4);
//        Game game5 = new Game();
//        SampleGames.add(game5);
//        Game game6 = new Game();
//        SampleGames.add(game6);
        return SampleGames;
    }

    public boolean IsTestRunning(boolean bool){

        return bool;
    }

    public String SampleCharityData() {
        String SampleCharityJSON = "results{\'name:Charity One\',\'001\',\'name:Charity Two\',\'002\'}";
        return SampleCharityJSON;
    }

    public static Game getDemoGame() {
        Game game1 = new Game();
        Team HomeTeam = new Team();
        HomeTeam.setTeamId(1);
        HomeTeam.setTeamName("Dallas Cowboys");
        Team AwayTeam = new Team();
        AwayTeam.setTeamId(2);
        AwayTeam.setTeamName("Pittsburgh Steelers");
        game1.setGameId(0);
        game1.setHome_score(7);
        game1.setAway_score(14);
        game1.setStartdate(today);
        game1.setPeriod(3);
        game1.setHomeTeam(HomeTeam);
        game1.setAwayTeam(AwayTeam);
        return game1;

    }
}
