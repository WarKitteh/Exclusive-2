package com.treehouse.realtimemessaging.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Simple utility class for handling username information and for determining if a user is
 * currently "logged in" to our Realtime Messaging application.
 */
public final class UserUtility {

    /**
     * Key for storing the username in SharedPreferences
     */
    private static final String USERNAME_KEY = "username";

    /**
     * Sets the username in SharedPreferences.
     * @param context Used for accessing SharedPreferences
     * @param username Username to store
     */
    public static void setUsername(Context context, String username) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USERNAME_KEY, username);
        editor.apply();
    }

    /**
     * Gets the username from SharedPreferences.
     * @param context Used for accessing SharedPreferences
     * @return The username of the user logged in; or null if no user is logged in
     */
    public static String getUsername(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String username = prefs.getString(USERNAME_KEY, null);
        return username;
    }

    /**
     * Used to determine if user is signed in or not.
     * @param context Used for accessing SharedPreferences
     * @return true if a user is logged in; false otherwise
     */
    public static boolean isUserSignedIn(Context context) {
        boolean isUserSignedIn = false;
        if (getUsername(context) != null) {
            isUserSignedIn = true;
        }
        return isUserSignedIn;
    }
}
