package com.example.registerandmaps.Models;

import java.io.Serializable;

public class HitchikerLocation implements Serializable {
    private double lat;
    private double lng;
    private int status;
    private String pickerCode;

    private String sharerCode;


    public HitchikerLocation(double lat, double lng, int status, String pickerCode, String sharerCode) {
        this.lat = lat;
        this.lng = lng;
        this.status = status;
        this.pickerCode = pickerCode;
        this.sharerCode = sharerCode;
    }

    public HitchikerLocation() {
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

    public String getPickerCode() {
        return pickerCode;
    }

    public void setPickerCode(String pickerCode) {
        this.pickerCode = pickerCode;
    }

    public String getSharerCode() {
        return sharerCode;
    }

    public void setSharerCode(String sharerCode) {
        this.sharerCode = sharerCode;
    }
}
