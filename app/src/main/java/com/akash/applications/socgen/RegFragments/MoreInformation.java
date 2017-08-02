package com.akash.applications.socgen.RegFragments;


import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.akash.applications.socgen.Modal.MoreInfoQuestionModel;
import com.akash.applications.socgen.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreInformation extends Fragment implements Step{



    public MoreInformation() {
        // Required empty public constructor
    }
    boolean isSpeaking = false;
    TextToSpeech textToSpeech;
    ListView listView;
    TextView question;
    RadioButton[] radioArray;
    ProgressBar progressBar;
    ArrayList<MoreInfoQuestionModel> modelArrayList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_information, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = getView().findViewById(R.id.progressSpeech);
        listView = getView().findViewById(R.id.moreInfoList);
        question = getView().findViewById(R.id.moreInfoQuestion);
        radioArray = new RadioButton[]{
                getView().findViewById(R.id.radio1),
                getView().findViewById(R.id.radio2),
                getView().findViewById(R.id.radio3),
                getView().findViewById(R.id.radio4)
            };

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    textToSpeech.setPitch(1.1f);
                    textToSpeech.setLanguage(Locale.US);
                }
                else
                    Log.e("checking","TTS ERROR CODE "+i);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String s) {
                         isSpeaking = true;

                }

                @Override
                public void onDone(String s) {
                    isSpeaking = false;
                }

                @Override
                public void onError(String s) {
                    isSpeaking = false;
                }
            });
        }
        else
        {
            textToSpeech.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                @Override
                public void onUtteranceCompleted(String s) {

                }
            });
        }
        setUpQuestion();
        for(MoreInfoQuestionModel model:modelArrayList)
        {
            moreInfoModel = model;
            displayOnSpeechView = question;
            textToSpeech.speak(model.getQuestion(),TextToSpeech.QUEUE_FLUSH,null,null);
            speechText = model.getQuestion();
            while (isSpeaking);

        }
    }
    MoreInfoQuestionModel moreInfoModel = null;
    View displayOnSpeechView;
    String speechText = "";

    private void setUpQuestion() {
        MoreInfoQuestionModel model = new MoreInfoQuestionModel("Source of income","Employment","Investment","Pension","Other");
        modelArrayList.add(model);

        model = new MoreInfoQuestionModel("Account Type","Current","Savings","Loan");
        modelArrayList.add(model);

        model = new MoreInfoQuestionModel("Card Type","Credit card","Debit card");
        modelArrayList.add(model);
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
