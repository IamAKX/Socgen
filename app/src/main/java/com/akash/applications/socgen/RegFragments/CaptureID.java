package com.akash.applications.socgen.RegFragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akash.applications.socgen.CameraActivity;
import com.akash.applications.socgen.Form;
import com.akash.applications.socgen.R;
import com.akash.applications.socgen.Utils.ImageEncoder;
import com.akash.applications.socgen.Utils.MyConstants;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CaptureID extends Fragment implements BlockingStep{
    //public static Button btnExtract;
    RelativeLayout btnRec;
    public static TextView frontLebel, backLebel;
    public static ImageView frontPreview, backPreview;
    public static String frontImgPath = "",backImgPath = "";
    public static boolean isFronSet = false, isBackSet = false;
    public static String frontPersist=null,backPersist=null;


    public CaptureID() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_capture_id, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //btnExtract = (Button)getView().findViewById(R.id.btnExtract);
        frontPreview =(ImageView) getView().findViewById(R.id.frontPreview);
        backPreview =(ImageView) getView().findViewById(R.id.backPreview);
        frontLebel = getView().findViewById(R.id.frontLabel);
        backLebel = getView().findViewById(R.id.backLabel);
        btnRec = (RelativeLayout) getView().findViewById(R.id.btnRec);

        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //camera.takePicture(null,null,null,pcb);
                startActivity(new Intent(getContext(), CameraActivity.class));
            }
        });


    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

       // Form.stepperLayout.setNextButtonEnabled(false);
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        callback.getStepperLayout().showProgress("Extracting your information.. Please wait....");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                persistFirst();

                callback.getStepperLayout().hideProgress();
                callback.goToNextStep();
            }
        },5000L);

    }


    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

    }


    private void persistFirst() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://preproduction.persist.signzy.tech/api/base64",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        Log.i("Checking", response + " ");
                        //JsonObject jsonObject = new JsonObject()
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonObject = jsonObject.getJSONObject("file");
                            frontPersist = jsonObject.getString("directURL");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        persistSecond();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        NetworkResponse networkResponse = error.networkResponse;
                        Log.i("Checking", new String(error.networkResponse.data));
                        if(networkResponse != null && networkResponse.data != null)
                        {
                            switch (networkResponse.statusCode)
                            {

                                default:
                                    break;
                            }
                        }
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                String strr = ImageEncoder.encode(frontImgPath);
                Log.i("mybase64",strr);
                params.put("base64String", strr);
                params.put("mimetype","image/jpeg");
                params.put("ttl","10 mins");
                return params;
            }
        };

        //Adding the string request to the queue
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();

        requestQueue.add(stringRequest);


    }


    private void persistSecond() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://preproduction.persist.signzy.tech/api/base64",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        Log.i("Checking", response + " ");
                        //JsonObject jsonObject = new JsonObject()
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonObject = jsonObject.getJSONObject("file");
                            backPersist = jsonObject.getString("directURL");
//                            Log.i("myobject",s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        finalUpload();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        NetworkResponse networkResponse = error.networkResponse;
                        Log.i("Checking", new String(error.networkResponse.data));
                        if(networkResponse != null && networkResponse.data != null)
                        {
                            switch (networkResponse.statusCode)
                            {

                                default:
                                    break;
                            }
                        }
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                String strr = ImageEncoder.encode(backImgPath);
                Log.i("mybase64",strr);
                params.put("base64String", strr);
                params.put("mimetype","image/jpeg");
                params.put("ttl","10 mins");
                return params;
            }
        };

        //Adding the string request to the queue
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();

        requestQueue.add(stringRequest);
        requestQueue.add(stringRequest);
    }

    public static String personalInfo = "";

    private void finalUpload() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://development.signzy.xyz/identities/dowod-osobisty/extractions",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        Log.i("Checking", response + " ");
                        //JsonObject jsonObject = new JsonObject()
                        personalInfo = response;
//                        Toast.makeText(getContext(),"success",Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(),personalInfo,Toast.LENGTH_SHORT).show();
                            Form.stepperLayout.setNextButtonEnabled(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        NetworkResponse networkResponse = error.networkResponse;
                        Log.i("Checking", new String(error.networkResponse.data));
                        if(networkResponse != null && networkResponse.data != null)
                        {
                            switch (networkResponse.statusCode)
                            {

                                default:
                                    break;
                            }
                        }
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                String Ss = "[\""+frontPersist+"\",\""+backPersist+"\"]";
                Log.i("myObject",Ss);
                params.put("files", Ss);
                params.put("type","new");
                return params;
            }
        };

        //Adding the string request to the queue
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();

        requestQueue.add(stringRequest);
        requestQueue.add(stringRequest);

    }

}
