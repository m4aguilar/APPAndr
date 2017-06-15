package com.example.mal90.appandr;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mal90 on 06/06/2017.
 */

public class QuestionActivity extends AppCompatActivity {

    String code;
    TextView textQuestion;
    Button buttonRA, buttonRB, buttonRC;
    RequestQueue webService;
    String url;
    String option = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        generarPregunta("http://192.168.1.134:8000/appQuestion/?user_key=0000");

        /*
        textQuestion = (TextView) findViewById(R.id.textQuestion);
        buttonRA = (Button) findViewById(R.id.buttonRA);
        buttonRB = (Button) findViewById(R.id.buttonRB);
        buttonRC = (Button) findViewById(R.id.buttonRC);


        //String code = getIntent().getStringExtra("hash");
        //textView.setText(code);

        url = "http://192.168.0.197:8000/appQuestion/?user_key=0000";

        webService = Volley.newRequestQueue(QuestionActivity.this);

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
                            String question = jObj.getString("question");
                            final String answer1 = jObj.getString("answer1");
                            final String answer2 = jObj.getString("answer2");
                            final String answer3 = jObj.getString("answer3");
                            final String correctAnswer = jObj.getString("correctAnswer");

                            textQuestion.setText(question);
                            buttonRA.setText(answer1);
                            buttonRB.setText(answer2);
                            buttonRC.setText(answer3);


                            Toast text = Toast.makeText(getApplicationContext(), "Opcion correcta: " + option, Toast.LENGTH_SHORT);
                            text.show();


                            buttonRA.setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(View v) {
                                    Toast text = Toast.makeText(getApplicationContext(), "boton 1 pulsado", Toast.LENGTH_SHORT);
                                    text.show();
                                    String button = "buttonRA";
                                    comprobarRespuesta(answer1, correctAnswer, button);
                                }
                            });
                            buttonRB.setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(View v) {
                                    Toast text = Toast.makeText(getApplicationContext(), "boton 2 pulsado", Toast.LENGTH_SHORT);
                                    text.show();
                                    String button = "buttonRB";
                                    comprobarRespuesta(answer2, correctAnswer, button);

                                }
                            });
                            buttonRC.setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(View v) {
                                    Toast text = Toast.makeText(getApplicationContext(), "boton 3 pulsado", Toast.LENGTH_SHORT);
                                    text.show();
                                    String button = "buttonRC";
                                    comprobarRespuesta(answer3, correctAnswer, button);
                                }
                            });
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
        */
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void comprobarRespuesta(String respuesta, String correctAnswer, String button) {

        //Para cargar el estilo del boton
        //ContextCompat.getDrawable(this, R.drawable.borderedondo);

        switch (button){
            case "buttonRA":
                if(respuesta.equals(correctAnswer)){
                    buttonRA.setBackground(ContextCompat.getDrawable(this, R.drawable.borderedondoverde));
                    sendPost();
                    Intent intent= new Intent(QuestionActivity.this, MapsActivity.class);
                    startActivityForResult(intent, 0);

                }
                else{
                    buttonRA.setBackground(ContextCompat.getDrawable(this, R.drawable.borderedondorojo));
                }
                break;
            case "buttonRB":
                if(respuesta.equals(correctAnswer)){
                    buttonRB.setBackground(ContextCompat.getDrawable(this, R.drawable.borderedondoverde));
                    sendPost();
                    Intent intent= new Intent(QuestionActivity.this, MapsActivity.class);
                    startActivityForResult(intent, 0);

                }
                else{
                    buttonRB.setBackground(ContextCompat.getDrawable(this, R.drawable.borderedondorojo));
                }
                break;
            case "buttonRC":
                if(respuesta.equals(correctAnswer)){
                    buttonRC.setBackground(ContextCompat.getDrawable(this, R.drawable.borderedondoverde));
                    sendPost();
                    Intent intent= new Intent(QuestionActivity.this, MapsActivity.class);
                    startActivityForResult(intent, 0);
                }
                else{
                    buttonRC.setBackground(ContextCompat.getDrawable(this, R.drawable.borderedondorojo));
                }
                break;
        }


        /*switch (option) {
            case "A":
                buttonRA.setBackground(ContextCompat.getDrawable(this, R.drawable.borderedondorojo));
                buttonRB.setBackground(ContextCompat.getDrawable(this, R.drawable.borderedondo));
                buttonRC.setBackground(ContextCompat.getDrawable(this, R.drawable.borderedondo));
                break;
            case "B":
                buttonRA.setBackground(ContextCompat.getDrawable(this, R.drawable.borderedondo));
                buttonRB.setBackground(ContextCompat.getDrawable(this, R.drawable.borderedondoverde));
                buttonRC.setBackground(ContextCompat.getDrawable(this, R.drawable.borderedondo));
                break;
            case "C":
                buttonRA.setBackground(ContextCompat.getDrawable(this, R.drawable.borderedondo));
                buttonRB.setBackground(ContextCompat.getDrawable(this, R.drawable.borderedondo));
                buttonRC.setBackground(ContextCompat.getDrawable(this, R.drawable.borderedondorojo));
                break;
        }*/
    }

    public void generarPregunta(String url){
        textQuestion = (TextView) findViewById(R.id.textQuestion);
        buttonRA = (Button) findViewById(R.id.buttonRA);
        buttonRB = (Button) findViewById(R.id.buttonRB);
        buttonRC = (Button) findViewById(R.id.buttonRC);


        //String code = getIntent().getStringExtra("hash");
        //textView.setText(code);

        //url = "";

        webService = Volley.newRequestQueue(QuestionActivity.this);

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
                            String question = jObj.getString("question");
                            final String answer1 = jObj.getString("answer1");
                            final String answer2 = jObj.getString("answer2");
                            final String answer3 = jObj.getString("answer3");
                            final String correctAnswer = jObj.getString("correctAnswer");

                            textQuestion.setText(question);
                            buttonRA.setText(answer1);
                            buttonRB.setText(answer2);
                            buttonRC.setText(answer3);


                            Toast text = Toast.makeText(getApplicationContext(), "Opcion correcta: " + option, Toast.LENGTH_SHORT);
                            text.show();


                            buttonRA.setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(View v) {
                                    Toast text = Toast.makeText(getApplicationContext(), "boton 1 pulsado", Toast.LENGTH_SHORT);
                                    text.show();
                                    String button = "buttonRA";
                                    comprobarRespuesta(answer1, correctAnswer, button);
                                }
                            });
                            buttonRB.setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(View v) {
                                    Toast text = Toast.makeText(getApplicationContext(), "boton 2 pulsado", Toast.LENGTH_SHORT);
                                    text.show();
                                    String button = "buttonRB";
                                    comprobarRespuesta(answer2, correctAnswer, button);

                                }
                            });
                            buttonRC.setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(View v) {
                                    Toast text = Toast.makeText(getApplicationContext(), "boton 3 pulsado", Toast.LENGTH_SHORT);
                                    text.show();
                                    String button = "buttonRC";
                                    comprobarRespuesta(answer3, correctAnswer, button);
                                }
                            });
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

    //Llama a una función del servidor para indicar que ha avanzado hasta la siguiente ubicación
    public void sendPost(){
        url = "http://192.168.1.134:8000/appProgress/?user_key=0000";

        webService = Volley.newRequestQueue(QuestionActivity.this);

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String data = response.toString();
                        JSONObject jObj = null;

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
