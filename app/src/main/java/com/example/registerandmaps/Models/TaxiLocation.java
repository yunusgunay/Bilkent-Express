package com.example.registerandmaps.Models;

import java.io.Serializable;

public class TaxiLocation implements Serializable {
    private double lat;
    private double lng;
    private int status;
    private String pickerUid;
    private String sharerUid;
    private String location;

    public TaxiLocation(double lat, double lng, int status, String pickerUid, String sharerUid,String location) {
        this.lat = lat;
        this.lng = lng;
        this.status = status;
        this.pickerUid = pickerUid;
        this.sharerUid = sharerUid;
        this.location = location;
    }

    public TaxiLocation() {
    }
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPickerUid(String pickerUid) {
        this.pickerUid = pickerUid;
    }

    public void setSharerUid(String sharerUid) {
        this.sharerUid = sharerUid;
    }

    public String getPickerUid() {
        return pickerUid;
    }

    public String getSharerUid() {
        return sharerUid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
