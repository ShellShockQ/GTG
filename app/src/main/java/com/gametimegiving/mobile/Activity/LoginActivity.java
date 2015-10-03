/***************************************************************************************
 * FileName : LoginActivity.java
 * <p/>
 * Dependencies :Facebook SDK, Twitter SDK, SharedPreferences
 * <p/>
 * Description : This is LoginActivity where user can login facebook, twitter or app userId password.
 * User can go signUp page and forgot password Scrren.
 ***************************************************************************************/
package com.gametimegiving.mobile.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Twitter.TwitterApp;
import com.gametimegiving.mobile.Utils.CommanDialog;
import com.gametimegiving.mobile.Utils.ConnectionDetector;
import com.gametimegiving.mobile.Utils.Constant;
import com.gametimegiving.mobile.Utils.Validation;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String twitter_consumer_key = "3qNkkdNh3vahsZ1i9xTIXqKAV";
    public static final String twitter_secret_key = "8JJe8Avu5MLgmngd8oMrSaTYQosuYn56aF9kcHibIToMdEDo0h";
    SharedPreferences sharedpreferences;
    private Button mBtnSignIn;
    private TextView mTvForgotPassword;
    private EditText mEtEmail, mEtPassword;
    private CallbackManager mCallbackManager;
    private LoginButton mLoginButton;
    private TwitterApp mTwitter;
    private Button mTwitterBtn;
    private String mProfileUrl = null;
    private final TwitterApp.TwDialogListener mTwLoginDialogListener = new TwitterApp.TwDialogListener() {
        @Override
        public void onComplete(String value) {
            mProfileName = mTwitter.getUsername();
            mProfileName = (mProfileName.equals("")) ? "No Name" : mProfileName;
            mProfileUrl = mTwitter.getUserImageUrl();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Constant.PROFILENAME, mProfileName);
            editor.putString(Constant.PROFILEURL, mProfileUrl);
            editor.putBoolean(Constant.ISLOGIN, true);
            editor.putString(Constant.ISLOGINFROM, "twitter");
            editor.commit();

            Intent i = new Intent(LoginActivity.this, MainActivity.class);

            startActivity(i);
            finish();

        }

        @Override
        public void onError(String value) {


            Toast.makeText(LoginActivity.this, "Twitter connection failed", Toast.LENGTH_LONG).show();
        }
    };
    private String mProfileId, mProfileName;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.login);

        context = this;
        mTwitterBtn = (Button) findViewById(R.id.twt);
        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mTwitterBtn.setOnClickListener(this);

        mTwitter = new TwitterApp(this, twitter_consumer_key, twitter_secret_key);

        mTwitter.setListener(mTwLoginDialogListener);

        if (mTwitter.hasAccessToken()) {


            String username = mTwitter.getUsername();
            String userImage = mTwitter.getUserImageUrl();
            username = (username.equals("")) ? "Unknown" : username;

        }

        mEtEmail = (EditText) findViewById(R.id.etmail);
        mEtPassword = (EditText) findViewById(R.id.etpassword);
        mBtnSignIn = (Button) findViewById(R.id.btn_signin);
        sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        mBtnSignIn.setOnClickListener(this);


        mTvForgotPassword = (TextView) findViewById(R.id.tvforgatpassword);
        mTvForgotPassword.setOnClickListener(this);

        TextView tvSignup = (TextView) findViewById(R.id.tvsignup);
        tvSignup.setOnClickListener(this);

        facebookLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void onTwitterClick() {
        if (ConnectionDetector.isConnectingToInternet(context)) {
            if (mTwitter.hasAccessToken()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Logout From Twitter")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mTwitter.resetAccessToken();


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();


                            }
                        });
                final AlertDialog alert = builder.create();

                alert.show();
            } else {
                mTwitter.authorize();
            }
        } else {
            CommanDialog.showAlertDialogForInternetConnection(context, "No Internet Connection", "You don't have internet connection.");
        }

    }

    /**
     * Login With FaceBook
     */
    private void facebookLogin() {
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton = (LoginButton) findViewById(R.id.login_button);
        mLoginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "public_profile");
        mLoginButton.setReadPermissions(permissionNeeds);

        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("onSuccess");
                GraphRequest request = GraphRequest.newMeRequest
                        (loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                Log.v("LoginActivity", response.toString());
                                //System.out.println("Check: " + response.toString());
                                try {
                                    String profileUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
//                                    profileId = object.getString("id");
//                                    profileName = object.getString("name");

                                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                                    editor.putString(Constant.PROFILEURL, profileUrl);
                                    editor.putString(Constant.PROFILENAME, object.getString("name"));
                                    editor.putString(Constant.PROFILEID, object.getString("id"));
                                    editor.putBoolean(Constant.ISLOGIN, true);
                                    editor.putString(Constant.ISLOGINFROM, "facebook");
                                    editor.commit();
                                    //TODO:Update the user table in database
                                    //
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,picture,birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // CommanDialog.showAlertDialogForInternetConnection(Login.this, "No Internet Connection", "You don't have internet connection.");
                System.out.println("onCancel");
            }


            @Override
            public void onError(FacebookException exception) {
                CommanDialog.showAlertDialogForInternetConnection(LoginActivity.this, "No Internet Connection", "You don't have internet connection.");
                System.out.println("onCancel");
            }
        });

    }

    /**
     * save data in sharedpreferences
     */
    private void sharedPreferences() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String strEmail = mEtEmail.getText().toString();
        String strPassword = mEtPassword.getText().toString();
        editor.putString(Constant.EMAILADDRESS, strEmail);
        editor.putString(Constant.PASSWORD, strPassword);
        editor.putBoolean(Constant.ISLOGIN, true);
        editor.putString(Constant.ISLOGINFROM, "gametime");
        editor.commit();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvsignup:
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tvforgatpassword:
                Intent intent1 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_signin:
                if (!Validation.isFieldEmpty(mEtEmail) && !Validation.isFieldEmpty(mEtPassword)) {
                    if (!Validation.isEmailValid(mEtEmail.getText().toString())) {
                        Intent intent3 = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent3);
                        finish();
                    } else {
                        mEtEmail.setError("Invalid E_mail");
                        Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Field can't be empty", Toast.LENGTH_SHORT).show();
                }
                sharedPreferences();
                break;
            case R.id.twt:
                onTwitterClick();

        }

    }
}
