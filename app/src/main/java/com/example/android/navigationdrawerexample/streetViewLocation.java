package com.example.android.navigationdrawerexample;

import com.orm.SugarRecord;

public class streetViewLocation extends SugarRecord<streetViewLocation> {

    private double tilt;
    private double bearing;
    private double latatidue;
    private double longitude;

    public streetViewLocation() {
    }

    public double getLatatidue() {
        return latatidue;
    }

    public streetViewLocation setLatatidue(double latatidue) {
        this.latatidue = latatidue;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public streetViewLocation setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getTilt() {
        return tilt;
    }

    public streetViewLocation setTilt(double tilt) {
        this.tilt = tilt;
        return this;
    }

    public double getBearing() {
        return bearing;
    }

    public streetViewLocation setBearing(double bearing) {
        this.bearing = bearing;
        return this;
    }
}
