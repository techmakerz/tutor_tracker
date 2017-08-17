package com.hypertrack.example_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

public class AddTask extends AppCompatActivity {
EditText task,v1,v2,v3;
    Button addTask;
    private Firebase mRef;
    EditText editTextTaskName;
   static String taskName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Firebase.setAndroidContext(this);
        mRef=new Firebase("https://handy-operation-138523.firebaseio.com/");
        task= (EditText) findViewById(R.id.task);
        v1= (EditText) findViewById(R.id.var1);
        v2= (EditText) findViewById(R.id.var2);
        v3= (EditText) findViewById(R.id.var3);
        addTask= (Button) findViewById(R.id.button_addTask);
        editTextTaskName= (EditText) findViewById(R.id.taskName);





        Log.d("task","taskName is "+taskName);

        addTask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getSharedPreferences("pref1", Context.MODE_PRIVATE);
                String gpid = sharedPref.getString("groupId", null);
                taskName=editTextTaskName.getText().toString();
                Firebase f=mRef.child(gpid);
               Firebase mRefChild=f.child("task");
                Log.d("task","taskName is "+taskName);
                Firebase taskChild=mRefChild.child(taskName);
                taskChild.setValue(task.getText().toString());

                addIntent();

            }
        });

    }

    void addIntent(){
        taskName=editTextTaskName.getText().toString();
        String[] s=new String[4];
       String  s11=task.getText().toString();
        String s1= v1.getText().toString();
        String s2= v2.getText().toString();
        String s3=v3.getText().toString();

        s[0]=s11;
        s[1]=s1;
        s[2]=s2;
        s[3]=s3;














        RetrieveFeedTask task = new RetrieveFeedTask();
        task.execute(s);
    }



    // Create serverRequest Method
    public void GetText(String[] s) throws UnsupportedEncodingException {

        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("https://api.api.ai/v1/intents?v=20150910");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("Authorization","Bearer 9e673195e2594198a41f7c20e7a9167d");
            conn.setRequestProperty("Content-Type", "application/json");

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("name", taskName);
            jsonParam.put("auto",true);
            JSONArray userSays=new JSONArray();
            for(int i=0;i<s.length;++i){
                JSONObject userObj=new JSONObject();
                JSONArray array=new JSONArray();
                JSONObject arrayObj=new JSONObject();
                arrayObj.put("text",s[i]);
               // arrayObj.put("data","turn");
                array.put(arrayObj);
                userObj.put("data",array);

                userSays.put(userObj);
            }
            jsonParam.put("userSays",userSays);


            JSONArray responseArray=new JSONArray();
            JSONObject responseObj=new JSONObject();
            responseObj.put("resetContexts",false);
            responseObj.put("speech","TRUE");
            responseArray.put(responseObj);
            jsonParam.put("responses",responseArray);

//            jsonParam.put("lang","en");
//            jsonParam.put("sessionId","1234567890");


            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            Log.d("karma","after conversion is "+jsonParam.toString());
            wr.write(jsonParam.toString());
            wr.flush();
            Log.d("karma", "json is " + jsonParam);

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;


            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }


            text = sb.toString();
            Log.d("karma ", "response is " + text);
        } catch (Exception ex) {
            Log.d("karma", "exception at last " + ex);
        } finally {
            try {

                reader.close();
            } catch (Exception ex) {
            }
        }


    }






    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getBaseContext(),"Task Added Successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getBaseContext(),Tasks.class));
        }

        @Override
        protected Void doInBackground(String... voids) {
            try {
                Log.d("karma", "called");
                GetText(voids);
                Log.d("karma", "after called");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d("karma", "Exception occurred " + e);
            }

            return null;
        }


    }







}
