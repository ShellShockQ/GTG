package com.gametimegiving.mobile.Parse;

import com.gametimegiving.mobile.Game;

import org.json.JSONObject;

public class MyGameJSONParser {
    public static Game parseFeed(String content) {
        try {
            JSONObject jsnobject = new JSONObject(content);
            org.json.JSONArray jsonArray = jsnobject.getJSONArray("items");
            Game game = new Game();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                game.setGameId(obj.getInt("game_id"));
                game.setHome_LongName(obj.getString("home_long"));
                game.setHome_score(obj.getInt("home_score"));
                game.setAway_LongName(obj.getString("away_long"));
                game.setAway_score(obj.getInt("away_score"));
                game.setPeriod(obj.getInt("period"));
                game.setTimeLeft(obj.getString("clock"));
                game.setHometeam_pledge(obj.getInt("home_pledge"));
                game.setVisitingteam_pledge(obj.getInt("away_pledge"));

            }

            return game;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
