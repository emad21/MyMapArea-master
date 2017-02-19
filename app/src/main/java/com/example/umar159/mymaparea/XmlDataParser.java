package com.example.umar159.mymaparea;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by UMAR159 on 2/12/17.
 */
public class XmlDataParser {
    public static List<PlaceDetails> parse( XmlPullParser parser) {

        // List of StackSites that we will return
        List<PlaceDetails> places;
        places = new ArrayList<PlaceDetails>();

        // temp holder for current StackSite while parsing
        PlaceDetails place = null;
        Location location = null;
        // temp holder for current text value while parsing
        String curText = "";

        try {
            // Get our factory and PullParser
        /*    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            // Open up InputStream and Reader of our file.
            FileInputStream fis = ctx.openFileInput("StackSites.xml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            // point the parser to our file.
            xpp.setInput(reader);*/

            // get initial eventType
            int eventType = parser.getEventType();

            // Loop through pull events until we reach END_DOCUMENT
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Get the current tag
                String tagname = parser.getName();

                // React to different event types appropriately
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equals("result")) {
                            // If we are starting a new <site> block we need
                            //a new StackSite object to represent it
                            place = new PlaceDetails();
                        }else if (tagname.equals("location")) {
                            // if </about> use setAbout() on curSite
                             location = new Location();              //parseLocation(parser);
                            // place.locationList = new ArrayList<Location>();
                           // place.setLocation_data(location);
                        } else if (location != null){
                            if (tagname.equals("lat")){
                                location.setLatitude(parser.nextText()); //country.name = parser.nextText();
                            } else if (tagname.equals("lng")){
                                location.setLongitude(parser.nextText());
                            }
                        }
                        break;

                    case XmlPullParser.TEXT:
                        //grab the current text so we can use it in END_TAG event
                        curText = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equals("result")) {
                            // if </site> then we are done with current Site
                            // add it to the list.
                            places.add(place);
                        } else if (tagname.equals("name")) {
                            // if </name> use setName() on curSite
                            place.setName(curText);
                        } else if (tagname.equals("formatted_address")) {
                            // if </link> use setLink() on curSite
                            place.setFormatted_address(curText);          ;
                        }else if (tagname.equals("location")) {
                            // if </about> use setAbout() on curSite
                           // Location location = parseLocation(parser);
                           // place.locationList = new ArrayList<Location>();
                            place.setLocation_data(location);
                        } else if (tagname.equalsIgnoreCase("rating")) {
                            // if </image> use setImgUrl() on curSite
                              place.setRating(Float.parseFloat(curText));
                        }
                        break;

                    default:
                        break;
                }
                //move on to next iteration
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return the populated list.
        return places;
    }
    public static Location parseLocation( XmlPullParser parser)throws XmlPullParserException
            , IOException  {
        String tagname = parser.getName();
               Location location = new Location();
       /*while (parser.nextTag() == XmlPullParser.START_TAG) {
            if (parser.getName().equals("lat")) {
                location.setLatitude(parser.getText());//     getText());
            }else if (parser.getName().equals("lng")) {
                location.setLongitude(parser.getText());       //getText());
            }else{
                parser.next();
            }

          //  }*/
        if (tagname.equals("lat")) {
            location.setLatitude(parser.getText());//     getText());
        }else if (tagname.equals("name")) {
            location.setLongitude(parser.getText());       //getText());
        }else{
            parser.next();
        }
        return location;
        }
}


