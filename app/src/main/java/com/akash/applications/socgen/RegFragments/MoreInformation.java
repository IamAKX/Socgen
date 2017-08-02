package com.akash.applications.socgen.RegFragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akash.applications.socgen.MainActivity;
import com.akash.applications.socgen.Modal.MoreInfoQuestionModel;
import com.akash.applications.socgen.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import net.gotev.speech.Speech;
import net.gotev.speech.TextToSpeechCallback;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreInformation extends Fragment implements Step, RecognitionListener {



    public MoreInformation() {
        // Required empty public constructor
    }
    static int CURRENT_LAYER = 1;
    SpeechRecognizer speech;
    Intent recogizerIntent;

    TextToSpeech textToSpeech;
    ListView listView;
    TextView question[];
    RadioButton[][] radioArray;
    ProgressBar speechPB;
    ArrayList<MoreInfoQuestionModel> modelArrayList = new ArrayList<>();
    RelativeLayout[] layers;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_information, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        speechPB = getView().findViewById(R.id.progressSpeech);
        speechPB.setVisibility(View.INVISIBLE);
        listView = getView().findViewById(R.id.moreInfoList);
        question = new TextView[]{
                getView().findViewById(R.id.moreInfoQuestionL1),
                getView().findViewById(R.id.moreInfoQuestionL2),
                getView().findViewById(R.id.moreInfoQuestionL3)
        };
        radioArray = new RadioButton[][]{{
                getView().findViewById(R.id.radio1L1),
                getView().findViewById(R.id.radio2L1),
                getView().findViewById(R.id.radio3L1),
                getView().findViewById(R.id.radio4L1)},

                {getView().findViewById(R.id.radio1L2),
                getView().findViewById(R.id.radio2L2),
                getView().findViewById(R.id.radio3L2),
                getView().findViewById(R.id.radio4L2)},

                {getView().findViewById(R.id.radio1L3),
                getView().findViewById(R.id.radio2L3),
                getView().findViewById(R.id.radio3L3),
                getView().findViewById(R.id.radio4L3)}
            };

        layers = new RelativeLayout[]
                {
                    getView().findViewById(R.id.layer3),
                    getView().findViewById(R.id.layer2),
                    getView().findViewById(R.id.layer1)
                };


        speech = SpeechRecognizer.createSpeechRecognizer(getContext());

        recogizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recogizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recogizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        recogizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getContext().getPackageName());
        speech.setRecognitionListener(this);

        setUpQuestion();

        for(int i = 0;i<3;i++)
        {
            populateData(i);
        }


    }

    private void populateData(int i) {
        MoreInfoQuestionModel model = modelArrayList.get(i);
        question[i].setText(model.getQuestion());
        for (int j = 0; j<4;j++)
        {
            radioArray[i][j].setText(model.getOptions()[j]);
        }
    }

    private void setUpQuestion() {
        MoreInfoQuestionModel model = new MoreInfoQuestionModel("Source of income",new String[]{"Employment","Investment","Pension","Other"});
        modelArrayList.add(model);

        model = new MoreInfoQuestionModel("Account Type",new String[]{"Current","Savings","Loan",""});
        modelArrayList.add(model);

        model = new MoreInfoQuestionModel("Card Type",new String[]{"Credit card","Debit card","",""});
        modelArrayList.add(model);
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

        MoreInfoQuestionModel model = modelArrayList.get(0);
//
//        Speech.getInstance().say("layer ", new TextToSpeechCallback() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onCompleted() {
//                layers[CURRENT_LAYER-1].setVisibility(View.GONE);
//                CURRENT_LAYER++;
//                Log.e("checking"," "+CURRENT_LAYER);
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
//
//        Speech.getInstance().say("layer 3", new TextToSpeechCallback() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onCompleted() {
//                layers[CURRENT_LAYER-1].setVisibility(View.GONE);
//                CURRENT_LAYER++;
//                Log.e("checking"," "+CURRENT_LAYER);
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });

        Speech.getInstance().say("What is your "+model.getQuestion()+".  .  . "+model.getOptions()[0]+","+model.getOptions()[1]+","+model.getOptions()[2]+", or "+model.getOptions()[3], new TextToSpeechCallback() {
            @Override
            public void onStart() {
                Log.e("checking"," start");
            }

            @Override
            public void onCompleted() {
                layers[CURRENT_LAYER-1].setVisibility(View.GONE);
                CURRENT_LAYER++;
                Log.e("checking"," end");

            }

            @Override
            public void onError() {
                Log.e("checking"," end");
            }
        });


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2500);
                            Toast.makeText(getContext(),"Start Speaking",Toast.LENGTH_SHORT).show();
                            speechPB.setVisibility(View.VISIBLE);
                            speechPB.setIndeterminate(true);
                            speech.startListening(recogizerIntent);
                            Thread.sleep(1000);
                            speech.stopListening();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        },1000);


    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }


    @Override
    public void onPause() {
        super.onPause();
        if(speech!=null)
            speech.destroy();
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {
        speechPB.setIndeterminate(false);
        speechPB.setVisibility(View.VISIBLE);
        speechPB.setMax(10);
    }

    @Override
    public void onRmsChanged(float v) {
        speechPB.setProgress((int)v);
    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        speechPB.setIndeterminate(true);
    }

    @Override
    public void onError(int i) {
        String errorMessage = getErrorText(i);
         Toast.makeText(getContext(),errorMessage,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String res = matches.get(0);
//        for (String s : matches)
//            res += s;
        Toast.makeText(getContext(), matches.get(0), Toast.LENGTH_LONG).show();
        if(res.equalsIgnoreCase(radioArray[CURRENT_LAYER-1][0].getText().toString()))
            radioArray[CURRENT_LAYER-1][0].setChecked(true);
        else
        if(res.equalsIgnoreCase(radioArray[CURRENT_LAYER-1][1].getText().toString()))
            radioArray[CURRENT_LAYER-1][1].setChecked(true);
        else
        if(res.equalsIgnoreCase(radioArray[CURRENT_LAYER-1][2].getText().toString()))
            radioArray[CURRENT_LAYER-1][2].setChecked(true);
        else
        if(res.equalsIgnoreCase(radioArray[CURRENT_LAYER-1][3].getText().toString()))
            radioArray[CURRENT_LAYER-1][3].setChecked(true);
        else {
            Speech.getInstance().say("Sorry, it is not an option. Speak again!");
            try {
                Thread.sleep(4000);
                speech.startListening(recogizerIntent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        speechPB.setIndeterminate(false);

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


}
