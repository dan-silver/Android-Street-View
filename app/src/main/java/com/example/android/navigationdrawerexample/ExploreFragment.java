package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

/**
 * Created by dan-silver on 12/14/14.
 */
public class ExploreFragment extends Fragment implements OnStreetViewPanoramaReadyCallback {
    private static StreetViewPanorama streetViewPanorama;
    private static View view;
    Activity activity;

    public ExploreFragment() {

    }

    public void setLocation(StreetViewLocationRecord loc) {
        Log.v(MainActivity.LOG, "setLocation()");

        setLocationWithoutTilt(loc.getPosition());
        streetViewPanorama.animateTo(
                new StreetViewPanoramaCamera.Builder()
                        .tilt((float) loc.getTilt())
                        .bearing((float) loc.getBearing())
                        .build(),
                1);
    }

    public void setLocationWithoutTilt(LatLng loc) {
        Log.v(MainActivity.LOG, "setLocationWithoutTilt()");
        streetViewPanorama.setPosition(loc);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favoriteLocation:
                saveCurrentLocation();
                return true;
            case R.id.action_random:
                setLocation(PresetLocations.getRandomLocation());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setHomeButtonEnabled(true);

        activity = getActivity();
        Log.v(MainActivity.LOG, "onCreateView()");
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.explore_frag, container, false);

            StreetViewPanoramaFragment streetViewPanoramaFragment =
                    (StreetViewPanoramaFragment) getFragmentManager()
                            .findFragmentById(R.id.streetviewpanorama);

            streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
        } catch (InflateException e) {
            Log.v(MainActivity.LOG, "onCreateView() caught error => map already there");
            /* map is already there, just return view as it is */
            setupMapLocation();
        }
        return view;
    }

    public void saveCurrentLocation() {
        Log.v(MainActivity.LOG, "saveCurrentLocation()");
        LatLng location = streetViewPanorama.getLocation().position;
        StreetViewPanoramaCamera camera = streetViewPanorama.getPanoramaCamera();

        StreetViewLocationRecord r = new StreetViewLocationRecord();
                                 r.setLatitude(location.latitude)
                                  .setLongitude(location.longitude)
                                  .setTilt(camera.tilt)
                                  .setBearing(camera.bearing)
                                  .save();
        Toast.makeText(getActivity().getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).newLocationIds.add(r.getId());
            Log.v(MainActivity.LOG, "added id: " + r.getId() + "to the arraylist!");
        }
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
        Log.v(MainActivity.LOG, "onStreetViewPanoramaReady()");
        streetViewPanorama = panorama;
        setupMapLocation();
    }

    private void setupMapLocation() {
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey("POSITION")) {
                Log.v(MainActivity.LOG, "args contained key position");
                int position = args.getInt("POSITION");
                StreetViewLocationRecord savedLocation = StreetViewLocationRecord.listAll(StreetViewLocationRecord.class).get(position);
                setLocation(savedLocation);

            } else if (args.containsKey("RECORD_ID")) {
                Log.v(MainActivity.LOG, "args contained RECORD_ID");
                long id = args.getLong("RECORD_ID");
                StreetViewLocationRecord savedLocation = StreetViewLocationRecord.findById(StreetViewLocationRecord.class, id);
                setLocation(savedLocation);
            } else if (args.containsKey("MANUAL_LAT")) {
                Log.v(MainActivity.LOG, "args contained MANUAL_LAT");
                double lat = args.getDouble("MANUAL_LAT");
                double lng = args.getDouble("MANUAL_LONG");
                setLocationWithoutTilt(new LatLng(lat, lng));
            }
        }
    }

    @Override
    public void onStop() {
        Log.v(MainActivity.LOG, "onStop()");
        super.onStop();
    }
}