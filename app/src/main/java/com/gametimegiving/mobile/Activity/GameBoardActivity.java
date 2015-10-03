
package com.gametimegiving.mobile.Activity;
/***************************************************************************************
 * FileName : GameBoardActivity.java
 * <p/>
 * Description : This is GameBoardActivity where user see detail of select team and donate money of charity.
 ***************************************************************************************/

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gametimegiving.mobile.Application.BaseApplication;
import com.gametimegiving.mobile.Game;
import com.gametimegiving.mobile.Parse.HttpManager;
import com.gametimegiving.mobile.Parse.MyGameJSONParser;
import com.gametimegiving.mobile.Parse.RequestPackage;
import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Team;
import com.gametimegiving.mobile.Utils.CustomizeDialog;
import com.gametimegiving.mobile.Utils.Utilities;

import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class GameBoardActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "GameBoard";
    private static final String LOGO_BASE_URL = BaseApplication.getInstance().getMetaData(BaseApplication.META_DATA_LOGO_BASE_URL);
    final int MaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int cacheSize = MaxMemory / 8;
    String mApiServerUrl = BaseApplication.getInstance().getMetaData(BaseApplication.META_DATA_API_SERVER_URL);
    Utilities util = new Utilities();
    private double mTeamPldgesValue = 15;
    private double mYourPldgesValue = 5;
    private double mLastPladgeValue = 0;
    private TextView tvPledges, tv_$15;
    private Button btn_$1, btn_$2, btn_$5;
    private Context context;
    private String[] arr = null;
    private TextView mTvYourTeam, mTvOpponentTeam;
    private Button mUndoLastPledge;
    private String GameStatus;
    private String mToken;
    private LruCache<Integer, Bitmap> imageMemCache;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gameboard);
        View l = findViewById(R.id.pledgeButtons);
        View ppl = findViewById(R.id.personalpledgelayout);
        Button btn = (Button) findViewById(R.id.btnundolastpledge);
        Button btnPay = (Button) findViewById(R.id.paynow);
        tvPledges = (TextView) findViewById(R.id.pledges);

        Game game = new Game();

        //TODO:Determine Game Status based on the time
        game.setGameStatus("InProgress");
        /* ********************************************** */

        switch (game.getGameStatus()) {
            case "NotStarted":
                TurnOffPledgeMechanisms(l, ppl, btn, btnPay);
                game.ClearBoard();
                break;
            case "InProgress":
                l.setVisibility(View.VISIBLE);
                btn.setVisibility(View.VISIBLE);
                btnPay.setVisibility(View.GONE);

                break;
            case "GameOver":
                if (tvPledges.getText() != "$0.00") {
                    l.setVisibility(View.GONE);
                    btn.setVisibility(View.GONE);
                    btnPay.setVisibility(View.VISIBLE);
                    btnPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handlePayNow();
                        }
                    });
                } else {
                    util.ShowMsg(this, "You have no pledges!");

                }
                break;
        }

        imageMemCache = new LruCache<>(cacheSize);
        //Set Period
        String ag = util.ReadSharedPref("activegame", this);
        //TODO:SetTeam logo photo
        //TODO:Get up to date game data

        context = this;

        mTvYourTeam = (TextView) findViewById(R.id.tvyourteam);

        mUndoLastPledge = (Button) findViewById(R.id.btnundolastpledge);

        mUndoLastPledge.setOnClickListener(this);

        mUndoLastPledge.setEnabled(false);

        mTvOpponentTeam = (TextView) findViewById(R.id.tvopponentteam);


        btn_$1 = (Button) findViewById(R.id.btn_$1);

        btn_$2 = (Button) findViewById(R.id.btn_$2);
        btn_$5 = (Button) findViewById(R.id.btn_$5);

        btn_$1.setOnClickListener(this);
        btn_$2.setOnClickListener(this);
        btn_$5.setOnClickListener(this);


        showdialog();
        getGame(260, 0);
        //  Bitmap homeTeamLogo = getTeamLogo("PHI");
