package com.akash.applications.socgen.RegFragments;


import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.akash.applications.socgen.FinalPage;
import com.akash.applications.socgen.Form;
import com.akash.applications.socgen.R;
import com.akash.applications.socgen.VideoActivity;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoVerification extends Fragment implements Step{

    public static RelativeLayout agreementLayout;
    CheckBox agreeChkBox;
    Button agreeBtn;
    public VideoVerification() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_verification, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        agreementLayout = getView().findViewById(R.id.agreementRealtiveLayout);
        agreeChkBox = getView().findViewById(R.id.agreeVhkbx);
        agreeBtn = getView().findViewById(R.id.btnAgree);
        agreeBtn.setEnabled(false);
        agreeBtn.setBackgroundColor(getResources().getColor(R.color.deactiveButton));
        agreeChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b)
                {
                    agreeBtn.setEnabled(b);
                    agreeBtn.setBackgroundColor(getResources().getColor(R.color.deactiveButton));
                }
                else
                {
                    agreeBtn.setEnabled(b);
                    agreeBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });
        
        getView().findViewById(R.id.btnvideoRecord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), VideoActivity.class));
                Form.textToSpeech.speak("Read this number",TextToSpeech.QUEUE_FLUSH,null,null);
            }
        });
        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCongoDialog();
            }
        });
    }

    private void showCongoDialog() {
        final AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
        View dialogView = (LayoutInflater.from(getContext())).inflate(R.layout.congo_layout,null);
        Button b = dialogView.findViewById(R.id.btnDone);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), FinalPage.class));
                getActivity().finish();
            }
        });
        builder.setView(dialogView);

        builder.create().show();
    }


    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
        agreementLayout.setVisibility(View.GONE);
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
