package com.example.john.openmap;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.john.openmap.UIconnector.tools;
import com.example.john.openmap.activity.LoginActivity;
import com.example.john.openmap.app.AppConfig;
import com.example.john.openmap.app.AppController;
import com.example.john.openmap.helper.SQLiteHandler;
import com.example.john.openmap.helper.SessionManager;
import com.example.john.openmap.helper.users_location_list;
import com.example.john.openmap.id_maker.new_id;
import com.example.john.openmap.provider_shower.providers;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.john.openmap.app.AppConfig.ADRESS;
import static com.example.john.openmap.app.AppConfig.USERS_LOCATION;

public class MainActivity extends Activity implements OnMapReadyCallback{

    private static final String TAG = MainActivity.class.getSimpleName();
    private SQLiteHandler db;
    private SessionManager session;
    info_window info_window = new info_window(MainActivity.this);
    users_location_list users_location_list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // SqLite database handle
        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Displaying the user details on the screen
        // Logout button click event
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        db = new SQLiteHandler(getApplicationContext());
        final HashMap<String,String> user_id;
        user_id = db.getUserDetails();

        Button menu_button = findViewById(R.id.information);
        Button provider_information = findViewById(R.id.provider);

        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go_to_information();
            }
        });

        provider_information.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                check_the_id(user_id.get("email"));
            }
        });
    }

    /**
 * check the users id is exciting.
 **/

    private void check_the_id(final String id){
        String tag_string_req = "check_req";
        StringRequest stringRequest = new StringRequest(Request.Method.POST , AppConfig.CHECK_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Provider Response:" + response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if(!error){
                        JSONObject user = jsonObject.getJSONObject("user");
                        Boolean provider_user_id = user.getBoolean("provider_user_id");
                        if(!provider_user_id){
                            go_to_new_id();
                        }else{
                            provider_page();
                        }
                    }else{
                        String errorMsg = jsonObject.getString("error_msg");
                        Log.e(TAG, errorMsg);
                        Toast.makeText(getApplicationContext(), errorMsg , Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "checking Error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("email", id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest,tag_string_req);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        final JsonArrayRequest stringRequest = new JsonArrayRequest(USERS_LOCATION, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                try {
                    for(int i = 0; i < response.length(); i++) {
                        JSONObject json = (JSONObject)response.get(i);
                        Double location_x = json.getDouble("location_x");
                        Double location_y = json.getDouble("location_y");
                        String id = json.getString("id");
                        String string_image = json.getString("head_image");
                        String title = json.getString("heed_title");
                        String image = ADRESS + string_image;
                        LatLng latLng = new LatLng(location_x, location_y);
                        users_location_list = new users_location_list(location_x,location_y,id,image,title);
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                        googleMap.setInfoWindowAdapter(info_window);
                        googleMap.setOnInfoWindowClickListener(info_window);
                        Marker marker = googleMap.addMarker(markerOptions);
                        marker.setTag(users_location_list);
                        marker.showInfoWindow();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("MainActivity :", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void go_to_new_id(){
        Intent intent = new Intent(MainActivity.this, new_id.class);
        startActivity(intent);
    }

    private void go_to_information(){
        Intent intent = new Intent(MainActivity.this, tools.class);
        startActivity(intent);
    }

    private void provider_page(){
        Intent intent = new Intent(MainActivity.this, providers.class);
        startActivity(intent);
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