//        ImageView homelogo = (ImageView) findViewById(R.id.hometeamlogo);
//        homelogo.setImageBitmap(homeTeamLogo);
//        Bitmap awayTeamLogo = getTeamLogo("DAL");
//        ImageView awaylogo = (ImageView) findViewById(R.id.awayteamlogo);
//        awaylogo.setImageBitmap(awayTeamLogo);

    }

    private void TurnOffPledgeMechanisms(View l, View ppl, Button btn, Button btnPay) {
        ppl.setVisibility(View.GONE);
        l.setVisibility(View.GONE);
        btn.setVisibility(View.GONE);
        btnPay.setVisibility(View.GONE);
    }

    /*
    *
    * Select Game Show in GameBoard Screen
    * */
    private void getGameName() {
        if (getIntent().getExtras() != null) {
            String games = getIntent().getExtras().getString("selectedgame");
//            arr = games.split("vs");
//            mTvYourTeam.setText(arr[0]);
//            mTvOpponentTeam.setText(arr[1]);

        }
    }


    /*
    * Add Pledges
    *
    * */
    private void addPledges(int value) {

        mUndoLastPledge.setEnabled(true);
        mLastPladgeValue = value;
        mTeamPldgesValue = mTeamPldgesValue + value;
        mYourPldgesValue = mYourPldgesValue + value;

        tvPledges.setText(util.FormatCurrency(mYourPldgesValue));
        //TODO: Add personal pledges to server totals
    }


    /*
    *
    * Undo Pledges
    *
    * */
    private void undoPladgesValue() {
        mUndoLastPledge.setEnabled(false);
        mTeamPldgesValue = mTeamPldgesValue - mLastPladgeValue;
        mYourPldgesValue = mYourPldgesValue - mLastPladgeValue;

        tvPledges.setText(util.FormatCurrency(mYourPldgesValue));
        //TODO:Remove personal pledge from server totals
    }

    /*
    * Show Dialog For Exit Application
    *
    * */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GameBoardActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void showdialog() {

        final CustomizeDialog customizeDialog = new CustomizeDialog(context);
        customizeDialog.setContentView(R.layout.undolastpledgedialog);
        TextView YourTeamName = (TextView) customizeDialog.findViewById(R.id.tvyourteamname);
        TextView SupportTeamName = (TextView) customizeDialog.findViewById(R.id.tvsupportteamname);
        Button button = (Button) customizeDialog.findViewById(R.id.btnok);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customizeDialog.dismiss();
            }
        });

        if (getIntent().getExtras() != null) {
            String games = getIntent().getExtras().getString("selectedgame");
            if (games != null) {
                arr = games.split("vs");
                YourTeamName.setText(arr[0]);
                SupportTeamName.setText(arr[1]);
            }
        }
        customizeDialog.show();
        customizeDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_$1:
                openDailogPledgesAdd(1);
                addPledges(1);
                break;
            case R.id.btn_$2:
                openDailogPledgesAdd(2);
                addPledges(2);
                break;
            case R.id.btn_$5:
                openDailogPledgesAdd(5);
                addPledges(5);
                break;
            case R.id.btnundolastpledge:
                undoPladgesValue();
                break;
        }
    }

    private void openDailogPledgesAdd(int pledge_value) {

        final CustomizeDialog customizeDialog = new CustomizeDialog(context);
        customizeDialog.setContentView(R.layout.dilogpledges);
        TextView tv_pledge_donation = (TextView) customizeDialog.findViewById(R.id.tv_pledge_donation);
        tv_pledge_donation.setText(util.FormatCurrency(pledge_value) + " Pledge");
        customizeDialog.show();
        customizeDialog.setCancelable(false);
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                customizeDialog.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 2000);
    }

    private void handlePayNow() {
        util.ShowMsg(this, "Calling Braintree");
    }

