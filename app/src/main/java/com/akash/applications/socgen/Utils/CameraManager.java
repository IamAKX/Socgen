package com.akash.applications.socgen.Utils;

import android.hardware.Camera;

import java.util.NoSuchElementException;

/**
 * Created by akash on 1/8/17.
 */

public class CameraManager
{
    public static int getFrontCamera() throws NoSuchElementException
    {

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int cameraIndex = 0; cameraIndex < Camera.getNumberOfCameras(); cameraIndex++) {
            Camera.getCameraInfo(cameraIndex, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    return cameraIndex;
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new NoSuchElementException("Can't find front camera.");

    }

    public static int getBackCamera() throws NoSuchElementException
    {

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int cameraIndex = 0; cameraIndex < Camera.getNumberOfCameras(); cameraIndex++) {
            Camera.getCameraInfo(cameraIndex, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                try {
                    return cameraIndex;
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new NoSuchElementException("Can't find front camera.");

    }
}
