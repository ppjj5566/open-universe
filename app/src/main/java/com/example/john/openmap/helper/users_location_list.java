package com.example.john.openmap.helper;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class users_location_list {

    private Double location_x;
    private Double location_y;

    private String id;

    private String title_image;
    private String title;

    public users_location_list(Double location_x, Double location_y, String id,  String title_image, String title) {
        this.location_x = location_x;
        this.location_y = location_y;
        this.id = id;
        this.title_image = title_image;
        this.title = title;
    }

    public String get_id(){
        return id;
    }

    public String get__title_iamge(){
        return title_image;
    }

    public String get_title(){
        return title;
    }

    public Double get_location_x(){
        return location_x;
    }

    public Double get_location_y(){
        return location_y;
    }


}
