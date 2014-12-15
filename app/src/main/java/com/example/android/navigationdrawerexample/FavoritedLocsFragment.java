package com.example.android.navigationdrawerexample;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dan-silver on 12/14/14.
 */
public class FavoritedLocsFragment extends Fragment {
    TextView tv;
    public FavoritedLocsFragment() {

    }

@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favorited_locations, container, false);
        tv = (TextView) v.findViewById(R.id.textView2);
        getFavorites();
        return v;
    }

    public void getFavorites() {
        List<streetViewLocation> locs = streetViewLocation.listAll(streetViewLocation.class);
        for (streetViewLocation loc : locs) {
            tv.append(loc.getLatatidue() + " ");
        }
    }
}