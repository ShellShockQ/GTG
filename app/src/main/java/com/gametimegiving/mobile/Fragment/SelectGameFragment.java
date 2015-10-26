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
import com.gametimegiving.mobile.Application.BaseApplication;
import com.gametimegiving.mobile.Game;
import com.gametimegiving.mobile.Parse.BaseApi;
import com.gametimegiving.mobile.Parse.GameJSONParser;
import com.gametimegiving.mobile.Parse.HttpManager;
import com.gametimegiving.mobile.Parse.RequestPackage;
import com.gametimegiving.mobile.Player;
import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Utils.Utilities;

import java.util.List;

public class SelectGameFragment extends BaseFragment implements View.OnClickListener {
    private final static String TAG = "SelectGameFragment";
    public String[] gameIdArray;
    protected BaseApi mApi;
    List<Game> listOfGames;
    String mApiServerUrl = BaseApplication.getInstance().getMetaData(BaseApplication.META_DATA_API_SERVER_URL);
    private Button mBtnSelect;
    private boolean isGameSelected = false;
    private String selctedgame;
    private String selectedgameid;
    private Spinner mSelectGameSpinner;
    private ArrayAdapter<Game> adapter;
    private boolean spinnerFlag = true;

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
        mBtnSelect = (Button) view.findViewById(R.id.btn_game_select);
        mSelectGameSpinner = (Spinner) view.findViewById(R.id.spinner1);
        mBtnSelect.setOnClickListener(this);
        //TODO: Get games from preferences
        Player player = new Player();

        GetGames(mApiServerUrl + "/api/game", 0, player);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_game_select:

                if (isGameSelected) {
                    selctedgame = mSelectGameSpinner.getSelectedItem().toString();
                    selectedgameid = gameIdArray[mSelectGameSpinner.getSelectedItemPosition()];
                    Intent intent = new Intent(getActivity(), GameBoardActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("selectedgame", selctedgame);
                    extras.putString("selectedgameid", selectedgameid);
                    intent.putExtras(extras);

                    //TODO:Write active game id to sharepreferences

                    Utilities util = new Utilities();
                    util.WriteSharedPref("activegame", selctedgame, getActivity());
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "please select a game", Toast.LENGTH_SHORT).show();

                }
        }
    }


    public void addListenerOnSpinnerItemSelection(List<Game> s) {
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
        gameIdArray = new String[s.size()];
        for (Game g : s) {
            //  theGame = g.getHome_LongName() + " vs " + g.getAway_LongName();
            adapter.add(String.format("%s vs. %s", g.getHome_LongName(), g.getAway_LongName()));
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
                    ((TextView) adapterView.getChildAt(0)).setText("Select a game");
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

    private void GetGames(String uri, int page, Player player) {
        RequestPackage p = new RequestPackage();
        p.setParam("page", Integer.toString(page));
//        p.setParam("team_ids",player.getMyTeams().toString());
        p.setMethod("POST");
        p.setUri(uri);
        GetGamesAsynch task = new GetGamesAsynch();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p);

    }

    public class GetGamesAsynch extends AsyncTask<RequestPackage, String, List<Game>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<Game> doInBackground(RequestPackage... strings) {
            String content = HttpManager.getData(strings[0]);
            listOfGames = GameJSONParser.parseFeed(content);
            return listOfGames;
        }

        @Override
        protected void onPostExecute(List<Game> s) {
            if (s == null) {
                Log.i(TAG, "Can't connect to Webservice");
                return;
            }
            addListenerOnSpinnerItemSelection(s);

        }
    }


}