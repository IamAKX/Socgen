package com.akash.applications.socgen.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.akash.applications.socgen.RegFragments.CaptureID;
import com.akash.applications.socgen.RegFragments.MoreInformation;
import com.akash.applications.socgen.RegFragments.MoreInformationSL;
import com.akash.applications.socgen.RegFragments.PersonalInfo;
import com.akash.applications.socgen.RegFragments.VideoVerification;
import com.akash.applications.socgen.Utils.MyConstants;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;


/**
 * Created by akash on 30/7/17.
 */

public class FormStepperAdapter extends AbstractFragmentStepAdapter {

    public FormStepperAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        switch (position)
        {
            case 0:
                final CaptureID stepperFragment = new CaptureID();
                Bundle b = new Bundle();
                b.putInt(MyConstants.CURRENT_STEP_POSITION_KEY,position);
                stepperFragment.setArguments(b);
                return stepperFragment;

            case 1:
                final PersonalInfo personalInfo = new PersonalInfo();
                b = new Bundle();
                b.putInt(MyConstants.CURRENT_STEP_POSITION_KEY,position);
                personalInfo.setArguments(b);
                return personalInfo;

            case 2:
                final MoreInformationSL moreInformation = new MoreInformationSL();
                b = new Bundle();
                b.putInt(MyConstants.CURRENT_STEP_POSITION_KEY,position);
                moreInformation.setArguments(b);
                return moreInformation;

            case 3:
                final VideoVerification videoVerification = new VideoVerification();
                b = new Bundle();
                b.putInt(MyConstants.CURRENT_STEP_POSITION_KEY,position);
                videoVerification.setArguments(b);
                return videoVerification;

        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0L) int position) {
        String title  = "";
        StepViewModel model = null;
        switch (position)
        {
            case 0:
                title = "Capture your ID";
                model = new StepViewModel.Builder(context)
                        .setTitle(title)
                        .setNextButtonLabel("EXTRACT")
                        .create();
                break;
            case 1:
                title = "Personal Information";
                model = new StepViewModel.Builder(context)
                        .setTitle(title)
                        .create();
                break;
            case 3:
                title = "Video Varification";
                model = new StepViewModel.Builder(context)
                        .setTitle(title)
                        .create();
                break;

            case 2:
                title = "More Information";
                model = new StepViewModel.Builder(context)
                        .setTitle(title)
                        .create();
                break;
        }

        return model;
    }
}
