package com.gametimegiving.mobile.Parse;

import com.gametimegiving.mobile.Charity;

import org.json.JSONObject;

public class CharityDetailJSONParser {
    public static Charity parseFeed(String content) {
        try {
            JSONObject jsnobject = new JSONObject(content);
            org.json.JSONArray jsonArray = jsnobject.getJSONArray("items");
            Charity theCharity = new Charity();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                theCharity.setCharityId(obj.getInt("charity_id"));
                theCharity.setCharityName(obj.getString("name"));
                theCharity.setDetail(obj.getString("detail"));
                theCharity.setMission(obj.getString("mission"));
                theCharity.setPurpose(obj.getString("purpose"));
                theCharity.setContactEmail(obj.getString("contactemail"));
                theCharity.setContactPhone(obj.getString("contactphone"));
                theCharity.setLogo(obj.getString("photo"));
            }
            return theCharity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
