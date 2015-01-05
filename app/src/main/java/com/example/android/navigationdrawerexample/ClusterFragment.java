package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dan-silver on 12/23/14.
 */

public class ClusterFragment extends Fragment implements GoogleMap.OnMapLongClickListener, ClusterManager.OnClusterClickListener<StreetViewLocationRecord>, ClusterManager.OnClusterInfoWindowClickListener<StreetViewLocationRecord>, ClusterManager.OnClusterItemClickListener<StreetViewLocationRecord>, ClusterManager.OnClusterItemInfoWindowClickListener<StreetViewLocationRecord> {
    private ClusterManager<StreetViewLocationRecord> mClusterManager;
    private GoogleMap mMap;
    private static View view;
    Activity activity;

    private void setUpMapIfNeeded() {
        if (mMap != null)
            return;

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.location_map)).getMap();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (mMap != null) {
            startDemo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        addNewItems();
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        getActivity().getActionBar().setHomeButtonEnabled(false);

    }

    private void addNewItems() {
        Log.v(MainActivity.LOG, "Adding new items");
        for (long id : ((MainActivity) activity).newLocationIds) {
            final StreetViewLocationRecord loc = StreetViewLocationRecord.findById(StreetViewLocationRecord.class, id);
            Log.v(MainActivity.LOG, "FOUND id: " + id + "in the arraylist!");
            MainActivity.il.loadImage(loc.getURL(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    loc.setImage(loadedImage);
                    addItem(loc);
                }
            });
        }
        ((MainActivity) activity).newLocationIds.clear();
    }

    private void addItem(StreetViewLocationRecord r) {
        mClusterManager.addItem(r);
        mClusterManager.cluster();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.cluster, container, false);
            setUpMapIfNeeded();
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        addNewItems();
        return view;
    }

    @Override
    public void onMapLongClick(LatLng point) {
        if (activity instanceof MainActivity)
            ((MainActivity) activity).switchToExploreWithPoint(point);
    }

    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    private class PersonRenderer extends DefaultClusterRenderer<StreetViewLocationRecord> {
        private final IconGenerator mIconGenerator = new IconGenerator(getActivity());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getActivity());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimensionWidth;
        private final int mDimensionHeight;

        public PersonRenderer() {
            super(getActivity(), mMap, mClusterManager);

            View multiProfile = getActivity().getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getActivity());
            mDimensionWidth = (int) getResources().getDimension(R.dimen.cluster_image_width);
            mDimensionHeight = (int) getResources().getDimension(R.dimen.cluster_image_height);

            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimensionWidth, mDimensionHeight));
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(StreetViewLocationRecord r, MarkerOptions markerOptions) {
            // Draw a single record
            // Load image, decode it to Bitmap and return Bitmap to callback
            mImageView.setImageBitmap(r.getImage());
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<StreetViewLocationRecord> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<>(Math.min(4, cluster.getSize()));

            for (StreetViewLocationRecord p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Bitmap bmp = p.getImage();
                Drawable drawable = new BitmapDrawable(null, bmp);
                drawable.setBounds(0, 0, mDimensionWidth, mDimensionHeight);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, mDimensionWidth, mDimensionHeight);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

    @Override
    public boolean onClusterClick(Cluster<StreetViewLocationRecord> cluster) {
        LatLngBounds.Builder cluster_bounds_builder = LatLngBounds.builder();
        for (StreetViewLocationRecord r : cluster.getItems())
            cluster_bounds_builder.include(r.getPosition());
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(cluster_bounds_builder.build(), 300));
        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<StreetViewLocationRecord> cluster) {
    }

    @Override
    public boolean onClusterItemClick(StreetViewLocationRecord r) {
        if (activity instanceof MainActivity)
            ((MainActivity) activity).switchToExploreWithRecord(r);
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(StreetViewLocationRecord r) {
        // Does nothing, but you could go into the user's profile page, for example.
    }

    protected void startDemo() {
        mClusterManager = new ClusterManager<>(getActivity(), mMap);
        mClusterManager.setRenderer(new PersonRenderer());
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            LatLngBounds.Builder builder = LatLngBounds.builder();
                List<StreetViewLocationRecord> allLocs = StreetViewLocationRecord.listAll(StreetViewLocationRecord.class);
                for (final StreetViewLocationRecord r : allLocs) {
                    builder.include(r.getPosition());
                }
                // Move camera
                if (allLocs.size() > 0)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
                else
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 3));
                // Remove listener to prevent position reset on camera move.
                mMap.setOnCameraChangeListener(mClusterManager);
            }
        });
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mClusterManager.cluster();
        addItems();
    }

    private void addItems() {
        for (final StreetViewLocationRecord r : StreetViewLocationRecord.listAll(StreetViewLocationRecord.class)) {
            //download & cache the image, then add it
            MainActivity.il.loadImage(r.getURL(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    r.setImage(loadedImage);
                    mClusterManager.addItem(r);
                    mClusterManager.cluster();
                }
            });
        }
    }
}