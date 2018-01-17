package com.example.john.openmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by ppjj5566 on 2018-01-08.
 *
 */

public class image_view extends AsyncTask<String, Void, Bitmap> {

    private Bitmap bitmap;

    public Bitmap image_view(){
        return bitmap;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            bitmap = BitmapFactory.decodeStream((InputStream)new URL(urls[0]).getContent());
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
