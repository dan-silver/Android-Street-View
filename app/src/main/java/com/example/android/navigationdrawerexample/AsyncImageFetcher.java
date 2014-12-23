package com.example.android.navigationdrawerexample;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.io.FilenameUtils;


/**
 * Created by dan-silver on 12/15/14.
 */

//handles caching (2 level) and loading icon
class AsyncImageFetcher extends AsyncTask<String, Void, Bitmap> {
    private ProgressBar loadingIcon;
    private ImageView iv;
    private Exception exception;
    private Context mContext;
    private String URL;
    private static HashMap<String, Bitmap> cache;

    public void clearCache() {
        cache.clear();
        ContextWrapper cw = new ContextWrapper(mContext);
        File dir = cw.getDir("streetViewImages", Context.MODE_PRIVATE);
        if (dir.isDirectory())
            for (String child: dir.list())
                new File(dir, child).delete();
    }

    public AsyncImageFetcher(Context c) {
        mContext = c;
        if (cache == null) {
            cache = new HashMap<>();
        }
    }

    protected Bitmap doInBackground(String... s) {
        URL = s[0];
        Log.v(MainActivity.LOG, URL);

        //main memory cache
        if (cache.containsKey(URL)) {
            return cache.get(URL);
        } else {
            //try load from hard disk
            Bitmap bmp = loadImageFromStorage(URL);
            if (bmp == null && isNetworkConnected()) {
                //fetch from web
                try {
                    bmp = BitmapFactory.decodeStream((new URL(URL)).openConnection().getInputStream());
                    cache.put(URL, bmp);
                    saveToInternalStorage(bmp, URL);
                    return bmp;
                } catch (Exception e) {
                    exception = e;
                }
            }
        }
        return null;
    }

    protected void onPostExecute(Bitmap bmp) {
        if (exception == null && bmp != null) {
            if (loadingIcon != null) loadingIcon.setVisibility(View.INVISIBLE);
            iv.setImageBitmap(bmp);
        }
        //otherwise try again?
    }

    public AsyncImageFetcher setLoadingIcon(ProgressBar loadingIcon) {
        this.loadingIcon = loadingIcon;
        return this;
    }

    public AsyncImageFetcher setImageView(ImageView iv) {
        this.iv = iv;
        return this;
    }

    private File getFile(String URL) {
        ContextWrapper cw = new ContextWrapper(mContext);
        File directory = cw.getDir("streetViewImages", Context.MODE_PRIVATE);
        String filename = FilenameUtils.getBaseName(URL);
        return new File(directory, filename);
    }

    private Bitmap loadImageFromStorage(String URL) {
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(new FileInputStream(getFile(URL)));
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return bmp;
    }

    private void saveToInternalStorage(Bitmap bitmapImage, String URL) {
        File myPath = getFile(URL);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        // There are no active networks.
        return ni != null;
    }

}