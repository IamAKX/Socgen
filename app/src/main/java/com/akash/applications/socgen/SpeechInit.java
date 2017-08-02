package com.akash.applications.socgen;

import android.app.Application;

import net.gotev.speech.Speech;

/**
 * Created by akash on 2/8/17.
 */

public class SpeechInit extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Speech.init(this,getPackageName());
    }
}
