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

    public void WriteSharedPref(String key, String val, Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch ("i") {
            case "s":
                editor.putString(Constant.ACTIVEGAME, val);
                break;
            case "b":
                boolean bVal = false;
                if (val == "true") {
                    bVal = true;
                }
                editor.putBoolean(Constant.ACTIVEGAME, bVal);
                break;
            case "i":
                Integer iVal = Integer.parseInt(val);
                editor.putInt(Constant.ACTIVEGAME, iVal);
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
}