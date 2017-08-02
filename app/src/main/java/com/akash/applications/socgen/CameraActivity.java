package com.akash.applications.socgen;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.akash.applications.socgen.RegFragments.CaptureID;
import com.akash.applications.socgen.Utils.CameraManager;
import com.akash.applications.socgen.Utils.DetectId;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {

    public static Camera camera;
    SurfaceHolder surfaceHolder;
    boolean camCondition = false;
    SurfaceView surfaceView;
    ImageButton takePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        takePic = (ImageButton) findViewById(R.id.btnTackePic);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceView.setFocusableInTouchMode(true);
        surfaceView.setFocusable(true);
        surfaceView.requestFocus();
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture(null,null,null,pcb);
            }
        });


    }

    Camera.PictureCallback pcb = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            try {
                String imgPath = Environment.getExternalStorageDirectory()+"/Socgen/Images"+"/img_"+ System.currentTimeMillis()+".jpg";
                FileOutputStream f = new FileOutputStream(imgPath);
                f.write(bytes);
                f.close();
                Log.e("unique naam","path "+imgPath);
                String s = new DetectId(getBaseContext()).checkId(imgPath);
                Log.e("unique naam","s "+s);
                switch (s.toLowerCase()){
                    case "front":
                        CaptureID.frontPreview.setImageBitmap(BitmapFactory.decodeFile(imgPath));
                        CaptureID.isFronSet = true;
                        CaptureID.frontImgPath = imgPath;
                        CaptureID.frontLebel.setVisibility(View.VISIBLE);
//                        camera.stopPreview();
//                        camera.release();
//                        camera= null;
                        camCondition = false;
                        finish();
                        break;

                    case "back":
                        CaptureID.backPreview.setImageBitmap(BitmapFactory.decodeFile(imgPath));
                        CaptureID.isBackSet=true;
                        CaptureID.backImgPath = imgPath;
                        CaptureID.backLebel.setVisibility(View.VISIBLE);
//                        camera.stopPreview();
//                        camera.release();
//                        camera= null;
                        camCondition = false;
                        finish();
                        break;

                    default:
                        Log.i("unique naam","def "+s);
                        camera.startPreview();
                        Toast.makeText(getBaseContext(),"Failed to detect",Toast.LENGTH_SHORT).show();

                }

                if(CaptureID.isFronSet && CaptureID.isBackSet)
                {
                    if(camera!=null)
                    {
//                        camera.stopPreview();
//                        camera.release();
//                        camera= null;
//                        camCondition = false;
                    }
                    Form.stepperLayout.setNextButtonEnabled(true);

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                //camera.startPreview();
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(camera!=null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open(CameraManager.getBackCamera());
        camera.setDisplayOrientation(90);
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        camera.setParameters(parameters);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if(camCondition)
        {
            camera.stopPreview();
            camCondition = false;
        }
        if(camera != null)
        {
            try{
                Camera.Parameters parameters = camera.getParameters();
                List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
                Camera.Size size = sizes.get(0);
                for(int x=0;x<sizes.size();x++)
                {
                    if(sizes.get(x).width > size.width)
                        size = sizes.get(x);
                }
                parameters.setPictureSize(size.width, size.height);
                sizes = parameters.getSupportedPreviewSizes();
                size = sizes.get(0);
                for(int x=0;x<sizes.size();x++)
                {
                    if(sizes.get(x).width > size.width)
                        size = sizes.get(x);
                }
                parameters.setPreviewSize(size.width, size.height);

                parameters.setColorEffect(Camera.Parameters.EFFECT_NONE);

//                String currentversion = android.os.Build.VERSION.SDK;
//                int currentInt = android.os.Build.VERSION.SDK_INT;
//
//                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                    if (currentInt != 7) {
//                        camera.setDisplayOrientation(90);
//                    } else {
//                        Log.d("System out", "Portrait " + currentInt);
//
//                        parameters.setRotation(90);
//
//
//                        camera.setParameters(parameters);
//                    }
//                }
//                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    // camera.setDisplayOrientation(0);
//                    if (currentInt != 7) {
//                        camera.setDisplayOrientation(0);
//                    } else {
//                        Log.d("System out", "Landscape " + currentInt);
//                        parameters.set("orientation", "landscape");
//                        parameters.set("rotation", 90);
//                        camera.setParameters(parameters);
//                    }
//                }

                camera.setParameters(parameters);
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                camCondition = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if(camera!=null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
            camCondition = false;
    }


}
