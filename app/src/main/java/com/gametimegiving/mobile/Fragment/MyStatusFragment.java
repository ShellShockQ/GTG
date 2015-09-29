package com.gametimegiving.mobile.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gametimegiving.mobile.R;

public class MyStatusFragment extends Fragment {

    public static MyStatusFragment newInstance() {
        MyStatusFragment fragment = new MyStatusFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_status, container, false);
    }
}
