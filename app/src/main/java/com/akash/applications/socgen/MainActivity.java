package com.akash.applications.socgen;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    public static TextToSpeech textToSpeech;
    MaterialEditText name, email,phone;
    ImageView logo;
    Context context;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        context = this;

        name = (MaterialEditText) findViewById(R.id.name);
        email = (MaterialEditText) findViewById(R.id.emailID);
        phone = (MaterialEditText) findViewById(R.id.mobileNo);
        logo = (ImageView) findViewById(R.id.logo);
        btn = (Button) findViewById(R.id.btnOpenAccount);


//        Glide.with(context)
//                .load(R.drawable.logo)
//                .bitmapTransform(new CropCircleTransformation(context))
//                .placeholder(R.drawable.logo)
//                .error(R.drawable.logo)
//                .into(logo);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO },1);
                else
                    launchForm();
            }
        });


    }


    private void launchForm() {
        startActivity(new Intent(context,Form.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1)
        {
            if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
                launchForm();
            } else {
                Toast.makeText(getBaseContext(), "Access to Storage or Camera is denied. You cannot take image", Toast.LENGTH_LONG).show();
            }

        }
    }

}
