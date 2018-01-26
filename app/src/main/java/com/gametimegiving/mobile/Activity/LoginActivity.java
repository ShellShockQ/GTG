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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

import com.facebook.AccessToken;
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

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG =getClass().getSimpleName() ;
    public static final String twitter_consumer_key = "3qNkkdNh3vahsZ1i9xTIXqKAV";
    public static final String twitter_secret_key = "8JJe8Avu5MLgmngd8oMrSaTYQosuYn56aF9kcHibIToMdEDo0h";
    SharedPreferences sharedpreferences;
    private Button mBtnSignIn;
    private TextView mTvForgotPassword;
    private EditText mEtEmail, mEtPassword;
    private CallbackManager mCallbackManager;
    private LoginButton mFaceBookLoginButton;
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
       /*TODO: If the user is logged In already simply skip this
         and go to the game board
       */
        if(isLoggedIn()){
            Toast.makeText(getApplicationContext(), "Great! you are already logged In with Facebook", Toast.LENGTH_SHORT).show();
            Log.i(TAG,"User is Already Logged In");
        }else {
            Log.i(TAG,"User is not Logged In Giving Login screen");
            setContentView(R.layout.login);
            setVersionNameOnView();
            Log.d(TAG, "onCreate");
            mFaceBookLoginButton = findViewById(R.id.fb_login_button);
            mFaceBookLoginButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Toast.makeText(getApplicationContext(), "Facebook Login Button Clicked", Toast.LENGTH_SHORT).show();
                }
            } );

        }
    }

    private void setVersionNameOnView() {
        try {
            PackageManager packageManager = getPackageManager();
            String packageName = getPackageName();
            PackageInfo pInfo = packageManager.getPackageInfo(packageName, 0);
            String versionName = pInfo.versionName;
            String version_value = String.format(java.util.Locale.ENGLISH, "Version %s", versionName);
            TextView tv_VersionOnLoginPage = (TextView) findViewById(R.id.versiontextonlogin);
            tv_VersionOnLoginPage.setText(version_value);
        } catch (Exception ignored)
        {
        }
    }

    private void loginWithTWitter() {
        context = this;
        mTwitterBtn = (Button) findViewById(R.id.twitterLoginButton);
        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mTwitterBtn.setOnClickListener(this);

        mTwitter = new TwitterApp(this, twitter_consumer_key);

        mTwitter.setListener(mTwLoginDialogListener);

        if (mTwitter.hasAccessToken()) {


            String username = mTwitter.getUsername();
            String userImage = mTwitter.getUserImageUrl();
            username = (username.equals("")) ? "Unknown" : username;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isLoggedIn()){
            Log.i(TAG,"User is Already Logged In at line 164");
        }else {
            Log.i(TAG,"User is not Already Logged In at line 166");
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        }}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     //   mCallbackManager.onActivityResult(requestCode, resultCode, data);
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
            CommanDialog.showAlertDialogForInternetConnection(context, "No Internet Connection");
        }

    }

    /**
     * Login With FaceBook
     */
    private void facebookLogin() {
        mCallbackManager = CallbackManager.Factory.create();
        mFaceBookLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        mFaceBookLoginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "public_profile");
        mFaceBookLoginButton.setReadPermissions(permissionNeeds);

        mFaceBookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(isLoggedIn()){
                    Toast.makeText(getApplicationContext(), "Great! you are already logged In with Facebook", Toast.LENGTH_SHORT).show();


                }else{
                    Toast.makeText(getApplicationContext(), "Great! you are about to log In with Facebook", Toast.LENGTH_SHORT).show();
                }
                GraphRequest request = GraphRequest.newMeRequest
                        (loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String profileUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(Constant.PROFILENAME, object.getString("name"));
                                    editor.putString(Constant.PROFILEID, object.getString("id"));
                                    editor.putBoolean(Constant.ISLOGIN, true);
                                    editor.putString(Constant.ISLOGINFROM, "facebook");
                                    editor.commit();
                                    //TODO:Update the user table in database
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
                Toast.makeText(getApplicationContext(), "Ok, We have cancelled your request to login...", Toast.LENGTH_SHORT).show();
                BaseActivity base = new BaseActivity();
                base.UserLogout();
            }


            @Override
            public void onError(FacebookException exception) {
                CommanDialog.showAlertDialogForInternetConnection(LoginActivity.this, "No Internet Connection");
                Toast.makeText(getApplicationContext(), "Oops! Something Went Wrong...", Toast.LENGTH_SHORT).show();
                System.out.println("onCancel");
            }
        });

    }
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
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
        editor.putBoolean(Constant.ISLOGIN, false);
        editor.putString(Constant.ISLOGINFROM, "gametime");
        editor.commit();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
//            case R.id.tvsignup:
//                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.tvforgatpassword:
//                Intent intent1 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
//                startActivity(intent1);
//                break;
//            case R.id.btn_signin:
//                if (Validation.isFieldEmpty(mEtEmail) && Validation.isFieldEmpty(mEtPassword)) {
//                    if (!Validation.isEmailValid(mEtEmail.getText().toString())) {
//                        Player player = new Player();
//                        if (Player.isRegisteredPlayer(mEtEmail.getText().toString(),
//                                mEtPassword.getText().toString(), LoginActivity.this)) {
//                            Intent intent3 = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent3);
//                            finish();
//
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Not Registered", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        mEtEmail.setError("Invalid E_mail");
//                        Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "Field can't be empty", Toast.LENGTH_SHORT).show();
//                }
//                sharedPreferences();
//                break;
//            case R.id.twitterLoginButton:
//                onTwitterClick();

        }

    }
}
