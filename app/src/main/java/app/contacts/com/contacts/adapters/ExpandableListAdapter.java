package app.contacts.com.contacts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import app.contacts.com.contacts.R;
import app.contacts.com.contacts.models.Contact;
import app.contacts.com.contacts.models.DuplicateContacts;
import app.contacts.com.contacts.services.DuplicatesActions;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private LayoutInflater inflater;
    private List<DuplicateContacts> parents;
    private int parentLayoutId;
    private int childLayoutId;
    private List<Contact> contacts;
    private List<DuplicateContacts> duplicates;

    public ExpandableListAdapter(Context context, int parentLayoutId, int childLayoutId, List<DuplicateContacts> duplicates, List<Contact> contacts) {

        inflater = LayoutInflater.from(context);
        parents = duplicates;
        this.parentLayoutId = parentLayoutId;
        this.childLayoutId = childLayoutId;
        this.contacts = contacts;
        this.duplicates = duplicates;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parentView) {

        ParentHolder holder;
        final DuplicateContacts parent = parents.get(groupPosition);

        if(convertView == null){
            holder = new ParentHolder();
            convertView = inflater.inflate(parentLayoutId, parentView, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.merge = (Button) convertView.findViewById(R.id.merge);
            convertView.setTag(holder);
        } else
            holder = (ParentHolder) convertView.getTag();

        holder.name.setText(parent.getContactName());

        DuplicatesActions.mergeOneDuplicate(holder.merge, contacts, duplicates);
        return convertView;
    }


    // This Function used to inflate child rows view
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parentView) {

        ChildHolder holder;
        final Contact child = parents.get(groupPosition).getList().get(childPosition);

        if(convertView == null){
            holder = new ChildHolder();
            convertView = inflater.inflate(childLayoutId, parentView, false);
            holder.contact = (TextView) convertView.findViewById(R.id.name);
            holder.phoneNumber = (TextView) convertView.findViewById(R.id.phone_number);
            convertView.setTag(holder);
        } else
            holder = (ChildHolder) convertView.getTag();

        holder.contact.setText(child.getFullName());
        holder.phoneNumber.setText(child.getPrimaryPhoneNumber());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }


    @Override
    public Contact getChild(int groupPosition, int childPosition) {
        //Log.i("Childs", groupPosition+"=  getChild =="+childPosition);
        return parents.get(groupPosition).getList().get(childPosition);
    }

    //Call when child row clicked
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = 0;
        if (parents.get(groupPosition).getList() != null)
            size = parents.get(groupPosition).getList().size();
        return size;
    }


    @Override
    public Object getGroup(int groupPosition) {

        return parents.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return parents.size();
    }

    //Call when parent row clicked
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public void notifyDataSetChanged() {
        // Refresh List rows
        super.notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty() {
        return ((parents == null) || parents.isEmpty());
    }

    class ParentHolder {
        TextView name;
        Button merge;
    }

    class ChildHolder{
        TextView contact;
        TextView phoneNumber;
    }
}