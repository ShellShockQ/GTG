package com.gametimegiving.mobile.Utils;

import com.gametimegiving.mobile.Adapter.CharityListAdapter;
import com.gametimegiving.mobile.Adapter.TeamListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Common {
    public List<String> listDataHeader;
    public HashMap<String, List<String>> listDataChild;


    /*
    *
    * Add Items in navigation Drawer Expandable Listview
    * */
    public void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

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

        List<String> game = new ArrayList<>();
        List<String> profile = new ArrayList<>();
        List<String> personalstatus = new ArrayList<>();
        List<String> aboutus = new ArrayList<>();
        List<String> settings = new ArrayList<>();
        List<String> logout = new ArrayList<>();

        List<String> charity = new ArrayList<>();

        List<String> teams = new ArrayList<>();


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
