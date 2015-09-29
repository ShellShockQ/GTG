package com.gametimegiving.mobile.Parse;

import com.gametimegiving.mobile.Charity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CharityJSONParser {
    public static List<Charity> parseFeed(String content) {
        try {
            JSONObject jsnobject = new JSONObject(content);
            org.json.JSONArray jsonArray = jsnobject.getJSONArray("items");
            List<Charity> listofCharities = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Charity charity = new Charity();
                charity.setCharityId(obj.getInt("charity_id"));
                charity.setCharityName(obj.getString("name"));
                listofCharities.add(charity);
            }

            return listofCharities;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
