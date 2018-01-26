package com.gametimegiving.mobile.Parse;

import com.gametimegiving.mobile.Game;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameJSONParser {
    public static List<Game> parseFeed(String content) {
        try {
            JSONObject jsonobject = new JSONObject(content);
            org.json.JSONArray jsonArray = jsonobject.getJSONArray("games");
            List<Game> listofGames = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Game game = new Game();
                game.setGameId(obj.getInt("game_id"));
                game.setHome_LongName(obj.getString("home_long"));
                game.setAway_LongName(obj.getString("away_long"));
                listofGames.add(game);
            }
            return listofGames;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
