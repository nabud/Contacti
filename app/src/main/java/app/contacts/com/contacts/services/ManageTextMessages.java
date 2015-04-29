package app.contacts.com.contacts.services;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.util.ArrayList;
import java.util.List;

import app.contacts.com.contacts.R;
import app.contacts.com.contacts.adapters.ContactListViewArrayAdapter;
import app.contacts.com.contacts.adapters.MessagesLogArrayAdapter;
import app.contacts.com.contacts.adapters.TextMessagesArrayAdapter;
import app.contacts.com.contacts.common.BroadCastRecievers;
import app.contacts.com.contacts.common.Communications;
import app.contacts.com.contacts.common.Constants;
import app.contacts.com.contacts.common.SMSManager;
import app.contacts.com.contacts.common.TextMessages;
import app.contacts.com.contacts.models.Contact;
import app.contacts.com.contacts.models.Message;
import app.contacts.com.contacts.utilities.CommonUtil;
import app.contacts.com.contacts.utilities.DateUtil;
import app.contacts.com.contacts.utilities.PopupUtility;
import app.contacts.com.contacts.utilities.UIWidgets;

public class ManageTextMessages {

    /**
     * Display text messages
     * @param context
     */
    public static void displayTextMessages(final Context context, TextMessagesArrayAdapter adapter, final List<Contact> list) {

        View view = CommonUtil.getActivityRoot(context);
        DynamicListView listView = (DynamicListView) view.findViewById(R.id.messages_list);
        UIWidgets.setAnimatedArrayAdapter(listView, adapter);
        view.findViewById(R.id.text_layout).setVisibility(View.GONE);
        messageRecordOnShortClick(context, view,  listView);
        messageRecordOnLongClick(context, listView);

        //startNewText(context,view,list);
    }

    /**
     * Message record on short click
     * @param context
     * @param listView
     */
    public static void messageRecordOnShortClick(final Context context, final View view, final DynamicListView listView) {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                listView.setVisibility(View.GONE);
                Message message = ((Message) listView.getItemAtPosition(position));
                MessagesLogArrayAdapter adapter = new MessagesLogArrayAdapter(context, R.layout.messages_listview_cell, TextMessages.getMessagesForPhoneNumber(context, message.getPhoneNumber()).get(message.getPhoneNumber()));
                DynamicListView listView1 = (DynamicListView) view.findViewById(R.id.messages_details_list);
                listView1.setVisibility(View.VISIBLE);
                view.findViewById(R.id.text_layout).setVisibility(View.VISIBLE);
                UIWidgets.setAnimatedArrayAdapter(listView1, adapter);
                sendTextmessage(adapter, view, message.getPhoneNumber());
                view.findViewById(R.id.texting_float_button).setVisibility(View.GONE);
                BroadCastRecievers.unRegisterReceiver(context);
                BroadCastRecievers.textMessageReciever(context, adapter, message.getPhoneNumber());
            }
        });
    }

    /**
     * Message record on long click
     * @param context
     * @param listView
     */
    private static void messageRecordOnLongClick(final Context context, final ListView listView) {

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {

                PopupWindow popupWindow = PopupUtility.getPopupWindowWithBlur(context, R.layout.record_actions, R.style.Animation_center_out_center_in);
                popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.CENTER, 0, 0);

                Communications.setRecordActions(context, popupWindow.getContentView(), ((Message) listView.getItemAtPosition(position)).getPhoneNumber());
                PopupUtility.dismissPopup(popupWindow, R.id.close);
                return true;
            }
        });
    }

    private static void sendTextmessage(final MessagesLogArrayAdapter adapter, final View view, final String phoneNumber){
        view.findViewById(R.id.send_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSManager.sendSMSMessage(((TextView) view.findViewById(R.id.sms_message_text)).getText().toString(), phoneNumber);
                Message newMessage = new Message();
                newMessage.setMessage(((TextView) view.findViewById(R.id.sms_message_text)).getText().toString());
                newMessage.setType(Constants.OUTGOING_MESSAGE);
                newMessage.setName("");
                newMessage.setDate(DateUtil.formatDateString(DateUtil.getCurrentDate(), DateUtil.Formats.DATE_FORMAT, DateUtil.Formats.DAY_DATE_TIME_FORMAT));


                adapter.add(newMessage);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Start a new Text with
     * @param context
     * @param view
     * @param list

    private static void startNewText(final Context context, final View view, final List<Contact> list){
        final FloatingActionButton actionA = (FloatingActionButton) view.findViewById(R.id.texting_float_button);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final PopupWindow popupWindow = PopupUtility.getPopupWindow(context, R.layout.contact_list_view,R.style.Animation_center_out_center_in);
                popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.CENTER, 0, 0);

                final DynamicListView dynamicListView = (DynamicListView) popupWindow.getContentView().findViewById(R.id.contacts_list_view);
                final ContactListViewArrayAdapter contactListViewArrayAdapter = new ContactListViewArrayAdapter(context, R.layout.contaact_list_view_cells, list);
                UIWidgets.setAnimatedArrayAdapter(dynamicListView, contactListViewArrayAdapter);

                dynamicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {

                        //get the contact phone number
                        String phoneNumber = contactListViewArrayAdapter.getItem(i).getPrimaryPhoneNumber();
                        MessagesLogArrayAdapter adapter = new MessagesLogArrayAdapter(context, R.layout.messages_listview_cell, new ArrayList<Message>());

                        DynamicListView listView1 = (DynamicListView) view.findViewById(R.id.messages_details_list);
                        listView1.setVisibility(View.VISIBLE);
                        dynamicListView.setVisibility(View.GONE);
                        UIWidgets.setAnimatedArrayAdapter(listView1, adapter);

                        sendTextmessage(adapter, view, phoneNumber);
                        view.findViewById(R.id.texting_float_button).setVisibility(View.GONE);
                        BroadCastRecievers.unRegisterReceiver(context);
                        BroadCastRecievers.textMessageReciever(context, adapter, phoneNumber);
                        popupWindow.dismiss();
                    }
                });
            }
        });
    }*/
}
