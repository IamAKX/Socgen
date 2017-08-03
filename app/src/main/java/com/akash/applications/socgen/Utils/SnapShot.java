package com.akash.applications.socgen.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by akash on 3/8/17.
 */

public class SnapShot {

    public static void saveScreenShot(final Activity activity, final String videoPath, final int time)
    {

        new Thread(){
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(videoPath);
                        Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(time); //unit in microsecond
                        saveToInternalStorage(bmFrame,time);
                        mediaMetadataRetriever.release();
                    }
                });
            }
        }.start();


    }

    public static void saveScreenShot(final Activity activity, final String videoPath, final int[] time)
    {
        new Thread(){
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(videoPath);
                        for(int t : time) {
                            Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(t); //unit in microsecond
                            saveToInternalStorage(bmFrame, t);
                        }
                        mediaMetadataRetriever.release();
                    }
                });
            }
        }.start();

    }

    private static void saveToInternalStorage(Bitmap bmFrame, int time) {
        File img = new File(MyConstants.CURRENT_ACCOUNT_FOLDER+"/snap"+time+".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bmFrame.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
