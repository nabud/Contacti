package app.contacts.com.contacts.common;

import android.telephony.SmsManager;
import java.util.List;
import app.contacts.com.contacts.models.Contact;
import app.contacts.com.contacts.utilities.LogUtility;

public class SMSManager {

    /**
     * Send sms message
     * @param message
     * @param phoneNumber
     */
    public static void sendSMSMessage(String message, String phoneNumber) {

        LogUtility.debug("SMSManager", "Send text message to: " + phoneNumber);
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, null,null);
    }

    /**
     * Send sms message
     * @param message
     * @param phoneNumbers
     */
    public static void sendSMSMessage(String message, String... phoneNumbers) {

        for (String phoneNumber: phoneNumbers) {
            LogUtility.debug("SMSManager", "Send text message to: " + phoneNumber);
            SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, null, null);
        }
    }

    /**
     * Send sms message
     * @param message
     * @param contacts
     */
    public static void sendSMSMessage(String message, List<Contact> contacts) {

        for (Contact contact: contacts) {
            LogUtility.debug("SMSManager", "Send text message to: " + contact.getPhoneNumbers());
//            SmsManager.getDefault().sendTextMessage(contact.getPhoneNumbers(), null, message, null, null);
        }
    }
}
