package app.contacts.com.contacts.services;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import app.contacts.com.contacts.utilities.LogUtility;

public class SkypeActions {

    /**
     * Initiate the actions encoded in the specified URI.
     * @param context
     * @param mySkypeUri
     */
    public static void initiateSkypeUri(Context context, String mySkypeUri, String username) {

        // Make sure the Skype for Android client is installed.
        if (!isSkypeClientInstalled(context)) {
            goToMarketToGetSkype(context);
            return;
        }

        // Create the Intent from our Skype URI.
        Uri skypeUri = Uri.parse(mySkypeUri);
        Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);

        // Restrict the Intent to being handled by the Skype for Android client only.
        myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Initiate the Intent. It should never fail because you've already established the
        // presence of its handler (although there is an extremely minute window where that
        // handler can go away).
        context.startActivity(myIntent);

        makeSkypeCall(context, username);

        return;
    }


    /**
     * Determine whether the Skype for Android client is installed on this device.
     * @param myContext
     * @return
     */
    public static boolean isSkypeClientInstalled(Context myContext) {

        try {

            PackageManager myPackageMgr = myContext.getPackageManager();
            myPackageMgr.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES);

        } catch (PackageManager.NameNotFoundException e) {
            return (false);
        }
        return (true);
    }


    /**
     * Install the Skype client through the market: URI scheme.
     * @param myContext
     */
    private static void goToMarketToGetSkype(Context myContext) {

        Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myContext.startActivity(myIntent);
        return;
    }

    /**
     * Make skype call
     * @param number
     * @param context
     */
    public static void makeSkypeCall(Context context, String number) {

        try {

            //Intent sky = new Intent("android.intent.action.CALL_PRIVILEGED");
            //the above line tries to create an intent for which the skype app doesn't supply public api

            Intent sky = new Intent("android.intent.action.VIEW");
            sky.setData(Uri.parse("skype:" + number));
            LogUtility.debug("UTILS", "tel:" + number);
            context.startActivity(sky);

        } catch (ActivityNotFoundException e) {
            LogUtility.error("SKYPE CALL", e.toString());
        }
    }
}
