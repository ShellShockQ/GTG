package com.gametimegiving.mobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gametimegiving.mobile.R;

import java.util.ArrayList;

/**
 * Created by Narendra on 8/6/2015.
 */
public class DonationSpinnerAdapter extends ArrayAdapter<String> implements CompoundButton.OnCheckedChangeListener {

    public static boolean[] selectedItems;
    public static ArrayList<String> selectedCharity = new ArrayList<>();
    Context context;
    String[] objects;
    String firstElement;
    boolean isFirstTime;

    public DonationSpinnerAdapter(Context context, int textViewResourceId, String[] objects, String defaultText) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
        this.isFirstTime = true;
        if (selectedItems == null) {
            selectedItems = new boolean[objects.length];
            for (int i = 0; i < objects.length; i++)
                selectedItems[i] = false;
        }
        setDefaultText(defaultText);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (isFirstTime) {
            objects[0] = firstElement;
            isFirstTime = false;
        }
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        notifyDataSetChanged();
        return getCustomView(position, convertView, parent);
    }

    public void setDefaultText(String defaultText) {
        this.firstElement = objects[0];
        objects[0] = defaultText;
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_row, parent, false);
        TextView label = (TextView) row.findViewById(R.id.spinner_text);
        Button btn = (Button) row.findViewById(R.id.btn_more);
        Button btnSubmit = (Button) row.findViewById(R.id.btn_submit);
        CheckBox chk = (CheckBox) row.findViewById(R.id.chk);

        btn.setVisibility(View.GONE);
        chk.setVisibility(View.GONE);


        label.setText(objects[position]);
        if (position == objects.length - 1) {

        }
        return row;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int position = (int) compoundButton.getTag();
        selectedItems[position] = b;
        if (b) {
            selectedCharity.add(objects[position]);
        } else {
            if (selectedCharity.contains(objects[position]))
                selectedCharity.remove(objects[position]);
        }

    }

}
