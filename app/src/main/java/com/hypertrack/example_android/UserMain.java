package com.hypertrack.example_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.firebase.client.Firebase;
import com.hypertrack.lib.HyperTrack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class UserMain extends AppCompatActivity implements LocationListener {
    Button imageUp;
    static Timer t;
    static double latitude;
    static double longitude;
    static String userId;
    private Context mContext;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    Location location;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute


    String name, email, adhaar, mobile;
    LinearLayout mLayout;
    private Firebase mRef;
    private ArrayList<String> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        Firebase.setAndroidContext(this);
        mContext = getBaseContext();



        mLayout = (LinearLayout) findViewById(R.id.taskLayout);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasks);
        SharedPreferences sharedPref = getSharedPreferences("pref1", Context.MODE_PRIVATE);
        String gpid = sharedPref.getString("groupIdUser", null);

        name = sharedPref.getString("user_name", null);
        adhaar = sharedPref.getString("login_adhar", null);
        email = sharedPref.getString("login_email", null);
        mobile = sharedPref.getString("login_mobile", null);
        userId = sharedPref.getString("userId", null);

        if(!canGetLocation()){
            showSettingsAlert();
        }

        Location location=get1Location();
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        Log.d("karma","location is "+location.getLatitude()+" long "+location.getLongitude());

//        mRef = new Firebase("https://handy-operation-138523.firebaseio.com/"+gpid+"/task");
//        mListView.setAdapter(adapter);
//        mRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                tasks.add(dataSnapshot.getValue(String.class));
//                adapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//
//
//    }
    }


    void tryIt(){
        startActivity(new Intent(getBaseContext(),CaptureCameraImage.class));
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        //alertDialog.show();









    }
















    public Location get1Location() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.;
                        Log.d("karma","permission not given");
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    public void stopTracking(View v) {
        HyperTrack.stopTracking();

        t.cancel();
    }

    public void startTracking(View v) {
        HyperTrack.startTracking();
        Log.d("karma", "You are now tracked by the admin");



        repeat();
    }


    void repeat() {


        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Log.d("karma", "its happening");
                Log.d("karma", "working");
                RetrieveFeedTask task = new RetrieveFeedTask();
                task.execute();
            }

        }, 0, 20000);
    }


    //method to get location


//    public void getLocation() {
//        String text = "";
//        BufferedReader reader = null;
//
//        // Send data
//        try {
//
//            Log.d("karma", "user id is " + userId);
//            // Defined URL  where to send data
//            URL url = new URL("https://app.hypertrack.io/api/v1/users/" + userId + "/");
//
//            // Send POST data request
//
//            URLConnection conn = url.openConnection();
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//
//            //conn.setRequestProperty("Authorization", "token sk_50a1ba0bae48d45f635ca0ac4b3e2fd050050548");
//            conn.setRequestProperty("Authorization", "token sk_50a1ba0bae48d45f635ca0ac4b3e2fd050050548");
//            conn.setRequestProperty("Content-Type", "application/json");
//
////            //Create JSONObject here
////            JSONObject jsonParam = new JSONObject();
////            jsonParam.put("lookup_id", userName.getText().toString());
////            jsonParam.put("name", name.getText().toString());
////            jsonParam.put("phone", number.getText().toString());
////
////            SharedPreferences sharedPref = getSharedPreferences("pref1", Context.MODE_PRIVATE);
////            String gpid = sharedPref.getString("groupId", null);
////            Log.d("karma", "gp id creating user " + gpid);
////            jsonParam.put("group_id", gpid);
////            jsonParam.put("lang","en");
////            jsonParam.put("sessionId","1234567890");
//
//
//            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//            //   Log.d("karma", "after conversion is " + jsonParam.toString());
//            //wr.write(jsonParam.toString());
//            // wr.write(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
//            wr.flush();
//            //Log.d("karma", "json is " + jsonParam);
//
//            // Get the server response
//
//            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//
//
//            // Read Server Response
//            while ((line = reader.readLine()) != null) {
//                // Append server response in string
//                sb.append(line + "\n");
//            }
//
//
//            text = sb.toString();
//            Log.d("karma ", "response is user created" + text);
//            // Toast.makeText(this,"USer created successfully",Toast.LENGTH_LONG).show();
//        } catch (Exception ex) {
//            Log.d("karma", "exception at last " + ex);
//        } finally {
//            try {
//
//                reader.close();
//            } catch (Exception ex) {
//            }
//        }
//
//    }


    // Create serverRequest Metod
    public void serverRequest() throws UnsupportedEncodingException {

        //String parameters = "param1=value1&param2=value2";
        // String parameters = "adhaar=1234&name=value&email=anu@gmail.com&u_latitude=23.34&u_longitude=11.02";
        // Create data variable for sent values to server

        String data = URLEncoder.encode("adhaar", "UTF-8")
                + "=" + URLEncoder.encode(adhaar, "UTF-8");

        data += "&" + URLEncoder.encode("name", "UTF-8") + "="
                + URLEncoder.encode(name, "UTF-8");

        data += "&" + URLEncoder.encode("email", "UTF-8") + "="
                + URLEncoder.encode(email, "UTF-8");

        data += "&" + URLEncoder.encode("u_latitude", "UTF-8")
                + "=" + URLEncoder.encode(latitude+"", "UTF-8");

        data += "&" + URLEncoder.encode("u_longitude", "UTF-8")
                + "=" + URLEncoder.encode(longitude+"", "UTF-8");

        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("http://openhub.tech/api/apitestpost.php");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

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
            Log.d("karma", "text is " + text);
        } catch (Exception ex) {
            Log.d("karma", "error occurred" + ex);

        } finally {
            try {

                reader.close();
            } catch (Exception ex) {
            }
        }

//        // Show response on activity
//        content.setText( text  );

    }

    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... voids) {
            try {
                Log.d("karma", "called");
                serverRequest();
                Log.d("karma", "after called");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d("karma", "Exception occurred " + e);
            }

            return null;
        }
    }

    class RetrieveFeedTask2 extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... voids) {
//            get1Location();

            return null;
        }
    }


}
