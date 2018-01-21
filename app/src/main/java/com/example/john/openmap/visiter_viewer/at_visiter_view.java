package com.example.john.openmap.visiter_viewer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.john.openmap.R;
import com.example.john.openmap.app.AppController;
import com.example.john.openmap.provider_shower.Data_from_server;
import com.example.john.openmap.provider_shower.data_list;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.john.openmap.app.AppConfig.ADRESS;
import static com.example.john.openmap.app.AppConfig.PROVIDER_MAIN;

// provider pages recycler_view add list

public class at_visiter_view extends Activity{
    RecyclerView recyclerView;
    ArrayList<Data_from_server> data_from_servers = new ArrayList<>();

    data_list data_list;

    @Override
    protected void onStart() {
        super.onStart();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_information);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        get_provider_state(extras.getString("id"));
        recyclerView = findViewById(R.id.recyclerView);


    }

    // Here you can change the procedure of provider page add list

    private void get_provider_state(final String id_) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROVIDER_MAIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if(!error){
                        data_from_servers.clear();
                        JSONObject user = jsonObject.getJSONObject("user");
                        String tit = user.getString("title");
                        String tit_image = user.getString("title_image");
                        String sto = user.getString("story");
                        String ima = user.getString("image");
                        Data_from_server title = new Data_from_server(tit,null,0,null,null);
                        data_from_servers.add(title);
                        Data_from_server title_image = new Data_from_server(null ,ADRESS + tit_image,1,null,null);
                        data_from_servers.add(title_image);
                        Data_from_server story = new Data_from_server(sto,null,2,null,null);
                        data_from_servers.add(story);
                        Data_from_server image = new Data_from_server(null,ADRESS + ima,3,null,null);
                        data_from_servers.add(image);
                        data_list = new data_list(data_from_servers);
                        recyclerView.setAdapter(data_list);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT);
                    }
                }catch (JSONException e){
                    Toast.makeText(getApplication(),e.getMessage(),Toast.LENGTH_LONG);
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("id",id_);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
