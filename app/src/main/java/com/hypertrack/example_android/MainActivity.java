package com.hypertrack.example_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hypertrack.example_android.util.BaseActivity;
import com.hypertrack.example_android.util.SharedPreferenceStore;
import com.hypertrack.lib.HyperTrack;
import com.hypertrack.lib.callbacks.HyperTrackCallback;
import com.hypertrack.lib.models.Action;
import com.hypertrack.lib.models.ActionParams;
import com.hypertrack.lib.models.ActionParamsBuilder;
import com.hypertrack.lib.models.ErrorResponse;
import com.hypertrack.lib.models.SuccessResponse;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ProgressDialog mProgressDialog;
    // Click Listener for AssignAction Button
    private View.OnClickListener assignActionBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            // Create ActionParams object to define Action params
            ActionParams params = new ActionParamsBuilder()
//                    .setExpectedPlace(expectedPlace)
                    .setType(Action.ACTION_TYPE_VISIT)
                    .build();

            // Call assignAction to start the tracking action
            HyperTrack.createAndAssignAction(params, new HyperTrackCallback() {
                @Override
                public void onSuccess(@NonNull SuccessResponse response) {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }

                    if (response.getResponseObject() != null) {
                        Action action = (Action) response.getResponseObject();
                        SharedPreferenceStore.setActionID(MainActivity.this, action.getId());

                        Toast.makeText(MainActivity.this, "Action (id = " + action.getId() + ") assigned successfully.",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(@NonNull ErrorResponse errorResponse) {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }

                    Toast.makeText(MainActivity.this, "Action assigned failed: " + errorResponse.getErrorMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
    // Click Listener for CompleteAction Button
    private View.OnClickListener completeActionBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String actionId = SharedPreferenceStore.getActionID(MainActivity.this);
            if (TextUtils.isEmpty(actionId)) {
                Toast.makeText(MainActivity.this, "CompleteAction Failed: ActionID is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Complete Action on HyperTrack SDK
            HyperTrack.completeAction(actionId);

            Toast.makeText(MainActivity.this, "Action (id = " + actionId + ") completed successfully.", Toast.LENGTH_SHORT).show();
            SharedPreferenceStore.clearActionID(MainActivity.this);
        }

    };

    private View.OnClickListener trackDriverOnMapBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String actionId = SharedPreferenceStore.getActionID(MainActivity.this);
            // Validate ActionId before tracking it on Live Tracking View
            if (TextUtils.isEmpty(actionId)) {
                Toast.makeText(MainActivity.this, "ActionId is empty. Please provide a valid actions " +
                        "for trackActionsForUser.", Toast.LENGTH_SHORT).show();
                return;
            }

            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            // Create a list of Actions to be tracked on Live Tracking View
            final ArrayList<String> actions = new ArrayList<>();
            actions.add(actionId);



//            HyperTrack.startTracking( new HyperTrackCallback() {
//                @Override
//                public void onSuccess(@NonNull SuccessResponse successResponse) {
//                    if (mProgressDialog != null) {
//                        mProgressDialog.dismiss();
//                    }
//
//                    Toast.makeText(MainActivity.this, "trackActionsForUser successful for ActionsList: "
//                            + actions, Toast.LENGTH_SHORT).show();
//
//                    // Open Activity with HyperTrack MapFragment
//                    proceedToLiveTrackingScreen(actions);
//                }
//
//                @Override
//                public void onError(@NonNull ErrorResponse errorResponse) {
//                    if (mProgressDialog != null) {
//                        mProgressDialog.dismiss();
//                    }
//
//                    Toast.makeText(MainActivity.this, errorResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });

            HyperTrack.trackAction(actions, new HyperTrackCallback() {
                @Override
                public void onSuccess(@NonNull SuccessResponse successResponse) {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }

                    Toast.makeText(MainActivity.this, "trackActionsForUser successful for ActionsList: "
                            + actions, Toast.LENGTH_SHORT).show();

                    // Open Activity with HyperTrack MapFragment
                    proceedToLiveTrackingScreen(actions);
                }

                @Override
                public void onError(@NonNull ErrorResponse errorResponse) {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }

                    Toast.makeText(MainActivity.this, errorResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar(getString(R.string.app_name), false);

        // Initialize UI Views
        initUIViews();

        /**
         * @IMPORTANT:
         * Implement Network call to fetch ORDERS/TRANSACTIONS for the User here.
         * Once the list of orders/transactions have been fetched, implement
         * assignAction and completeAction calls either with or without user interaction
         * depending on the specific requirements in the workflow of your business and your app.
         */
    }

    private void initUIViews() {
        // Initialize AssignAction Button
        Button assignActionBtn = (Button) findViewById(R.id.assignActionButton);
        if (assignActionBtn != null)
            assignActionBtn.setOnClickListener(assignActionBtnListener);

        // Initialize CompleteAction Button
        Button completeActionBtn = (Button) findViewById(R.id.completeActionButton);
        if (completeActionBtn != null)
            completeActionBtn.setOnClickListener(completeActionBtnListener);

        Button trackDriverOnMapButton = (Button) findViewById(R.id.trackUserOnMapButton);
        if (trackDriverOnMapButton != null)
            trackDriverOnMapButton.setOnClickListener(trackDriverOnMapBtnClickListener);
    }

    private void proceedToLiveTrackingScreen(ArrayList<String> actions) {
        Intent userMapIntent = new Intent(MainActivity.this, UserMapActivity.class);
        userMapIntent.putStringArrayListExtra("actions", actions);
        startActivity(userMapIntent);
    }

    public void onLogoutClicked(MenuItem menuItem) {
        Toast.makeText(MainActivity.this, R.string.main_logout_success_msg, Toast.LENGTH_SHORT).show();

        // Stop HyperTrack SDK
        HyperTrack.stopTracking();

        // Proceed to LoginActivity for a fresh User Login
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
