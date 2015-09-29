package com.gametimegiving.mobile.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.text.NumberFormat;

public class Utilities {
    public void ShowMsg(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    public void NotYetImplemented(Context ctx) {
        Toast.makeText(ctx, "Not Yet Implemented", Toast.LENGTH_LONG).show();
    }

    public void WriteSharedPref(String key, String val, Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public String ReadSharedPref(String key, Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public String FormatCurrency(double num, int precision) {
        String value;
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        value = defaultFormat.format(num);
        if (value.endsWith(".00")) {
            int centsIndex = value.lastIndexOf(".00");
            if (centsIndex != -1) {
                value = value.substring(0, centsIndex);
            }
        }
        return value;

    }

    public String FormatCurrency(double num) {
        String value;
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        return defaultFormat.format(num);

    }
}