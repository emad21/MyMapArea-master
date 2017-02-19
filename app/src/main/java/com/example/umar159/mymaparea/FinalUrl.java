package com.example.umar159.mymaparea;

/**
 * Created by UMAR159 on 2/12/17.
 */
public class FinalUrl {

    String url ="";

    public void setFinalUrl(String address,String place){

        address = address.replaceAll(" ,","+");
        address = address.replaceAll(" ","+");
        StringBuilder googlePlacesUrl = new StringBuilder("//https://maps.googleapis.com/maps/api/place/textsearch/xml?");
        googlePlacesUrl.append("query=nearby"+"+"+place+"+"+"in"+"+"+address);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=AIzaSyCcdwwAYJvH0yPm5a2eOsFOdgp4lMv7yYo");

        url = googlePlacesUrl.toString();

    }
    public String getFinalUrl(){
        return  url;
    }
}
