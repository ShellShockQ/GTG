/***************************************************************************************
 * FileName : ForgotPasswordActivity.java
 * <p/>
 * Description : This is ForgotPasswordActivity, where user can get new password.
 ***************************************************************************************/
package com.gametimegiving.mobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Utils.Validation;


public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView mTvReturn;
    private EditText mEtEmail;
    private Button mButtonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        mTvReturn = (TextView) findViewById(R.id.tvreturn);
        mEtEmail = (EditText) findViewById(R.id.etemail);
        mButtonSend = (Button) findViewById(R.id.btnsend);
        mButtonSend.setOnClickListener(this);
        mTvReturn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnsend:

                if (!Validation.isFieldEmpty(mEtEmail)) {
                    if (!Validation.isEmailValid(mEtEmail.getText().toString())) {
                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "A link has been sent to your E-mail Address to reset password", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        mEtEmail.setError("Invalid E_mail");
                        Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                    }
                } else

                {
                    Toast.makeText(getApplicationContext(), "Field can't be empty", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tvreturn:
                finish();
        }
    }
}