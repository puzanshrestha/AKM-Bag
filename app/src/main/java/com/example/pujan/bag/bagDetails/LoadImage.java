package com.example.pujan.bag.bagDetails;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class LoadImage extends AsyncTask<Void, Void, Bitmap> {

    private Context context;

    private ImageView imageView;
    private int imageResource;

    public LoadImage(Context context, ImageView imageView, int imageResource) {
        this.context = context;
        this.imageView = imageView;
        this.imageResource = imageResource;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        return BitmapFactory.decodeResource(context.getResources(), imageResource);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        imageView.setImageBitmap(bitmap);
    }
}