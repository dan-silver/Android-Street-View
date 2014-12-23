package com.example.android.navigationdrawerexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.orm.query.Select;

/**
 * Created by dan-silver on 12/15/14.
 */

public class FavImageAdapter extends BaseAdapter {
    private Context mContext;

    public FavImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return (int) Select.from(StreetViewLocationRecord.class).count();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public void removeItem(int position) {
        Select.from(StreetViewLocationRecord.class).orderBy("id").limit(position + ", 1").list().get(0).delete();
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(mContext).inflate(R.layout.image_view_loading, parent, false);
        } else {
            row = convertView;
        }

        StreetViewLocationRecord loc = Select.from(StreetViewLocationRecord.class).orderBy("id").limit(position + ", 1").list().get(0);

        new AsyncImageFetcher(mContext)
              .setImageView((ImageView) row.findViewById(R.id.loaded_image))
              .setLoadingIcon((ProgressBar) row.findViewById(R.id.progress))
              .execute("https://maps.googleapis.com/maps/api/streetview?size=800x400&location="+loc.getLatitude()+","+loc.getLongitude()+"&fov=90&heading=" +
                        loc.getBearing() + "&pitch=" + loc.getTilt());

        return row;
    }
}