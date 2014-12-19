package com.example.android.navigationdrawerexample;

import android.app.Fragment;
import android.os.Bundle;
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
        LatLng loc_ = new LatLng(loc.getLatatidue(), loc.getLongitude());
        streetViewPanorama.setPosition(loc_);
        StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera.Builder()
                .tilt((float) loc.getTilt())
                .bearing((float) loc.getBearing())
                .build();
        streetViewPanorama.animateTo(camera, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        useSavedLoc = false;
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.explore_frag, container, false);
            //getActivity().setTitle(planet);

            StreetViewPanoramaFragment streetViewPanoramaFragment =
                    (StreetViewPanoramaFragment) getFragmentManager()
                            .findFragmentById(R.id.streetviewpanorama);

            streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        ((MainActivity) getActivity()).displayMenuItem(R.id.action_favoriteLocation, true);



        Bundle args = getArguments();
        if (args != null && args.containsKey("POSITION")) {
            useSavedLoc = true;
            int position = args.getInt("POSITION");
            savedLocation = StreetViewLocationRecord.listAll(StreetViewLocationRecord.class).get(position);
            Toast.makeText(getActivity().getApplicationContext(), "in Explore: " + position, Toast.LENGTH_SHORT).show();
            setLocation(savedLocation);

        }
        return view;
    }

    public void getLocation() {
        LatLng location = streetViewPanorama.getLocation().position;
        StreetViewPanoramaCamera camera = streetViewPanorama.getPanoramaCamera();


        (new StreetViewLocationRecord()).setLatatidue(location.latitude)
                                  .setLongitude(location.longitude)
                                  .setTilt(camera.tilt)
                                  .setBearing(camera.bearing)
                                  .save();
        }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
        streetViewPanorama = panorama;
        if (!useSavedLoc) {
            panorama.setPosition(new LatLng(48.856897, 2.298041));
        }
    }

    @Override
    public void onStop() {
        ((MainActivity) getActivity()).displayMenuItem(R.id.action_favoriteLocation, false);
        super.onStop();
    }

}