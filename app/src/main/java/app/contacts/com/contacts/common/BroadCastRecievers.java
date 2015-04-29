package app.contacts.com.contacts.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import app.contacts.com.contacts.adapters.MessagesLogArrayAdapter;
import app.contacts.com.contacts.models.Message;
import app.contacts.com.contacts.utilities.DateUtil;
import app.contacts.com.contacts.utilities.LogUtility;

/**
 * Created by roberto on 4/14/15.
 */
public class BroadCastRecievers {

    private static BroadcastReceiver receiver;


    /**
     * Get SMS Message
     * @param context
     * @param adapter
     * @param phoneNumber
     */
    public static void textMessageReciever(Context context, final MessagesLogArrayAdapter adapter, final String phoneNumber){

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        if(receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // Retrieves a map of extended data from the intent.
                    final Bundle bundle = intent.getExtras();

                    try {

                        if (bundle != null) {

                            final Object[] pdusObj = (Object[]) bundle.get("pdus");

                            for (int i = 0; i < pdusObj.length; i++) {

                                SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                                String recPhoneNumber = currentMessage.getDisplayOriginatingAddress();

                                String senderNum = recPhoneNumber;
                                String message = currentMessage.getDisplayMessageBody();

                                Log.i("SmsReceiver", "senderNum: " + senderNum + " " + phoneNumber + "; message: " + message);

                                if (adapter != null && phoneNumber.contains(senderNum)) {
                                    Message newMessage = new Message();
                                    newMessage.setDate(DateUtil.getCurrentDate());
                                    newMessage.setMessage(message);
                                    newMessage.setPhoneNumber(senderNum);
                                    newMessage.setType(Constants.INCOMING_MESSAGE);
                                    adapter.add(newMessage);
                                }


                            } // end for loop
                        } // bundle is null

                    } catch (Exception e) {
                        Log.e("SmsReceiver", "Exception smsReceiver" + e);

                    }
                }
            };
            context.registerReceiver(receiver, filter);
        }
    }

    /**
     * un register the broadcast receiver
     * @param context
     */
    public static void unRegisterReceiver(Context context){

        if(receiver != null) {
            context.unregisterReceiver(receiver);
            receiver = null;
        }
    }

}
