package com.gametimegiving.mobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    private final static String TAG = "TeamList";
    public static boolean[] selectedItems;
    public static ArrayList<String> selectedTeams = new ArrayList<>();
    Context context;
    String[] objects;
    private List<Team> listOfTeams;

    public TeamListAdapter(Context context, int resource, List<Team> objects) {
        this.context = context;
        this.listOfTeams = objects;
        if (selectedItems == null && listOfTeams != null) {
            selectedItems = new boolean[listOfTeams.size()];
            for (int i = 0; i < listOfTeams.size(); i++)
                selectedItems[i] = false;
        }
        if (null == selectedTeams)
            selectedTeams = new ArrayList<>();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        if (null != listOfTeams)
            return listOfTeams.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return objects[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        notifyDataSetChanged();
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_row, parent, false);
        Team team = listOfTeams.get(position);

        TextView label = (TextView) row.findViewById(R.id.spinner_text);
        Button btn = (Button) row.findViewById(R.id.btn_more);
        Button btnSubmit = (Button) row.findViewById(R.id.btn_submit);
        CheckBox chk = (CheckBox) row.findViewById(R.id.chk);
        btn.setVisibility(View.GONE);
        chk.setChecked(selectedItems[position]);
        chk.setTag(position);
        chk.setOnCheckedChangeListener(this);

        label.setText(team.getTeamName());
//        if (position == objects.length - 1) {
//
//        }
        return row;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int position = (int) compoundButton.getTag();
        selectedItems[position] = b;
        Team team = listOfTeams.get(position);
        String teamName = team.getTeamName();
        if (b) {
            if (selectedTeams.size() <= 4) {
                selectedTeams.add(teamName);
            } else {
                selectedItems[position] = !b;
                compoundButton.setChecked(!b);
            }
        } else {
            if (selectedTeams.contains(teamName))
                selectedTeams.remove(teamName);
        }

    }

}
