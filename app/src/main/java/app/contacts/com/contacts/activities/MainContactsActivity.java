package app.contacts.com.contacts.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.contacts.com.contacts.R;
import app.contacts.com.contacts.adapters.CallLogsArrayAdapter;
import app.contacts.com.contacts.adapters.ContactsGridViewAdapter;
import app.contacts.com.contacts.adapters.TextMessagesArrayAdapter;
import app.contacts.com.contacts.common.CallLogs;
import app.contacts.com.contacts.common.Constants;
import app.contacts.com.contacts.common.Duplicates;
import app.contacts.com.contacts.common.PhoneBook;
import app.contacts.com.contacts.common.Sort;
import app.contacts.com.contacts.common.TextMessages;
import app.contacts.com.contacts.models.CallLogRecords;
import app.contacts.com.contacts.models.Contact;
import app.contacts.com.contacts.models.Message;
import app.contacts.com.contacts.services.ContactActions;
import app.contacts.com.contacts.services.LeftSidebarActions;
import app.contacts.com.contacts.services.ManageCallLogs;
import app.contacts.com.contacts.services.ManageTextMessages;
import app.contacts.com.contacts.utilities.LogUtility;
import app.contacts.com.contacts.utilities.ThemeUtil;

public class MainContactsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ThemeUtil.onActivityCreateSetTheme(this, Constants.WHITE_THEME);
        setContentView(R.layout.activity_main_contacts);

        final List<Contact> contacts = PhoneBook.getPhoneBookContacts(MainContactsActivity.this, true);
        GridView gridView = (GridView) findViewById(R.id.gridView1);
        final ContactsGridViewAdapter contactsAdapter = new ContactsGridViewAdapter(this, R.layout.contacts_grid_view_cell, contacts);
        gridView.setAdapter(contactsAdapter);

        new Thread(new Runnable() {

            @Override
            public void run() {

                final List<Contact> contacts = PhoneBook.getPhoneBookContacts(MainContactsActivity.this, false);
                MainContactsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    // TODO there is no clear in here
                        contactsAdapter.clear();
                        contactsAdapter.addAll(contacts);
                        contactsAdapter.notifyDataSetChanged();

                        for (Contact contact: Duplicates.findDuplicatesByMultipleFields(contacts, Constants.FIELDS[Constants.FIRST_NAME]))
                            System.out.println(contact.getFullName());
                    }
                });
            }
        }).start();

        final List<Message> messages = new ArrayList<>();
        for(Map.Entry<String,Message> entry: TextMessages.getMessages(MainContactsActivity.this, true).entrySet())
            messages.add(entry.getValue());
        final TextMessagesArrayAdapter messagesAdapter = new TextMessagesArrayAdapter(MainContactsActivity.this, R.layout.message_logs_cells, new ArrayList<Message>());

        final List<CallLogRecords> callLogs = CallLogs.getCallLogRecords(MainContactsActivity.this, true);
        final CallLogsArrayAdapter callLogsAdapter = new CallLogsArrayAdapter(MainContactsActivity.this, R.layout.call_logs_cells, callLogs);

        setUpTabs(callLogsAdapter, messagesAdapter, contactsAdapter.getContacts());

        new Thread(new Runnable() {

            @Override
            public void run() {

                final List<Message> messages = new ArrayList<>();
                for(Map.Entry<String,Message> entry: TextMessages.getMessages(MainContactsActivity.this, false).entrySet())
                    messages.add(entry.getValue());

                MainContactsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messagesAdapter.clear();
                        messagesAdapter.addAll(messages);
                        messagesAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

        new Thread(new Runnable() {

            @Override
            public void run() {

                final List<CallLogRecords> callLogs = CallLogs.getCallLogRecords(MainContactsActivity.this, false);

                MainContactsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callLogsAdapter.clear();
                        callLogsAdapter.addAll(callLogs);
                        callLogsAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

        sort(gridView, contacts, contactsAdapter);
        LeftSidebarActions.showLeftMenu(this, contacts);
        ContactActions.setContactActions(this, gridView, contactsAdapter);




//        NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_launcher) // notification icon
//                .setContentTitle("Notification!") // title for notification
//                .setContentText("Hello word") // message for notification
//                .setAutoCancel(true); // clear notification after click
//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
//        mBuilder.setContentIntent(pi);
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(0, mBuilder.build());
//
//
//
    }
//
//    private void buildNotification( Notification.Action action ) {
//        Notification.MediaStyle style = new Notification.MediaStyle();
//
//        Intent intent = new Intent( getApplicationContext(), MediaPlayerService.class );
//        intent.setAction( ACTION_STOP );
//        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
//        Notification.Builder builder = new Notification.Builder( this )
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setContentTitle( "Lock Screen Media example Service" )
//                .setContentText( "Artist Name" )
//                .setDeleteIntent( pendingIntent )
//                .setStyle(style);
//        style.setShowActionsInCompactView(0,1,2,3,4);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
//        notificationManager.notify( 1, builder.build() );
//    }



    /**
     * Sort
     * @param contacts
     * @param adapter
     */
    private void sort(final GridView gridView, final List<Contact> contacts, final ContactsGridViewAdapter adapter) {

        final Spinner sortBySpinner = ((Spinner) findViewById(R.id.sort_by));
        final Spinner orderSpinner = ((Spinner) findViewById(R.id.order));
        onAccountType(sortBySpinner, orderSpinner);

        findViewById(R.id.sort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogUtility.debug("MainContactActivity", "Sort contacts");

                int order = Constants.ASCENDING;
                switch ((int) orderSpinner.getSelectedItemId()) {
                    case Constants.ASCENDING:
                        order = Constants.ASCENDING;
                        break;

                    case Constants.DESCENDING:
                        order = Constants.DESCENDING;
                        break;
                }

                switch ((int) sortBySpinner.getSelectedItemId()) {
                    case Constants.FULL_NAME:
                        Sort.sort(contacts, Constants.FIELDS[Constants.FULL_NAME], order);
                        gridView.smoothScrollToPosition(0);
                        break;

                    case Constants.FIRST_NAME:
                        Sort.sort(contacts, Constants.FIELDS[Constants.FIRST_NAME], order);
                        gridView.smoothScrollToPosition(0);
                        break;

                    case Constants.LAST_NAME:
                        Sort.sort(contacts, Constants.FIELDS[Constants.LAST_NAME], order);
                        gridView.smoothScrollToPosition(0);
                        break;

                    case Constants.PHONE_NUMBER:
                        Sort.sort(contacts, Constants.FIELDS[Constants.PHONE_NUMBER], order);
                        gridView.smoothScrollToPosition(0);
                        break;

                    case Constants.EMAIL_ADDRESS:
                        Sort.sort(contacts, Constants.FIELDS[Constants.EMAIL_ADDRESS], order);
                        gridView.smoothScrollToPosition(0);
                        break;

                    case Constants.ACCOUNT_TYPE:
                        Sort.sort(contacts, Constants.FIELDS[Constants.ACCOUNT_TYPE], Constants.ASCENDING);
                        gridView.smoothScrollToPosition(0);
                        break;

                    case Constants.LAST_CONTACTED:
                        Sort.sort(contacts, Constants.FIELDS[Constants.LAST_CONTACTED], order);
                        gridView.smoothScrollToPosition(0);
                        break;
                }

                adapter.setSortedContacts(contacts);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * On account type sort
     * @param sortBySpinner
     * @param orderSpinner
     */
    private void onAccountType(final Spinner sortBySpinner, final Spinner orderSpinner) {

        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if ((int) sortBySpinner.getItemIdAtPosition(position) == Constants.ACCOUNT_TYPE) {

                    LogUtility.debug("MainContactsActivity", "on Account type");
                    ArrayAdapter spinnerAdapter = new ArrayAdapter(MainContactsActivity.this, android.R.layout.simple_spinner_item, Constants.ACCOUNTS);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    orderSpinner.setAdapter(spinnerAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * Set up tabs
     * @param callLogsAdapter
     * @param messagesAdapter
     */
    private void setUpTabs(final CallLogsArrayAdapter callLogsAdapter, final TextMessagesArrayAdapter messagesAdapter, final List<Contact> list){

        final TabHost tabs=(TabHost) findViewById(R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("MessagesTab");
        spec.setContent(R.id.messages_tab);
        spec.setIndicator("Messages");
        tabs.addTab(spec);

        spec=tabs.newTabSpec("ContactsTab");
        spec.setContent(R.id.contacts_tab);
        spec.setIndicator("Contacts");
        tabs.addTab(spec);

        spec=tabs.newTabSpec("CallLogsTab");
        spec.setContent(R.id.call_logs_tab);
        spec.setIndicator("logs");
        tabs.addTab(spec);
        tabs.setCurrentTab(getIntent().getIntExtra("tab", 1));

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                switch (tabId) {
                    case "MessagesTab":
                        ManageTextMessages.displayTextMessages(MainContactsActivity.this, messagesAdapter,list);
                        break;

                    case "CallLogsTab":
                            ManageCallLogs.displayCallLogs(MainContactsActivity.this, callLogsAdapter);
                        break;
                }
            }
        });
    }
}
