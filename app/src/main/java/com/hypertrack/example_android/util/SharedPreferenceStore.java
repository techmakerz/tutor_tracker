package com.hypertrack.example_android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by piyush on 30/09/16.
 */
public class SharedPreferenceStore {
    private static final String PREF_NAME = "com.hypertrack.example_android";

    private static final String ACTION_ID_KEY = "action_id";

    public static void setActionID(Context context, String actionID) {
        if (TextUtils.isEmpty(actionID))
            return;

        SharedPreferences.Editor editor = getEditor(context);

        editor.putString(ACTION_ID_KEY, actionID);
        editor.commit();
    }

    public static String getActionID(Context context) {
        return getSharedPreferences(context).getString(ACTION_ID_KEY, null);
    }

    public static void clearActionID(Context context) {
        SharedPreferences.Editor editor = getEditor(context);

        editor.remove(ACTION_ID_KEY);
        editor.apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }
}
