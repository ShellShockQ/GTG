package com.gametimegiving.mobile.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.gametimegiving.mobile.R;


public class CommanDialog {
    /**
     * Function to display simple Alert Dialog
     *
     * @param context - application context
     * @param title   - alert dialog title
     */
    public static void showAlertDialogForInternetConnection(Context context, String title) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle("No Internet Connection");

        // Setting Dialog Message
        alertDialog.setMessage("You don't have internet connection.");

        // Setting alert dialog icon
        alertDialog.setIcon(R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    /*
    *
    *
    * Show dialog for Exit Application
    *
    * */

    public static void exitfromapplication(final Activity ctx) {
        new AlertDialog.Builder(ctx)
                // .setIcon(R.drawable.ic_launcher)

                //  .setTitle("GAME TIME GIVING")
                .setMessage("Are you want to exit from GTG?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ctx.finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


}
