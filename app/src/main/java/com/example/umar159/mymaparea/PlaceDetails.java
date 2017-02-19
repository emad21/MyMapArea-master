package com.example.umar159.mymaparea;

import java.util.ArrayList;

/**
 * Created by UMAR159 on 2/12/17.
 */
public class PlaceDetails {


    public String name;
    public String formatted_address;

    public float rating;
    public String place_id;
    public Location location_data;

    public void setLocation_data(Location location_data) {
        this.location_data = location_data;
    }

    public Location getLocation_data() {

        return location_data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }


    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }
}
