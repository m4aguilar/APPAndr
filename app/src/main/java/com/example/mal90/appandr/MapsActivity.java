package com.example.mal90.appandr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private GoogleMap mMap;
    private static final String TAG = MapsActivity.class.getSimpleName();
    Location mLastLocation;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;
    RequestQueue webService;
    Button buttonScanner;
    TextView textView;


    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1 ;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        buttonScanner = (Button) findViewById(R.id.buttonScanner);
        buttonScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view.getContext(), ScannerActivity.class);
                startActivityForResult(intent, 0);
            }
        });



        //Pedir permiso de locacización
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            }
        }

        // Create a GoogleApiClient instance
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();



    }


    //Función para pedir permisos
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);


        //Añadir marcadores
        generarMarkers("http://192.168.0.134:8000/appMarkers/?user_key=0000");

        //Actualizar pista
        updateTrail("http://192.168.0.134:8000/appTrail/?user_key=0000");



        //Modifica el estilo del mapa
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        //Comprueba si los permisos están concedidos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            //Toast text = Toast.makeText(getApplicationContext(), "Permiso concedido", Toast.LENGTH_SHORT);
            //text.show();
        } else {
            // Show rationale and request permission.
            //Toast text = Toast.makeText(getApplicationContext(), "Permiso denegado", Toast.LENGTH_SHORT);
            //text.show();
        }
    }

    //Se ejecuta continuamente para obtener la ubicación, se actualiza cada 1000 ms
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            // The Wearable API is unavailable
            Toast text = Toast.makeText(getApplicationContext(), "Fallo en ConnectionFailed",
                    Toast.LENGTH_SHORT);
            text.show();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //MarkerOptions markerOptions = new MarkerOptions();
        //markerOptions.position(latLng);
        //markerOptions.title("Current Position");
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        //mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        LatLng nuevaLocation = new LatLng(location.getLatitude(), location.getLongitude());
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(nuevaLocation));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        //stop location updates
        if (mGoogleApiClient != null) {
            //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    public void updateTrail(String url){

        webService = Volley.newRequestQueue(MapsActivity.this);
        textView = (TextView) findViewById(R.id.textView);
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
                            String trail = jObj.getString("trail");
                            textView.setText(trail);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        Toast text = Toast.makeText(getApplicationContext(), "onErrorResponse updateTrail", Toast.LENGTH_SHORT);
                        text.show();

                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        webService.add(request);

    }

    public void generarMarkers(String url){
        webService = Volley.newRequestQueue(MapsActivity.this);

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject object = response;
                        try {
                            JSONArray Jarray  = object.getJSONArray("locations");
                            for (int i = 0; i < Jarray.length(); i++){
                                JSONObject Jasonobject = Jarray.getJSONObject(i);
                                String latitud = Jasonobject.getString("latitud");
                                String longitud = Jasonobject.getString("longitud");
                                double lat = Double.parseDouble(latitud);
                                double lon = Double.parseDouble(longitud);
                                LatLng marker = new LatLng(lat, lon);

                                mMap.addMarker(new MarkerOptions().position(marker).
                                        icon(BitmapDescriptorFactory.
                                                defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        Toast text = Toast.makeText(getApplicationContext(), "onErrorResponse generarMarkers", Toast.LENGTH_SHORT);
                        text.show();

                    }
                });
        // Adds the JSON array request "arrayreq" to the request queue


        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        webService.add(request);
    }



}
