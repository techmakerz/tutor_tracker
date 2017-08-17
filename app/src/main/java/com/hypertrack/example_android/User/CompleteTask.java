package com.hypertrack.example_android.User;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.hypertrack.example_android.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.Result;

public class CompleteTask extends AppCompatActivity implements AIListener {
    private static final int RECORD_REQUEST_CODE = 101;
    private Button listenButton;
    private TextView resultTextView,progress;
    private AIService aiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_task);
        Firebase.setAndroidContext(this);
        listenButton = (Button) findViewById(R.id.listenButton);
progress= (TextView) findViewById(R.id.progress);

        resultTextView = (TextView) findViewById(R.id.resultTextView);
        final AIConfiguration config = new AIConfiguration("cc9a6867a28c47408ebb292c04c7b05c",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);


        // for permission at runtime
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }


        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aiService.startListening();
            }
        });


        SharedPreferences sharedPref = getSharedPreferences("pref1",Context.MODE_PRIVATE);
        int count = sharedPref.getInt("userCount", 0);
        progress.setText("Your have completed "+count+" tasks so far");


    }


    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                RECORD_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RECORD_REQUEST_CODE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                } else {
                    Log.i("karma", "Permission has been granted by user");
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    @Override
    public void onResult(ai.api.model.AIResponse response) {

        Result result = response.getResult();
        Log.d("karma", "result response " + response);
        Log.d("karma", "result action " + result.toString());
        String resolvedQuery = result.getResolvedQuery();

        // Get parameters
        String parameterString = "";
//        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
//            Log.d("karma ", "not null");
//
//            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
//                Log.d("karma ", "inside for");
//                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
//            }
//        }

        // Show results in TextView.
        resultTextView.setText("Query:" + result.getResolvedQuery() +
                "\nAction: " + result.getAction() +
                "\nParameters: " + parameterString);


        RetrieveFeedTask task = new RetrieveFeedTask();
        task.execute(resolvedQuery);


    }

    @Override
    public void onError(ai.api.model.AIError error) {
        resultTextView.setText(error.toString());
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }


    // Create serverRequest Metod
    public void GetText(String query) throws UnsupportedEncodingException {

        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("https://api.api.ai/v1/query?v=20150910");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("Authorization", "Bearer cc9a6867a28c47408ebb292c04c7b05c");
            conn.setRequestProperty("Content-Type", "application/json");

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            JSONArray queryArray = new JSONArray();
            queryArray.put(query);
            jsonParam.put("query", queryArray);
//            jsonParam.put("name", "order a medium pizza");
            jsonParam.put("lang", "en");
            jsonParam.put("sessionId", "1234567890");


            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            Log.d("karma", "after conversion is " + jsonParam.toString());
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
            JSONObject object1 = new JSONObject(text);
            JSONObject object=object1.getJSONObject("result");
            JSONObject fulfillment = null;
            String speech = null;
//            if (object.has("fulfillment")) {
                fulfillment = object.getJSONObject("fulfillment");
//                if (fulfillment.has("speech")) {
                    speech = fulfillment.optString("speech");
//                }
//            }
            SharedPreferences sharedPref = getSharedPreferences("pref1", Context.MODE_PRIVATE);
            String userid = sharedPref.getString("userId", null);

            Log.d("karma", "speech is " + speech);
            if (speech.equals("TRUE")) {
                Log.d("karma", "inside " + speech);
                updateUserTask(userid);

            }


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


    void updateUserTask(String userid) {
        Firebase mRef = new Firebase("https://handy-operation-138523.firebaseio.com/");
        Firebase user = mRef.child(userid);
        Firebase userChild = user.child("count");
//    userChild.addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            count =Integer.parseInt(dataSnapshot.getValue(String.class));
//        }
//
//        @Override
//        public void onCancelled(FirebaseError firebaseError) {
//
//        }
//    });
        userChild.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                int count = Integer.parseInt(dataSnapshot.getValue(String.class));
                SharedPreferences sharedpreferences = getSharedPreferences("pref1", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("userCount", count);
                editor.commit();
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


        //update value
        SharedPreferences sharedPref = getSharedPreferences("pref1", Context.MODE_PRIVATE);
        int count = sharedPref.getInt("userCount", 0);
        count++;
        userChild.setValue(count);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("userCount", count);
        Log.d("karma", "task done " + count);

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


    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... voids) {
            try {
                Log.d("karma", "called");
                GetText(voids[0]);
                Log.d("karma", "after called");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d("karma", "Exception occurred " + e);
            }

            return null;
        }
    }


}
