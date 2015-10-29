package com.gametimegiving.mobile.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gametimegiving.mobile.Activity.BaseActivity;
import com.gametimegiving.mobile.Adapter.CharityListAdapter;
import com.gametimegiving.mobile.Adapter.TeamListAdapter;
import com.gametimegiving.mobile.Application.BaseApplication;
import com.gametimegiving.mobile.Charity;
import com.gametimegiving.mobile.Parse.BaseApi;
import com.gametimegiving.mobile.Parse.BaseApiListener;
import com.gametimegiving.mobile.Parse.CharityJSONParser;
import com.gametimegiving.mobile.Parse.HttpManager;
import com.gametimegiving.mobile.Parse.JSONArray;
import com.gametimegiving.mobile.Parse.JSONObject;
import com.gametimegiving.mobile.Parse.RequestPackage;
import com.gametimegiving.mobile.Parse.TeamJSONParser;
import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Team;
import com.gametimegiving.mobile.Utils.Constant;
import com.gametimegiving.mobile.Utils.CustomizeDialog;
import com.gametimegiving.mobile.Utils.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    private final static String TAG = "ProfileFragment";
    public static boolean hasProfileUpdate = false;
    private static int RESULT_LOAD_IMAGE = 1;
    protected BaseApi mApi;
    boolean isDonationSelect = false;
    String mApiServerUrl = BaseApplication.getInstance().getMetaData(BaseApplication.META_DATA_API_SERVER_URL);
    private TextView TvProfileName;
    private Button mBtnCharity, mBtnTeam, mBtnSave;
    private RoundedImageView mProfilepictureView;
    private ImageView mImgEdit;
    private String mProfileUrl, mProfileId;
    private String isLogInFrom;
    private boolean isDrawerLocked;
    private DisplayImageOptions mOptions;
    private Spinner mDonationMethodSpinner;
    private boolean spinnerFlag = true;
    private CustomizeDialog mCustomizeDialog;
    private String[] arr_charty;
    private String[] arr_team;
    private List<Team> listOfTeams;
    private List<Charity> listOfCharities;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static boolean isNetworkStatusAvialable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if (netInfos != null)
                if (netInfos.isConnected())
                    return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);
        mApi = BaseApplication.getInstance().Api;

        TvProfileName = (TextView) view.findViewById(R.id.profile_name);
        TvProfileName.setSelected(true);
        mBtnCharity = (Button) view.findViewById(R.id.btncharity_spinner);
        mBtnCharity.setOnClickListener(this);
        mBtnTeam = (Button) view.findViewById(R.id.btnteam_spinner);
        mBtnTeam.setOnClickListener(this);

        mDonationMethodSpinner = (Spinner) view.findViewById(R.id.spinner3);

        if (CharityListAdapter.selectedCharity != null && CharityListAdapter.selectedCharity.size() > 0) {
            String chariy = CharityListAdapter.selectedCharity.toString();
            mBtnCharity.setText(chariy.substring(1, chariy.length() - 1));
        }
        if (TeamListAdapter.selectedTeams != null && TeamListAdapter.selectedTeams.size() > 0) {
            String team = TeamListAdapter.selectedTeams.toString();
            mBtnTeam.setText(team.substring(1, team.length() - 1));
        }


        /*
        * Save Data in SharedPreference
        * */
        final SharedPreferences sharedpreferences = getActivity().getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        final String profileName = sharedpreferences.getString(Constant.PROFILENAME, null);
        mProfileUrl = sharedpreferences.getString(Constant.PROFILEURL, null);
        mProfileId = sharedpreferences.getString(Constant.PROFILEID, null);
        isLogInFrom = sharedpreferences.getString(Constant.ISLOGINFROM, null);
        isDrawerLocked = sharedpreferences.getBoolean(Constant.ISPROFILESUBMITTED, false);
        mProfilepictureView = (RoundedImageView) view.findViewById(R.id.profilePicture);


        /*
        * Set Profile Name come from Facebook Twitter and Signup
        * */
        if (null != profileName)
            TvProfileName.setText(profileName);
        setProfileImage();

        mImgEdit = (ImageView) view.findViewById(R.id.editimage);

        if (isLogInFrom.equals("facebook") || isLogInFrom.equals("twitter"))
            mImgEdit.setEnabled(false);

        mImgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasProfileUpdate = true;
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        mBtnSave = (Button) view.findViewById(R.id.btn_save);

        addListenerOnSpinnerItemSelection();
        mApi.getCharity();
        mApi.getTeam();

        mBtnSave.setOnClickListener(new View.OnClickListener() {

            /*
            * check item select or not in spinner
            * */
            @Override
            public void onClick(View v) {
                Map<String, String> CharityIds = new HashMap<>();
                if (CharityListAdapter.selectedCharity != null && CharityListAdapter.selectedCharity.size() > 0 &&
                        TeamListAdapter.selectedTeams != null && TeamListAdapter.selectedTeams.size() > 0 && isDonationSelect) {
                    SharedPreferences sharedpreferences = getActivity().getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("mycharities", CharityListAdapter.selectedCharity.toString());
                    editor.putString("myCharityIds", CharityListAdapter.selectedCharityIds.toString());
                    editor.putString("myTeamIds", TeamListAdapter.selectedTeamsIds.toString());
                    editor.putString("myteam", TeamListAdapter.selectedTeams.toString());
                    editor.putBoolean(Constant.ISPROFILESUBMITTED, true);
                    editor.commit();
                    BaseActivity.mDrawerToggle.setDrawerIndicatorEnabled(true);
                    BaseActivity.mToolbarTitle.setText("Select Game");
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.ll_container, SelectGameFragment.newInstance());
                    ft.commit();
                } else {
                    Toast.makeText(getActivity(), "Please Select Charity , Team And Donation Method", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        if (BaseActivity.bitmap != null)
            mProfilepictureView.setImageBitmap(BaseActivity.bitmap);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("TAG", "onPause");
        mApi.removeEventListener((BaseApiListener) getActivity());
    }

    /*
    *
    * set Profile Image Come from Facebook Twitter
    * */
    public void setProfileImage() {

        if (null != mProfileUrl) {
            new LoadProfileImage(mProfilepictureView).execute(mProfileUrl);
        } else {
            mOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true).cacheOnDisc(true)
                    .displayer(new RoundedBitmapDisplayer(200))
                    .build();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    getActivity()).defaultDisplayImageOptions(mOptions)
                    .build();
            ImageLoader.getInstance().init(config);
            if (null != mProfileId)
                ImageLoader.getInstance().displayImage("http://graph.facebook.com/" + mProfileId + "/picture?type=large", mProfilepictureView, mOptions);

        }
    }

    /*
    *
    *
    * Spinner Donation Method
    * */
    public void addListenerOnSpinnerItemSelection() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount())
                    ((TextView) v.findViewById(android.R.id.text1)).setText("Donation Method");
                ((TextView) v.findViewById(android.R.id.text1)).setTextColor(Color.BLACK);
                ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Pay Pal");
        adapter.add("Credit/Debit card");
        adapter.add("Donation Method"); //This is the text that will be displayed as hint.


        mDonationMethodSpinner.setAdapter(adapter);
        if (BaseActivity.selectedDonationMethod != -1)
            mDonationMethodSpinner.setSelection(BaseActivity.selectedDonationMethod);
        else
            mDonationMethodSpinner.setSelection(adapter.getCount()); //set the hint the default selection so it appears on launch.

        mDonationMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

                                                         {

                                                             @Override
                                                             public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                 if (spinnerFlag && BaseActivity.selectedDonationMethod == -1) {
                                                                     ((TextView) adapterView.getChildAt(0)).setText("Donation Method");
                                                                     ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                                                                     ((TextView) adapterView.getChildAt(0)).setTextSize(18);
                                                                     spinnerFlag = false;
                                                                 } else {
                                                                     BaseActivity.selectedDonationMethod = i;
                                                                     isDonationSelect = true;
                                                                     int sid = mDonationMethodSpinner.getSelectedItemPosition();

                                                                     ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                                                                     ((TextView) adapterView.getChildAt(0)).setTextSize(18);

                                                                 }
                                                             }

                                                             @Override
                                                             public void onNothingSelected(AdapterView<?> adapterView) {

                                                             }
                                                         }

        );
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.btncharity_spinner:
                GetCharities(mApiServerUrl + "/api/charity");
                mCustomizeDialog = new CustomizeDialog(getActivity());
                mCustomizeDialog.setCancelable(false);
                mCustomizeDialog.setContentView(R.layout.organization_dialog);
                TextView tvTitle = (TextView) mCustomizeDialog.findViewById(R.id.tvtitle);
                Button btnDone = (Button) mCustomizeDialog.findViewById(R.id.btn_done);

                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCustomizeDialog.dismiss();
                    }
                });

                mCustomizeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (CharityListAdapter.selectedCharity != null && CharityListAdapter.selectedCharity.size() > 0) {
                            String chariy = CharityListAdapter.selectedCharity.toString();
                            mBtnCharity.setText(chariy.substring(1, chariy.length() - 1));
                        } else {
                            mBtnCharity.setText(getResources().getString(R.string.Charityprompt));
                        }
                    }
                });
                mCustomizeDialog.show();
                break;
            case R.id.btnteam_spinner:
                GetTeams(mApiServerUrl + "/api/team");
                mCustomizeDialog = new CustomizeDialog(getActivity());
                mCustomizeDialog.setCancelable(false);
                mCustomizeDialog.setContentView(R.layout.organization_dialog);
                TextView tvTitle1 = (TextView) mCustomizeDialog.findViewById(R.id.tvtitle);
                tvTitle1.setText("Select Team");
                Button btnDone1 = (Button) mCustomizeDialog.findViewById(R.id.btn_done);
                btnDone1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCustomizeDialog.dismiss();
                    }
                });
                mCustomizeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (TeamListAdapter.selectedTeams != null && TeamListAdapter.selectedTeams.size() > 0) {
                            String team = TeamListAdapter.selectedTeams.toString();
                            mBtnTeam.setText(team.substring(1, team.length() - 1));
                        } else {
                            mBtnTeam.setText(getResources().getString(R.string.Teamprompt));
                        }
                    }
                });
                mCustomizeDialog.show();
                break;


        }
    }

    private void GetTeams(String uri) {
        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(uri);
        GetTeamsAsynch task = new GetTeamsAsynch();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p);

    }

    private void GetCharities(String uri) {
        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(uri);
        GetCharitiesAsynch task = new GetCharitiesAsynch();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            BaseActivity.bitmap = BitmapFactory.decodeFile(picturePath);
            mProfilepictureView.setImageBitmap(BaseActivity.bitmap);

        }
    }

    /*  *
      * Get Team List From Json
      * */
    @Override
    public void onGetTeam(JSONArray results) {
        int len = results.length();
        arr_team = new String[len];
        if (len > 0) {
            for (int i = 0; i < results.length(); i++) {
                try {
                    JSONObject json = new JSONObject(results.getJSONObject(i));
                    String name = json.getString("name");

                    arr_team[i] = name;
                } catch (Exception e) {
                    Log.d("TAG", e.getMessage());
                }
            }


        }
    }

    /*
    *
    * Get Charity List From Json
    * */
    @Override
    public void onGetCharity(JSONArray results) {
        int len = results.length();
        arr_charty = new String[len];
        if (len > 0) {
            for (int i = 0; i < results.length(); i++) {
                try {
                    JSONObject json = new JSONObject(results.getJSONObject(i));
                    String name = json.getString("name");

                    arr_charty[i] = name;
                } catch (Exception e) {
                    Log.d("TAG", e.getMessage());
                }
            }
        }
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            bmImage.setImageBitmap(bitmap);
        }
    }

    public class GetTeamsAsynch extends AsyncTask<RequestPackage, String, List<Team>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<Team> doInBackground(RequestPackage... strings) {

            String content = HttpManager.getData(strings[0]);
            listOfTeams = TeamJSONParser.parseFeed(content);
            return listOfTeams;
        }

        @Override
        protected void onPostExecute(List<Team> s) {
            if (s == null) {
                Log.i(TAG, "Can't connect to Webservice");
                return;
            }
            Log.i(TAG, "OnPost Execute :");
            //FlowerList = FlowerXMLParser.parseFeed(s);
//            updateDisplay();
            ListView listView1 = (ListView) mCustomizeDialog.findViewById(R.id.list_organisation);
            TeamListAdapter teamListAdapter = new TeamListAdapter(getActivity(), listOfTeams);
            listView1.setAdapter(teamListAdapter);


        }

    }

    public class GetCharitiesAsynch extends AsyncTask<RequestPackage, String, List<Charity>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<Charity> doInBackground(RequestPackage... strings) {
            String content = HttpManager.getData(strings[0]);
            listOfCharities = CharityJSONParser.parseFeed(content);
            return listOfCharities;
        }

        @Override
        protected void onPostExecute(List<Charity> s) {
            if (s == null) {
                Log.i(TAG, "Can't connect to Webservice");
                return;
            }
            Log.i(TAG, "OnPost Execute :");
            //FlowerList = FlowerXMLParser.parseFeed(s);
//            updateDisplay();
            ListView listView1 = (ListView) mCustomizeDialog.findViewById(R.id.list_organisation);
            CharityListAdapter charityListAdapter = new CharityListAdapter(getActivity(), listOfCharities);
            listView1.setAdapter(charityListAdapter);


        }

    }
}
