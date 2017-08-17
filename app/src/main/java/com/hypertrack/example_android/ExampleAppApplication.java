package com.hypertrack.example_android;

import android.app.Application;

import com.firebase.client.Firebase;
import com.hypertrack.lib.HyperTrack;

/**
 * Created by piyush on 23/09/16.
 */
public class ExampleAppApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize HyperTrack SDK with the Publishable Key
        // Refer to documentation at https://docs.hypertrack.com/v3/gettingstarted/authentication.html#publishable-key
        // @NOTE: Add **YOUR_PUBLISHABLE_KEY** here for SDK to be authenticated with HyperTrack Server
        HyperTrack.initialize(this,"pk_fdfd039e83cc8cfdc9e22a23ffd9eeb9602b26cf");
        Firebase.setAndroidContext(this);
//        if (!FirebaseApp.getApps(this).isEmpty()) {
//            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        }
    }
}
