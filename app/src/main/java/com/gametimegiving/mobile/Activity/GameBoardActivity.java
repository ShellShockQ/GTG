
package com.gametimegiving.mobile.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.Group;
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

import com.gametimegiving.mobile.Application.BaseApplication;
import com.gametimegiving.mobile.Game;
import com.gametimegiving.mobile.Parse.HttpManager;
import com.gametimegiving.mobile.Parse.MyGameJSONParser;
import com.gametimegiving.mobile.Parse.RequestPackage;
import com.gametimegiving.mobile.Payment;
import com.gametimegiving.mobile.Player;
import com.gametimegiving.mobile.Pledge;
import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Utils.Constant;
import com.gametimegiving.mobile.Utils.CustomizeDialog;
import com.gametimegiving.mobile.Utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class GameBoardActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private static final String LOGO_BASE_URL = BaseApplication.getInstance().getMetaData(BaseApplication.META_DATA_LOGO_BASE_URL);
    private final static int SUBMIT_PAYMENT_REQUEST_CODE = 100;
    final int MaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int cacheSize = MaxMemory / 8;
    final Handler handler = new Handler();
    private final Player player = new Player();
    public Integer ActiveGameID,
            mUserTeamID;
    public String ClientToken;
    public Payment payment;
    public String MyPledgeAmount;
    public Boolean PreferredCharityNoticeShown;
    public boolean bFirstTimeIn = true;
    public Game mGame = null;
    Utilities utilities = new Utilities();
    Timer timer;
    TimerTask timerTask;
    Group pledgeButtons;
    private ImageView mHomeLogo,
            mAwayLogo;
    private Context context;
    private String[] arr = null;
    private Integer mMyTeamsPledgeTotals = 0,
            mTheirTeamsPledgeTotals = 0,
            mMyPledgeTotals = 0,
            mMyLastPledge = 0;
    private TextView tv_VisitorTeamName,
            tv_HomeTeamName,
            tv_homeTeamMascot,
            tv_visitorTeamMascot,
            tv_AwayTeamScore,
            tv_homeTeamScore,
            tv_pledges,
            tv_MyTeamPledgeTotals,
            tv_TheirTeamPledgeTotals,
            tv_GamePeriod;
    private Button mUndoLastPledge,
            pledgeBtn1,
            pledgeBtn2,
            pledgeBtn3,
            btnPayNow;
    private String mToken;
    private LruCache<Integer, Bitmap> imageMemCache;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        player.setPlayer_id();
        setContentView(R.layout.gameboard);
        //Find and set all the textviews on the view
        tv_HomeTeamName = findViewById(R.id.HomeTeamName);
        tv_homeTeamMascot = findViewById(R.id.HomeTeamMascot);
        tv_VisitorTeamName = findViewById(R.id.VisitorTeamName);
        tv_visitorTeamMascot = findViewById(R.id.VisitorTeamMascot);
        tv_pledges = findViewById(R.id.pledge);
        tv_homeTeamScore = findViewById(R.id.tv_HomeTeamScore);
        tv_AwayTeamScore = findViewById(R.id.tv_AwayTeamScore);
        tv_pledges = findViewById(R.id.pledge);
        tv_MyTeamPledgeTotals = findViewById(R.id.tv_HomeTeamPledges);
        tv_TheirTeamPledgeTotals = findViewById(R.id.tv_AwayTeamPledges);
        tv_GamePeriod = findViewById(R.id.tv_GamePeriod);
        //Find and set all the buttons on the view
        mUndoLastPledge = findViewById(R.id.btnundolastpledge);
        mUndoLastPledge.setOnClickListener(this);
        mUndoLastPledge.setEnabled(false);
        pledgeBtn1 = findViewById(R.id.PledgeButton1);
        pledgeBtn1.setOnClickListener(this);
        pledgeBtn2 = findViewById(R.id.PledgeButton2);
        pledgeBtn2.setOnClickListener(this);
        pledgeBtn3 = findViewById(R.id.PledgeButton3);
        pledgeBtn3.setOnClickListener(this);
        btnPayNow = findViewById(R.id.btnpaynow);
        //Find all the imageviews
        mHomeLogo = findViewById(R.id.hometeamlogo);
        mAwayLogo = findViewById(R.id.awayteamlogo);
        //Find the groups on the view
        pledgeButtons = findViewById(R.id.pledgeButtons);

        //Create a Game Objec to hold the current Game
        Game currentGame = new Game();
        mGame = currentGame.getCurrentGame();
        SetGameBoardMode();
        UpdateGameBoard();

        if (getIntent().getExtras() != null) {
            try {
                Bundle extras = getIntent().getExtras();
                bFirstTimeIn = extras.getBoolean(Constant.ISFIRSTTIMEIN);
                ActiveGameID = extras.getInt("selectedgameid");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
        imageMemCache = new LruCache<>(cacheSize);
        Integer ag = utilities.ReadSharedPref("activegame", this);
        context = this;
//        if (bFirstTimeIn) {
//            showdialog();
//        }

    }
    public void SetUpDemo(){
        mGame.setAway_score(14);
        mGame.setHome_score(21);
        mGame.setPeriod("3rd");
        mGame.setTimeLeft("7:56");
        mGame.setVisitingteam_pledge(23);
        if(mGame.getHometeam_pledge() ==0) {
            utilities.WriteSharedPref(Constant.LASTPLEDGEDAMOUNT, "0", this, "i");
            utilities.WriteSharedPref(Constant.MYTOTALPLEDGEDAMOUNT, "0", this, "i");
            mGame.setHometeam_pledge(18);
        }
        if(mGame.getHometeam_pledge()>mGame.getVisitingteam_pledge()){
            mGame.setGameStatus(Constant.GAMEOVER);
            mGame.setPeriod("4th");
            mGame.setTimeLeft("0:00");
        }
    }

    private void SetGameBoardMode() {

        View l = findViewById(R.id.pledgeButtons);
        View ppl = null; //TODO: Replace this with a group
        switch (mGame.getGameStatus()) {
            case Constant.GAMENOTSTARTED:
                pledgeButtons.setVisibility(View.GONE);
                mGame.ClearBoard();
                break;
            case Constant.GAMEINPROGRESS:
                pledgeButtons.setVisibility(View.VISIBLE);
                l.setVisibility(View.VISIBLE);
                btnPayNow.setVisibility(View.GONE);
                break;
            case Constant.GAMEOVER:
                GenerateClientToken();
                if (tv_pledges.getText() != getString(R.string.zerodollars)) {
                    MyPledgeAmount = tv_pledges.getText().toString();
                    l.setVisibility(View.GONE);
                    pledgeButtons.setVisibility(View.GONE);
                    btnPayNow.setVisibility(View.VISIBLE);
                    btnPayNow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                     MakeBrainTreePayment();
                        }
                    });
                } else {
                    utilities.ShowMsg("You have no pledges", this);

                }
                break;
        }
    }

    private void MakeBrainTreePayment() {
//        Customization customization = new Customization.CustomizationBuilder()
//                .primaryDescription("My Pledge  ")
//                .amount(MyPledgeAmount)
//                .submitButtonText("Donate Now")
//                .build();
//        Intent intent = new Intent(context, BraintreePaymentActivity.class)
//                .putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN,
//                        ClientToken);
//        intent.putExtra(BraintreePaymentActivity.EXTRA_CUSTOMIZATION, customization);
//        startActivityForResult(intent, SUBMIT_PAYMENT_REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
//TODO:Add the Braintree functionality back into the project
//        if (requestCode == SUBMIT_PAYMENT_REQUEST_CODE) {
//            if (resultCode == BraintreePaymentActivity.RESULT_OK) {
//                String nonce = data.getStringExtra(
//                        BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
//                );


 //               SendNonceToBraintree(nonce, MyPledgeAmount);
 //           }
  //      }
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
        player.setMyLastPledgeAmount(value);
        utilities.WriteSharedPref(Constant.GAMEDATAREFRESHED, "false", this, "b");
        utilities.WriteSharedPref(Constant.LASTPLEDGEDAMOUNT, Integer.toString(player.getMyTotalPledgeAmount(this)), this, "i");
        player.setMyTotalPledgeAmount(player.getMyTotalPledgeAmount(this) + value);
        mMyPledgeTotals = player.getMyTotalPledgeAmount(this);
        tv_pledges.setText(utilities.FormatCurrency(player.getMyTotalPledgeAmount(this)));
        utilities.WriteSharedPref(Constant.MYTOTALPLEDGEDAMOUNT, Integer.toString(player.getMyTotalPledgeAmount(this)), this, "i");
        mMyTeamsPledgeTotals = utilities.RemoveCurrency(tv_MyTeamPledgeTotals.getText().toString()) + value;
        tv_MyTeamPledgeTotals.setText(utilities.FormatCurrency(mMyTeamsPledgeTotals));
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
        tv_pledges.setText(utilities.FormatCurrency(player.getMyTotalPledgeAmount(this)));
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
        TextView YourTeamName = customizeDialog.findViewById(R.id.tvyourteamname);
        TextView SupportTeamName = customizeDialog.findViewById(R.id.tvsupportteamname);
        Button button = customizeDialog.findViewById(R.id.btnok);
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
           /* case R.id.btn_$1:
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
            */
            case R.id.btnundolastpledge:
                undoLastPledge(ActiveGameID, mUserTeamID);
                openDailogPledgesAdd(mMyLastPledge * -1);
                break;
        }
        //getGame(ActiveGameID);
        //getCurrentGame(ActiveGameID);
    }

    public void openDailogPledgesAdd(int pledge_value) {
        String sConfirmation;
        final CustomizeDialog pledgeDialog = new CustomizeDialog(context);
        pledgeDialog.setContentView(R.layout.dilogpledges);
        TextView tv_pledge_donation = pledgeDialog.findViewById(R.id.tv_pledge_donation);
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


    public void UpdateGameBoard() {
        tv_homeTeamScore.setText(Integer.toString(mGame.getHome_score()));
        tv_AwayTeamScore.setText(Integer.toString(mGame.getAway_score()));
        tv_GamePeriod.setText(String.format("%s", mGame.getPeriod()));
        tv_HomeTeamName.setText(mGame.getHomeTeam().getTeamName());
        tv_homeTeamMascot.setText(mGame.getHomeTeam().getNickName());
        tv_VisitorTeamName.setText(mGame.getAwayTeam().getTeamName());
        tv_visitorTeamMascot.setText(mGame.getAwayTeam().getNickName());
        tv_pledges.setText(String.format("%s", utilities.FormatCurrency(player.getMyTotalPledgeAmount(this))));
        tv_GamePeriod.setText(String.format("%s in the %s", mGame.getTimeLeft(), mGame.getPeriod()));
        tv_MyTeamPledgeTotals.setText(utilities.FormatCurrency(mGame.getHometeam_pledge()));
        tv_TheirTeamPledgeTotals.setText(utilities.FormatCurrency(mGame.getVisitingteam_pledge()));
        String homelogourl = String.format("%s%s.png", LOGO_BASE_URL, mGame.getHomeLogo());
        String awaylogourl = String.format("%s%s.png", LOGO_BASE_URL, mGame.getAwayLogo());
        SetTeamLogo(homelogourl, mHomeLogo);
        SetTeamLogo(awaylogourl, mAwayLogo);
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
                homeBitmap = getBitmapFromMemCache(game.getHomeTeam().getTeamId());
                awayBitmap = getBitmapFromMemCache(game.getAwayTeam().getTeamId());
                if (homeBitmap == null) {
                    String homelogourl = String.format("%s%s.png", LOGO_BASE_URL, game.getHomeLogo());
                    homeIn = (InputStream) new URL(homelogourl).getContent();
                    homeBitmap = BitmapFactory.decodeStream(homeIn);
                    game.setHomeLogobitmap(homeBitmap);
                    homeIn.close();
                    addBitmapToMemoryCache(game.getHomeTeam().getTeamId(), homeBitmap);
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
                    addBitmapToMemoryCache(game.getAwayTeam().getTeamId(), awayBitmap);
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
            game.setUserteam_id(game.getHomeTeam().getTeamId());
            mUserTeamID = game.getUserteam_id();
            UpdateGameBoard();

        }

    }
}





