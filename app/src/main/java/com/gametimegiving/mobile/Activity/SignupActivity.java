/***************************************************************************************
 * FileName : SignupActivity.java
 * <p/>
 * Dependencies : SharedPreferences
 * <p/>
 * Description : This is SignupActivity where user can signUp and enter app main screen.
 ***************************************************************************************/
package com.gametimegiving.mobile.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Utils.Constant;
import com.gametimegiving.mobile.Utils.Validation;


public class SignupActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    private Button mBtn_Submit;
    private EditText mEtName, mEtEmail, mEtPassword, mEtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.sign_up);

        mEtName = (EditText) findViewById(R.id.etname);
        mEtEmail = (EditText) findViewById(R.id.etemailaddress);
        mEtPassword = (EditText) findViewById(R.id.etpassword);
        mEtConfirmPassword = (EditText) findViewById(R.id.etconfirmpassword);
        mBtn_Submit = (Button) findViewById(R.id.btn_singup);
        sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        mBtn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Validation.isFieldEmpty(mEtName) && !Validation.isFieldEmpty(mEtEmail) && !Validation.isFieldEmpty(mEtPassword) && !Validation.isFieldEmpty(mEtConfirmPassword)) {
                    if (Validation.validateFirstName(mEtName)) {
                        Toast.makeText(getApplicationContext(), "name should start from alphabet and can't less than 2 characters", Toast.LENGTH_SHORT).show();
                    } else if (Validation.isEmailValid(mEtEmail.getText().toString())) {
                        mEtEmail.setError("Invalid Email");
                    } else if (Validation.isPasswordValid(mEtPassword)) {
                        mEtPassword.setError("password can not be less than 8 characters");
                    } else if (Validation.isPasswordMatch(mEtPassword, mEtConfirmPassword)) {
                        mEtConfirmPassword.setError("password don't match");
                    } else if (!Validation.validateFirstName(mEtName) && !Validation.isPasswordValid(mEtPassword) && !Validation.isEmailValid(mEtEmail.getText().toString()) && !Validation.isPasswordMatch(mEtPassword, mEtConfirmPassword)) {
                        sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Constant.PROFILENAME, mEtName.getText().toString());
                        editor.putBoolean(Constant.ISLOGIN, true);
                        editor.putString(Constant.ISLOGINFROM, "signup");
                        editor.commit();
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), "all fields required", Toast.LENGTH_SHORT).show();
                }

                //==============================================================================
                //this code for sharedpreferences//
                SharedPreferences.Editor editor = sharedpreferences.edit();

                String strname = mEtName.getText().toString();
                String strEmail = mEtEmail.getText().toString();
                String strPassword = mEtPassword.getText().toString();
                String strConfirmpassword = mEtConfirmPassword.getText().toString();

                editor.putString(Constant.PROFILENAME, strname);
                editor.putString(Constant.EMAILADDRESS, strEmail);
                editor.putString(Constant.PASSWORD, strPassword);
                editor.putString(Constant.CONFIRMPASSWORD, strConfirmpassword);
                editor.commit();
                //   =========================================================================


            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}


