package com.example.john.openmap.UIconnector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.john.openmap.app.AppConfig.GET_PROVIDER_INFORMATION;

/**
 * Created by John on 2017-11-17.
 *
 */

public class go_to_title_editer extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.story_maker);

        final EditText editText = findViewById(R.id.tool_for_make_story);
        Button button = findViewById(R.id.show_story);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = getIntent();
                SQLiteHandler db = new SQLiteHandler(getApplicationContext());
                HashMap<String,String> user = db.getUserDetails();
                String id = user.get("email");
                Bundle story = intent.getExtras();
                assert story != null;
                upload_head_title(editText.getText().toString(), story.getString("image"),id);
            }
        });
    }

    private void upload_head_title(final String title, final String image, final String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PROVIDER_INFORMATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject user = jsonObject.getJSONObject("user");
                    boolean state = user.getBoolean("state");
                    if(state){
                        go_to_tools();
                    }else{
                        Toast.makeText(getApplicationContext(),jsonObject.getString("error_msg"),Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    Log.e("json_error", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
                    Log.e("Request error",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",id);
                map.put("image",image);
                map.put("title",title);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void go_to_tools(){
        Intent intent = new Intent(getApplicationContext(),tools.class);
        startActivity(intent);
        finish();
    }
}
