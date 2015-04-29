package app.contacts.com.contacts.common;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.contacts.com.contacts.models.Message;
import app.contacts.com.contacts.utilities.DateUtil;
import app.contacts.com.contacts.utilities.LogUtility;
import app.contacts.com.contacts.utilities.StringUtil;

public class TextMessages {

    /**
     * Get text messages
     * @param context
     * @return
     */
    public static Map<String, List<Message>> getTextMessages(Context context) {

        Map<String, List<Message>> messages = new HashMap<>();
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, "date DESC");

        try {

            if (cursor.moveToFirst()) {

                String phoneNumber;
                Message message;
                List<Message> messagesList;

                do {

                    phoneNumber = cursor.getString(cursor.getColumnIndex("address"));
                    message = new Message(
                                            getContactName(context, phoneNumber),
                                            phoneNumber,
                                            cursor.getString(cursor.getColumnIndex("body")),
                                            DateUtil.formatDateString(new Date(Long.valueOf(StringUtil.returnEmptyIfNull(cursor.getString(cursor.getColumnIndex("date"))))).toString(),
                                            DateUtil.Formats.CALL_DATE_FORMAT, DateUtil.Formats.DAY_DATE_TIME_FORMAT));

                    if (messages.containsKey(phoneNumber))
                        messages.get(phoneNumber).add(message);

                    else {
                        messagesList = new ArrayList<>();
                        messagesList.add(message);
                        messages.put(phoneNumber, messagesList);
                    }

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            LogUtility.error("TextMessages", e.toString());
        } finally {
            cursor.close();
        }
        return messages;
    }

    /**
     * Get the last message for each contact
     * @param context
     * @return
     */
    public static Map<String,Message> getMessages(Context context, boolean limited) {

        Map<String, Message> messages = new HashMap<>();

        String filter = (limited) ? "date ASC limit 60" : "date ASC";
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms"), null, null, null, filter);

        try {

            if (cursor.moveToFirst()) {

                String phoneNumber;
                Message message;

                do {

                    phoneNumber = cursor.getString(cursor.getColumnIndex("address"));

                    if (!messages.containsKey(phoneNumber)) {
                        message = new Message(
                                getContactName(context, phoneNumber),
                                phoneNumber,
                                cursor.getString(cursor.getColumnIndex("body")),
                                DateUtil.formatDateString(new Date(Long.valueOf(StringUtil.returnEmptyIfNull(cursor.getString(cursor.getColumnIndex("date"))))).toString(),
                                        DateUtil.Formats.CALL_DATE_FORMAT, DateUtil.Formats.DAY_DATE_TIME_FORMAT));

                        messages.put(phoneNumber, message);
                    }

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            LogUtility.error("TextMessages", e.toString());
        } finally {
            cursor.close();
        }
        return messages;
    }

    /**
     * Get messages for phone number
     * @param context
     * @param phoneNumber
     * @return
     */
    public static Map<String, List<Message>> getMessagesForPhoneNumber(Context context, String phoneNumber) {

        Cursor cursor1 = context.getContentResolver().query(Uri.parse("content://sms"), new String[] { "_id", "thread_id", "address", "person", "date","body", "type" }, null, null, null);
        String[] columns = new String[] { "address", "person", "date", "body","type" };

        Map<String, List<Message>> messages = new HashMap<>();
        if (cursor1.getCount() > 0) {

            Message message;
            List<Message> messagesList;
            while (cursor1.moveToNext()){
                String address = cursor1.getString(cursor1.getColumnIndex(columns[0]));

                if(!StringUtil.isNullOrEmpty(address) && address.equalsIgnoreCase(phoneNumber)){

                    message = new Message(
                            cursor1.getString(cursor1.getColumnIndex(columns[1])),
                            address,
                            cursor1.getString(cursor1.getColumnIndex(columns[3])),
                            DateUtil.formatDateString(new Date(Long.valueOf(StringUtil.returnEmptyIfNull(cursor1.getString(cursor1.getColumnIndex(columns[2]))))).toString(),
                                                      DateUtil.Formats.CALL_DATE_FORMAT, DateUtil.Formats.DAY_DATE_TIME_FORMAT),
                            cursor1.getInt(cursor1.getColumnIndex(columns[4])));

                    if (messages.containsKey(phoneNumber))
                        messages.get(phoneNumber).add(message);

                    else {
                        messagesList = new ArrayList<>();
                        messagesList.add(message);
                        messages.put(phoneNumber, messagesList);
                    }
                }

            }
        }
        return messages;
    }

    /**
     * Get contact name
     * @param context
     * @param phoneNumber
     * @return
     */
    private static String getContactName(Context context, String phoneNumber) {

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = context.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        if (cursor == null) return "";

        String contactName = null;
        if (cursor.moveToFirst())
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return contactName;
    }
}
