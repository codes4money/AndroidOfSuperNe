package com.donal.superne.app.config;

import android.net.Uri;


/**
 * Created by donal on 14-7-1.
 */
public class Constant {

    public static final String BASE_URL = "";

    public static final String BASE_API = "";

    //Connect Action and Type
    public static final String CONNECT_ACTION = "ConnectAction";
    public static final String CONNECT_TYPE= "ConnectType";
    public static final int WIFI = 1;
    public static final int GPRS = 2;
    public static final int NO_CONNECT = 3;

    //request code for login
    public static final int REQUEST_REGISTER = 1;

    //request code for Register
    public static final int REQUEST_REGISTERPASS = 1;
    public static final String MOBILE = "mobile";

    //search users
    public static final String USERS = "users";

    public static final String ACTION_REQUEST_FROM_USER_ADD_FRIEND = "ACTION_REQUEST_FROM_USER_ADD_FRIEND";
    public static final String ACTION_FRIEND_COMFIRE = "ACTION_FRIEND_COMFIRE";
    public static final String ACTION_FRIEND_DELETE = "ACTION_FRIEND_DELETE";

    //DataBase
    public static final Uri USER_URI = Uri.parse("content://org.jivesoftware.smackx.superne/Conversation");
}
