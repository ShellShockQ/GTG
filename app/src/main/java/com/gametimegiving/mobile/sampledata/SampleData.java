package com.gametimegiving.mobile.sampledata;

import com.gametimegiving.mobile.Game;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Detrick on 1/25/2018.
 */

public  class SampleData {
    static public List<Game> GetListOfGamesFromSampleData() {
        String JSONGames= "{\"games\":[{\"game_id\":\"000\",\"home_long\":\"Dallas Cowboys\",\"away_long\":\"Pittsburgh Steelers\"},{\"game_id\":\"001\",\"home_long\":\"LA Clippers\",\"away_long\":\"Milwaukee Bucks\"}]}";
        Date today = Calendar.getInstance().getTime();
        List<Game> SampleGames=null;
        Game game1 = new Game();
        game1.setGameId(0);
        game1.setHome_LongName("Dallas Cowboys");
        game1.setAway_LongName("Pittsburgh Steelers");
        game1.setStartdate(today);
        SampleGames.add(game1);
        Game game2 = new Game();
        game2.setGameId(1);
        game2.setHome_LongName("LA Clippers");
        game2.setAway_LongName("Milwaukee Bucks");
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
}
