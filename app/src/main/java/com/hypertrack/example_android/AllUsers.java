package com.hypertrack.example_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllUsers extends AppCompatActivity {
static  int count;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        Intent intent = getIntent();
        String users = intent.getStringExtra("users");
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.addItemDecoration(new VerticalSpace(30));
        Log.d("karma", "inside all users activity " + users);
        try {
            getData(users);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    void getData(String data) throws JSONException {
        ArrayList<customUser> arrayList=new ArrayList<>();
        JSONObject object=new JSONObject(data);
        count=object.getInt("count");
        Log.d("karma","count is "+count);
        JSONArray array=object.getJSONArray("results");
        for(int i=0;i<count;i++){
            JSONObject currObj=array.getJSONObject(i);
            customUser user=new customUser();
            user.setId(currObj.getString("id"));
            user.setName(currObj.getString("name"));
            arrayList.add(user);
        }
        CardViewAdapter adapter = new CardViewAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

    }
}
