package com.hypertrack.example_android.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.hypertrack.example_android.R;

/**
 * Created by piyush on 04/11/16.
 */
public class PermissionUtils {
    public static final int REQUEST_CODE_PERMISSION_LOCATION = 1;

    public static boolean checkForPermission(@NonNull final Activity activity,
                                             @NonNull final String permission) {
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }

    /**
     * Return true if the permission available, else starts permission request from user
     */
    public static boolean requestPermission(@NonNull final Activity activity,
                                            @NonNull final String permission) {

        if (checkForPermission(activity, permission)) {
            return true;
        }

        int requestCode = getRequestCodeByPermission(permission);

        ActivityCompat.requestPermissions(activity,
                new String[]{permission},
                requestCode);
        return false;
    }

    private static int getRequestCodeByPermission(final String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                return REQUEST_CODE_PERMISSION_LOCATION;
            default:
                return -1;
        }
    }

    public static AlertDialog showRationaleMessageAsDialog(@NonNull final Activity activity,
                                                           @NonNull final String permission,
                                                           @NonNull final String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setPositiveButton(activity.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PermissionUtils.requestPermission(activity, permission);
            }
        });
        return builder.show();
    }

    public static AlertDialog showRationaleMessageAsDialog(@NonNull final Activity activity,
                                                           @NonNull final String permission,
                                                           @NonNull final String title,
                                                           @NonNull final String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(activity.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PermissionUtils.requestPermission(activity, permission);
            }
        });
        return builder.show();
    }

    public static void openSettings(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    /**
     * @param activity   Context where the Snackbar will be shown
     * @param permission Permission for which Snackbar has to be shown,
     *                   helps in deciding the message string for Snackbar
     * @return snackbar snackbar instance which can be useful to set callbacks,if needed
     */
    public static AlertDialog.Builder showPermissionDeclineDialog(@NonNull final Activity activity,
                                                                  @NonNull final String permission,
                                                                  @NonNull final String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setPositiveButton(activity.getString(R.string.action_settings), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                openSettings(activity);
            }
        });
        builder.setNegativeButton(activity.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

        return builder;
    }

    /**
     * @param activity   Context where the Snackbar will be shown
     * @param permission Permission for which Snackbar has to be shown,
     *                   helps in deciding the message string for Snackbar
     * @return snackbar snackbar instance which can be useful to set callbacks,if needed
     */
    public static AlertDialog.Builder showPermissionDeclineDialog(@NonNull final Activity activity,
                                                                  @NonNull final String permission,
                                                                  @NonNull final String title,
                                                                  @NonNull final String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(activity.getString(R.string.action_settings), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                openSettings(activity);
            }
        });
        builder.setNegativeButton(activity.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

        return builder;
    }
}
