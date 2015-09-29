package com.gametimegiving.mobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gametimegiving.mobile.Charity;
import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Utils.CustomizeDialog;

import java.util.ArrayList;
import java.util.List;

public class CharityListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    private final static String TAG = "CharityList";
    public static boolean[] selectedItems;
    public static ArrayList<String> selectedCharity = new ArrayList<>();
    Context context;
    String[] objects;
    private List<Charity> listOfCharities;

    public CharityListAdapter(Context context, int resource, List<Charity> objects) {
        this.context = context;
        this.listOfCharities = objects;
        if (selectedItems == null && null != objects) {
            selectedItems = new boolean[listOfCharities.size()];
            for (int i = 0; i < listOfCharities.size(); i++)
                selectedItems[i] = false;
        }
        if (selectedCharity == null) {
            selectedCharity = new ArrayList<>();
        }
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        if (null != listOfCharities)
            return listOfCharities.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int i) {
        return objects[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        notifyDataSetChanged();
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View row = inflater.inflate(R.layout.spinner_row, parent, false);
        Charity charity = listOfCharities.get(position);
        TextView label = (TextView) row.findViewById(R.id.spinner_text);
        Button btnMore = (Button) row.findViewById(R.id.btn_more);
        Button btnSubmit = (Button) row.findViewById(R.id.btn_submit);

        CheckBox chk = (CheckBox) row.findViewById(R.id.chk);
        chk.setChecked(selectedItems[position]);
        chk.setTag(position);
        chk.setOnCheckedChangeListener(this);
        label.setText(charity.getCharityName());
//        if (position == objects.length - 1) {
////            btnSubmit.setVisibility(View.VISIBLE);
//        }
        //       btnMore.setTag(position + " " + objects[position]);

        btnMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                final String tag = (String) v.getTag();
                final String[] arr = tag.split(" ");
                final CustomizeDialog charitycustomizeDialog = new CustomizeDialog(context);
                charitycustomizeDialog.setContentView(R.layout.charitiesdialog);
                final ImageButton imgClose = (ImageButton) charitycustomizeDialog.findViewById(R.id.imgbtn_cancel);
                Button btnAddToProfile = (Button) charitycustomizeDialog.findViewById(R.id.btnaddtoprofile);
                if (selectedItems[Integer.parseInt(arr[0])])
                    btnAddToProfile.setVisibility(View.GONE);


                btnAddToProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final CustomizeDialog infoCustomizeDialog = new CustomizeDialog(context);
                        if (selectedCharity.size() > 3) {
                            infoCustomizeDialog.setContentView(R.layout.addtoprofiledialog);
                            Button btnOk = (Button) infoCustomizeDialog.findViewById(R.id.btnok);
                            btnOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    infoCustomizeDialog.dismiss();
                                }
                            });
                            infoCustomizeDialog.show();
                        } else {
                            charitycustomizeDialog.dismiss();

                            selectedItems[Integer.parseInt(arr[0])] = true;
                            // selectedCharity.add(arr[1]);

                            selectedCharity.add(objects[Integer.parseInt(arr[0])]);
                            notifyDataSetChanged();


                        }

                    }
                });
                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        charitycustomizeDialog.dismiss();
                    }
                });

                charitycustomizeDialog.show();

                charitycustomizeDialog.setCancelable(false);
            }

        });

        return row;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int position = (int) compoundButton.getTag();
        selectedItems[position] = b;
        Charity charity = listOfCharities.get(position);
        String charityName = charity.getCharityName();
        if (b) {
            if (selectedCharity.size() <= 3) {
                selectedCharity.add(charityName);
            } else {
                selectedItems[position] = !b;

                compoundButton.setChecked(!b);
            }
        } else {
            if (selectedCharity.contains(charityName)) {
                selectedCharity.remove(charityName);
            }

        }

    }

}
