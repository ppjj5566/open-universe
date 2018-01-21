package com.example.john.openmap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.john.openmap.helper.users_location_list;
import com.example.john.openmap.visiter_viewer.at_visiter_view;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

/**
 * Created by ppjj5 on 2018-01-17.
 * it's can show users title and title_image!
 */

public class info_window implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {
    private Activity context;
    private users_location_list object;

    info_window(Activity context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        @SuppressLint("InflateParams")
        View v = context.getLayoutInflater().inflate(R.layout.marker_view,null);
        ImageView imageView = v.findViewById(R.id.title_image_view);
        TextView textView = v.findViewById(R.id.title_view);
        object = (users_location_list) marker.getTag();
        if(object != null) {
            Picasso.with(context)
                    .load(object.get_title_image())
                    .error(R.mipmap.ic_launcher) // will be displayed if the image cannot be loaded
                    .into(imageView);
            textView.setText(object.get_title());
        }else{
            Toast.makeText(context.getApplicationContext(),"no Object in marker!",Toast.LENGTH_LONG).show();
        }
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker)

    {
        at_users_view(object.get_id());
    }

    private void at_users_view(String id){
        Intent intent = new Intent(context.getApplicationContext(), at_visiter_view.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
}
