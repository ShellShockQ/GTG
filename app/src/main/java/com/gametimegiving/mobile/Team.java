package com.gametimegiving.mobile;

import android.graphics.Bitmap;

import java.util.List;

public class Team {
    List<Team> listOfTeams;
    private int teamId;
    private String teamName;
    private String nickName;
    private String logo;
    private Bitmap bitmap;

    private Charity preferredCharity;

    public Charity getPreferredCharity() {
        return preferredCharity;
    }

    public void setPreferredCharity(Charity preferredCharity) {
        this.preferredCharity = preferredCharity;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}