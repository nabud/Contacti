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
import app.contacts.com.contacts.common.Constants;
import app.contacts.com.contacts.models.Message;


public class MessagesLogArrayAdapter extends ArrayAdapter<Message> {

    private Context context;
    private int layoutResourceId;
    private List<Message> list = null;

    public MessagesLogArrayAdapter(Context context, int layoutResourceId, List<Message> list) {
        super(context, layoutResourceId, list);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Holder holder;
        final Message message = getItem(position);

        if(view == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, parent, false);

            holder = new Holder();
            if (message.getType() == Constants.INCOMING_MESSAGE) {
                view.findViewById(R.id.incomming).setVisibility(View.VISIBLE);
                view.findViewById(R.id.outgoing).setVisibility(View.GONE);
                holder.message = (TextView) view.findViewById(R.id.text);
                holder.timeStamp = (TextView) view.findViewById(R.id.time_stamp_in);
                holder.picture = (ImageView) view.findViewById(R.id.image_in);

            } else if (message.getType() == Constants.OUTGOING_MESSAGE) {
                view.findViewById(R.id.incomming).setVisibility(View.GONE);
                view.findViewById(R.id.outgoing).setVisibility(View.VISIBLE);
                holder.message = (TextView) view.findViewById(R.id.text_out);
                holder.timeStamp = (TextView) view.findViewById(R.id.time_stamp_out);
                holder.picture = (ImageView) view.findViewById(R.id.image_out);
            }
            view.setTag(holder);
        }
        else
            holder = (Holder)view.getTag();

        // only show this if your in the message log
        if (message.getType() == Constants.INCOMING_MESSAGE) {

            view.findViewById(R.id.incomming).setVisibility(View.VISIBLE);
            view.findViewById(R.id.outgoing).setVisibility(View.GONE);
            holder.message = (TextView) view.findViewById(R.id.text);
            holder.timeStamp = (TextView) view.findViewById(R.id.time_stamp_in);
            holder.picture = (ImageView) view.findViewById(R.id.image_in);
            holder.message.setBackgroundResource(R.drawable.incomming_message);

        } else if (message.getType() == Constants.OUTGOING_MESSAGE) {
            view.findViewById(R.id.incomming).setVisibility(View.GONE);
            view.findViewById(R.id.outgoing).setVisibility(View.VISIBLE);
            holder.message = (TextView) view.findViewById(R.id.text_out);
            holder.timeStamp = (TextView) view.findViewById(R.id.time_stamp_out);
            holder.picture = (ImageView) view.findViewById(R.id.image_out);
            holder.message.setBackgroundResource(R.drawable.outgoing_message);
        }

        System.out.println(message.getType());
        holder.message.setText(message.getMessage());
        holder.timeStamp.setText(message.getDate());
        return view;
    }

    static class Holder{
        TextView message;
        TextView timeStamp;
        ImageView picture;
    }
}