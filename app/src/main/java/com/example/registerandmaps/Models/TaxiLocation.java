package com.example.registerandmaps.Models;

import java.io.Serializable;

public class TaxiLocation implements Serializable {
    private double lat;
    private double lng;
    private int status;

    public TaxiLocation() {
    }

    public TaxiLocation(double lat, double lng, int status) {
        this.lat = lat;
        this.lng = lng;
        this.status = status;
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
}
