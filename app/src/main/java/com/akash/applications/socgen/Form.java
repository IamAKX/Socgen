package com.akash.applications.socgen;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.akash.applications.socgen.Adapter.FormStepperAdapter;
import com.akash.applications.socgen.RegFragments.CaptureID;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.Locale;

public class Form extends AppCompatActivity implements StepperLayout.StepperListener {
    public static TextToSpeech textToSpeech;
    public static Activity activity;
    public static StepperLayout stepperLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        activity = this;
        textToSpeech = new TextToSpeech(getBaseContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR)
                    textToSpeech.setLanguage(Locale.US);
                else
                    Log.e("checking","TTS ERROR CODE "+i);
            }
        });

        stepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        stepperLayout.setAdapter(new FormStepperAdapter(getSupportFragmentManager(),this));


    }

    @Override
    public void onCompleted(View completeButton) {

    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

        switch (newStepPosition)
        {
            case 0:
                getSupportActionBar().setTitle("Capture your ID");
                break;

            case 1:
                getSupportActionBar().setTitle("Personal Information");
                break;

            case 2:
                getSupportActionBar().setTitle("More Information");
                break;

            case 3:
                getSupportActionBar().setTitle("Video Varification");
                break;

        }

    }

    @Override
    public void onReturn() {

    }
}
