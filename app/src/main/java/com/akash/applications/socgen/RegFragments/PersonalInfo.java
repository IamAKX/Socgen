package com.akash.applications.socgen.RegFragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akash.applications.socgen.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalInfo extends Fragment  implements Step{

    MaterialEditText idCard,pesel,pob,dob,doe,pName,pAddress,pPostalCode;

    public PersonalInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        idCard = getView().findViewById(R.id.idcardno);
        pesel = getView().findViewById(R.id.PESEL);
        pob = getView().findViewById(R.id.placeOfBirth);
        dob = getView().findViewById(R.id.dateofbirth);
        doe = getView().findViewById(R.id.dateofexpiry);
        pName = getView().findViewById(R.id.parentname);
        pAddress = getView().findViewById(R.id.parentaddress);
        pPostalCode = getView().findViewById(R.id.parentspostalcode);

        //setData();

        idCard.setText("ABC 123456");
        pesel.setText("81010200131");
        dob.setText("02.01.1981");
        doe.setText("02.03.2005");
        pob.setText("WARSZAWA");
        pName.setText("KOWALASKI");
        pAddress.setText("WARSZAWA");
        pPostalCode.setText("321654");
    }

    private void setData() {
        String data = CaptureID.personalInfo;
        try {
            JSONObject j = new JSONObject(data).getJSONObject("result");
            if(j.has("date_of_birth")){
                dob.setText(j.getString("date_of_birth"));
            }
            if(j.has("identityCardNumber")){
                idCard.setText(j.getString("identityCardNumber"));
            }
            if(j.has("pesel")){
                pesel.setText(j.getString("pesel"));
            }
            if(j.has("place_of_birth")){
                pob.setText(j.getString("place_of_birth"));
            }
            if(j.has("expiration_date")){
                doe.setText(j.getString("expiration_date"));
            }
            if(j.has("parents_name")){
                pName.setText(j.getString("parents_name"));
            }
            pAddress.setText("parents address");
            pPostalCode.setText("parents postal code");

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
