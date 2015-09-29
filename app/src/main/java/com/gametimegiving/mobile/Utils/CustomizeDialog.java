/***************************************************************************************
 * FileName : CustomizeDialog.java
 * Version : 1.0
 * DateOfCreation : 11/05/2012
 * Author : Sanjay Sharma
 * Dependencies :
 * Description : This is show a dialog box.
 * Classes : CustomizeDialog.java
 * ChangeHistory :
 ***************************************************************************************/

package com.gametimegiving.mobile.Utils;


import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.gametimegiving.mobile.R;


public class CustomizeDialog extends Dialog {

    Context context;

    public CustomizeDialog(Context context) {

        super(context, R.style.Theme_Dialog_Translucent);
        try {
            /** 'Window.FEATURE_NO_TITLE' - Used to hide the title */


            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            this.context = context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
