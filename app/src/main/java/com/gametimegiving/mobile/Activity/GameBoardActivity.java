
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
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.braintreepayments.api.dropin.Customization;
import com.gametimegiving.mobile.Application.BaseApplication;
import com.gametimegiving.mobile.Game;
import com.gametimegiving.mobile.Parse.HttpManager;
import com.gametimegiving.mobile.Parse.MyGameJSONParser;
import com.gametimegiving.mobile.Parse.RequestPackage;
import com.gametimegiving.mobile.Payment;
import com.gametimegiving.mobile.Player;
import com.gametimegiving.mobile.Pledge;
import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Team;
import com.gametimegiving.mobile.Utils.Constant;
import com.gametimegiving.mobile.Utils.CustomizeDialog;
import com.gametimegiving.mobile.Utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GameBoardActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "GAMEBOARD";
    private static final String LOGO_BASE_URL = BaseApplication.getInstance().getMetaData(BaseApplication.META_DATA_LOGO_BASE_URL);
    private final static int SUBMIT_PAYMENT_REQUEST_CODE = 100;
    final int MaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int cacheSize = MaxMemory / 8;
    final Handler handler = new Handler();
    private final Player player = new Player();
    public Integer ActiveGameID;
    public Integer mUserTeamID;
    public String ClientToken;
    public Payment payment;
    public String MyPledgeAmount;
    public Boolean PreferredCharityNoticeShown;
    public boolean bFirstTimeIn = true;
    public Game mGame = new Game();
    Utilities utilities = new Utilities();
    TextView tvMyTeamPledgeTotals;
    Timer timer;
    TimerTask timerTask;
    private Integer mMyTeamsPledgeTotals = 0;
    private Integer mTheirTeamsPledgeTotals = 0;
    private Integer mMyPledgeTotals = 0;
    private Integer mMyLastPledge = 0;
    private TextView tvPledges, tv_$15;
    private Button btn_$1, btn_$2, btn_$5;
    private Context context;
    private String[] arr = null;
    private TextView mTvYourTeam, mTvOpponentTeam;
    private Button mUndoLastPledge;
    private String mToken;
    private LruCache<Integer, Bitmap> imageMemCache;
    private Button btn;
    private Button btnPay;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        player.setPlayer_id();
        setContentView(R.layout.gameboard);
        startTimer();


        tvPledges = (TextView) findViewById(R.id.pledges);
        if (getIntent().getExtras() != null) {
            try {
                Bundle extras = getIntent().getExtras();
                bFirstTimeIn = extras.getBoolean(Constant.ISFIRSTTIMEIN);
                ActiveGameID = extras.getInt("selectedgameid");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
        getCurrentGame(ActiveGameID);

        //TODO:Determine Game Status based on the time

        /* ********************************************** */


        imageMemCache = new LruCache<>(cacheSize);
        //Set Period
        Integer ag = utilities.ReadSharedPref("activegame", this);


        context = this;

        mTvYourTeam = (TextView) findViewById(R.id.tvyourteam);

        mUndoLastPledge = (Button) findViewById(R.id.btnundolastpledge);

        mUndoLastPledge.setOnClickListener(this);

        mUndoLastPledge.setEnabled(false);

        mTvOpponentTeam = (TextView) findViewById(R.id.tvopponentteam);

        //getGame(ActiveGameID);


        btn_$1 = (Button) findViewById(R.id.btn_$1);
        btn_$2 = (Button) findViewById(R.id.btn_$2);
        btn_$5 = (Button) findViewById(R.id.btn_$5);

        btn_$1.setOnClickListener(this);
        btn_$2.setOnClickListener(this);
        btn_$5.setOnClickListener(this);
        if (bFirstTimeIn) {
            showdialog();
        }

    }

    private void SetGameBoard(Game mGame) {
        View l = findViewById(R.id.pledgeButtons);
        View ppl = findViewById(R.id.personalpledgelayout);
        Button btn = (Button) findViewById(R.id.btnundolastpledge);
        Button btnPay = (Button) findViewById(R.id.paynow);
        switch (mGame.getGameStatus()) {
            case Constant.GAMENOTSTARTED:
                TurnOffPledgeMechanisms(l, ppl, btn, btnPay);
                mGame.ClearBoard();
                break;
            case Constant.GAMEINPROGRESS:
                l.setVisibility(View.VISIBLE);
                btn.setVisibility(View.VISIBLE);
                btnPay.setVisibility(View.GONE);

                break;
            case Constant.GAMEOVER:
                GenerateClientToken();
                if (tvPledges.getText() != "$0.00") {
                    MyPledgeAmount = tvPledges.getText().toString();
                    l.setVisibility(View.GONE);
                    btn.setVisibility(View.GONE);
                    btnPay.setVisibility(View.VISIBLE);
                    btnPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Customization customization = new Customization.CustomizationBuilder()
                                    .primaryDescription("My Pledge  ")
                                    .amount(MyPledgeAmount)
                                    .submitButtonText("Donate Now")
                                    .build();
                            Intent intent = new Intent(context, BraintreePaymentActivity.class)
                                    .putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN,
                                            ClientToken);
                            intent.putExtra(BraintreePaymentActivity.EXTRA_CUSTOMIZATION, customization);
                            startActivityForResult(intent, SUBMIT_PAYMENT_REQUEST_CODE);

                        }
                    });
                } else {
                    utilities.ShowMsg("You have no pledges", this);

                }
                break;
        }
    }

    private void startTimer() {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        RefreshGame();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, 10000); //

    }

    public void RefreshGame() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        getCurrentGame(ActiveGameID);
                    }
                });
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SUBMIT_PAYMENT_REQUEST_CODE) {
            if (resultCode == BraintreePaymentActivity.RESULT_OK) {
                String nonce = data.getStringExtra(
                        BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                );

                SendNonceToBraintree(nonce, MyPledgeAmount);
            }
        }
    }
    private void TurnOffPledgeMechanisms(View l, View ppl, Button btn, Button btnPay) {
        ppl.setVisibility(View.GONE);
        l.setVisibility(View.GONE);
        btn.setVisibility(View.GONE);
        btnPay.setVisibility(View.GONE);
    }

    public void SendNonceToBraintree(String nonce, String donation) {
        RequestPackage p = new RequestPackage();
        String method = "nonce";
        p.setMethod("POST");
        p.setParam("payment_method_nonce", nonce);
        p.setParam("amount", donation);
        p.setUri(String.format("%s/api/%s", Constant.APISERVERURL, method));
        SendNonceTask task = new SendNonceTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p);

    }

    public void getCurrentGame(Integer gameId) {

        final RequestPackage p = new RequestPackage();
        try {
            String method = "game";
            p.setMethod("POST");
            p.setUri(String.format(java.util.Locale.ENGLISH, "%s/api/%s", Constant.APISERVERURL, "game"));
            p.setParam("token", null);
            p.setParam("page", Integer.toString(0));
            p.setParam("game_id", Integer.toString(gameId));
            String args = p.getEncodedParams();
            StringRequest request = new StringRequest(Request.Method.POST,
                    p.getUri(),
                    new Response.Listener<String>() {
                        @Override

                        public void onResponse(String response) {
                            mGame = MyGameJSONParser.parseFeed(response);
                            SetGameBoard(mGame);
                            UpdateGameBoard(mGame);
                            mGame.setUserteam_id(mGame.getHome_Id());
                            mUserTeamID = mGame.getUserteam_id();

                        }
                    },

                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, String.format("The Error from volley response was %s", error.toString()));

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("args", p.getEncodedParams().replace("args=", ""));
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }


            };
            BaseApplication.getInstance().addToRequestQueue(request);

            Log.d(TAG, String.format("The volley request was:%s", request.toString()));
        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }


    }
    /*
    *
    * Select Game Show in GameBoard Screen
    * */
    private void getGameName() {
        if (getIntent().getExtras() != null) {
            String games = getIntent().getExtras().getString("selectedgame");

        }
    }

    public void GenerateClientToken() {
        RequestPackage p = new RequestPackage();
        String method = "token";
        p.setMethod("POST");
        p.setUri(String.format("%s/api/%s", Constant.APISERVERURL, method));
        GetClientTokenTask task = new GetClientTokenTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p);
    }

    /*
    * Add Pledges
    *
    * */
    private void addPledges(int value, int game_id, int team_id) {
        mUndoLastPledge.setEnabled(true);
        mMyLastPledge = value;
        tvMyTeamPledgeTotals = (TextView)findViewById(R.id.tv_HomeTeamPledges);
        player.setMyLastPledgeAmount(value);
        utilities.WriteSharedPref(Constant.GAMEDATAREFRESHED, "false", this, "b");
        utilities.WriteSharedPref(Constant.LASTPLEDGEDAMOUNT, Integer.toString(player.getMyTotalPledgeAmount(this)), this, "i");

        player.setMyTotalPledgeAmount(player.getMyTotalPledgeAmount(this) + value);
        mMyPledgeTotals = player.getMyTotalPledgeAmount(this);
        tvPledges.setText(utilities.FormatCurrency(player.getMyTotalPledgeAmount(this)));
        utilities.WriteSharedPref(Constant.MYTOTALPLEDGEDAMOUNT, Integer.toString(player.getMyTotalPledgeAmount(this)), this, "i");
        mMyTeamsPledgeTotals= utilities.RemoveCurrency(tvMyTeamPledgeTotals.getText().toString())+value;
        tvMyTeamPledgeTotals.setText(utilities.FormatCurrency(mMyTeamsPledgeTotals));
        utilities.WriteSharedPref(Constant.MYTEAMTOTALPLEDGEDAMOUNT,Integer.toString(mMyTeamsPledgeTotals),this,"i");
        //Write the new total to Shared Preferences
        //Insert Pledge data in SQLLite Database
        Pledge pledge = new Pledge(this);

//        ContentValues values = new ContentValues();
        pledge.setAmount(value);
  //      values.put(DBOpenHelper.PLEDGE_AMT, value);
        pledge.setUser(player.getPlayer_id());
  //      values.put(DBOpenHelper.USER_ID, player.getPlayer_id());
        pledge.setPreferredCharity_id();
        pledge.setGame_id(game_id);
   //     values.put(DBOpenHelper.GAME_ID, game_id);
        pledge.setTeam_id(team_id);
       // getContentResolver().insert(PledgeProvider.CONTENT_URI, values);
        pledge.SubmitPledge();
    }

    /*
    *
    * Undo Pledges
    *
    * */
    private void undoLastPledge(int game_id, int team_id) {
        mUndoLastPledge.setEnabled(false);
        player.setMyTotalPledgeAmount(player.getMyTotalPledgeAmount(this) - player.getMyLastPledgeAmount());
        tvPledges.setText(utilities.FormatCurrency(player.getMyTotalPledgeAmount(this)));
        //TODO:Remove personal pledge from server totals
        Pledge pledge = new Pledge(this);
        pledge.setAmount(player.getMyLastPledgeAmount() * -1);
        pledge.setUser(player.getPlayer_id());//TODO:Get the User ID
        pledge.setPreferredCharity_id();
        pledge.setGame_id(game_id);
        pledge.setTeam_id(team_id);
        pledge.SubmitPledge();

    }

    public void SetTeamLogo(String sTeamLogoUrl, final ImageView mImageView) {
        ImageRequest request = new ImageRequest(sTeamLogoUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        mImageView.setImageResource(R.drawable.fail);
                    }
                });

        BaseApplication.getInstance().addToRequestQueue(request);


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
                addPledges(1, ActiveGameID, mUserTeamID);
                openDailogPledgesAdd(1);
                break;
            case R.id.btn_$2:
                addPledges(2, ActiveGameID, mUserTeamID);
                openDailogPledgesAdd(2);
                break;
            case R.id.btn_$5:
                addPledges(5, ActiveGameID, mUserTeamID);
                openDailogPledgesAdd(5);
                break;
            case R.id.btnundolastpledge:
                undoLastPledge(ActiveGameID, mUserTeamID);
                openDailogPledgesAdd(mMyLastPledge * -1);
                break;
        }
        //getGame(ActiveGameID);
        getCurrentGame(ActiveGameID);
    }

    public void openDailogPledgesAdd(int pledge_value) {
        String sConfirmation;
        final CustomizeDialog pledgeDialog = new CustomizeDialog(context);
        pledgeDialog.setContentView(R.layout.dilogpledges);
        TextView tv_pledge_donation = (TextView) pledgeDialog.findViewById(R.id.tv_pledge_donation);
        if (pledge_value < 0) {
            sConfirmation = String.format("Your last pledge of <br /><b>%s</b><br /> has been undone", utilities.FormatCurrency(pledge_value).replace("-", ""));

        } else {
            sConfirmation = String.format("Your pledge of <br /><b>%s</b><br /> is confirmed", utilities.FormatCurrency(pledge_value));
        }
        tv_pledge_donation.setText(Html.fromHtml(sConfirmation));
        pledgeDialog.show();
        pledgeDialog.setCancelable(false);
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                pledgeDialog.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 2000);
    }

    public void UpdateGameBoard(Game mGame) {
        Team homeTeam = null;
        Team awayTeam = null;
        TextView tv_homeTeamScore = (TextView) findViewById(R.id.tv_HomeTeamScore);
        TextView tv_awayTeamScore = (TextView) findViewById(R.id.tv_AwayTeamScore);
        TextView pledges = (TextView) findViewById(R.id.pledges);
        TextView tvMyTeamPledgeTotals = (TextView) findViewById(R.id.tv_HomeTeamPledges);
        TextView tvTheirTeamPledgeTotals = (TextView) findViewById(R.id.tv_AwayTeamPledges);
        tv_homeTeamScore.setText(Integer.toString(mGame.getHome_score()));
        tv_awayTeamScore.setText(Integer.toString(mGame.getAway_score()));
        mTvYourTeam.setText(mGame.getHome_LongName());
        mTvOpponentTeam.setText(mGame.getAway_LongName());
        pledges.setText(String.format("%s", utilities.FormatCurrency(player.getMyTotalPledgeAmount(this))));
        TextView tv_GamePeriod = (TextView) findViewById(R.id.tv_GamePeriod);
        tv_GamePeriod.setText(String.format("%s in %s", mGame.getTimeLeft(), Integer.toString(mGame.getPeriod())));
        tvMyTeamPledgeTotals.setText(utilities.FormatCurrency(mGame.getHometeam_pledge()));
        tvTheirTeamPledgeTotals.setText(utilities.FormatCurrency(mGame.getVisitingteam_pledge()));
        String homelogourl = String.format("%s%s.png", LOGO_BASE_URL, mGame.getHomeLogo());
        String awaylogourl = String.format("%s%s.png", LOGO_BASE_URL, mGame.getAwayLogo());
        ImageView mHomeLogo = (ImageView) findViewById(R.id.hometeamlogo);
        ImageView mAwayLogo = (ImageView) findViewById(R.id.awayteamlogo);
        SetTeamLogo(homelogourl, mHomeLogo);
        SetTeamLogo(awaylogourl, mAwayLogo);
    }

    public void getGame(int game_id) {
        try {
            String url = String.format(java.util.Locale.ENGLISH, "%s/api/%s", Constant.APISERVERURL, "game");
            String method = "game";
            RequestPackage p = new RequestPackage();
            p.setMethod("POST");
            p.setUri(url);
            p.setParam("token", null);
            p.setParam("page", Integer.toString(0));
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

    private class SendNonceTask extends AsyncTask<RequestPackage, Void, String> {

        @Override
        protected String doInBackground(RequestPackage... params) {

            return HttpManager.getData(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }

    private class GetClientTokenTask extends AsyncTask<RequestPackage, Void, String> {

        @Override
        protected String doInBackground(RequestPackage... params) {

            return HttpManager.getData(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(result);
                ClientToken = jsonObject.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private class GetMyGame extends AsyncTask<RequestPackage, Void, Game> {

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
                    String homelogourl = String.format("%s%s.png", LOGO_BASE_URL, game.getHomeLogo());
                    homeIn = (InputStream) new URL(homelogourl).getContent();
                    homeBitmap = BitmapFactory.decodeStream(homeIn);
                    game.setHomeLogobitmap(homeBitmap);
                    homeIn.close();
                    addBitmapToMemoryCache(game.getHome_Id(), homeBitmap);
                } else {
                    game.setHomeLogobitmap(homeBitmap);
                }

                if (awayBitmap == null) {
                    String awaylogourl = String.format("%s%s.png", LOGO_BASE_URL, game.getAwayLogo());
                    //String awaylogourl = LOGO_BASE_URL + game.getAwayLogo()+".png";
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
            game.setUserteam_id(game.getHome_Id());
            mUserTeamID = game.getUserteam_id();
            UpdateGameBoard(game);

        }

    }
}





