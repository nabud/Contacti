package app.contacts.com.contacts.services;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.contacts.com.contacts.R;
import app.contacts.com.contacts.adapters.ExpandableListAdapter;
import app.contacts.com.contacts.common.Duplicates;
import app.contacts.com.contacts.common.Merge;
import app.contacts.com.contacts.models.Contact;
import app.contacts.com.contacts.models.DuplicateContacts;

public class DuplicatesActions {

    public static void findDuplicatesByName(final Context context, final PopupWindow duplicatesWindow, final List<Contact> contacts) {

        duplicatesWindow.getContentView().findViewById(R.id.duplicates_by_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, List<Contact>> duplicates = Duplicates.findDuplicatesByOneField(contacts, "fullName");

                List<DuplicateContacts> duplicateContacts = new ArrayList<>();
                for (String fullName: duplicates.keySet())
                    duplicateContacts.add(new DuplicateContacts(fullName, duplicates.get(fullName)));

                duplicatesWindow.getContentView().findViewById(R.id.manage_duplicates_layout).setVisibility(View.GONE);
                duplicatesWindow.getContentView().findViewById(R.id.duplicates_list_view_layout).setVisibility(View.VISIBLE);

                ExpandableListAdapter duplicatesAdapter = new ExpandableListAdapter(context, R.layout.duplicates_expandable_parent_cell, R.layout.duplicates_expandable_child_cell, duplicateContacts, contacts);
                ExpandableListView listView = (ExpandableListView) duplicatesWindow.getContentView().findViewById(R.id.duplicates_listview);
                listView.setAdapter(duplicatesAdapter);
            }
        });
    }

    public static void mergeOneDuplicate(Button merge, final List<Contact> contacts, final List<DuplicateContacts> duplicates) {

        merge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Merge.merge(contacts, duplicates);
            }
        });
    }
}
