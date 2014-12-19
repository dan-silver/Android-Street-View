package com.example.android.navigationdrawerexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.net.URL;


/**
 * Created by dan-silver on 12/15/14.
 */

class AsyncImageFetcher extends AsyncTask<String, Void, Void> {
    private StreetViewLocationRecord loc;
    private ImageView iv;

    public void setLoadingIcon(ProgressBar loadingIcon) {
        this.loadingIcon = loadingIcon;
    }

    private ProgressBar loadingIcon;

    Exception exception;

    protected Void doInBackground(String... url) {
        try {

            //use urls[0] for first string passed in

            URL url_ = new URL("https://maps.googleapis.com/maps/api/streetview?size=800x400&location="+loc.getLatatidue()+","+loc.getLongitude()+"&fov=90&heading=" +
                    loc.getBearing() + "&pitch=" + loc.getTilt());
            Log.v("silver", "url=" + url_.toString());
            Bitmap bmp = BitmapFactory.decodeStream(url_.openConnection().getInputStream());
            iv.setImageBitmap(bmp);
//            iv.setImageResource(R.drawable.powered_by_google_light);
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
        return null;
    }

    protected void onPostExecute(Void v) {
        loadingIcon.setVisibility(View.INVISIBLE);
    }

    public void setImageView(ImageView iv) {
        this.iv = iv;
    }

    public void setLoc(StreetViewLocationRecord loc) {
        this.loc = loc;
    }

}
