package com.skilrock.retailapp.utils;

import android.content.Context;

public class SharedPrefUtil {

    public static final String USER_PREF                = "userPref";
    public static final String USER_PREF_LANGUAGE       = "userPrefLanguage";
    public static final String AUTH_TOKEN               = "authToken";
    public static final String USERNAME                 = "username";
    public static final String PASSWORD                 = "password";
    public static final String LAST_TICKET_NUMBER       = "lastTicketNumber";
    public static final String USER_ID                  = "userId";
    public static final String LOGIN_BEAN               = "loginBean";
    public static final String SHOW_HOME_SCREEN         = "showHomeScreen";
    public static final String DATE_TIME_FORMAT         = "datetimeformat";
    public static final String COUNTRY_CODE             = "countrycode";
    public static final String DEFAULT_DATETIME_FORMAT  = "defaultdatetimeformat";
    public static final String DEFAULT_DATE_FORMAT      = "defaultdateformat";
    public static final String APP_LANGUAGE             = "appLanguage";
    public static final String DRAW_LAST_TICKET_NUMBER  = "drawLastTicketNumber";
    public static final String WIN_LAST_TICKET_NUMBER   = "winLasTticketNumber";
    public static final String SBS_LAST_TICKET_NUMBER   = "winLasTticketNumber";
    public static final String DRAW_LAST_GAME_CODE      = "drawLastGameCode";
    public static final String PRINT_NO_PAPER           = "printNoPaper";
    public static final String CONTACT_US_NUMBER        = "contactUsNumber";
    public static final String USER_NAME                = "userName";
    public static final String RETAILER_LOCATION        = "retailer_location";

    public static String getLanguage(Context context) {
        return context.getSharedPreferences(USER_PREF_LANGUAGE, Context.MODE_PRIVATE).getString(APP_LANGUAGE, "en");
    }

    public static void putLanguage(Context context, String value) {
        context.getSharedPreferences(USER_PREF_LANGUAGE, Context.MODE_PRIVATE).edit().putString(APP_LANGUAGE, value).apply();
    }

    public static String getString(Context context, String key) {
        return context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE).getString(key, "");
    }

    public static boolean getBoolean(Context context, String key) {
        return context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE).getBoolean(key, true);
    }

    public static void putString(Context context, String key, String value) {
        context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    public static void putBoolean(Context context, String key, boolean flag) {
        context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE).edit().putBoolean(key, flag).apply();
    }

    public static void clearAppSharedPref(Context context) {
        context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE).edit().clear().apply();
    }

    public static void putLastTicketNumber(Context context, String key, String value) {
        context.getSharedPreferences(DRAW_LAST_TICKET_NUMBER, Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    public static String getLastTicketNumber(Context context, String retailerName) {
        return context.getSharedPreferences(DRAW_LAST_TICKET_NUMBER, Context.MODE_PRIVATE).getString(retailerName, "0");
    }

    public static void putLastTicketNumberWinning(Context context, String key, String value) {
        context.getSharedPreferences(WIN_LAST_TICKET_NUMBER, Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    public static String getLastTicketNumberWinning(Context context, String retailerName) {
        return context.getSharedPreferences(WIN_LAST_TICKET_NUMBER, Context.MODE_PRIVATE).getString(retailerName, "0");
    }


    public static void putLastTicketSbs(Context context, String key, String value) {
        context.getSharedPreferences(SBS_LAST_TICKET_NUMBER, Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    public static String getLastTicketNumberSbs(Context context, String retailerName) {
        return context.getSharedPreferences(SBS_LAST_TICKET_NUMBER, Context.MODE_PRIVATE).getString(retailerName, "0");
    }

    public static void putLastGameCode(Context context, String key, String value) {
        context.getSharedPreferences(DRAW_LAST_GAME_CODE, Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    public static String getLastGameCode(Context context, String gameCode) {
        return context.getSharedPreferences(DRAW_LAST_GAME_CODE, Context.MODE_PRIVATE).getString(gameCode, "0");
    }

    public static void putBooleanPrint(Context context, String key, boolean flag) {
        context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE).edit().putBoolean(key, flag).apply();
    }

    public static boolean getBooleanPrint(Context context, String key) {
        return context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE).getBoolean(key, true);
    }

    public static void putUserName(Context context, String userName) {
        context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE).edit().putString(USER_NAME, userName).apply();
    }

    public static String getUserName(Context context) {
        return context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE).getString(USER_NAME, "");
    }



}
