package com.example.umar159.mymaparea;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ToiletRateActivity extends AppCompatActivity {

    private ListView lvPlaces;
    String url_="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listtoiletlayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvPlaces = (ListView)findViewById(R.id.lview22);

        url_ = getIntent().getExtras().getString("theurl");

        new GetNearbyPlacesList().execute(url_);


    }
    public class GetNearbyPlacesList extends AsyncTask<String, String, List<PlaceDetails>> {

        String googlePlacesData;
        // GoogleMap mMap;
        String _url;

        @Override
        protected List<PlaceDetails> doInBackground(String... params) {
            try {
                Log.d("GetNearbyPlacesData", "doInBackground entered");
                //  mMap = (GoogleMap) params[0];
                _url = (String) params[0];

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
                PlaceAdapter adapter = new PlaceAdapter(getApplicationContext(), R.layout.ratetoiletlayout, result);
                lvPlaces.setAdapter(adapter);
                lvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // list item click opens a new detailed activity
                    @Override 
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


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
                holder.tvName = (TextView) convertView.findViewById(R.id.tvName1);
                holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress1);
                holder.rbToiletRating = (RatingBar)convertView.findViewById(R.id.rbToilet);
                holder.rbToiletRating1 = (RatingBar)convertView.findViewById(R.id.rbToil);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // Then later, when you want to display image
            final ViewHolder finalHolder = holder;

            holder.tvName.setText(nearbyPlaceslList.get(position).getName());        // get("place_name"));
            holder.tvAddress.setText(nearbyPlaceslList.get(position).getFormatted_address());   //get("vicinity"));
            // rating bar
            holder.rbToiletRating.setRating(nearbyPlaceslList.get(position).getRating());

          //  holder.rbToiletRating1.s
            return convertView;
        }


        class ViewHolder {
            private TextView tvName;
            private TextView tvAddress;
            private RatingBar rbToiletRating;
            private RatingBar rbToiletRating1;
        }

    }


}
