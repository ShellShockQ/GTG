
package com.gametimegiving.mobile.Utils;


import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.gametimegiving.mobile.R;


public class CustomizeDialog extends Dialog {

    private Context context;

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
