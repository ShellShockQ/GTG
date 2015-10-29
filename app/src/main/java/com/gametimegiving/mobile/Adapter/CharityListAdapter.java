package com.gametimegiving.mobile.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gametimegiving.mobile.Application.BaseApplication;
import com.gametimegiving.mobile.Charity;
import com.gametimegiving.mobile.Parse.CharityDetailJSONParser;
import com.gametimegiving.mobile.Parse.HttpManager;
import com.gametimegiving.mobile.Parse.RequestPackage;
import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Utils.Constant;
import com.gametimegiving.mobile.Utils.CustomizeDialog;
import com.gametimegiving.mobile.Utils.Utilities;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CharityListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
    private static final String LOGO_BASE_URL = BaseApplication.getInstance().getMetaData(BaseApplication.META_DATA_LOGO_BASE_URL);
    private final static String TAG = "CHARITYLISTADAPTER";
    public static boolean[] selectedItems;
    public static ArrayList<String> selectedCharity = new ArrayList<>();
    public static ArrayList<Integer> selectedCharityIds = new ArrayList<>();
    public String[] CharityIdArray;
    public CustomizeDialog charitycustomizeDialog;
    String mApiServerUrl = BaseApplication.getInstance().getMetaData(BaseApplication.META_DATA_API_SERVER_URL);
    Context context;
    String[] objects;
    private List<Charity> listOfCharities;
    private Utilities util = new Utilities();

    public CharityListAdapter(Context context, List<Charity> objects) {
        this.context = context;
        this.listOfCharities = objects;
        if (selectedItems == null && null != objects) {
            selectedItems = new boolean[listOfCharities.size()];
            for (int i = 0; i < listOfCharities.size(); i++)
                selectedItems[i] = false;
        }
        if (selectedCharity == null) {
            selectedCharity = new ArrayList<>();
            selectedCharityIds = new ArrayList<>();
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
        SharedPreferences sharedPref = parent.getContext().getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        String MyCharityIds = sharedPref.getString("myCharityIds", "");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View row = inflater.inflate(R.layout.spinner_row, parent, false);
        final Charity charity = listOfCharities.get(position);
        String CharityName = charity.getCharityName();
        Integer CharityId = charity.getCharityId();
        TextView label = (TextView) row.findViewById(R.id.spinner_text);
        Button btnMore = (Button) row.findViewById(R.id.btn_more);
        Button btnSubmit = (Button) row.findViewById(R.id.btn_submit);
        label.setText(CharityName);
        btnMore.setTag(String.valueOf(CharityId));
        CheckBox chk = (CheckBox) row.findViewById(R.id.chk);
        chk.setChecked(selectedItems[position]);
        //   chk.setChecked(MyCharityIds.contains(String.valueOf(CharityId)));
        chk.setTag(position);
        chk.setOnCheckedChangeListener(this);


        btnMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                final String tag = (String) v.getTag();
                //final String[] arr = tag.split(" ");
                charitycustomizeDialog = new CustomizeDialog(context);
                charitycustomizeDialog.setContentView(R.layout.charitiesdialog);
                final ImageButton imgClose = (ImageButton) charitycustomizeDialog.findViewById(R.id.imgbtn_cancel);
                Button btnAddToProfile = (Button) charitycustomizeDialog.findViewById(R.id.btnaddtoprofile);
                GetCharityDetail(tag);
                //  if (selectedItems[Integer.parseInt(tag)])
                btnAddToProfile.setVisibility(View.VISIBLE);


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

                            //                          selectedItems[Integer.parseInt(tag)] = true;
                            selectedCharity.add(charity.getCharityName());
                            selectedCharityIds.add(charity.getCharityId());

                            //          selectedCharity.add(objects[Integer.parseInt(tag)]);
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
        Integer charityId = charity.getCharityId();
        if (b) {
            if (selectedCharity.size() <= 3) {
                selectedCharity.add(charityName);
                selectedCharityIds.add(charityId);
            } else {
                selectedItems[position] = !b;

                compoundButton.setChecked(!b);
            }
        } else {
            if (selectedCharity.contains(charityName)) {
                selectedCharity.remove(charityName);
                selectedCharityIds.remove(charityId);
//                //remove this charity from the sharedpreference
//                SharedPreferences sharedPref =context.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPref.edit();
//                editor.putString("myCharityIds",selectedCharityIds.toString());
            }

        }

    }

    public void UpdateCharityDetail(Charity theCharity) {
        TextView tv_MissionStatement = (TextView) charitycustomizeDialog.findViewById(R.id.missionstatment);
        TextView tv_PurposeStatement = (TextView) charitycustomizeDialog.findViewById(R.id.purposestatment);
        TextView tv_CharityName = (TextView) charitycustomizeDialog.findViewById(R.id.charityName);
        ImageView CharityLogo = (ImageView) charitycustomizeDialog.findViewById(R.id.charityLogo);
        CharityLogo.setImageBitmap(theCharity.getBitmap());
        tv_MissionStatement.setText(theCharity.getMission());
        tv_PurposeStatement.setText(theCharity.getPurpose());
        tv_CharityName.setText(theCharity.getCharityName());
    }

    public void GetCharityDetail(String charity_id) {
        try {
            String method = "charity";
            String url = String.format(java.util.Locale.ENGLISH, "%s/api/%s", mApiServerUrl, method);

            RequestPackage p = new RequestPackage();
            p.setMethod("POST");
            p.setUri(url);
            p.setParam("token", null);
            p.setParam("charity_id", charity_id);
            GetThisCharity task = new GetThisCharity();
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p);

        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
    }

    private class GetThisCharity extends AsyncTask<RequestPackage, Void, Charity> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Charity doInBackground(RequestPackage... strings) {

            String content = HttpManager.getData(strings[0]);
            Charity charity = CharityDetailJSONParser.parseFeed(content);
            String CharityLogoURL = String.format("%s%s.png", LOGO_BASE_URL, charity.getLogo());
            try {
                InputStream CharityIn = (InputStream) new URL(CharityLogoURL).getContent();
                Bitmap CharityLogoBitmap = BitmapFactory.decodeStream(CharityIn);
                charity.setBitmap(CharityLogoBitmap);
                CharityIn.close();
            } catch (Exception e) {
                e.printStackTrace();

            }

            return charity;
        }

        @Override
        protected void onPostExecute(Charity charity) {
            UpdateCharityDetail(charity);

        }

    }
}
