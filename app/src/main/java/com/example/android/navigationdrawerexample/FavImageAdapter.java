package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.net.URL;

/**
 * Created by dan-silver on 12/15/14.
 */

public class FavImageAdapter extends BaseAdapter {
    private Context mContext;

    public FavImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return StreetViewLocationRecord.listAll(StreetViewLocationRecord.class).size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(mContext).inflate(R.layout.image_view_loading, parent, false);
        } else {
            row = convertView;
        }

        StreetViewLocationRecord loc = StreetViewLocationRecord.listAll(StreetViewLocationRecord.class).get(position);

        new AsyncImageFetcher(mContext)
              .setImageView((ImageView) row.findViewById(R.id.loaded_image))
              .setLoadingIcon((ProgressBar) row.findViewById(R.id.progress))
              .execute("https://maps.googleapis.com/maps/api/streetview?size=800x400&location="+loc.getLatatidue()+","+loc.getLongitude()+"&fov=90&heading=" +
                        loc.getBearing() + "&pitch=" + loc.getTilt());

        return row;
    }
}