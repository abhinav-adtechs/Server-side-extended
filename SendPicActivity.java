package com.example.android.camera2basic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jordiie-admin on 5/3/16.
 */
public class SendPicActivity extends Activity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    Bitmap resized;
    String UPLOAD_URL="http://nodetry.hostzi.com/buysend.php";
    String EMOTION_API_URL = "https://api.projectoxford.ai/emotion/v1.0/recognize" ;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.send_pic);
        ImageView imageView;
        imageView = (ImageView) findViewById(R.id.image_set);
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String path = sharedpreferences.getString("file_path", null);
        Log.v("path", path);
        File imgFile = new File(path);
        if (imgFile.exists()) {
            Bitmap mybitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            resized = mybitmap.createScaledBitmap(mybitmap, 500, 400, false);

            imageView.setImageBitmap(resized);


        }

        uploadImage();


    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private String imageURL ;


    private void uploadImage() {
        Log.v("CLICKED", "Clicked");

        StringRequest postRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.v("UPLOAD_IMAGE_RESPONSE", response);

                        imageURL = response;

                        Toast.makeText(getApplicationContext(),
                                "Successfully Posted",
                                Toast.LENGTH_SHORT).show();
                        getSmile();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        "failed to insert", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String uploadImage = getStringImage(resized);
                Log.v("uploadedimage", uploadImage);
                Map<String, String> params = new HashMap<String, String>();

                params.put("image", uploadImage);


                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(postRequest);

    }

    public void getSmile()
    {
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, EMOTION_API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.v("response",response);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> mHeaders = new HashMap<String, String>() ;
                mHeaders.put("Content-Type", "application/json" );
                mHeaders.put("Ocp-Apim-Subscription-Key", "5ab8c3eabf484de780a722cd6e9d0775");

                return mHeaders ;
            }



            @Override
            public byte[] getBody() {


                String body="{ \"url\": \"http://idahopedsgi.com/wp-content/uploads/2016/02/happy-human-face-happy-face.jpg\" }";
                return body.getBytes();
            }


        } ;

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }
}




