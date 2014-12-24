package com.example.android.navigationdrawerexample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;

import java.util.Set;

/**
 * Created by dan-silver on 12/23/14.
 */
public class ClusterFragment extends Fragment implements OnMapReadyCallback{

    private ClusterManager<StreetViewLocationRecord> mClusterManager;
    private static GoogleMap map;
    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.v(MainActivity.LOG, "onCreateView");

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.cluster, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }


        if (map == null) {
            Log.v(MainActivity.LOG, "map was null");
            map = ((MapFragment) getFragmentManager() .findFragmentById(R.id.location_map)).getMap();
            setUpCluster();
        }
//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.location_map);
//        mapFragment.getMapAsync(this);
        return view;
    }
    private void setUpCluster() {
        Log.v(MainActivity.LOG, "setUpCluster");

        // Position the map.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(getActivity().getApplicationContext(), map);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraChangeListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setRenderer();

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            StreetViewLocationRecord offsetItem = new StreetViewLocationRecord(lat, lng);
            mClusterManager.addItem(offsetItem);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.v(MainActivity.LOG, "onMapReady");
        setUpCluster();
    }
}