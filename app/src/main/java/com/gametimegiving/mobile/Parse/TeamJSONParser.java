package com.gametimegiving.mobile.Parse;

import com.gametimegiving.mobile.Team;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeamJSONParser {
    public static List<Team> parseFeed(String content) {
        try {
            JSONObject jsnobject = new JSONObject(content);
            org.json.JSONArray jsonArray = jsnobject.getJSONArray("items");
            List<Team> listofTeams = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Team team = new Team();
                team.setTeamId(obj.getInt("team_id"));
                team.setTeamName(obj.getString("name"));
                listofTeams.add(team);
            }

            return listofTeams;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
