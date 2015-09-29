package com.gametimegiving.mobile;

import android.graphics.Bitmap;

import java.util.List;

public class Charity {
    List<Charity> listOfCharities;
    private int CharityId;
    private String CharityName;
    private String Summary;
    private String Description;
    private Bitmap logo;

    public int getCharityId() {
        return CharityId;
    }

    public void setCharityId(int charityId) {
        this.CharityId = charityId;
    }

    public String getCharityName() {
        return CharityName;
    }

    public void setCharityName(String teamName) {
        this.CharityName = teamName;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        this.Summary = summary;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

}