package com.example.john.openmap.UIconnector;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.john.openmap.MainActivity;
import com.example.john.openmap.R;
import com.example.john.openmap.activity.LoginActivity;
import com.example.john.openmap.app.AppController;
import com.example.john.openmap.helper.SQLiteHandler;
import com.example.john.openmap.helper.SessionManager;
import com.example.john.openmap.provider_shower.Data_from_server;
import com.example.john.openmap.provider_shower.data_list;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.john.openmap.app.AppConfig.ADRESS;
import static com.example.john.openmap.app.AppConfig.SEND_PROVIDERS_INFORMATION;

public class tools extends Activity{

    private SQLiteHandler db;

    private static ArrayList<Data_from_server> data_from_servers = new ArrayList<>();

    data_list data_list;

    RecyclerView recyclerView;

    private static String TAG = tools.class.getSimpleName();

    static String name;
    static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_information);

        recyclerView =findViewById(R.id.recyclerView);

        db = new SQLiteHandler(getApplicationContext());

        HashMap<String,String> user = db.getUserDetails();

        name = user.get("name");
        email = user.get("email");

        data_from_servers.clear();

        owner_information_request(email);

        Data_from_server data_from_server = new Data_from_server(null, null, 4, name, email);
        data_from_servers.add(data_from_server);
        Data_from_server data_from_server1 = new Data_from_server(null,null,6,null,null);
        data_from_servers.add(data_from_server1);

        data_list = new data_list(data_from_servers);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    void owner_information_request(final String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEND_PROVIDERS_INFORMATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean error = jsonObject.getBoolean("error");
                    if(!error){
                        Log.d(TAG,"response : " + response);
                        JSONObject user = jsonObject.getJSONObject("user");
                        String image = user.getString("image");
                        String title = user.getString("title");
                        Data_from_server data_from_server = new Data_from_server(title, ADRESS + image, 5, null, null);
                        data_from_servers.add(data_from_server);
                        recyclerView.setAdapter(data_list);
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Request error" + e.getMessage(),Toast.LENGTH_LONG).show();
                    Log.e("Request error", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Volley error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Volley error", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",id);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(tools.this, MainActivity.class));
        data_from_servers.clear();
        finish();
    }
}
