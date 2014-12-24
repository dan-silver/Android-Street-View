package com.example.android.navigationdrawerexample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
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
import com.google.maps.android.clustering.ClusterManager;

/**
 * Created by dan-silver on 12/23/14.
 */
public class ClusterFragment extends Fragment implements OnMapReadyCallback {


    private SupportMapFragment fragment;
    private ClusterManager<StreetViewLocationRecord> mClusterManager;
    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cluster, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
    }




    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            map = fragment.getMap();
            map.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.cluster, container, false);
        if (map == null) {
            map = ((MapFragment) getFragmentManager() .findFragmentById(R.id.location_map)).getMap();
        }
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.location_map);
        mapFragment.getMapAsync(this);

        return view;
    }
    private void setUpCluster() {


        // Position the map.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(getActivity().getApplicationContext(), map);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraChangeListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);

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
        setUpCluster();
        map = googleMap;
    }
}
