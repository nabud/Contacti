package app.contacts.com.contacts.services;

import android.accounts.Account;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

import a_vcard.android.provider.Contacts;
import a_vcard.android.syncml.pim.vcard.ContactStruct;
import a_vcard.android.syncml.pim.vcard.VCardComposer;
import app.contacts.com.contacts.R;
import app.contacts.com.contacts.adapters.ContactsGridViewAdapter;
import app.contacts.com.contacts.common.Communications;
import app.contacts.com.contacts.common.SMSManager;
import app.contacts.com.contacts.common.circular_menu.CircleLayout;
import app.contacts.com.contacts.models.Contact;
import app.contacts.com.contacts.utilities.LogUtility;
import app.contacts.com.contacts.utilities.PopupUtility;

public class ContactCircleMenuActions  {

    public static void circleMenuActions(final Context context, final PopupWindow popupWindow, CircleLayout circleMenu, final Contact contact, final ContactsGridViewAdapter contactsAdapter){

        circleMenu.setOnItemSelectedListener(new CircleLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, String name) {
//                preformAction(context, popupWindow, view, contact);
            }
        });

        circleMenu.setOnItemClickListener(new CircleLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String name) {
                preformAction(context, popupWindow, view, contact, contactsAdapter);
            }
        });

        circleMenu.setOnRotationFinishedListener(new CircleLayout.OnRotationFinishedListener() {
            @Override
            public void onRotationFinished(View view, String name) {
                Animation animation = new RotateAnimation(0, 360, view.getWidth() / 2, view.getHeight() / 2);
                animation.setDuration(250);
                view.startAnimation(animation);
            }
        });

        circleMenu.setOnCenterClickListener(new CircleLayout.OnCenterClickListener() {
            @Override
            public void onCenterClick() {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * CircleMenu actions
     * @param popupWindow
     * @param view
     * @param contact
     * @param contactsAdapter
     */
    private static void preformAction(Context context, PopupWindow popupWindow, View view, Contact contact, ContactsGridViewAdapter contactsAdapter){

        switch (view.getId()) {

            case R.id.call_image:
                Communications.call(context, contact);
                popupWindow.dismiss();
                break;

            case R.id.text_image:
                Communications.text(context, contact);
                break;

            case R.id.details_image:
                ContactActions.showContactDetails(context, contact, contactsAdapter);
                break;

            case R.id.block_image:
                // Handle key selection
                break;

            case R.id.share_contact_image:
                share(context, contact);
                break;

            case R.id.delete_image:
                ContactActions.deleteContact(context, null, view, contact, contactsAdapter);
                break;
        }
    }


    private static void share(Context context, Contact contact) {

        try {
//            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("example.vcard"), "UTF-8");

            BufferedWriter writer = new BufferedWriter(new FileWriter(context.getFilesDir() + "example.vcf"));
            VCardComposer composer = new VCardComposer();

            //create a contact
            ContactStruct contact1 = new ContactStruct();
            contact1.name = "Neo";
            contact1.company = "The Company";
            contact1.addPhone(Contacts.Phones.TYPE_MOBILE, "+123456789", null, true);

            //create vCard representation
            String vcardString = composer.createVCard(contact1, VCardComposer.VERSION_VCARD30_INT);

            //write vCard to the output stream
            writer.write(vcardString);
            writer.write("\n"); //add empty lines between contacts

            System.out.println("vcardString: " + vcardString);
            writer.close();


                    Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM,  vcardString);
        sendIntent.setType("text/x-vcard");
        context.startActivity(sendIntent);


        } catch (Exception e) {
            LogUtility.error("", e.toString());
        }

//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_STREAM,  Uri.parse(.toURL().toString());
//        sendIntent.setType("text/x-vcard");
//        context.startActivity(sendIntent);

    }
}