//    private void GetMyGame(String uri) {
//        //String url = String.format(java.util.Locale.ENGLISH,  "%s/api/%s", mApiServerUrl, "game");
//        String url = String.format(java.util.Locale.ENGLISH, "%s/dwn/%s", mApiServerUrl, "testgame.php");
//        RequestPackage p = new RequestPackage();
//        p.setMethod("POST");
//        p.setUri(url);
//        GetMyGame task = new GetMyGame();
//        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p);
//
//    }

    public void UpdateGameBoard(Game game) {
        Team homeTeam = null;
        Team awayTeam = null;

        TextView tv_homeTeamScore = (TextView) findViewById(R.id.tv_HomeTeamScore);
        TextView tv_awayTeamScore = (TextView) findViewById(R.id.tv_AwayTeamScore);
        TextView pledges = (TextView) findViewById(R.id.pledges);
        TextView tv_HomeTeamPledges = (TextView) findViewById(R.id.tv_HomeTeamPledges);
        TextView tv_AwayTeamPledges = (TextView) findViewById(R.id.tv_AwayTeamPledges);
        tv_homeTeamScore.setText(Integer.toString(game.getHome_score()));
        tv_awayTeamScore.setText(Integer.toString(game.getAway_score()));
        mTvYourTeam.setText(game.getHome_LongName());
        mTvOpponentTeam.setText(game.getAway_LongName());
        pledges.setText(String.format("%s", util.FormatCurrency(game.getPersonalPledgeAmt())));
        TextView tv_GamePeriod = (TextView) findViewById(R.id.tv_GamePeriod);
        tv_GamePeriod.setText(String.format("%s in %s", game.getTimeLeft(), Integer.toString(game.getPeriod())));
        tv_HomeTeamPledges.setText(util.FormatCurrency(game.getHometeam_pledge(), 0));
        tv_AwayTeamPledges.setText(util.FormatCurrency(game.getVisitingteam_pledge(), 0));
        ImageView homeLogo = (ImageView) findViewById(R.id.hometeamlogo);
        ImageView awayLogo = (ImageView) findViewById(R.id.awayteamlogo);
        homeLogo.setImageBitmap(game.getHomeLogobitmap());
        awayLogo.setImageBitmap(game.getAwayLogobitmap());
        //TODO:Increase the size of the images if possible
    }

    public void getGame(int game_id, int page) {
        try {
            String url = String.format(java.util.Locale.ENGLISH, "%s/api/%s", mApiServerUrl, "game");
            String method = "game";

            RequestPackage p = new RequestPackage();
            p.setMethod("POST");
            p.setUri(url);
            p.setParam("token", null);
            p.setParam("page", "0");
            p.setParam("game_id", Integer.toString(game_id));
            GetMyGame task = new GetMyGame();
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p);

        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    public Bitmap getBitmapFromMemCache(int key) {
        try {
            return imageMemCache.get(key);
        } catch (Exception e) {
            return null;

        }

    }

    public void addBitmapToMemoryCache(int key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            imageMemCache.put(key, bitmap);
        }
    }

    public class Imageloader extends AsyncTask<String, Void, Team> {

        @Override
        protected Team doInBackground(String... Team) {
            Team t = new Team();
            try {
                String imageurl = LOGO_BASE_URL + t.getLogo();
                InputStream in = (InputStream) new URL(imageurl).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                t.setBitmap(bitmap);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        // @Override
        //protected void onPostExecute(Bitmap image) {
        //     ImageView image = (ImageView)result.view.findViewById(R.id.imageView1);
        //     image.setImageBitmap(result.bitmap);
        //     //result.flower.setBitmap(result.bitmap);
        //     imageCache.put(result.flower.getProductId(),result.bitmap);
        //}


    }
    public class GetMyGame extends AsyncTask<RequestPackage, String, Game> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Game doInBackground(RequestPackage... strings) {

            String content = HttpManager.getData(strings[0]);
            Game game = MyGameJSONParser.parseFeed(content);
            String imageurl;
            Bitmap homeBitmap;
            Bitmap awayBitmap;
            InputStream homeIn;
            InputStream awayIn;

            try {
                homeBitmap = getBitmapFromMemCache(game.getHome_Id());
                awayBitmap = getBitmapFromMemCache(game.getAway_Id());
                if (homeBitmap == null) {
                    String homelogourl = LOGO_BASE_URL + game.getHomeLogo();
                    homeIn = (InputStream) new URL(homelogourl).getContent();
                    homeBitmap = BitmapFactory.decodeStream(homeIn);
                    game.setHomeLogobitmap(homeBitmap);
                    homeIn.close();
                    addBitmapToMemoryCache(game.getHome_Id(), homeBitmap);
                } else {
                    game.setHomeLogobitmap(homeBitmap);
                }

                if (awayBitmap == null) {
                    String awaylogourl = LOGO_BASE_URL + game.getAwayLogo();
                    awayIn = (InputStream) new URL(awaylogourl).getContent();
                    awayBitmap = BitmapFactory.decodeStream(awayIn);
                    game.setAwayLogobitmap(awayBitmap);
                    awayIn.close();
                    addBitmapToMemoryCache(game.getAway_Id(), awayBitmap);
                } else {
                    game.setAwayLogobitmap(awayBitmap);
                }


            } catch (Exception e) {
                e.printStackTrace();

            }
            return game;
        }

        @Override
        protected void onPostExecute(Game game) {
            if (game == null) {
                Log.i(TAG, "Can't connect to Webservice");
                return;
            }
            UpdateGameBoard(game);

        }

    }
}





