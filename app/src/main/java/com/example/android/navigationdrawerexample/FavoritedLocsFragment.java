package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by dan-silver on 12/14/14.
 */
public class FavoritedLocsFragment extends Fragment {
    public FavoritedLocsFragment() {

    }

@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Activity activity = getActivity();

        View v = inflater.inflate(R.layout.favorited_locations, container, false);
        final GridView gridview = (GridView) v.findViewById(R.id.favorited_locations_grid);
        gridview.setAdapter(new FavImageAdapter(getActivity().getApplicationContext()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (activity instanceof MainActivity)
                    ((MainActivity) activity).switchToExploreWithSaved(position);
            }
        });
        return v;
    }
}