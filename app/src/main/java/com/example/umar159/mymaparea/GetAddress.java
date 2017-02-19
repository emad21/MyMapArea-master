package com.example.umar159.mymaparea;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

/**
 * Created by UMAR159 on 2/12/17.
 */
public class GetAddress  extends AsyncTask<Object, String, String> {

    String _url;
    FinalUrl furl;
    String nearbyplace;

    @Override
    protected String doInBackground(Object... params) {

        furl =(FinalUrl) params[0];
        _url =(String)params[1];
        nearbyplace =(String)params[2];
        String googlePlacesData = "";
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlacesData = downloadUrl.readUrl(_url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        JSONArray jsonArray = null;
        JSONObject jsonObject = null;
        String address="";
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
        furl.setFinalUrl(address,nearbyplace);

    }
}


