package app.contacts.com.contacts.services;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.util.List;

import app.contacts.com.contacts.R;
import app.contacts.com.contacts.adapters.CallLogsArrayAdapter;
import app.contacts.com.contacts.common.CallLogs;
import app.contacts.com.contacts.common.Communications;
import app.contacts.com.contacts.models.CallLogRecords;
import app.contacts.com.contacts.models.Contact;
import app.contacts.com.contacts.utilities.CommonUtil;
import app.contacts.com.contacts.utilities.PopupUtility;
import app.contacts.com.contacts.utilities.StringUtil;
import app.contacts.com.contacts.utilities.UIWidgets;

public class ManageCallLogs {

    public static void displayCallLogs(Context context, CallLogsArrayAdapter adapter) {

        View view = CommonUtil.getActivityRoot(context);
        DynamicListView listView = (DynamicListView) view.findViewById(R.id.logs_list);
        UIWidgets.setAnimatedArrayAdapter(listView, adapter);

        callRecordOnShortClick(context, listView);
    }

    /**
     * Call record on short click
     * @param context
     * @param listView
     */
    private static void callRecordOnShortClick(final Context context, final ListView listView) {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                PopupWindow popupWindow = PopupUtility.getPopupWindowWithBlur(context, R.layout.record_actions, R.style.Animation_center_out_center_in);
                popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.CENTER, 0, 0);

                Communications.setRecordActions(context, popupWindow.getContentView(), ((CallLogRecords) listView.getItemAtPosition(position)).getCallNumber());
                PopupUtility.dismissPopup(popupWindow, R.id.close);
            }
        });
    }
}
