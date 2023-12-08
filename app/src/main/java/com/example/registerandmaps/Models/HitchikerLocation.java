package com.example.registerandmaps.Models;

import java.io.Serializable;
import java.util.Random;

public class HitchikerLocation implements Serializable {
    private double lat;
    private double lng;
    private int status;
    private String pickerUid;

    private String sharerUid;

    private String pickerEndCode;
    private String sharerEndCode;


    public HitchikerLocation(double lat, double lng, int status, String pickerUid, String sharerUid) {
        this.lat = lat;
        this.lng = lng;
        this.status = status;
        this.pickerUid = pickerUid;
        this.sharerUid = sharerUid;

        this.pickerEndCode = generateRandomEndCode();
        this.sharerEndCode = generateRandomEndCode();

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

    public String getPickerUid() {
        return pickerUid;
    }

    public void setPickerUid(String pickerUid) {
        this.pickerUid = pickerUid;
    }

    public String getSharerUid() {
        return sharerUid;
    }

    public void setSharerUid(String sharerUid) {
        this.sharerUid = sharerUid;
    }

    public String getPickerEndCode() {
        return pickerEndCode;
    }
    public String getSharerEndCode() {
        return sharerEndCode;
    }

    private String generateRandomEndCode() {
        Random random = new Random();
        int endCode = 10000 + random.nextInt(90000);
        return String.valueOf(endCode);
    }

}
