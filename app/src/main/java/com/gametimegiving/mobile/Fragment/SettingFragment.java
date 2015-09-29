package com.gametimegiving.mobile.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Utils.Validation;

public class SettingFragment extends Fragment implements View.OnClickListener {
    private Button mBtnUpdate;
    private EditText mEtEmail, mEtpassword;

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);
        mBtnUpdate = (Button) view.findViewById(R.id.btn_update);
        mEtEmail = (EditText) view.findViewById(R.id.et_emailid);
        mEtpassword = (EditText) view.findViewById(R.id.et_password);
        mBtnUpdate.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:

                if (!Validation.isFieldEmpty(mEtEmail) && !Validation.isFieldEmpty(mEtpassword)) {
                    if (Validation.isEmailValid(mEtEmail.getText().toString())) {
                        mEtEmail.setError("Invalid Email");
                    }
                    if (Validation.isPasswordValid(mEtpassword)) {
                        mEtpassword.setError("password can not be less than 8 characters");
                    } else {
                        Toast.makeText(getActivity(), "Email-ID and Password has been Updated", Toast.LENGTH_SHORT).show();

                    }


                } else {

                    Toast.makeText(getActivity(), "All fields required", Toast.LENGTH_SHORT).show();
                }

        }
    }
}
