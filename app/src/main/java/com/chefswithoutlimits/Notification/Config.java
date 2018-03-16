package com.chefswithoutlimits.Notification;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ravi Tamada on 28/09/16.
 * www.androidhive.info
 */

public class Config {

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";

    public static int getID() {
        /*AtomicInteger c = new AtomicInteger(0);
        return c.incrementAndGet();*/
        long time = new Date().getTime();
        String tmpStr = String.valueOf(time);
        String last4Str = tmpStr.substring(tmpStr.length() - 5);
        int notificationId = Integer.valueOf(last4Str);

        System.out.println("Notification ID =============== "+notificationId);
        return notificationId;
    }

    public static String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        // System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        return  formattedDate;
    }
}
