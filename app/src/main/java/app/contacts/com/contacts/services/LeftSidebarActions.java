package app.contacts.com.contacts.services;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import java.util.List;

import app.contacts.com.contacts.R;
import app.contacts.com.contacts.models.Contact;
import app.contacts.com.contacts.utilities.CommonUtil;
import app.contacts.com.contacts.utilities.PopupUtility;

public class LeftSidebarActions {


    public static void showLeftMenu(final Context context, final List<Contact> contacts){

        View view = CommonUtil.getActivityRoot(context);
        view.findViewById(R.id.side_bar_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupWindow leftMenu = PopupUtility.getPopupWindow(context, R.layout.left_side_menu, R.style.Animation_center_out_center_in);
                leftMenu.showAtLocation(leftMenu.getContentView(), Gravity.START, 0, 0);

                findDuplicates(context, leftMenu, contacts);
            }
        });
    }

    /**
     * Find duplicates
     * @param context
     * @param leftMenuWindow
     * @param contacts
     */
    private static void findDuplicates(final Context context, PopupWindow leftMenuWindow, final List<Contact> contacts) {

        leftMenuWindow.getContentView().findViewById(R.id.duplicates).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupWindow duplicatesWindow = PopupUtility.getPopupWindow(context, R.layout.manage_duplicates_layouts, 0);
                duplicatesWindow.showAtLocation(duplicatesWindow.getContentView(), Gravity.CENTER, 0, 0);

                DuplicatesActions.findDuplicatesByName(context, duplicatesWindow, contacts);
            }
        });
    }
}
