package com.example.tudor.popularmovies.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by tudor on 09.04.2018.
 */

public class ImageUtils {

    public static void downloadMovieImage(Context context, String imageUrl, final String imageName) {
        Picasso.with(context)
                .load(imageUrl)
                .into(new Target() {
                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                              try {
                                  String root = Environment.getExternalStorageDirectory().toString();
                                  File myDir = new File(root + "/images");

                                  if (!myDir.exists()) {
                                      myDir.mkdirs();
                                  }


                                  myDir = new File(myDir, imageName);

                                  Log.v("IMAGE UTILS", myDir.toString());

                                  FileOutputStream out = new FileOutputStream(myDir);
                                  bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                  out.flush();
                                  out.close();
                              } catch(Exception e){
                                  // some action
                              }
                          }

                          @Override
                          public void onBitmapFailed(Drawable errorDrawable) {
                          }

                          @Override
                          public void onPrepareLoad(Drawable placeHolderDrawable) {
                          }
                      }
                );
    }

    public static void loadMovieImageFromStorage(Context context, String imagePath, ImageView imageView) {

        //Picasso.with(context).load(new File(path)).into(imageView);

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/images");

        if (!myDir.exists()) {
            myDir.mkdirs();
        }


        myDir = new File(myDir, imagePath);

        //Picasso.with(context).load(myDir).into(imageView);

        Picasso.with(context).load("file://"+myDir).config(Bitmap.Config.RGB_565).into(imageView);

    }
}
