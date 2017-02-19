package com.example.umar159.mymaparea;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

public class ToiletsMapsActivity extends FragmentActivity implements OnMapReadyCallback,
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            LocationListener {

        private GoogleMap mMap;
        double latitude;
        double longitude;
        private int PROXIMITY_RADIUS = 10000;
        GoogleApiClient mGoogleApiClient;
        android.location.Location mLastLocation;
        Marker mCurrLocationMarker;
        LocationRequest mLocationRequest;
        String _url = "";
        private ListView lvPlaces;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.toilet_map_activity);


            lvPlaces = (ListView)findViewById(R.id.lview1);
            _url = getIntent().getExtras().getString("thisurl");
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }

            //Check if Google Play Services Available or not
            if (!CheckGooglePlayServices()) {
                Log.d("onCreate", "Finishing test case since Google Play Services are not available");
                finish();
            } else {
                Log.d("onCreate", "Google Play Services available.");
            }

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map3p);
            mapFragment.getMapAsync(this);
        }

        private boolean CheckGooglePlayServices() {
            GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
            int result = googleAPI.isGooglePlayServicesAvailable(this);
            if (result != ConnectionResult.SUCCESS) {
                if (googleAPI.isUserResolvableError(result)) {
                    googleAPI.getErrorDialog(this, result,
                            0).show();
                }
                return false;
            }
            return true;
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
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            //Initialize Google Play Services
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
            mMap.clear();
            Object[] DataTransfer = new Object[2];
            DataTransfer[0] = mMap;
            DataTransfer[1] = _url;
          //  GetPlaceDetailsXml getNearbyPlacesData = new GetPlaceDetailsXml();
            //getNearbyPlacesData.execute(DataTransfer);
                  new GetNearbyPlacesList().execute(_url);
            Toast.makeText(ToiletsMapsActivity.this, "Nearby Toilets", Toast.LENGTH_LONG).show();


            Button btnTrate = (Button) findViewById(R.id.toil1);
            btnTrate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("onClick", "Button is Clicked");
                    mMap.clear();
                    Intent listintent = new Intent(ToiletsMapsActivity.this, ToiletRateActivity.class);
                    listintent.putExtra("theurl", _url);
                    startActivity(listintent);
                }
            });


        }

        protected synchronized void buildGoogleApiClient() {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

        @Override
        public void onConnected(Bundle bundle) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }


        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onLocationChanged(android.location.Location location) {
            Log.d("onLocationChanged", "entered");

            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            //Place current location marker
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = mMap.addMarker(markerOptions);

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            Toast.makeText(ToiletsMapsActivity.this, "Your Current Location", Toast.LENGTH_LONG).show();

            Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

            //stop location updates
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                Log.d("onLocationChanged", "Removing Location Updates");
            }
            Log.d("onLocationChanged", "Exit");

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }

        public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

        public boolean checkLocationPermission() {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Asking user if explanation is needed
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    //Prompt the user once explanation has been shown
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);


                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
                return false;
            } else {
                return true;
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               String permissions[], int[] grantResults) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_LOCATION: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted. Do the
                        // contacts-related task you need to do.
                        if (ContextCompat.checkSelfPermission(this,
                                android.Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

                            if (mGoogleApiClient == null) {
                                buildGoogleApiClient();
                            }
                            mMap.setMyLocationEnabled(true);
                        }

                    } else {

                        // Permission denied, Disable the functionality that depends on this permission.
                        Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                    }
                    return;
                }

                // other 'case' lines to check for other permissions this app might request.
                // You can add here other case statements according to your requirement.
            }
        }



    public class GetNearbyPlacesList extends AsyncTask<String, String, List<PlaceDetails>> {

        String googlePlacesData;
        // GoogleMap mMap;
        String url;

        @Override
        protected List<PlaceDetails> doInBackground(String... params) {
            try {
                Log.d("GetNearbyPlacesData", "doInBackground entered");
                //  mMap = (GoogleMap) params[0];
                url = (String) params[0];

                List<PlaceDetails> placeDetails=null;
                XmlPullParserFactory factory = null;
                factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = null;
                parser = factory.newPullParser();
                URL url = null;
                try {
                    url = new URL(_url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                URLConnection ucon = null;
                try {
                    ucon = url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputStream is = null;
                try {
                    is = ucon.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    parser.setInput(is, null);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                placeDetails=XmlDataParser.parse(parser);



                return placeDetails;
                //Log.d("GooglePlacesReadTask", "doInBackground Exit");
            } catch (Exception e) {
                Log.d("GooglePlacesReadTask", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<PlaceDetails> result) {
            Log.d("GooglePlacesReadTask", "onPostExecute Entered");
            if(result != null) {
                PlaceAdapter adapter = new PlaceAdapter(getApplicationContext(), R.layout.t_rowlayout, result);
                lvPlaces.setAdapter(adapter);
                lvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // list item click opens a new detailed activity
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        PlaceDetails googlePlace = result.get(position); // getting the model
                        double lat = Double.parseDouble(googlePlace.getLocation_data().getLatitude());//    get("lat"));
                        double lng = Double.parseDouble(googlePlace.getLocation_data().getLongitude());//              .get("lng"));
                        String placeName = googlePlace.getName();               //get("place_name");
                        String vicinity = googlePlace.getFormatted_address();                       //get("vicinity");
                        LatLng latLng = new LatLng(lat, lng);
                        markerOptions.position(latLng);
                        markerOptions.title(placeName + " : " + vicinity);
                        mMap.addMarker(markerOptions);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        //move map camera
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
            //  ShowNearbyPlaces(nearbyPlacesList);
            //Log.d("GooglePlacesReadTask", "onPostExecute Exit");
        }
    }

    public class PlaceAdapter extends ArrayAdapter {

        private List<PlaceDetails> nearbyPlaceslList;
        private int resource;
        private LayoutInflater inflater;

        public PlaceAdapter(Context context, int resource, List<PlaceDetails> objects) {
            super(context, resource, objects);
            nearbyPlaceslList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv4);
                holder.tvAddress = (TextView) convertView.findViewById(R.id.tv5);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // Then later, when you want to display image
            final ViewHolder finalHolder = holder;

            holder.tvName.setText(nearbyPlaceslList.get(position).getName());        // get("place_name"));
            holder.tvAddress.setText(nearbyPlaceslList.get(position).getFormatted_address());   //get("vicinity"));
            return convertView;
        }


        class ViewHolder {
            private TextView tvName;
            private TextView tvAddress;
        }

    }
    }