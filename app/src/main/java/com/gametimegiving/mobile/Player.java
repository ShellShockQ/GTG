package com.gametimegiving.mobile;

public class Player {
    private int player_id;
    private int myteam_id;
    private int myTotalPledgeAmount;
    private int myLastPledgeAmount;
    private Charity[] myCharities;
    private Team[] myTeams;

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public int getMyteam_id() {
        return myteam_id;
    }

    public void setMyteam_id(int myteam_id) {
        this.myteam_id = myteam_id;
    }

    public int getMyTotalPledgeAmount() {
        return myTotalPledgeAmount;
    }

    public void setMyTotalPledgeAmount(int myTotalPledgeAmount) {
        this.myTotalPledgeAmount = myTotalPledgeAmount;
    }

    public Charity[] getMyCharities() {
        return myCharities;
    }

    public void setMyCharities(Charity[] myCharities) {
        this.myCharities = myCharities;
    }

    public Team[] getMyTeams() {
        return myTeams;
    }

    public void setMyTeams(Team[] myTeams) {
        this.myTeams = myTeams;
    }

    public int getMyLastPledgeAmount() {
        return myLastPledgeAmount;
    }

    public void setMyLastPledgeAmount(int myLastPledgeAmount) {
        this.myLastPledgeAmount = myLastPledgeAmount;
    }
}
