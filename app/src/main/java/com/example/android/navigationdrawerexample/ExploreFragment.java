package com.example.android.navigationdrawerexample;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
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
    private Boolean useSavedLoc;
    private StreetViewLocationRecord savedLocation;

    public ExploreFragment() {

    }

    public void setLocation(StreetViewLocationRecord loc) {
        Log.v(MainActivity.LOG, "setLocation()");
        LatLng loc_ = new LatLng(loc.getLatitude(), loc.getLongitude());
        streetViewPanorama.setPosition(loc_);
        streetViewPanorama.animateTo(
                new StreetViewPanoramaCamera.Builder()
                        .tilt((float) loc.getTilt())
                        .bearing((float) loc.getBearing())
                        .build(),
                0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(MainActivity.LOG, "onCreateView()");
        useSavedLoc = false;
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
        }

        ((MainActivity) getActivity()).displayMenuItem(R.id.action_favoriteLocation, true);



        Bundle args = getArguments();
        if (args != null && args.containsKey("POSITION")) {
            Log.v(MainActivity.LOG, "args contained key position");
            useSavedLoc = true;
            int position = args.getInt("POSITION");
            savedLocation = StreetViewLocationRecord.listAll(StreetViewLocationRecord.class).get(position);
            Toast.makeText(getActivity().getApplicationContext(), "in Explore: " + position, Toast.LENGTH_SHORT).show();
            setLocation(savedLocation);

        }
        return view;
    }

    public void getLocation() {
        Log.v(MainActivity.LOG, "getLocation()");
        LatLng location = streetViewPanorama.getLocation().position;
        StreetViewPanoramaCamera camera = streetViewPanorama.getPanoramaCamera();


        (new StreetViewLocationRecord()).setLatitude(location.latitude)
                                  .setLongitude(location.longitude)
                                  .setTilt(camera.tilt)
                                  .setBearing(camera.bearing)
                                  .save();
        }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
        Log.v(MainActivity.LOG, "onStreetViewPanoramaReady()");
        streetViewPanorama = panorama;
        if (!useSavedLoc) {
            panorama.setPosition(new LatLng(38.8976701, -77.0363995));
        }
    }

    @Override
    public void onStop() {
        Log.v(MainActivity.LOG, "onStop()");
        ((MainActivity) getActivity()).displayMenuItem(R.id.action_favoriteLocation, false);
        super.onStop();
    }

}