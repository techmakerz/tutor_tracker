package com.hypertrack.example_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class CaptureCameraImage extends Activity {
    private static final int CAMERA_REQUEST = 999;
    public static int cameraID = 0;
    public static boolean isBlack = true;
    public static ImageView image;
static  int count=0;
    Button upload;
    TextView tv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_camera_image);
        image = (ImageView) findViewById(R.id.imgView);
        upload= (Button) findViewById(R.id.uploadPic);
        tv= (TextView) findViewById(R.id.noOfPics);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                tv.setText("You have uploaded "+count+" pics");
                if(count==3){
                    startActivity(new Intent(getBaseContext(),UserMain.class));
                }
            }
        });
       // repeat2();
    }



    void repeat2() {

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Log.d("karma", "its happening");
                RadioButton rdbBlack = (RadioButton) findViewById(R.id.rdb_black);
                if (rdbBlack.isChecked()) {
                    isBlack = true;
                } else {
                    isBlack = false;
                }
                cameraID = 1;
                Intent i = new Intent(CaptureCameraImage.this, CameraView.class);
                startActivityForResult(i, CAMERA_REQUEST);
            }

        }, 0, 10000);
    }


    void repeat() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 20 seconds
//
                RadioButton rdbBlack = (RadioButton) findViewById(R.id.rdb_black);
                if (rdbBlack.isChecked()) {
                    isBlack = true;
                } else {
                    isBlack = false;
                }
                cameraID = 1;
                Intent i = new Intent(CaptureCameraImage.this, CameraView.class);
                startActivityForResult(i, CAMERA_REQUEST);

            }
        }, 20000);
    }

    public void onFrontClick(View v) {
        RadioButton rdbBlack = (RadioButton) findViewById(R.id.rdb_black);
        if (rdbBlack.isChecked()) {
            isBlack = true;
        } else {
            isBlack = false;
        }
        cameraID = 1;
        Intent i = new Intent(CaptureCameraImage.this, CameraView.class);
        startActivityForResult(i, CAMERA_REQUEST);
    }


    public void onBackClick(View v) {
        RadioButton rdbBlack = (RadioButton) findViewById(R.id.rdb_black);
        if (rdbBlack.isChecked()) {
            isBlack = true;
        } else {
            isBlack = false;
        }
        cameraID = 0;
        Intent i = new Intent(CaptureCameraImage.this, CameraView.class);
        startActivityForResult(i, 999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("image", "enter");

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Log.d("karma", "lele aa gya");
        }

    }
}