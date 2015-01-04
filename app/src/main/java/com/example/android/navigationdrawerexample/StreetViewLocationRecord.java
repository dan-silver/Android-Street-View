package com.example.android.navigationdrawerexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.io.InputStream;

public class StreetViewLocationRecord extends SugarRecord<StreetViewLocationRecord> implements ClusterItem {

    private double tilt;
    private double bearing;
    private double latitude;
    private double longitude;
    @Ignore
    private Bitmap image;

    public StreetViewLocationRecord() {}

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


    public void setImage(Bitmap bmp) {
        this.image = bmp;
    }
    public Bitmap getImage() {
        return image;
    }
    @Override
    public LatLng getPosition() {
        return new LatLng(getLatitude(), getLongitude());
    }

    public String getURL() {
        return "http://maps.googleapis.com/maps/api/streetview?size=640x400&location=" + getLatitude() + "," + getLongitude() + "&fov=90&heading=" +
        getBearing() + "&pitch=" + getTilt();
    }
}