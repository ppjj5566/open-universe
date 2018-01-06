package com.example.john.openmap.provider_editor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.john.openmap.app.AppConfig.UPLOAD_IMAGE;

@SuppressLint("Registered")
public class title_image_edit extends Activity {
    ImageView imageview;
    Button done;

    private String image;
    String id;

    int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.image_editor);
        super.onCreate(savedInstanceState);
        imageview = findViewById(R.id.imageView);
        done = findViewById(R.id.done);

        imageview.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 SQLiteHandler db = new SQLiteHandler(getApplicationContext());
                 HashMap<String, String> user = db.getUserDetails();
                 id = user.get("email");
                 image_is_chused();
             }
         });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_title_image(id,image);
            }
        });
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
                imageview.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Failed to get image", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void upload_title_image(final String id, final String title_Image){
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
                params.put("number", String.valueOf(1));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void go_to_provider(){
        Intent intent = new Intent(title_image_edit.this,providers.class);
        startActivity(intent);
    }

}
