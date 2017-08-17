package com.hypertrack.example_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.TaskStackBuilder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hypertrack.example_android.util.BaseActivity;
import com.hypertrack.lib.HyperTrack;
import com.hypertrack.lib.callbacks.HyperTrackCallback;
import com.hypertrack.lib.models.ErrorResponse;
import com.hypertrack.lib.models.SuccessResponse;
import com.hypertrack.lib.models.User;

/**
 * Created by piyush on 30/09/16.
 */
public class LoginActivity extends BaseActivity {

    private TextInputLayout nameHeader, phoneNumberHeader;
    private EditText nameText, phoneNumberText, emailText, adharText;
    private LinearLayout loginBtnLoader;

    private TextWatcher userNameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && s.length() > 0) {
                nameHeader.setError(null);
            }
        }
    };

    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && s.length() > 0) {
                phoneNumberHeader.setError(null);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Toolbar
        initToolbar(getString(R.string.login_activity_title), false);

        // Initialize UI Views
        initUIViews();
    }

    /**
     * Call this method to initialize UI views and handle listeners for these views
     */
    private void initUIViews() {
        // Initialize UserName Views
        nameHeader = (TextInputLayout) findViewById(R.id.login_name_header);
        nameText = (EditText) findViewById(R.id.login_name);
        emailText = (EditText) findViewById(R.id.login_email);
        adharText = (EditText) findViewById(R.id.login_adhar);


        if (nameText != null)
            nameText.addTextChangedListener(userNameTextWatcher);

        // Initialize Password Views
        phoneNumberHeader = (TextInputLayout) findViewById(R.id.login_phone_number_header);
        phoneNumberText = (EditText) findViewById(R.id.login_phone_number);
        if (phoneNumberText != null)
            phoneNumberText.addTextChangedListener(passwordTextWatcher);

        // Initialize Login Btn Loader
        loginBtnLoader = (LinearLayout) findViewById(R.id.login_user_login_btn_loader);
    }

    /**
     * Call this method when User Login button has been clicked.
     * Note that this method is linked with the layout file (content_login.xml)
     * using this button's layout's onClick attribute. So no need to invoke this
     * method or handle login button's click listener explicitly.
     *
     * @param view
     */
    public void onLoginButtonClick(View view) {
        // Check if Location Settings are enabled, if yes then attempt DriverLogin
        checkForLocationSettings();


    }

    /**
     * Call this method to check Location Settings before proceeding for User Login
     */
    private void checkForLocationSettings() {
        // Check for Location permission
        if (!HyperTrack.checkLocationPermission(this)) {
            HyperTrack.requestPermissions(this);
            return;
        }

        // Check for Location settings
        if (!HyperTrack.checkLocationServices(this)) {
            HyperTrack.requestLocationServices(this, null);
        }

        // Location Permissions and Settings have been enabled
        // Proceed with your app logic here i.e User Login in this case
        attemptUserLogin();
    }

    /**
     * Call this method to attempt user login. This method will create a User on HyperTrack Server
     * and configure the SDK using this generated UserId.
     */
    private void attemptUserLogin() {
        // Show Login Button loader
        loginBtnLoader.setVisibility(View.VISIBLE);

        // Get User details, if specified
        final String name = nameText.getText().toString();
        final String phoneNumber = phoneNumberText.getText().toString();
        final String lookup = name;
        final String email = emailText.getText().toString();
        final String adhar = adharText.getText().toString();
        /**
         * Create a User on HyperTrack Server here to login your user & configure HyperTrack SDK with
         * this generated HyperTrack UserId.
         * OR
         * Implement your API call for User Login and get back a HyperTrack UserId from your API Server
         * to be configured in the HyperTrack SDK.
         */


        HyperTrack.getOrCreateUser(name, phoneNumber, lookup, new HyperTrackCallback() {
            @Override
            public void onSuccess(@NonNull SuccessResponse successResponse) {
                // Hide Login Button loader
                loginBtnLoader.setVisibility(View.GONE);

                User user = (User) successResponse.getResponseObject();
                String gpid = user.getGroupId();
                String userid = user.getId();
                SharedPreferences sharedpreferences = getSharedPreferences("pref1", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("groupIdUser", gpid);
//                editor.putInt("taskNo",0);
                editor.putString("user_name", name);
                editor.putString("userId", userid);
                editor.putString("login_email", email);
                editor.putString("login_adhar", adhar);

                editor.commit();
                // Handle createUser success here, if required
                // HyperTrack SDK auto-configures UserId on createUser API call, so no need to call
                // HyperTrack.setUserId() API

                // On UserLogin success
                onUserLoginSuccess();
            }

            @Override
            public void onError(@NonNull ErrorResponse errorResponse) {
                // Hide Login Button loader
                loginBtnLoader.setVisibility(View.GONE);

                Toast.makeText(LoginActivity.this, R.string.login_error_msg + " " + errorResponse.getErrorMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Call this method when user has successfully logged in
     */
    private void onUserLoginSuccess() {
        HyperTrack.startTracking(new HyperTrackCallback() {
            @Override
            public void onSuccess(@NonNull SuccessResponse successResponse) {
                Log.d("karma", "user logged in success " + successResponse.getResponseObject().toString());

                // Hide Login Button loader
                loginBtnLoader.setVisibility(View.GONE);

                Toast.makeText(LoginActivity.this, R.string.login_success_msg, Toast.LENGTH_SHORT).show();
//startActivity(new Intent(getBaseContext(),CaptureCameraImage.class));
                // Start User Session by starting MainActivity
                TaskStackBuilder.create(LoginActivity.this)
                        .addNextIntentWithParentStack(new Intent(LoginActivity.this, CaptureCameraImage.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        .startActivities();
                finish();
            }

            @Override
            public void onError(@NonNull ErrorResponse errorResponse) {
                // Hide Login Button loader
                loginBtnLoader.setVisibility(View.GONE);

                Toast.makeText(LoginActivity.this, R.string.login_error_msg + " " + errorResponse.getErrorMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == HyperTrack.REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkForLocationSettings();

            } else {
                // Handle Location Permission denied error
                Toast.makeText(this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HyperTrack.REQUEST_CODE_LOCATION_SERVICES) {
            if (resultCode == Activity.RESULT_OK) {
                checkForLocationSettings();
            } else {
                // Handle Enable Location Services request denied error
                Toast.makeText(this, R.string.enable_location_settings, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
