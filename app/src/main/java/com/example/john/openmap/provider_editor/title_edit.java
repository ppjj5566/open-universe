package com.example.john.openmap.provider_editor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.john.openmap.R;
import com.example.john.openmap.app.AppController;
import com.example.john.openmap.helper.SQLiteHandler;
import com.example.john.openmap.provider_shower.providers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.john.openmap.app.AppConfig.UPLOAD_STORY;

public class title_edit extends Activity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_maker);

        final EditText story_maker = findViewById(R.id.tool_for_make_story);
        Button show_story = findViewById(R.id.show_story);

        show_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteHandler db = new SQLiteHandler(getApplicationContext());
                HashMap<String,String> user = db.getUserDetails();
                String id = user.get("email");
                upload_image(id, story_maker.getText().toString());
            }
        });

    }

    private void upload_image(final String id, final String story) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_STORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject user = jsonObject.getJSONObject("user");
                    Boolean upload_state = user.getBoolean("state");
                    if(upload_state){
                        go_to_provider();
                    }else{
                        Toast.makeText(getApplicationContext(),"android code error", Toast.LENGTH_SHORT);
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplication(),"Json error: " + e.getMessage(), Toast.LENGTH_LONG);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("story", story);
                params.put("id", id);
                params.put("number", String.valueOf(2));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(title_edit.this, providers.class);
        startActivity(intent);
    }

    private void go_to_provider(){
        Intent intent = new Intent(getApplicationContext(),providers.class);
        startActivity(intent);
    }
}
