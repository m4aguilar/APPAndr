package com.example.mal90.appandr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


/**
 * Created by mal90 on 07/06/2017.
 */

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView zXingScannerView;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1 ;
    RequestQueue webService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

            }
        }
    }

    public void scan(View view){
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(final Result result) {
        String data = result.getText();
        Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
        //zXingScannerView.resumeCameraPreview(this);


        check(data);
    }


    public void check(final String result){
        //Comunicación con el servidor
        String url = "http://192.168.0.197:8000/check/?user_key=0000";


        //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();


        webService = Volley.newRequestQueue(ScannerActivity.this);

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String data = response.toString();
                        JSONObject jObj = null;
                        try {
                            jObj = new JSONObject(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            String hash = jObj.getString("hash");
                            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getApplicationContext(), hash, Toast.LENGTH_SHORT).show();
                            if(hash.equals(result)){
                                Intent intent= new Intent(ScannerActivity.this, QuestionActivity.class);
                                intent.putExtra("hash", "Es correcto");
                                startActivityForResult(intent, 0);
                            }else{
                                Intent intent= new Intent(ScannerActivity.this, MapsActivity.class);
                                startActivityForResult(intent, 0);
                                Toast.makeText(getApplicationContext(), "No es correcto", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        Toast text = Toast.makeText(getApplicationContext(), "onErrorResponse", Toast.LENGTH_SHORT);
                        text.show();

                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        webService.add(request);
    }


}
