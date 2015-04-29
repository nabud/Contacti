package app.contacts.com.contacts.repositories;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.Contacts.People;

import java.util.ArrayList;
import java.util.List;

import app.contacts.com.contacts.models.Contact;
import app.contacts.com.contacts.utilities.LogUtility;

public class ContactRepository {

    /**
     * Insert contact
     * @param context
     * @param contact
     */
    public static void insertContact(Context context, Contact contact) {

        ContentValues values = new ContentValues();
        values.put(People.NUMBER, contact.getPrimaryPhoneNumber());
        values.put(People.TYPE, Phone.TYPE_CUSTOM);
        values.put(People.LABEL, "");
        values.put(People.NAME, contact.getFullName());
        Uri dataUri = context.getContentResolver().insert(People.CONTENT_URI, values);
        Uri updateUri = Uri.withAppendedPath(dataUri, People.Phones.CONTENT_DIRECTORY);
        values.clear();
        values.put(People.Phones.TYPE, People.TYPE_MOBILE);
        values.put(People.NUMBER, contact.getPrimaryPhoneNumber());
        context.getContentResolver().insert(updateUri, values);
    }

    /**
     * Update contact
     * @param context
     * @param contact
     */
    public static void updateContact(Context context, Contact contact) {

        try {

            ArrayList<ContentProviderOperation> operations = new ArrayList<>();
            operations.add(ContentProviderOperation
                      .newUpdate(ContactsContract.Data.CONTENT_URI)
                      .withSelection(ContactsContract.Data._ID + "=?", new String[]{ contact.getContactId() })
                      .withValue(ContactsContract.CommonDataKinds.Email.DATA, contact.getPrimaryEmailAddress())
                      .build());
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operations);

        } catch (Exception e) {
            LogUtility.error("ContactRepository", e.toString());
        }
    }

    /**
     * Delete contact
     * @param context
     * @param contact
     */
    public static void deleteContact(Context context, Contact contact) {

        try {

            LogUtility.debug("ContactRepository", "Delete contact");
            ArrayList<ContentProviderOperation> operations = new ArrayList<>();
            operations.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                      .withSelection(ContactsContract.Contacts.Data._ID + "=?", new String[]{ contact.getContactId() })
                      .build());
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operations);

        } catch (Exception e) {
            LogUtility.error("ContactRepository", e.toString());
        }
    }


    /**
     * Updates a single contact to the platform contacts provider.
     *
     * @param context the Authenticator Activity context
     * @param resolver the ContentResolver to use
     * @param accountName the account the contact belongs to
     * @param user the sample SyncAdapter contact object.
     * @param rawContactId the unique Id for this rawContact in contacts
     *        provider
     */
//    private static void updateContact(Context context, ContentResolver resolver, String accountName, User user,
//                                      long rawContactId, BatchOperation batchOperation) {
//        Uri uri;
//        String cellPhone = null;
//        String otherPhone = null;
//        String email = null;
//
//        final Cursor c =
//                resolver.query(ContactsContract.Data.CONTENT_URI, DataQuery.PROJECTION,
//                        DataQuery.SELECTION,
//                        new String[] {String.valueOf(rawContactId)}, null);
//        final ContactOperations contactOp =
//                ContactOperations.updateExistingContact(context, rawContactId,
//                        batchOperation);
//
//        try {
//            while (c.moveToNext()) {
//                final long id = c.getLong(DataQuery.COLUMN_ID);
//                final String mimeType = c.getString(DataQuery.COLUMN_MIMETYPE);
//                uri = ContentUris.withAppendedId(Data.CONTENT_URI, id);
//
//                if (mimeType.equals(StructuredName.CONTENT_ITEM_TYPE)) {
//                    final String lastName =
//                            c.getString(DataQuery.COLUMN_FAMILY_NAME);
//                    final String firstName =
//                            c.getString(DataQuery.COLUMN_GIVEN_NAME);
//                    contactOp.updateName(uri, firstName, lastName, user
//                            .getFirstName(), user.getLastName());
//                }
//
//                else if (mimeType.equals(Phone.CONTENT_ITEM_TYPE)) {
//                    final int type = c.getInt(DataQuery.COLUMN_PHONE_TYPE);
//
//                    if (type == Phone.TYPE_MOBILE) {
//                        cellPhone = c.getString(DataQuery.COLUMN_PHONE_NUMBER);
//                        contactOp.updatePhone(cellPhone, user.getCellPhone(),
//                                uri);
//                    } else if (type == Phone.TYPE_OTHER) {
//                        otherPhone = c.getString(DataQuery.COLUMN_PHONE_NUMBER);
//                        contactOp.updatePhone(otherPhone, user.getHomePhone(),
//                                uri);
//                    }
//                }
//
//                else if (Data.MIMETYPE.equals(Email.CONTENT_ITEM_TYPE)) {
//                    email = c.getString(DataQuery.COLUMN_EMAIL_ADDRESS);
//                    contactOp.updateEmail(user.getEmail(), email, uri);
//
//                }
//            } // while
//        } finally {
//            c.close();
//        }
//
//        // Add the cell phone, if present and not updated above
//        if (cellPhone == null) {
//            contactOp.addPhone(user.getCellPhone(), Phone.TYPE_MOBILE);
//        }
//
//        // Add the other phone, if present and not updated above
//        if (otherPhone == null) {
//            contactOp.addPhone(user.getHomePhone(), Phone.TYPE_OTHER);
//        }
//
//        // Add the email address, if present and not updated above
//        if (email == null) {
//            contactOp.addEmail(user.getEmail());
//        }
//
//    }
//
//    /**
//     * Deletes a contact from the platform contacts provider.
//     *
//     * @param context the Authenticator Activity context
//     * @param rawContactId the unique Id for this rawContact in contacts
//     *        provider
//     */
//    private static void deleteContact(Context context, long rawContactId,
//                                      BatchOperation batchOperation) {
//        batchOperation.add(ContactOperations.newDeleteCpo(
//                ContentUris.withAppendedId(RawContacts.CONTENT_URI, rawContactId),
//                true).build());
//    }
//
//    /**
//     * Returns the RawContact id for a sample SyncAdapter contact, or 0 if the
//     * sample SyncAdapter user isn't found.
//     *
//     * @param context the Authenticator Activity context
//     * @param userId the sample SyncAdapter user ID to lookup
//     * @return the RawContact id, or 0 if not found
//     */
//    private static long lookupRawContact(ContentResolver resolver, long userId) {
//        long authorId = 0;
//        final Cursor c =
//                resolver.query(RawContacts.CONTENT_URI, UserIdQuery.PROJECTION,
//                        UserIdQuery.SELECTION, new String[] {String.valueOf(userId)},
//                        null);
//        try {
//            if (c.moveToFirst()) {
//                authorId = c.getLong(UserIdQuery.COLUMN_ID);
//            }
//        } finally {
//            if (c != null) {
//                c.close();
//            }
//        }
//        return authorId;
//    }

}