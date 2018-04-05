package com.example.john.openmap.provider_editor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.john.openmap.app.AppController;
import com.example.john.openmap.helper.SQLiteHandler;
import com.example.john.openmap.provider_shower.providers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.john.openmap.app.AppConfig.UPLOAD_IMAGE;

/**
 * Created by John on 2017-11-05.
 *
 */

public class image_edit extends Activity{

    private String image;
    String id;
    int sequence;
    int PICK_IMAGE_REQUEST = 1;
    Intent intent = getIntent();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        id = user.get("email");
        sequence = intent.getIntExtra("amp_number",1);
        image_is_chused();
    }

    private void image_is_chused(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image = getStringImage(bitmap);
                upload_image(id,image,sequence);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Failed to get image", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void upload_image(final String id, final String title_Image, final int list_number){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_IMAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error = jsonObject.getBoolean("error");
                    JSONObject user = jsonObject.getJSONObject("user");
                    Boolean state = user.getBoolean("state");
                    if(!error) {
                        if (state) {
                            go_to_provider();
                        } else {
                            Toast.makeText(getApplication(), "can't assec the image", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplication(),"json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",id);
                params.put("title_image",title_Image);
                params.put("number", String.valueOf(2));
                params.put("list_number",String.valueOf(list_number));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encoded_Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encoded_Image;
    }

    private void go_to_provider(){
        Intent intent = new Intent(getApplicationContext(),providers.class);
        startActivity(intent);
    }

}
