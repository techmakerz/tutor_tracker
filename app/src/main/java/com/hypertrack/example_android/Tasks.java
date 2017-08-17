package com.hypertrack.example_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

public class Tasks extends AppCompatActivity {
    Button addTask, complete;
    ListView mListView;
    LinearLayout mLayout;
    private Firebase mRef;
    private ArrayList<String> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        addTask = (Button) findViewById(R.id.addTask);
        complete = (Button) findViewById(R.id.buttonComplete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),AdminMain.class));
            }
        });
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AddTask.class));
            }
        });


        Firebase.setAndroidContext(this);
        mListView = (ListView) findViewById(R.id.listView);
        mLayout = (LinearLayout) findViewById(R.id.taskLayout);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasks);

        SharedPreferences sharedPref = getSharedPreferences("pref1", Context.MODE_PRIVATE);
        String gpid = sharedPref.getString("groupId", null);


        mRef = new Firebase("https://handy-operation-138523.firebaseio.com/"+gpid+"/task");
        mListView.setAdapter(adapter);
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                tasks.add(dataSnapshot.getValue(String.class));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }
}
