package app.contacts.com.contacts.common;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import app.contacts.com.contacts.R;
import app.contacts.com.contacts.models.Contact;
import app.contacts.com.contacts.services.SkypeActions;
import app.contacts.com.contacts.utilities.LogUtility;
import app.contacts.com.contacts.utilities.PopupUtility;

public class Communications {

    /**
     * Call phone number
     * @param context
     * @param contact
     */
    public static void call(final Context context, Contact contact) {

        try {

            if (contact.getPhoneNumbers().size() > 0 ) {

                if (contact.getPhoneNumbers().size() == 1)
                    callNumber(context, contact.getPrimaryPhoneNumber());

                else {

                    // Show a listview of phone numbers to call
                    // TODO user dynamic listview instead
                    final PopupWindow popupWindow = PopupUtility.getPopupWindowWithBlur(context, R.layout.simple_listview_layout, R.style.Animation_center_out_center_in);
                    popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.CENTER, 0, 0);

                    final ListView listView = (ListView) popupWindow.getContentView().findViewById(R.id.listView1);
                    ArrayAdapter<String> phonesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, contact.getPhoneNumbers());
                    listView.setAdapter(phonesAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            callNumber(context, (String) listView.getItemAtPosition(position));
                        }
                    });

                    popupWindow.getContentView().findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                }

            } else {
                //TODO
                // Prompt user to add a phone number

            }

        } catch (ActivityNotFoundException activityException) {
            LogUtility.error("ContactCircleMenuActions", activityException.toString());
        }
    }

    /**
     * Call number
     * @param context
     * @param number
     */
    public static void callNumber(Context context, String number) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        context.startActivity(callIntent);
    }

    /**
     * Text
     * @param context
     * @param contact
     */
    public static void text(final Context context, final Contact contact) {

        if (contact.getPhoneNumbers().size() > 0 ) {

            if (contact.getPhoneNumbers().size() == 1)
                textPhoneNumber(context, contact.getPrimaryPhoneNumber());

            else {

                // Show a listview of phone numbers to choose from
                // TODO user dynamic listview instead
                final PopupWindow popupWindow = PopupUtility.getPopupWindowWithBlur(context, R.layout.simple_listview_layout, R.style.Animation_center_out_center_in);
                popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.CENTER, 0, 0);

                final ListView listView = (ListView) popupWindow.getContentView().findViewById(R.id.listView1);
                ArrayAdapter<String> phonesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, contact.getPhoneNumbers());
                listView.setAdapter(phonesAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        textPhoneNumber(context, (String) listView.getItemAtPosition(position));
                    }
                });

                popupWindow.getContentView().findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

            }

        } else {
            //TODO
            // Prompt user to add a phone number

        }
    }

    /**
     * Text number
     * @param context
     * @param primaryPhoneNumber
     */
    public static void textPhoneNumber(Context context, final String primaryPhoneNumber) {

        final PopupWindow textWindow = PopupUtility.getPopupWindow(context, R.layout.text_layout, R.style.Animation_center_out_center_in);
        textWindow.showAtLocation(textWindow.getContentView(), Gravity.CENTER, 0, 0);

        textWindow.getContentView().findViewById(R.id.send_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSManager.sendSMSMessage(((TextView) textWindow.getContentView().findViewById(R.id.message)).getText().toString(), primaryPhoneNumber);
                textWindow.dismiss();
            }
        });
        PopupUtility.dismissPopup(textWindow, R.id.close);
    }

    public static void sendEmail(final Context context, final Contact contact) {

        if (contact.getEmailAddresses().size() > 0 ) {

            if (contact.getEmailAddresses().size() == 1)
                email(context, contact.getPrimaryEmailAddress());

            else {

                // Show a listview of phone numbers to choose from
                // TODO user dynamic listview instead
                final PopupWindow popupWindow = PopupUtility.getPopupWindowWithBlur(context, R.layout.simple_listview_layout, R.style.Animation_center_out_center_in);
                popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.CENTER, 0, 0);

                final ListView listView = (ListView) popupWindow.getContentView().findViewById(R.id.listView1);
                ArrayAdapter<String> phonesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, contact.getEmailAddresses());
                listView.setAdapter(phonesAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        email(context, contact.getPrimaryEmailAddress());
                    }
                });

                popupWindow.getContentView().findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

            }

        } else {
            //TODO
            // Prompt user to add an email address

        }

    }

    private static void email(Context context, String emailAddress) {


        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {

            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            LogUtility.debug("Communications", "Sending email");
        } catch (android.content.ActivityNotFoundException ex) {
        }
    }

    /**
     * Set record actions
     * @param context
     * @param view
     * @param phoneNumber
     */
    public static void setRecordActions(final Context context, View view, final String phoneNumber) {

        // call
        (view.findViewById(R.id.call)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Communications.callNumber(context, phoneNumber);
            }
        });

        // Text
        (view.findViewById(R.id.text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Communications.textPhoneNumber(context, phoneNumber);
            }
        });

    }

    /**
     * Call via Skype
     * @param context
     * @param username
     */
    public static void callViaSkype(final Context context, final String username) {

        LogUtility.debug("Communication", "Calling with Skype");
        SkypeActions.initiateSkypeUri(context, "skype:".concat(username).concat("?call"), username);
    }

}
