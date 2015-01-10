package com.example.android.navigationdrawerexample;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class StreetViewLocationRecord extends SugarRecord<StreetViewLocationRecord> implements ClusterItem {

    private double tilt;
    private double bearing;
    private double latitude;
    private double longitude;
    @Ignore
    private Bitmap image;

    public StreetViewLocationRecord() {
    }

    public double getLatitude() {
        return latitude;
    }

    public StreetViewLocationRecord setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public StreetViewLocationRecord setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getTilt() {
        return tilt;
    }

    public StreetViewLocationRecord setTilt(double tilt) {
        this.tilt = tilt;
        return this;
    }

    public double getBearing() {
        return bearing;
    }

    public StreetViewLocationRecord setBearing(double bearing) {
        this.bearing = bearing;
        return this;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap bmp) {
        this.image = bmp;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(getLatitude(), getLongitude());
    }

    public String getURL() {
        return "https://maps.googleapis.com/maps/api/streetview?size=640x400&location=" + getLatitude() + "," + getLongitude() + "&fov=90&heading=" +
                getBearing() + "&pitch=" + getTilt() + "&key=" + MainActivity.STREET_VIEW_IMAGE_API_KEY;
    }
}