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

import com.hypertrack.lib.HyperTrack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

public class AdminMain extends AppCompatActivity {


    static String gpId = null,allUsers=null;
    static  int idM;
    Button button_createGp, getButton_createUser, button_getAllUsers, button_setTask;
    EditText gpName, userName, name,number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        HyperTrack.requestPermissions(this);
        HyperTrack.requestLocationServices(this, null);
        button_createGp = (Button) findViewById(R.id.button_createGroup);
        getButton_createUser = (Button) findViewById(R.id.button_createUser);
        button_getAllUsers = (Button) findViewById(R.id.button_getUsers);
        button_setTask = (Button) findViewById(R.id.buttonSetTask);
        gpName = (EditText) findViewById(R.id.edit_text_group);
        userName = (EditText) findViewById(R.id.edit_text_userName);
        number= (EditText) findViewById(R.id.edit_textNo);
        name = (EditText) findViewById(R.id.edit_text_name);
        button_createGp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrieveFeedTask task = new RetrieveFeedTask();
                task.execute(1);
            }
        });

        getButton_createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrieveFeedTask task = new RetrieveFeedTask();
                task.execute(2);
            }
        });

        button_getAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrieveFeedTask task = new RetrieveFeedTask();
                task.execute(3);
            }
        });

        button_setTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), Tasks.class));
            }
        });


        SharedPreferences sharedPref = getSharedPreferences("pref1", Context.MODE_PRIVATE);
        String gpid = sharedPref.getString("groupId", null);
        Log.d("karma", "gp id is " + gpid);
        if (gpid != null) {
            button_createGp.setVisibility(View.GONE);
            gpName.setVisibility(View.GONE);
        }


    }


    public void startTracking() {
        Log.d("karma", "start tracking called");
        HyperTrack.startTracking();
        HyperTrack.startTracking();
    }

    public void stopTracking() {
        Log.d("karma", "stop tracking");
        HyperTrack.stopTracking();
    }


    public String fetchData() {
        String result = null;
        HttpURLConnection conn = null;
        InputStream stream = null;
        try {
            Log.d("click", "inside try");

            // Defined URL  where to send data
            SharedPreferences sharedPref = getSharedPreferences("pref1", Context.MODE_PRIVATE);
            String gpid = sharedPref.getString("groupId", null);
            URL url = new URL("https://api.hypertrack.com/api/v1/users/?group_id="+gpid);
            conn = (HttpURLConnection) url.openConnection();
            Log.d("click", "inside try 2");

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "token sk_50a1ba0bae48d45f635ca0ac4b3e2fd050050548");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            conn.connect();
            if (conn.getResponseCode() == 200) {

                stream = conn.getInputStream();
                Log.d("click", "inside try 3");
                if (stream != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder builder = new StringBuilder();
                    Log.d("click", "inside tr4y");
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {

                        builder.append(line + "\n");
                        Log.d("line", line);
                    }

                    result = builder.toString();
                    Log.d("karma","result users is "+result);
                }
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }


    void getAllusers() {
        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("https://api.hypertrack.com/api/v1/users/");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("Authorization", "token sk_50a1ba0bae48d45f635ca0ac4b3e2fd050050548");
            conn.setRequestProperty("Content-Type", "application/json");

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("group_id", "278b736-8166-4744-ae70-81031e147c69");
//            jsonParam.put("lang","en");
//            jsonParam.put("sessionId","1234567890");


            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            Log.d("karma", "after conversion is " + jsonParam.toString());
            wr.write(jsonParam.toString());
            // wr.write(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
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
            Log.d("karma ", "acccessed all the users" + text);
            // Toast.makeText(this,"USer created successfully",Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.d("karma", "exception at last " + ex);
        } finally {
            try {

                reader.close();
            } catch (Exception ex) {
            }
        }
    }


    public void createUser() {
        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("https://api.hypertrack.com/api/v1/users/");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("Authorization", "token sk_50a1ba0bae48d45f635ca0ac4b3e2fd050050548");
            conn.setRequestProperty("Content-Type", "application/json");

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("lookup_id", userName.getText().toString());
            jsonParam.put("name", name.getText().toString());
            jsonParam.put("phone", number.getText().toString());

            SharedPreferences sharedPref = getSharedPreferences("pref1", Context.MODE_PRIVATE);
            String gpid = sharedPref.getString("groupId", null);
            Log.d("karma", "gp id creating user " + gpid);
            jsonParam.put("group_id", gpid);
//            jsonParam.put("lang","en");
//            jsonParam.put("sessionId","1234567890");


            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            Log.d("karma", "after conversion is " + jsonParam.toString());
            wr.write(jsonParam.toString());
            // wr.write(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
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
            Log.d("karma ", "response is user created" + text);
            // Toast.makeText(this,"USer created successfully",Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.d("karma", "exception at last " + ex);
        } finally {
            try {

                reader.close();
            } catch (Exception ex) {
            }
        }

    }


    // Create createGroup Metod
    public void createGroup() throws UnsupportedEncodingException {

        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("https://api.hypertrack.com/api/v1/groups/");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("Authorization", "token sk_50a1ba0bae48d45f635ca0ac4b3e2fd050050548");
            conn.setRequestProperty("Content-Type", "application/json");

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("name", gpName.getText().toString());
//            jsonParam.put("lang","en");
//            jsonParam.put("sessionId","1234567890");


            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            Log.d("karma", "after conversion is " + jsonParam.toString());
            wr.write(jsonParam.toString());
            // wr.write(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
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
            JSONObject obj = new JSONObject(text);
            String id = obj.optString("id");
            gpId = id;


            //  Log.d("karma ", "response is " + text);
            // Toast.makeText(this,"Gp created successfully",Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.d("karma", "exception at last " + ex);
        } finally {
            try {

                reader.close();
            } catch (Exception ex) {
            }
        }


    }


    void setGroupId(String text) throws JSONException {

        JSONObject obj = new JSONObject(text);
        if (obj.has("id")) {
            gpId = obj.getString("id");
        }
    }









    /*

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }
    */

// This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            Log.d("karma", spokenText);
            // Do something with spokenText
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
*/


    class RetrieveFeedTask extends AsyncTask<Integer, Void, Void> {





        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("karma", "inside on post");
            super.onPostExecute(aVoid);
            if(idM==1){
                if (gpId != null) {
                    Log.d("karma", "inside if");
//                Context context = getBaseContext();
//                SharedPreferences sharedPref = context.getSharedPreferences(
//                        "pref", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPref.edit();
//                editor.putString("groupId", gpId);
//                editor.commit();
                    SharedPreferences sharedpreferences = getSharedPreferences("pref1", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("groupId", gpId);
                    editor.commit();
                }
            }
            if(idM==2){}
            if(idM==3){
                Intent intent=new Intent(getBaseContext(),AllUsers.class);
                intent.putExtra("users",allUsers);
                startActivity(new Intent(intent));
            }


            Toast.makeText(getBaseContext(), "Success full Action", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Integer... voids) {
            int id = voids[0];

            try {
//            Log.d("karma", "called");
                if (id == 1){
                    createGroup();
                    idM=1;
                }

                if (id == 2){
                    idM=2;
                    createUser();
                }

                 if(id==3){
                     idM=3;
                     allUsers=fetchData();
                 }



            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d("karma", "Exception occurred " + e);
            }

            return null;
        }
    }


}
