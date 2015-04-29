package app.contacts.com.contacts.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import com.cengalabs.flatui.views.FlatTextView;

import java.util.ArrayList;
import java.util.List;

import app.contacts.com.contacts.R;
import app.contacts.com.contacts.models.Contact;

public class ContactListViewArrayAdapter extends ArrayAdapter<Contact> implements Filterable{

    private Context context;
    private int layoutResourceId;
    private Filter filter = null;
    private List<Contact> contacts = null;
    private static List<Contact> selectedContacts = new ArrayList<>();
    private List<Contact> filteredContacts = null;
    private int selectedContactIndex = -1;

    public ContactListViewArrayAdapter(Context context, int layoutResourceId, List<Contact> list) {
        super(context, layoutResourceId, list);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.contacts = list;
        this.filteredContacts = list;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final ContactHolder holder;

        if(view == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, parent, false);

            holder = new ContactHolder();
            holder.contactImage = (ImageView) view.findViewById(R.id.contact_image);
            holder.name = (FlatTextView) view.findViewById(R.id.name);
            holder.phoneNumber = (TextView) view.findViewById(R.id.phone_number);
            view.setTag(holder);

        } else
            holder = (ContactHolder)view.getTag();

        final Contact contact = contacts.get(position);
        holder.name.setText(contact.getFullName());
        if (contact.getPhoneNumbers() != null && !contact.getPhoneNumbers().isEmpty())
            holder.phoneNumber.setText(String.valueOf(contact.getPhoneNumbers().get(0)));
        if(contact.getPicture() != null)
            holder.contactImage.setImageBitmap(contact.getPicture());
        return view;
    }

    static class ContactHolder {
        ImageView contactImage;
        TextView name;
        TextView phoneNumber;
    }


    public Filter getFilter() {
        return (filter == null) ? filter = new CustomFilter() : filter;
    }

    //Filtering Class for Contacts
    private class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = contacts;
                results.count = contacts.size();

            } else {

                String string = constraint.toString().toLowerCase();
                List<Contact> newContacts = new ArrayList<Contact>();
                for (Contact contact : contacts)
                    if (contact.getFirstName().toLowerCase().contains(string) || contact.getLastName().toLowerCase().contains(string))
                        newContacts.add(contact);

                results.values = newContacts;
                results.count = newContacts.size();
            }
            return results;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filteredContacts = (List<Contact>) results.values;
            notifyDataSetChanged();
        }
    }

    public void setSelectedContactIndex(int index) {
        selectedContactIndex = index;
    }

    public int getSelectedContactIndex() {
        return selectedContactIndex;
    }

}