package com.gametimegiving.mobile.Fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gametimegiving.mobile.R;

public class AboutUsFragment extends Fragment {

    public static AboutUsFragment newInstance() {
        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aboutus, container, false);
        TextView version = (TextView) view.findViewById(R.id.versionnum);
        TextView copy = (TextView) view.findViewById(R.id.copy);
        TextView credits = (TextView) view.findViewById(R.id.credits);
        TextView rate = (TextView) view.findViewById(R.id.rate);
        TextView support = (TextView) view.findViewById(R.id.support);
        TextView terms = (TextView) view.findViewById(R.id.terms);
        TextView privacy = (TextView) view.findViewById(R.id.privacy);

        Resources resources = getResources();
        String version_value = resources.getString(R.string.about_version);
        String copy_value = resources.getString(R.string.about_copy);
        String credits_value = resources.getString(R.string.about_credits);
        String rate_value = resources.getString(R.string.about_rate);
        String support_value = resources.getString(R.string.about_support);
        String terms_value = resources.getString(R.string.about_terms);
        String privacy_value = resources.getString(R.string.about_privacy);

        try {
            PackageManager packageManager = getActivity().getPackageManager();
            String packageName = getActivity().getPackageName();
            PackageInfo pInfo = packageManager.getPackageInfo(packageName, 0);
            String versionName = pInfo.versionName;
            version_value = String.format(java.util.Locale.ENGLISH, "Version %s", versionName);
        } catch (Exception exc) {
        }

        version.setText(Html.fromHtml(version_value));
        copy.setText(Html.fromHtml(copy_value));
        credits.setText(Html.fromHtml(credits_value));
        rate.setText(Html.fromHtml(rate_value));
        support.setText(Html.fromHtml(support_value));
        terms.setText(Html.fromHtml(terms_value));
        privacy.setText(Html.fromHtml(privacy_value));

        version.setMovementMethod(LinkMovementMethod.getInstance());
        copy.setMovementMethod(LinkMovementMethod.getInstance());
        credits.setMovementMethod(LinkMovementMethod.getInstance());
        rate.setMovementMethod(LinkMovementMethod.getInstance());
        support.setMovementMethod(LinkMovementMethod.getInstance());
        terms.setMovementMethod(LinkMovementMethod.getInstance());
        privacy.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }

    public void onFacebookClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.gametimegiving.com/facebook"));
        startActivity(intent);
    }

    public void onTwitterClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.gametimegiving.com/twitter"));
        startActivity(intent);
    }

    public void onGraceNoteClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.gametimegiving.com/gracenote"));
        startActivity(intent);
    }

}
