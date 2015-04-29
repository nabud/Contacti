package app.contacts.com.contacts.common;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.contacts.com.contacts.models.Contact;
import app.contacts.com.contacts.utilities.DateUtil;
import app.contacts.com.contacts.utilities.ImageUtil;
import app.contacts.com.contacts.utilities.LogUtility;
import app.contacts.com.contacts.utilities.StringUtil;

public class PhoneBook {

    /**
     * Get Phone Book contacts
     * @param context
     * @return
     */
    public static List<Contact> getPhoneBookContacts(Context context, boolean limited) {
        return getContactsFromSIMCard(context, getPhoneContacts(context, limited));
    }

    /**
     * Get contact id from phone number
     * @param context
     * @param number
     * @return
     */
    public static String getContactIdFromNumber(Context context, String number) {

        String[] projection = new String[]{ ContactsContract.CommonDataKinds.Phone._ID };
        Uri contactUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(number));
        Cursor cursor = context.getContentResolver().query(contactUri, projection, null, null, null);

        try {

            if (cursor.moveToFirst())
                return cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));

        } catch (Exception e) {
            LogUtility.error("PhoneBook", e.toString());

        } finally {
            cursor.close();
        }
        return null;
    }

    /**
     * Get Phone Contacts
     * @param context
     * @return
     */
    private static List<Contact> getPhoneContacts(Context context, boolean limited) {

        Contact contact;
        List<Contact> contacts = new ArrayList<>();

        String filter = (limited) ? " ASC limit 60" : " ASC";
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                                                           null,
                                                           null,
                                                           null,
                                                           ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME.concat(filter));

        if (cursor.getCount() > 0) {

            List<String> phoneNumbersList;
            List<String> emailAddressList;

            while (cursor.moveToNext()) {

                contact = new Contact();
                contact.setContactId(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
                contact.setFullName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

                String[] names = StringUtil.splitStringFirstOccurance(contact.getFullName(), "\\s");
                contact.setFirstName((!StringUtil.isNullOrEmpty(names[0])) ? names[0] : "");
                contact.setLastName((names.length == 2 && !StringUtil.isNullOrEmpty(names[1])) ? names[1] : "");


                Cursor accountCursor =  context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                                                           null,
                                                                           null,
                                                                           null,
                                                                           ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME.concat(filter));

                while (accountCursor.moveToNext()) {

                    String accountType = accountCursor.getString(accountCursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));
                    if (!StringUtil.isNullOrEmpty(accountType))
                        setAccountType(contact, accountType);
                }

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                // Get picture
                String image_uri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                if (image_uri != null) {

                    try {

                        contact.setPicture(ImageUtil.GetBitmapClippedCircle(MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(image_uri))));

                    } catch (IOException e) {
                        LogUtility.error("PhoneBook", e.toString());
                    }
                }

                // Query and loop for every phone number of the contact
                if (hasPhoneNumber > 0) {

                    phoneNumbersList = new ArrayList<>();
                    Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                                                            null,
                                                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID.concat(" = ?"),
                                                                            new String[]{ contact.getContactId() },
                                                                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME.concat(filter));

                    try {

                        while (phoneCursor.moveToNext()) {
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            if (StringUtil.isNullOrEmpty(contact.getPrimaryPhoneNumber()))
                                contact.setPrimaryPhoneNumber(StringUtil.removeCharactersFromPhoneNumber(phoneNumber));
                            phoneNumbersList.add(StringUtil.cleanAndFormatPhoneNumber(phoneNumber));

                            contact.setLastContactedDate(DateUtil.millisecondToReadableDate(Long.parseLong(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED))),
                                                                                            DateUtil.Formats.CALENDAR_DATE_TIME_FORMAT));

                            contact.setLastUpdatedDate(DateUtil.millisecondToReadableDate(Long.parseLong(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP))),
                                                                                          DateUtil.Formats.CALENDAR_DATE_TIME_FORMAT));

//                            String accountType =  cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));
//                            if (!StringUtil.isNullOrEmpty(accountType))
//                                setAccountType(contact, accountType);
                        }
                        contact.setPhoneNumbers(phoneNumbersList);

                    } catch (Exception e) {
                        LogUtility.error("PhoneBook", e.toString());

                    } finally {
                        phoneCursor.close();
                    }

                }

                // Query and loop for every email of the contact
                Cursor emailCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                                                        null,
                                                                        ContactsContract.CommonDataKinds.Email.CONTACT_ID.concat(" = ?"),
                                                                        new String[]{ contact.getContactId() },
                                                                        ContactsContract.CommonDataKinds.Email.DISPLAY_NAME.concat(filter));

                try {

                    emailAddressList = new ArrayList<>();
                    while (emailCursor.moveToNext()) {
                        if (StringUtil.isNullOrEmpty(contact.getPrimaryEmailAddress()))
                            contact.setPrimaryEmailAddress(emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
                        emailAddressList.add(emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
                    }
                    contact.setEmailAddresses(emailAddressList);

                } catch (Exception e) {
                    LogUtility.error("PhoneBook", e.toString());

                } finally {
                    emailCursor.close();
                }


                Cursor skypeCursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                                                                        null,
                                                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID.concat(" = ?"),
                                                                        new String[]{ contact.getContactId() },
                                                                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME.concat(filter));

                while (skypeCursor.moveToNext()) {
                    if (skypeCursor.getInt(skypeCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL)) == ContactsContract.CommonDataKinds.Im.PROTOCOL_SKYPE) {
                        contact.setAccountType(Constants.ACOUNT_TYPE_SKYPE);
                        contact.setAccountUserName(skypeCursor.getString(skypeCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA)));
                    }
                }
                contacts.add(contact);
            }
            cursor.close();
        }
        return contacts;

