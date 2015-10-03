package com.gametimegiving.mobile.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.gametimegiving.mobile.Application.BaseApplication;
import com.gametimegiving.mobile.Team;

import java.io.InputStream;
import java.net.URL;


public class Imageloader extends AsyncTask<String, Void, Team> {
    private static final String LOGO_BASE_URL = BaseApplication.getInstance().getMetaData(BaseApplication.META_DATA_LOGO_BASE_URL);

    @Override
    protected Team doInBackground(String... Team) {
        Team t = new Team();
        try {
            String imageurl = LOGO_BASE_URL + t.getLogo();
            InputStream in = (InputStream) new URL(imageurl).getContent();
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            t.setBitmap(bitmap);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    // @Override
    //protected void onPostExecute(Bitmap image) {
    //     ImageView image = (ImageView)result.view.findViewById(R.id.imageView1);
    //     image.setImageBitmap(result.bitmap);
    //     //result.flower.setBitmap(result.bitmap);
    //     imageCache.put(result.flower.getProductId(),result.bitmap);
    //}


}