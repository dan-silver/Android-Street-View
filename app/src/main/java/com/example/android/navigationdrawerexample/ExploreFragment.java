package com.example.android.navigationdrawerexample;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

/**
 * Created by dan-silver on 12/14/14.
 */
public class ExploreFragment extends Fragment implements OnStreetViewPanoramaReadyCallback {
    private static StreetViewPanorama streetViewPanorama;
    private static View view;

    public ExploreFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        return view;
    }

    public void getLocation() {
        LatLng location = streetViewPanorama.getLocation().position;
        String locationStr = "Location: " + location.longitude + "  " + location.latitude;
        Toast.makeText(getActivity().getApplicationContext(), locationStr, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
        streetViewPanorama = panorama;
        panorama.setPosition(new LatLng(-33.87365, 151.20689));

    }

    @Override
    public void onStop() {
        ((MainActivity) getActivity()).displayMenuItem(R.id.action_favoriteLocation, false);
        super.onStop();
    }

}