package app.contacts.com.contacts.common;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import app.contacts.com.contacts.models.CallLogRecords;
import app.contacts.com.contacts.utilities.DateUtil;
import app.contacts.com.contacts.utilities.ImageUtil;
import app.contacts.com.contacts.utilities.LogUtility;
import app.contacts.com.contacts.utilities.StringUtil;

/**
 * Created by zeyad on 4/11/15.
 */
public class CallLogs {

    /**
     * Get call log records
     * @param context
     * @return
     */
    public static List<CallLogRecords> getCallLogRecords(Context context, boolean limited) {

        String filter = (limited) ? " DESC LIMIT 60" : " DESC";
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                                                           null,
                                                           null,
                                                           null,
                                                           CallLog.Calls.DATE + filter);

        int name = cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME);
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

        CallLogRecords record;
        List<CallLogRecords> logRecords = new ArrayList<>();

        try {

            String phoneNumber;
            while (cursor.moveToNext()) {

                record = new CallLogRecords();
                record.setCallerName(StringUtil.returnEmptyIfNull(cursor.getString(name), "Unknown"));

                phoneNumber = cursor.getString(number);
                record.setCallNumber(phoneNumber);
                record.setCallDisplayNumber(StringUtil.cleanAndFormatPhoneNumber(phoneNumber, "Unknown"));

                record.setCallDayTime(DateUtil.formatDateString(new Date(Long.valueOf(StringUtil.returnEmptyIfNull(cursor.getString(date)))).toString(),
                        DateUtil.Formats.CALL_DATE_FORMAT, DateUtil.Formats.DAY_DATE_TIME_FORMAT));

                record.setCallDuration(DateUtil.timeConversion(cursor.getInt(duration)));

                switch (cursor.getInt(type)) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        record.setCallType(Constants.OUTGOING_CALL);
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        record.setCallType(Constants.INCOMING_CALL);
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        record.setCallType(Constants.MISSED_CALL);
                        break;
                    default:
                        record.setCallType(0);
                        break;
                }

//                Uri uri = getPhotoUri(context, Long.parseLong(PhoneBook.getContactIdFromNumber(context, record.getCallNumber())));
//                if (uri != null)
//                    record.setPicture(uri);

//                System.out.println(PhoneBook.getContactIdFromNumber(context, record.getCallNumber()));
                System.out.println(record.getCallNumber());
                record.setPicture(getPhotoUri(context, PhoneBook.getContactIdFromNumber(context, record.getCallNumber())));
                logRecords.add(record);
            }

        } catch (Exception e) {
            LogUtility.error("CallLogs", e.toString());

        } finally {
            if( cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return logRecords;
    }
//
//    public static CallLogRecords getContactPicture(Context context, CallLogRecords record) {
//
////        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
////        if (cursor.moveToNext()) {
//
//        System.out.println(record.getCallNumber());
//            Uri contactPhotoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(PhoneBook.getContactIdFromNumber(context, record.getCallNumber())));
//            record.setPicture(BitmapFactory.decodeStream(ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), contactPhotoUri)));
////        }
//        return record;
//    }


//    public static Uri getPhotoUri(Context context, long contactId) {
//
//        System.out.println(contactId);
//
//        try {
//            Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
//                                                               null,
//                                                               ContactsContract.Data.CONTACT_ID + "=" + contactId,
////                    + " AND "
////                    + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'",
//                                                               null,
//                                                               null,
//                                                               null);
//
//            if (cursor != null) {
//                if (!cursor.moveToFirst()) {
//                    return null; // no photo
//                }
//            } else {
//                return null; // error in cursor process
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
//        return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
//    }

    public static Bitmap getPhotoUri(Context context, String contactId) {

        LogUtility.debug("CallLogs", "Get photoUri for contactId: " + contactId);
        Bitmap bitmap =null;

        if (!StringUtil.isNullOrEmpty(contactId)) {
            try {
                Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + "=" + contactId + " AND " + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null, null);

                if (cursor != null) {
                    if (!cursor.moveToFirst()) {
                        return null; // no photo
                    }
                } else {
                    return null; // error in cursor process
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            try {

                System.out.println(contactId);
                long contact = Long.parseLong(contactId);
                Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contact);
                person = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                bitmap = ImageUtil.GetBitmapClippedCircle(MediaStore.Images.Media.getBitmap(context.getContentResolver(), person));

            } catch (Exception e) {

            }
        }
        return bitmap;
    }
}
