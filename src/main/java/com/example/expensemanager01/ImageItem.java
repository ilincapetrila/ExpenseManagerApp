package com.example.expensemanager01;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ImageItem {
    private Bitmap bitmap;
    private String url;
    private String userUID;
    private Date date;

    public ImageItem(Bitmap bitmap, Date date) {
        this.bitmap = bitmap;
        this.date = date;
    }

    public ImageItem(String downloadUrl, Date date, String  user) {
        this.url = downloadUrl;
        this.date = date;
        this.userUID = user;
    }

    public ImageItem(String downloadUrl, Date date) {
        this.url = downloadUrl;
        this.bitmap = loadBitmapFromUrl(downloadUrl);
        this.date = date;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Date getDate() {
        return date;
    }
    public String getUrl(){ return url; }
    public String getUserUID(){ return userUID; }

    @SuppressLint("StaticFieldLeak")
    private Bitmap loadBitmapFromUrl(String imageUrl) {
        try {
            return new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    try {
                        URL url = new URL(imageUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();

                        InputStream input = connection.getInputStream();

                        Bitmap bitmap = BitmapFactory.decodeStream(input);

                        input.close();
                        connection.disconnect();

                        return bitmap;
                    } catch (IOException e) {
                        Log.e("ImageItem", "Error loading bitmap from URL", e);
                        return null;
                    }
                }
            }.execute().get(); // .get() to retrieve the result on the main thread
        } catch (Exception e) {
            Log.e("ImageItem", "Error executing AsyncTask", e);
            return null;
        }
    }

}

