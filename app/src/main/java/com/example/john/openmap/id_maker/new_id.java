package com.example.john.openmap.id_maker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.john.openmap.map_activity.MainActivity;
import com.example.john.openmap.R;
import com.example.john.openmap.app.AppController;
import com.example.john.openmap.helper.SQLiteHandler;
import com.example.john.openmap.provider_shower.providers;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.john.openmap.app.AppConfig.NEW_LOCATION;

public class new_id extends Activity implements OnMapReadyCallback{

    Button make;
    double loc_x;
    double loc_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_maker);

        make = findViewById(R.id.make);
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment location_setting = (MapFragment) fragmentManager.findFragmentById(R.id.location_setting);
        location_setting.getMapAsync(this);

        make.setOnClickListener(new View.OnClickListener() {
            SQLiteHandler db = new SQLiteHandler(getApplicationContext());
            @Override
            public void onClick(View v) {
                HashMap<String,String> user = db.getUserDetails();
                String user_email = user.get("email");
                upload_location_to_server(user_email,loc_x,loc_y);
            }
        });
    }

    private void upload_location_to_server(final String id_, final double location_x, final double location_y){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NEW_LOCATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if(!error){
                        boolean upload_state = jsonObject.getBoolean("upload_state");
                        if(upload_state){
                            go_to_story_maker();
                            Toast.makeText(getApplicationContext(),
                                    "Now you can create your own message to share other people",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"server error",Toast.LENGTH_LONG);
                        }
                    }
                    else{
                        String errorMsg = jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        },
            new Response.ErrorListener(){
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("provider_id",id_);
                params.put("locx", String.valueOf(location_x));
                params.put("locy", String.valueOf(location_y));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        go_to_main_activity();
    }

    public void go_to_story_maker(){
        Intent intent = new Intent(new_id.this, providers.class);
        startActivity(intent);
        finish();
    }

    public void go_to_main_activity(){
        Intent intent = new Intent(new_id.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    final LatLng Sando = new LatLng(37.399452, 126.927704);

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(Sando));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
                googleMap.clear();
                googleMap.addMarker(markerOptions.title(latLng.toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                loc_x = latLng.latitude;
                loc_y = latLng.longitude;
            }
        });
    }
}
