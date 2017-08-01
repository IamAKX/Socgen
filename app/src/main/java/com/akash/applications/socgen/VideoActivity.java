package com.akash.applications.socgen;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akash.applications.socgen.RegFragments.VideoVerification;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class VideoActivity extends Activity implements RecognitionListener, OnProgressBarListener {

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    SpeechRecognizer speech;
    Intent recogizerIntent;
    private ProgressBar speechPB;
    private Camera myCamera;
    private MyCameraSurfaceView myCameraSurfaceView;
    private MediaRecorder mediaRecorder;
    ImageButton myButton;
    NumberProgressBar numberProgressBar;
    SurfaceHolder surfaceHolder;
    boolean recording;
    Timer timer;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recording = false;

        setContentView(R.layout.activity_video);

        //Get Camera for preview
        myCamera = getCameraInstance();
        if (myCamera == null) {
            Toast.makeText(getBaseContext(),
                    "Fail to get Camera",
                    Toast.LENGTH_LONG).show();
        }

        myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
        FrameLayout myCameraPreview = (FrameLayout) findViewById(R.id.videoview);
        numberProgressBar = findViewById(R.id.number_progress_bar);
        myCameraPreview.addView(myCameraSurfaceView);
        speechPB = findViewById(R.id.speacRecogProgress);
        myButton = (ImageButton) findViewById(R.id.btnRecVideo);


        speechPB.setVisibility(View.INVISIBLE);
        numberProgressBar.setProgress(0);
        numberProgressBar.setMax(100);
        numberProgressBar.setOnProgressBarListener(this);
        Random r = new Random();
        int i = r.nextInt(1000000);


        TextView digitsTextView = (TextView) findViewById(R.id.randomtext);
        digitsTextView.setText(String.valueOf(i));
        speech = SpeechRecognizer.createSpeechRecognizer(getBaseContext());

        recogizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recogizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recogizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        recogizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
        speech.setRecognitionListener(this);
        myButton.setOnClickListener(myButtonOnClickListener);



    }

    Button.OnClickListener myButtonOnClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub


            if (recording) {
                // stop recording and release camera
                mediaRecorder.stop();  // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
                speech.stopListening();
                speech.destroy();
                VideoVerification.agreementLayout.setVisibility(View.VISIBLE);
                //Exit after saved
                finish();
            } else {


                //Release Camera before MediaRecorder start
                releaseCamera();
                //speech.stopListening();
                if (!prepareMediaRecorder()) {
                    Toast.makeText(getBaseContext(),
                            "Fail in prepareMediaRecorder()!\n - Ended -",
                            Toast.LENGTH_LONG).show();
                    finish();
                }

                mediaRecorder.start();
                recording = true;
                //Toast.makeText(getBaseContext(), "recording started", Toast.LENGTH_SHORT).show();
                speechPB.setVisibility(View.VISIBLE);
                speechPB.setIndeterminate(true);

//                startlistening();
                timer = new Timer();

                timer.schedule(new TimerTask() {
                    int sum = 0;
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                numberProgressBar.incrementProgressBy(6);
                                sum+=6;
                                if(sum>=90)
                               {
                                   // stop recording and release camera
                                   if(recording) {
                                        // stop the recording
                                       releaseMediaRecorder(); // release the MediaRecorder object
                                   }
                                   speech.stopListening();
                                   speech.destroy();
                                   //Exit after saved
                                   VideoVerification.agreementLayout.setVisibility(View.VISIBLE);
                                   finish();

                                }
                            }
                        });
                    }

                },1000,1000);
                speechPB.setIndeterminate(true);
                speech.startListening(recogizerIntent);
            }
        }
    };
//
//    private void startlistening() {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
//                "Read the number");
//        try {
//            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
//        } catch (ActivityNotFoundException a) {
//            Toast.makeText(getApplicationContext(), "Speach no supported",
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * Receiving speech input
//     * */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case REQ_CODE_SPEECH_INPUT: {
//                if (resultCode == RESULT_OK && null != data) {
//
//                    ArrayList<String> result = data
//                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    Toast.makeText(getBaseContext(),result.get(0),Toast.LENGTH_LONG).show();
//                }
//                break;
//            }
//
//        }
//    }
//

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speech.stopListening();

    }

    private Camera getCameraInstance() {
// TODO Auto-generated method stub
        Camera c = null;
        try {
            c = Camera.open();

            c.setDisplayOrientation(90);
            Camera.Parameters parameters = c.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            c.setParameters(parameters);
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private boolean prepareMediaRecorder() {
        myCamera = getCameraInstance();
        mediaRecorder = new MediaRecorder();

        myCamera.unlock();
        mediaRecorder.setCamera(myCamera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        mediaRecorder.setOutputFile("/sdcard/myvideo" + System.currentTimeMillis() + ".mp4");
        mediaRecorder.setMaxDuration(15000); // Set max duration 15 sec.
        mediaRecorder.setMaxFileSize(500000000); // Set max file size 500MB

        mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null)
            speech.destroy();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            myCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera() {
        if (myCamera != null) {
            myCamera.release();        // release the camera for other applications
            myCamera = null;
        }
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {
        speechPB.setIndeterminate(false);
        speechPB.setMax(10);

    }

    @Override
    public void onRmsChanged(float v) {
        speechPB.setProgress((int) v);
    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        speechPB.setIndeterminate(true);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
       // Toast.makeText(getBaseContext(),errorMessage,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String res = "";
        for (String s : matches)
            res += s;
        //Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    @Override
    public void onProgressChange(int current, int max) {
        if(current>=90)
            numberProgressBar.setProgress(max);
    }

    public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

        private SurfaceHolder mHolder;
        private Camera mCamera;

        public MyCameraSurfaceView(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int weight,
                                   int height) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null) {
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                // ignore: tried to stop a non-existent preview
            }

            // make any resize, rotate or reformatting changes here

            // start preview with new settings
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
                Camera.Size size = sizes.get(0);
                for (int x = 0; x < sizes.size(); x++) {
                    if (sizes.get(x).width > size.width)
                        size = sizes.get(x);
                }
                parameters.setPictureSize(size.width, size.height);
                sizes = parameters.getSupportedPreviewSizes();
                size = sizes.get(0);
                for (int x = 0; x < sizes.size(); x++) {
                    if (sizes.get(x).width > size.width)
                        size = sizes.get(x);
                }
                parameters.setPreviewSize(size.width, size.height);

                parameters.setColorEffect(Camera.Parameters.EFFECT_NONE);
                mCamera.setParameters(parameters);
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e) {
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub

        }
    }
}