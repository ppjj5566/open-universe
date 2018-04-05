package com.example.john.openmap.provider_shower;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.john.openmap.R;
import com.example.john.openmap.app.AppController;
import com.example.john.openmap.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.john.openmap.app.AppConfig.USERS_STORY;

/**
 * Created by ppjj5 on 2018-02-25.
 *
 */

@SuppressLint("ValidFragment")
public class work_shop extends Fragment {
    private static final String TAG = providers.class.getSimpleName();

    private static ArrayList<Data_from_server> data_from_servers = new ArrayList<>();

    data_list data_list;

    RecyclerView recyclerView;

    String id;

    Context context;

    int number_of_story;

    @SuppressLint("ValidFragment")
    public work_shop(Context context){
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
        View v = inflater.inflate(R.layout.provider_information, container, false);

        SQLiteHandler db = new SQLiteHandler(context.getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();

        id = user.get("email");

        final JsonArrayRequest info_request = new JsonArrayRequest(USERS_STORY, new Response.Listener<JSONArray>() {
            @SuppressLint("ShowToast")
            @Override
            public void onResponse(JSONArray jsonArray) {
                try {
                    data_from_servers.clear();
                    number_of_story = jsonArray.length();
                    if(number_of_story == 0){

                    }else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            String image = jsonObject.getString("image");
                            String sentence = jsonObject.getString("sentence");
                            int list_of_story = jsonObject.getInt("story_list");
                            int content = jsonObject.getInt("content");
                            data_from_servers.add(new Data_from_server(sentence, image, content, null, null));
                            data_list = new data_list(data_from_servers);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(),volleyError.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(info_request);

        data_from_servers.add(new Data_from_server(null,null,7,null,null));
        data_list = new data_list(data_from_servers);
        recyclerView = v.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(data_list);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return v;
    }
}
