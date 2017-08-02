package com.akash.applications.socgen.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.akash.applications.socgen.RegFragments.CaptureID;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdullahghani on 30/07/17.
 */

public class DetectId {
    Context context ;
    List<String> frontdata = new ArrayList<>();
    List<String> backdata = new ArrayList<>();

    public DetectId(Context context) {
        this.context = context;
        frontdata.add("Polska");
        frontdata.add("Rzeczpospolita");
        frontdata.add("Dowod");
        frontdata.add("osobísty");
        frontdata.add("osobisty");
        frontdata.add("Dowód");

        backdata.add("81010200131");
        backdata.add("PLACE OF BIRTH");
        backdata.add("AUTHORITY");
        backdata.add("<<<<");
        backdata.add("EXPI");


    }




    public String checkId(String idData){

        Bitmap textBitmap = BitmapFactory.decodeFile(idData);
//        CaptureID.backPreview.setImageBitmap(textBitmap);
        Log.i("unique naam",textBitmap.toString()+" ");
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();

        if (!textRecognizer.isOperational()) {
            return "False";
        }

        Frame frame = new Frame.Builder().setBitmap(textBitmap).build();
        SparseArray<TextBlock> text = textRecognizer.detect(frame);
        String s= "";

        Log.i("unique naam",s+" null hai"+text.size());
        for (int i = 0; i < text.size(); ++i) {
            TextBlock item = text.valueAt(i);
            if (item != null && item.getValue() != null) {
                s+=item.getValue();
                Log.i("unique naam",item.getValue()+" ");

            }
        }


       return checkType(s);
    }

    public String checkType(String ocrText){
        for(String s:frontdata)
        {
            if(ocrText.contains(s))
                return "front";
        }
        for(String s:backdata)
        {
            if(ocrText.contains(s))
                return "back";
        }
        return "false";

    }

}
