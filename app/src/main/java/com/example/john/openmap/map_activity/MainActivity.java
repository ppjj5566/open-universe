package com.example.john.openmap.map_activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
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
import com.example.john.openmap.R;
import com.example.john.openmap.app.AppController;
import com.example.john.openmap.helper.users_location_list;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import static com.example.john.openmap.app.AppConfig.ADRESS;
import static com.example.john.openmap.app.AppConfig.USERS_LOCATION;

@SuppressLint("ValidFragment")
public class MainActivity extends Fragment implements OnMapReadyCallback{

    private static final String TAG = MainActivity.class.getSimpleName();
    users_location_list users_location_list;
    GoogleMap googleMap;
    Context context;
    Activity activity;
    SupportMapFragment mapView;
    info_window info_window;
    View v;

    @SuppressLint("ValidFragment")
    public MainActivity(Context context,Activity activity){
        this.context = context;
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        v = layoutInflater.inflate(R.layout.main_activity,viewGroup,false);
        mapView = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapView.getMapAsync(this);
        mapView.getActivity();
        info_window = new info_window(mapView.getActivity(),mapView.getContext());
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
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
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject json = (JSONObject) response.get(i);
                            Double location_x = json.getDouble("location_x");
                            Double location_y = json.getDouble("location_y");
                            String id = json.getString("id");
                            String string_image = json.getString("head_image");
                            String title = json.getString("heed_title");
                            LatLng latLng = new LatLng(location_x, location_y);
                            users_location_list = new users_location_list(id, ADRESS + string_image, title);
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                            googleMap.setInfoWindowAdapter(info_window);
                            googleMap.setOnInfoWindowClickListener(info_window);
                            Marker marker = googleMap.addMarker(markerOptions);
                            marker.setTag(users_location_list);
                            marker.showInfoWindow();
                        }
                } catch (Exception e) {
                    Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