//        Contact contact;
//        List<Contact> contacts = new ArrayList<>();
//
//        Cursor mainCursor =  context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
//        System.out.println("Count = " + mainCursor.getCount());
//        while (mainCursor.moveToNext()) {
//
//            String contactId = mainCursor.getString(mainCursor.getColumnIndex(BaseColumns._ID));
//            int hasPhone = mainCursor.getInt(mainCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
//
//            contact = new Contact();
//            contact.setFullName(mainCursor.getString(mainCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
//
//            String[] names = StringUtil.splitStringFirstOccurance(contact.getFullName(), "\\s");
//            contact.setFirstName((!StringUtil.isNullOrEmpty(names[0])) ? names[0] : "");
//            contact.setLastName((names.length == 2 && !StringUtil.isNullOrEmpty(names[1])) ? names[1] : "");
//
//            String accountLoginName = mainCursor.getString(mainCursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME));
//            contact.setAccountLoginName((accountLoginName.contains("contact.phone") ? "" : accountLoginName));
//
//            String accountType = mainCursor.getString(mainCursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));
//            if (!StringUtil.isNullOrEmpty(accountType))
//                setAccountType(contact, accountType);
//
//            // Get picture
//            String image_uri = mainCursor.getString(mainCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
//            if (image_uri != null) {
//
//                try {
//                    contact.setPicture(MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(image_uri)));
//                } catch (IOException e) {
//                    LogUtility.error("PhoneBook", e.toString());
//                }
//            }
//            contacts.add(contact);
//
//
//            if (hasPhone == Constants.TRUE) {
//
//                Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                                                                        null,
//                                                                        ContactsContract.Contacts._ID + " = ?",
//                                                                        new String[] { contactId },
//                                                                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
//                List<String> phoneNumbers;
//                while (phoneCursor.moveToNext()) {
//
//                    phoneNumbers = new ArrayList<>();
//                    phoneNumbers.add(StringUtil.cleanAndFormatPhoneNumber(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));
//                    contact.setPhoneNumbers(phoneNumbers);
//                    contact.setLastContactedDate(DateUtil.millisecondToReadableDate(Long.parseLong(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED))),
//                                                                                    DateUtil.Formats.CALENDAR_DATE_TIME_FORMAT));
//
//                    contact.setLastUpdatedDate(DateUtil.millisecondToReadableDate(Long.parseLong(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP))),
//                                                                                  DateUtil.Formats.CALENDAR_DATE_TIME_FORMAT));
//                }
//                phoneCursor.close();
//            }
//
//            //TODO problem getting an email address
//            // Get email address
//            Cursor emailCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
//                                                                    null,
//                                                                    ContactsContract.Contacts._ID + " = ?",
//                                                                    new String[] { contactId },
//                                                                    ContactsContract.CommonDataKinds.Email.DISPLAY_NAME + " ASC");
//
//            while (emailCursor.moveToNext()) {
//                System.out.println("===> " + emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
//                String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
//                contact.setEmailAddress(!StringUtil.isNullOrEmpty(email) ? email : "");
//            }
//            emailCursor.close();
//        }
//        mainCursor.close();
//
//        return contacts;
    }

    /**
     * Get Contacts from SIM card
     * @param context
     * @param contacts
     * @return
     */
    private static List<Contact> getContactsFromSIMCard(Context context, List<Contact> contacts) {

        try {

            String name;
            String phoneNumber;

            Uri simUri = Uri.parse("content://icc/adn");
            Cursor cursorSim = context.getContentResolver().query(simUri,null,null,null,null);

            Log.i("PhoneBook", "total: " + cursorSim.getCount());

            while (cursorSim.moveToNext()) {
                name =cursorSim.getString(cursorSim.getColumnIndex("name"));
                phoneNumber = cursorSim.getString(cursorSim.getColumnIndex("number"));
                phoneNumber.replaceAll("\\D", "");
                phoneNumber.replaceAll("&", "");
                name = name.replace("|","");

                //TODO figure out how to get picture
//                contacts.add(new Contact(name, phoneNumber));
                LogUtility.debug("PhoneContact", "name: " + name + " phone: " + phoneNumber);
            }

        } catch(Exception e) {
            Log.e("PhoneBook", e.toString());
        }
        return contacts;
    }

    /**
     * Set account type
     * @param contact
     * @param accountType
     * @return
     */
    private static Contact setAccountType(Contact contact, String accountType) {

        if (accountType.contains(Constants.PHONE))
            contact.setAccountType(Constants.ACOUNT_TYPE_PHONE);

        if (accountType.contains(Constants.LINKEDIN))
            contact.setAccountType(Constants.ACOUNT_TYPE_LINKEDIN);

        else if (accountType.contains(Constants.SKYPE))
            contact.setAccountType(Constants.ACOUNT_TYPE_SKYPE);

        if (accountType.contains(Constants.GOOGLE))
            contact.setAccountType(Constants.ACOUNT_TYPE_GOOGLE);

        return contact;
    }
}
