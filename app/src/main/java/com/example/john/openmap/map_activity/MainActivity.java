package com.example.john.openmap.map_activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.john.openmap.app.AppController;
import com.example.john.openmap.helper.users_location_list;
import com.example.john.openmap.tab_layout;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.example.john.openmap.app.AppConfig.ADRESS;
import static com.example.john.openmap.app.AppConfig.USERS_LOCATION;

@SuppressLint("ValidFragment")
public class MainActivity extends SupportMapFragment implements OnMapReadyCallback{

    private static final String TAG = MainActivity.class.getSimpleName();
    com.example.john.openmap.map_activity.info_window info_window;
    users_location_list users_location_list;
    GoogleMap googleMap;
    Context context;
    tab_layout tab_layout;

    @SuppressLint("ValidFragment")
    public MainActivity(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMapAsync(this);
    }

    /**
 * check the users id is exciting.
 **/

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap = googleMap;
        final JsonArrayRequest stringRequest = new JsonArrayRequest(USERS_LOCATION, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                try {
                    new info_window(getActivity());
                    for(int i = 0; i < response.length(); i++) {
                        JSONObject json = (JSONObject)response.get(i);
                        Double location_x = json.getDouble("location_x");
                        Double location_y = json.getDouble("location_y");
                        String id = json.getString("id");
                        String string_image = json.getString("head_image");
                        String title = json.getString("heed_title");
                        LatLng latLng = new LatLng(location_x, location_y);
                        users_location_list = new users_location_list(id,ADRESS + string_image,title);
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                        googleMap.setInfoWindowAdapter(info_window);
                        googleMap.setOnInfoWindowClickListener(info_window);
                        Marker marker = googleMap.addMarker(markerOptions);
                        marker.setTag(users_location_list);
                        marker.showInfoWindow();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("MainActivity :", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}
