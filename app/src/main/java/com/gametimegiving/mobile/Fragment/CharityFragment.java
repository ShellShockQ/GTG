package com.gametimegiving.mobile.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gametimegiving.mobile.R;

public class CharityFragment extends Fragment {

    public static CharityFragment newInstance() {
        CharityFragment fragment = new CharityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.charities, container, false);
        return view;
    }
}
