package com.example.botmdtest;

public class LocationItem {
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    private String locationName;
    public LocationItem(String name){
        this.locationName = name;
    }


}
