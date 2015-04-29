package app.contacts.com.contacts.utilities;

import android.util.Log;

public class LogUtility {

    /**
     * Log debug messages
     * @param className
     * @param message
     */
    public static void debug(String className, String message) {
        Log.d(className, message);
    }

    /**
     * log error messages
     * @param className
     * @param message
     */
    public static void error(String className, String message) {
        Log.e(className, message);
    }

}
