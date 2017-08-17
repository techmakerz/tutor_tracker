package com.hypertrack.example_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class OptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
    }

    public void adminLogin(View v) {
        startActivity(new Intent(this,AdminMain.class));
    }



    public void userLogin(View v) {

    }
}
