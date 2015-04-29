package app.contacts.com.contacts.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.contacts.com.contacts.R;
import app.contacts.com.contacts.models.Message;
import app.contacts.com.contacts.utilities.StringUtil;

public class TextMessagesArrayAdapter extends ArrayAdapter<Message> {

    private Context context;
    private int layoutResourceId;
    private List<Message> messages = null;

    public TextMessagesArrayAdapter(Context context, int layoutResourceId, List<Message> messages) {
        super(context, layoutResourceId, messages);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final TextMessageHolder holder;
        Message message = getItem(position);

        if(view == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, parent, false);

            holder = new TextMessageHolder();
            holder.picture = (ImageView) view.findViewById(R.id.image);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.message = (TextView) view.findViewById(R.id.message);
            holder.date = (TextView) view.findViewById(R.id.date);
            view.setTag(holder);

        } else
            holder = (TextMessageHolder) view.getTag();

        holder.name.setText(message.getName());
        holder.message.setText(StringUtil.abbreviate(message.getMessage()));
        holder.date.setText(message.getDate());
        return view;
    }

    static class TextMessageHolder {
        ImageView picture;
        TextView name;
        TextView message;
        TextView date;
    }
}