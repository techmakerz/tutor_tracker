package com.hypertrack.example_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.view.Window;

import com.hypertrack.example_android.util.BaseActivity;
import com.hypertrack.lib.HyperTrack;

/**
 * Created by piyush on 21/03/17.
 */

public class UserFirstTime extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Check if User is logged in currently
        String userId = HyperTrack.getUserId();
        if (TextUtils.isEmpty(userId)) {

            // Start User Login by starting LoginActivity
            TaskStackBuilder.create(UserFirstTime.this)
                    .addNextIntentWithParentStack(new Intent(UserFirstTime.this, LoginActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    .startActivities();
            finish();
        } else {

            // Start User Session by starting MainActivity
            TaskStackBuilder.create(UserFirstTime.this)
                    .addNextIntentWithParentStack(new Intent(UserFirstTime.this, CaptureCameraImage.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    .startActivities();
            finish();
        }
    }
}
