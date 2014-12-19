package com.example.android.navigationdrawerexample;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

//handles caching and loading icon
class AsyncImageFetcher extends AsyncTask<String, Void, Bitmap> {
    private ProgressBar loadingIcon;
    private ImageView iv;
    Exception exception;
    private Context mContext;
    private static HashMap<String, Bitmap> cache;

    public AsyncImageFetcher(Context c) {
        mContext = c;
        exception = null;
        if (cache == null ) {
            cache = new HashMap<>();
        }
    }

    protected Bitmap doInBackground(String... s) {
        Bitmap bmp;
        //main memory cache
        if (cache.containsKey(s[0])) {
            Log.v("SILVER", "using mm cache");
            bmp = cache.get(s[0]);
        } else {
            //try load from hard disk
            bmp = loadImageFromStorage(s[0]);
            if (bmp == null) {
                //fetch from web
                try {
                    bmp = BitmapFactory.decodeStream((new URL(s[0])).openConnection().getInputStream());
                } catch (Exception e) {
                    exception = e;
                    Log.v("SILVER", e.toString());
                }
                cache.put(s[0], bmp);
                saveToInternalStorage(bmp, s[0]);
            } else {
                Log.v("SILVER", "using internal storage");
            }
        }
        return bmp;
    }

    protected void onPostExecute(Bitmap bmp) {
        if (exception == null && bmp != null) {
            loadingIcon.setVisibility(View.INVISIBLE);
            iv.setImageBitmap(bmp);
        }
    }

    public void setLoadingIcon(ProgressBar loadingIcon) {
        this.loadingIcon = loadingIcon;
    }

    public void setImageView(ImageView iv) {
        this.iv = iv;
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

    private void saveToInternalStorage(Bitmap bitmapImage, String URL){
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

}