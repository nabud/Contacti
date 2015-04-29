package app.contacts.com.contacts.services;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import app.contacts.com.contacts.R;
import app.contacts.com.contacts.adapters.ContactsGridViewAdapter;
import app.contacts.com.contacts.common.Communications;
import app.contacts.com.contacts.common.Constants;
import app.contacts.com.contacts.common.circular_menu.CircleLayout;
import app.contacts.com.contacts.models.Contact;
import app.contacts.com.contacts.repositories.ContactRepository;
import app.contacts.com.contacts.utilities.ImageUtil;
import app.contacts.com.contacts.utilities.LogUtility;
import app.contacts.com.contacts.utilities.PopupUtility;
import app.contacts.com.contacts.utilities.StringUtil;

public class ContactActions {

    /**
     * Set contact actions
     * @param context
     * @param gridView
     * @param contactsAdapter
     */
    public static void setContactActions(Context context, GridView gridView, ContactsGridViewAdapter contactsAdapter) {

        contactOnShortClick(context, gridView, contactsAdapter);
        contactOnLongClick(context, gridView, contactsAdapter);
    }

    /**
     * Contact on short click
     * @param context
     * @param gridView
     * @param contactsAdapter
     */
    private static void contactOnShortClick(final Context context, final GridView gridView, final ContactsGridViewAdapter contactsAdapter) {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                LogUtility.debug("ContactActions", "Contact on short click");
                showContactDetails(context, (Contact) gridView.getItemAtPosition(position), contactsAdapter);
            }
        });
    }

    /**
     * Show contact details
     * @param context
     * @param contact
     * @param contactsAdapter
     */
    public static void showContactDetails(final Context context, final Contact contact, ContactsGridViewAdapter contactsAdapter) {

        PopupWindow contactDetailsWindow = PopupUtility.getPopupWindowWithBlur(context, R.layout.contact_details, R.style.Animation_center_out_center_in);
        contactDetailsWindow.showAtLocation(contactDetailsWindow.getContentView(), Gravity.CENTER, 0, 0);

        final View view = contactDetailsWindow.getContentView();
        ((ImageView) view.findViewById(R.id.picture)).setImageBitmap((contact.getPicture() != null) ? contact.getPicture() : ImageUtil.getBitmapFromRes(context, R.drawable.default_contact));
        ((TextView) view.findViewById(R.id.full_name)).setText(contact.getFullName());
        ((TextView) view.findViewById(R.id.last_contacted)).setText(contact.getLastContactedDate());

        // Phone/call/text
        if (contact.getPhoneNumbers() != null) {
            StringBuilder builder = new StringBuilder();
            for (String phoneNumber : contact.getPhoneNumbers())
                builder.append(phoneNumber).append("\n");
            ((TextView) view.findViewById(R.id.phone_Number)).setText(builder.toString());

            // call
            ImageView call = ((ImageView) view.findViewById(R.id.call));
            call.setVisibility(View.VISIBLE);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Communications.call(context, contact);
                }
            });

            // Text
            ImageView text = ((ImageView) view.findViewById(R.id.text));
            text.setVisibility(View.VISIBLE);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Communications.text(context, contact);
                }
            });

        } else
            (view.findViewById(R.id.phone_Number_layout)).setVisibility(View.GONE);

        // Email
        if (!StringUtil.isNullOrEmpty(contact.getPrimaryEmailAddress())) {
            ImageView email = ((ImageView) view.findViewById(R.id.email));
            ((TextView) view.findViewById(R.id.email_address)).setText(contact.getPrimaryEmailAddress());
            email.setVisibility(View.VISIBLE);
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Communications.sendEmail(context, contact);
                }
            });

        } else
            (view.findViewById(R.id.email_address_layout)).setVisibility(View.GONE);

        if ((contact.getAccountType() == Constants.ACOUNT_TYPE_SKYPE) && !StringUtil.isNullOrEmpty(contact.getAccountUserName())) {
            ImageView skype = ((ImageView) view.findViewById(R.id.skype));
            skype.setVisibility(View.VISIBLE);
            skype.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtility.debug("ContactActions", "Clicking on skype button");
                    Communications.callViaSkype(context, contact.getAccountUserName());
                }
            });

        } else
            (view.findViewById(R.id.email_address_layout)).setVisibility(View.GONE);

        PopupUtility.dismissPopup(contactDetailsWindow, R.id.close);

        updateContact(context, view, contact);
        deleteContact(context, contactDetailsWindow, view, contact, contactsAdapter);
    }

    /**
     * Update a contact
     * @param context
     * @param view
     * @param contact
     */
    private static void updateContact(final Context context, final View view, final Contact contact) {


    }

    /**
     * Deleting a contact
     * @param context
     * @param contactDetailsWindow
     * @param view
     * @param contact
     * @param contactsAdapter
     */
    public static void deleteContact(final Context context, final PopupWindow contactDetailsWindow, final View view, final Contact contact, final ContactsGridViewAdapter contactsAdapter) {

        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final PopupWindow window = PopupUtility.showConfirmActionWindowWithBlur(context, R.drawable.warning, "Delete a contact", "Contact will be deleted permanently from your phone?\n are you sure you want to perform this action?");
                window.showAtLocation(window.getContentView(), Gravity.CENTER, 0, 0);

                window.getContentView().findViewById(R.id.proceed).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContactRepository.deleteContact(context, contact);
                        contactsAdapter.remove(contact);
                        contactsAdapter.notifyDataSetChanged();
                        window.dismiss();

                        if (contactDetailsWindow != null)
                            contactDetailsWindow.dismiss();
                    }
                });
            }
        });
    }

    /**
     * Contact on long click
     * @param context
     * @param gridView
     * @param contactsAdapter
     */
    private static void contactOnLongClick(final Context context, final GridView gridView, final ContactsGridViewAdapter contactsAdapter) {

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                LogUtility.debug("ContactActions", "Contact on short click");

                final PopupWindow popupWindow = PopupUtility.getPopupWindowWithBlur(context, R.layout.circle_menu, R.style.AnimationMarkerPopup);
                popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.CENTER, 0, 0);
                CircleLayout circleMenu = (CircleLayout) popupWindow.getContentView().findViewById(R.id.main_circle_layout);

                Contact contact = (Contact) gridView.getItemAtPosition(position);
                ImageView circularPicture = ((ImageView) popupWindow.getContentView().findViewById(R.id.circular_picture));
                circularPicture.setImageBitmap((contact.getPicture() != null ? contact.getPicture() : ImageUtil.getBitmapFromRes(context, R.drawable.default_contact)));
                ContactCircleMenuActions.circleMenuActions(context, popupWindow, circleMenu, contact, contactsAdapter);

                circularPicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                return true;
            }
        });
    }
}
