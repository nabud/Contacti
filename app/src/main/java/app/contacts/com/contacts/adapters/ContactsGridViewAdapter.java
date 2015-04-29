package app.contacts.com.contacts.adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.contacts.com.contacts.R;
import app.contacts.com.contacts.models.Contact;
import app.contacts.com.contacts.utilities.ImageUtil;
import app.contacts.com.contacts.utilities.StringUtil;

public class ContactsGridViewAdapter extends BaseAdapter{

    Context context;
    private List<Contact> contacts = null;
    private int layoutId;
    private Bitmap defaultBitmap;

    public ContactsGridViewAdapter(Context context, int layout, List<Contact> list) {
        this.context=context;
        this.contacts = list;
        this.layoutId = layout;
        this.defaultBitmap = ImageUtil.getCircleBitmapFromRes(context, R.drawable.default_contact);
    }

    public void addAll(List<Contact> list){
       contacts.addAll(list);
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Contact getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView name;
        ImageView image;
    }

    @Override
    public View getView(final int position, View rowView, ViewGroup parent) {

        Holder holder;
        Contact contact = getItem(position);

        if(rowView == null){
            holder = new Holder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            rowView = inflater.inflate(layoutId, parent, false);
            holder.name=(TextView) rowView.findViewById(R.id.contact_name);
            holder.image=(ImageView) rowView.findViewById(R.id.contact_image);
            rowView.setTag(holder);
        }
        else {
            holder = (Holder) rowView.getTag();
        }

        holder.name.setText(StringUtil.abbreviate(contact.getFullName()));

        if(contact.getPicture() != null)
            holder.image.setImageBitmap(contact.getPicture());
        else
            holder.image.setImageBitmap(defaultBitmap);
        return rowView;
    }

    public List<Contact> getContacts(){
        return contacts;
    }

    public void setSortedContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public void remove(Contact contact) {
        contacts.remove(contact);
    }

    public void clear() {
        contacts.clear();
    }
}