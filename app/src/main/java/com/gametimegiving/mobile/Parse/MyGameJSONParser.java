package com.gametimegiving.mobile.Parse;

import com.gametimegiving.mobile.Game;
import com.gametimegiving.mobile.Utils.Constant;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyGameJSONParser {
    public static Game parseFeed(String content) {
        try {
            JSONObject jsnobject = new JSONObject(content);
            org.json.JSONArray jsonArray = jsnobject.getJSONArray("items");
            Game game = new Game();
            int i = 0;
                JSONObject obj = jsonArray.getJSONObject(i);
                game.setGameId(obj.getInt("game_id"));
            game.getHomeTeam().setTeamName(obj.getString("home_long"));
                game.setHome_score(obj.getInt("home_score"));
            game.getAwayTeam().setTeamName(obj.getString("away_long"));
                game.setAway_score(obj.getInt("away_score"));
            game.setPeriod(obj.getString("period"));
                game.setTimeLeft(obj.getString("clock"));
                game.setHometeam_pledge(obj.getInt("home_pledge"));
                game.setVisitingteam_pledge(obj.getInt("away_pledge"));
                game.setHomeLogo(obj.getString("home_photo"));
                game.setAwayLogo(obj.getString("away_photo"));
            game.getHomeTeam().setTeamId(obj.getInt("home_id"));
            game.getAwayTeam().setTeamId(obj.getInt("away_id"));
            game.setStarttime(obj.getString("start_dtm"));
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dStartTime = formatter.parse(game.getStarttime());
                Calendar calendar = Calendar.getInstance();
                if (dStartTime.before(calendar.getTime())) {
                    game.setGameStatus(Constant.GAMENOTSTARTED);
                } else {
                    game.setGameStatus(Constant.GAMEINPROGRESS);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            return game;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
