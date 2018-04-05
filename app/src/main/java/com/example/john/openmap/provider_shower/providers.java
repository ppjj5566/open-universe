package com.example.john.openmap.provider_shower;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

@SuppressLint("ValidFragment")
public class providers extends Fragment {
    private static final String TAG = providers.class.getSimpleName();

    private static ArrayList<Data_from_server> data_from_servers = new ArrayList<>();

    data_list data_list;

    RecyclerView recyclerView;

    String id;

    Context context;

    @SuppressLint("ValidFragment")
    public providers(Context context){
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.provider_information,container,false);

        SQLiteHandler db = new SQLiteHandler(context.getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();

        id = user.get("email");

        get_provider_state(id);

        data_list = new data_list(data_from_servers);
        recyclerView = v.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return v;

    }

    private void get_provider_state(final String id_) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROVIDER_MAIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d(TAG,"response : " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        JSONObject user = jsonObject.getJSONObject(" ");
                        String tit_image = user.getString("title_image");
                        String tit = user.getString("title");
                        String ima = user.getString("image");
                        String sto = user.getString("story");
                        data_from_servers.clear();
                        Data_from_server story = new Data_from_server(null, ADRESS + tit_image, 1,null,null);
                        data_from_servers.add(story);
                        story = new Data_from_server(tit, null, 0,null,null);
                        data_from_servers.add(story);
                        story = new Data_from_server(null, ADRESS + ima, 3,null,null);
                        data_from_servers.add(story);
                        story = new Data_from_server(sto, null, 2,null,null);
                        data_from_servers.add(story);
                        story = new Data_from_server(null,null,7,null,null);
                        data_from_servers.add(story);
                        recyclerView.setAdapter(data_list);
                    } else {
                        Toast.makeText(context.getApplicationContext(), "error", Toast.LENGTH_SHORT);
                    }
                } catch (JSONException e) {
                    Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(),
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
}