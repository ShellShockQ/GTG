package com.gametimegiving.mobile.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gametimegiving.mobile.Activity.GameBoardActivity;
import com.gametimegiving.mobile.Activity.MainActivity;
import com.gametimegiving.mobile.Application.BaseApplication;
import com.gametimegiving.mobile.Game;
import com.gametimegiving.mobile.GamesForTheDay;
import com.gametimegiving.mobile.Parse.BaseApi;
import com.gametimegiving.mobile.Parse.RequestPackage;
import com.gametimegiving.mobile.Player;
import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Utils.Constant;
import com.gametimegiving.mobile.Utils.Utilities;

import java.util.ArrayList;
import java.util.List;

import static com.gametimegiving.mobile.sampledata.SampleData.GetListOfGamesFromSampleData;

public class SelectGameFragment extends BaseFragment implements View.OnClickListener {
    private final static String TAG = "SELECTGAMEFRAGMENT";
    public String[] gameIdArray;
    public Utilities util;
    protected BaseApi mApi;
    private List<Game> listOfGames = new ArrayList<>();
    private Button mBtnSelect;
    private boolean isGameSelected = false;
    private String selctedgame;
    private Integer selectedgameid;
    private Spinner mSelectGameSpinner;
    private ArrayAdapter<Game> adapter;
    private boolean spinnerFlag = true;

    public SelectGameFragment() {
        util = new Utilities();
    }

    public static SelectGameFragment newInstance() {
        SelectGameFragment fragment = new SelectGameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selectgame, container, false);
        mApi = BaseApplication.getInstance().Api;
        mBtnSelect = view.findViewById(R.id.btn_game_select);
        mSelectGameSpinner = view.findViewById(R.id.spinner1);
        mBtnSelect.setOnClickListener(this);
        Player player = new Player();
        GetGames(Constant.APISERVERURL + "/api/game", player);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_game_select:
                if (isGameSelected) {
                    selctedgame = mSelectGameSpinner.getSelectedItem().toString();
                    selectedgameid = Integer.parseInt(gameIdArray[mSelectGameSpinner.getSelectedItemPosition()]);
                    Intent intent = new Intent(getActivity(), GameBoardActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("selectedgame", selctedgame);
                    extras.putInt("selectedgameid", selectedgameid);
                    Integer lastActiveGame = util.ReadSharedPref(Constant.ACTIVEGAME, getActivity());
                    if (lastActiveGame.equals(selectedgameid)) {
                        extras.putBoolean(Constant.ISFIRSTTIMEIN, false);
                    } else {
                        extras.putBoolean(Constant.ISFIRSTTIMEIN, true);
                    }
                    intent.putExtras(extras);
                    util.WriteSharedPref(Constant.ACTIVEGAME, Integer.toString(selectedgameid), getActivity(), "i");
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "please select a game", Toast.LENGTH_SHORT).show();

                }
        }
    }


    public void addListenerOnSpinnerItemSelection(GamesForTheDay s) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setTextColor(Color.BLACK);
                    //  ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //  String theGame;
        int cnt = 0;
        gameIdArray = new String[s.getGameId()];
        ArrayList<Game> TodaysGames = new ArrayList<>();
        for (Game g : TodaysGames) {
            //  theGame = g.getHome_LongName() + " vs " + g.getAway_LongName();
            adapter.add(String.format("%s vs. %s", g.getHomeTeam().getTeamName(), g.getAwayTeam().getTeamName()));
            gameIdArray[cnt] = String.valueOf(g.getGameId());
            cnt++;
        }
        adapter.add("Select a game"); //This is the text that will be displayed as hint.
        Log.d(TAG, String.format("%d items in the gameidarray", gameIdArray.length));

        mSelectGameSpinner.setAdapter(adapter);
        mSelectGameSpinner.setSelection(adapter.getCount()); //set the hint the default selection so it appears on launch.

        mSelectGameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerFlag) {
                    ((TextView) adapterView.getChildAt(0)).setText(R.string.selectgame);
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    ((TextView) adapterView.getChildAt(0)).setTextSize(18);
                    spinnerFlag = false;
                } else {
                    isGameSelected = true;
                    int sid = mSelectGameSpinner.getSelectedItemPosition();
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    ((TextView) adapterView.getChildAt(0)).setTextSize(18);

                }
                //Game selected = parent.getItemAtPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void GetGames(String uri, Player player) {
        RequestPackage p = new RequestPackage();
        p.setParam("page", Integer.toString(0));
        String MyTeams = MainActivity.MyTeamIds.replace("[", "").replace("]", "").replace(" ", "");
        p.setParam("team_ids", MyTeams);
        p.setMethod("POST");
        p.setUri(uri);
        GetGamesAsynch task = new GetGamesAsynch();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p);

    }

    public static class GetGamesAsynch extends AsyncTask<RequestPackage, String, List<Game>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<Game> doInBackground(RequestPackage... strings) {
            Log.d(TAG, "Doing HTTP Get in the Background");
            //String content = HttpManager.getData(strings[0]);
            // GamesForTheDay listOfGames= new GamesForTheDay();
     //           listOfGames = GameJSONParser.parseFeed(content);
            return GetListOfGamesFromSampleData();

        }


        protected void onPostExecute(GamesForTheDay s) {
            Log.d(TAG, String.format("Results of the HTTP Get for Games: %s",s.getTodaysGames().toString()));
            SelectGameFragment selectGameFragment = new SelectGameFragment();
            selectGameFragment.addListenerOnSpinnerItemSelection(s);

        }
    }



}