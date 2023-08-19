package com.example.uktourism.Model.DataModel;

import android.net.Uri;

public class Place {

    private String placeId,placeName,location,description,placePic;

    public Place(String placeName, String location, String description, String placePic) {
        this.placeName = placeName;
        this.location = location;
        this.description = description;
        this.placePic = placePic;
    }

    public Place(){

    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPlacePic() {
        return placePic;
    }

    public void setPlacePic(String placePic) {
        this.placePic = placePic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
