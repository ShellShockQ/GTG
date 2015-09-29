package com.gametimegiving.mobile.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gametimegiving.mobile.Game;

import java.util.List;

public class GameListSpinnerAdapter extends ArrayAdapter<Game> {
    String[] objects;
    String theGame = "Select My Game";
    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private List<Game> listOfGames;

    public GameListSpinnerAdapter(Context context, int textViewResourceId,
                                  List<Game> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.listOfGames = objects;
    }

    @Override
    public int getCount() {
        return listOfGames.size();
    }

    @Override
    public Game getItem(int position) {
        return listOfGames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        Game game = listOfGames.get(position);
        label.setTextColor(Color.BLACK);
        label.setTextSize(16);
        theGame = game.getHome_LongName() + " vs. " + game.getAway_LongName();
        label.setText(theGame);

        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
//            Game game = listOfGames.get(position);
        TextView label = new TextView(context);
//            label.setTextColor(Color.BLACK);
//            label.setText(game.getHome_LongName());
//            label.setTextSize(18);
//           // ((TextView) adapterView.getChildAt(0)).setText("Select My Game");
        // ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
        // ((TextView) adapterView.getChildAt(0)).setTextSize(18);

        return label;
    }
}