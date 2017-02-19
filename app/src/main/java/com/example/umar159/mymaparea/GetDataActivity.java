package com.example.umar159.mymaparea;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;


public class GetDataActivity extends ActionBarActivity {

    private TextView tvdata1;
    String url_="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test3_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        url_ = getIntent().getExtras().getString("thisurl");
        tvdata1 = (TextView) findViewById(R.id.tvx);

        Button btn = (Button) findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            String School = "foodstalls";

            @Override
            public void onClick(View v) {
                Log.d("onClick", "Button is Clicked");
                new jsonData().execute("https://maps.googleapis.com/maps/api/geocode/json?latlng=4.0587,101.6917&sensor=true&key=AIzaSyCcdwwAYJvH0yPm5a2eOsFOdgp4lMv7yYo");
              //  new jsonData().execute("https://maps.googleapis.com/maps/api/place/textsearch/xml?query=nearby+toilets+in+petalingjaya&location=3.0570872,101.6880012&sensor=true&key=AIzaSyCcdwwAYJvH0yPm5a2eOsFOdgp4lMv7yYo");
                 // new jsonData().execute(url_);
            }


        });


    }


    public class jsonData extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            String _url = params[0];
            String googlePlacesData = "";
           DownloadUrl downloadUrl = new DownloadUrl();
            try {
                googlePlacesData = downloadUrl.readUrl(_url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return googlePlacesData;
        }

      /*   List<PlaceDetails> placeDetails = null;
          XmlPullParserFactory factory = null;
          try {
              factory = XmlPullParserFactory.newInstance();
          } catch (XmlPullParserException e) {
              e.printStackTrace();
          }
          factory.setNamespaceAware(false);
          XmlPullParser parser = null;
          try {
              parser = factory.newPullParser();
          } catch (XmlPullParserException e) {
              e.printStackTrace();
          }

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
          placeDetails = XmlDataParser.parse(parser);


          return placeDetails;*/
    //        return _url;
     // }

  //      @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONArray jsonArray = null;
            JSONObject jsonObject = null;
            String address = "";
            try {
                jsonObject = new JSONObject((String) result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonArray = jsonObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject c = null;
            try {
                c = jsonArray.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                address = c.getString("formatted_address");
            } catch (JSONException e) {
                e.printStackTrace();

            }
            //address = address.replaceAll(", ","+");
           //address = address.replaceAll(" ","+");
            String[] items = address.split(" ,");
            int l = items.length;
          //  int x = (Integer)items.size();
            tvdata1.setText(l+":::"+items[l-1]);

        }}}
    //}
       /* protected void onPostExecute(String result) {
            super.onPostExecute(result);
   /*    FileOutputStream fileout = null;
       try {
           fileout = openFileOutput("text.txt", MODE_PRIVATE);
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
       OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
*/

         /*   for (int i = 0; i < result.size(); i++) {
                PlaceDetails placeDetail = result.get(i);

                String name = placeDetail.getName();
                String address = placeDetail.getFormatted_address();
                float rating = placeDetail.getRating();
                String lat = placeDetail.getLocation_data().getLatitude();
                String lng = placeDetail.getLocation_data().getLongitude();


                tvdata1.append(i + ":"+lat+":"+lng);

            }

             */
       //     tvdata1.append(result);            //setText(substr);

        //}
    //}*/
//}}
