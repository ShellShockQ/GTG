package com.gametimegiving.mobile;

import android.graphics.Bitmap;

import java.util.List;

public class Team {
    List<Team> listOfTeams;
    private int teamId;
    private String teamName;
    private String nickName;
    private Bitmap logo;

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

    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }


}