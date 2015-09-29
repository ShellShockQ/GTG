package com.gametimegiving.mobile.Utils;

import com.gametimegiving.mobile.Adapter.CharityListAdapter;
import com.gametimegiving.mobile.Adapter.TeamListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by T011Prakash on 8/21/2015.
 */
public class Common {
    public List<String> listDataHeader;
    public HashMap<String, List<String>> listDataChild;


    /*
    *
    * Add Items in navigation Drawer Expandable Listview
    * */
    public void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Game");
        listDataHeader.add("Teams");
        listDataHeader.add("Charities");


        listDataHeader.add("Profile");
        listDataHeader.add("Personal Status");
        listDataHeader.add("About Us");
        listDataHeader.add("Settings");
        listDataHeader.add("Logout");

        // Adding child data

        List<String> game = new ArrayList<String>();
        List<String> profile = new ArrayList<String>();
        List<String> personalstatus = new ArrayList<String>();
        List<String> aboutus = new ArrayList<String>();
        List<String> settings = new ArrayList<String>();
        List<String> logout = new ArrayList<String>();

        List<String> charity = new ArrayList<String>();

        List<String> teams = new ArrayList<String>();


        listDataChild.put(listDataHeader.get(0), game);
        if (null == TeamListAdapter.selectedTeams)
            TeamListAdapter.selectedTeams = new ArrayList<>();
        listDataChild.put(listDataHeader.get(1), TeamListAdapter.selectedTeams);
        if (null == CharityListAdapter.selectedCharity)
            CharityListAdapter.selectedCharity = new ArrayList<>();
        listDataChild.put(listDataHeader.get(2), CharityListAdapter.selectedCharity);
        listDataChild.put(listDataHeader.get(3), profile);
        listDataChild.put(listDataHeader.get(4), personalstatus);
        listDataChild.put(listDataHeader.get(5), aboutus);
        listDataChild.put(listDataHeader.get(6), settings);
        listDataChild.put(listDataHeader.get(7), logout);
    }


}
