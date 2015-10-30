package com.gametimegiving.mobile.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.text.NumberFormat;

public class Utilities {
    public void ShowMsg(Context ctx) {
        Toast.makeText(ctx, "You have no pledges!", Toast.LENGTH_LONG).show();
    }

    public void NotYetImplemented(Context ctx) {
        Toast.makeText(ctx, "Not Yet Implemented", Toast.LENGTH_LONG).show();
    }

    public void WriteSharedPref(String key, String val, Activity activity, String type) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (type) {
            case "s":
                editor.putString(key, val);
                break;
            case "b":
                boolean bVal = false;
                if (val == "true") {
                    bVal = true;
                }
                editor.putBoolean(key, bVal);
                break;
            case "i":
                Integer iVal = Integer.parseInt(val);
                editor.putInt(key, iVal);
                break;

        }

        editor.commit();
    }

    public String ReadSharedPref(String key, Activity activity, String type) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public Integer ReadSharedPref(String key, Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getInt(key, 0);
    }


    public String FormatCurrency(double num) {
        String value;
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        return defaultFormat.format(num);

    }

    public int RemoveCurrency(String dollars) {
        return Integer.parseInt(dollars.replace("$", "").replace(".00", ""));
    }

    public void ClearSharedPrefs(Activity activity) {
        final SharedPreferences sharedpreferences = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().commit();
    }
}