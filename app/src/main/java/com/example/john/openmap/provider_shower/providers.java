package com.example.john.openmap.provider_shower;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.john.openmap.MainActivity;
import com.example.john.openmap.R;
import com.example.john.openmap.app.AppController;
import com.example.john.openmap.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.john.openmap.app.AppConfig.ADRESS;
import static com.example.john.openmap.app.AppConfig.PROVIDER_MAIN;

public class providers extends Activity {
    private static final String TAG = providers.class.getSimpleName();

    private static ArrayList<Data_from_server> data_from_servers = new ArrayList<>();
    int image_code;

    data_list data_list;

    RecyclerView recyclerView;

    String id;
    public int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_information);

        recyclerView = findViewById(R.id.recyclerView);

        SQLiteHandler db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();

        id = user.get("email");

        get_provider_state(id);

        data_list = new data_list(data_from_servers);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(data_list);
    }

    private void get_provider_state(final String id_) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROVIDER_MAIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        JSONObject user = jsonObject.getJSONObject("user");
                        String tit_image = user.getString("title_image");
                        String tit = user.getString("title");
                        String ima = user.getString("image");
                        String sto = user.getString("story");

                        data_from_servers.clear();
                        Data_from_server story = new Data_from_server(null, ADRESS + tit_image, 1,null,null);
                        data_from_servers.add(story);
                        story = new Data_from_server(tit, null, 0,null,null);
                        data_from_servers.add(story);
                        story = new Data_from_server(null, ADRESS + tit, 3,null,null);
                        data_from_servers.add(story);
                        story = new Data_from_server(sto, null, 2,null,null);
                        data_from_servers.add(story);
                    } else {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG);
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id_);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void image_is_chused(int location, Intent intent){
        image_code = location;
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(providers.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}