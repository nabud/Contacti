package app.contacts.com.contacts.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.contacts.com.contacts.R;
import app.contacts.com.contacts.common.Constants;
import app.contacts.com.contacts.models.CallLogRecords;

public class CallLogsArrayAdapter extends ArrayAdapter<CallLogRecords> implements Filterable{

    private Context context;
    private int layoutResourceId;
    private Filter filter = null;
    private List<CallLogRecords> logs = null;

    public CallLogsArrayAdapter(Context context, int layoutResourceId, List<CallLogRecords> list) {
        super(context, layoutResourceId, list);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.logs = list;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final CallLogsHolder holder;
        CallLogRecords callLogRecords = getItem(position);

        if(view == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, parent, false);

            holder = new CallLogsHolder();
            holder.contactImage = (ImageView) view.findViewById(R.id.profile_image);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.date = (TextView) view.findViewById(R.id.date);
            holder.phoneNumber = (TextView) view.findViewById(R.id.number);
            holder.duration = (TextView) view.findViewById(R.id.duration);
            holder.callTypeImage = (ImageView) view.findViewById(R.id.call_type);
            view.setTag(holder);

        } else
            holder = (CallLogsHolder)view.getTag();

        if (callLogRecords.getPicture() != null)
            holder.contactImage.setImageBitmap(callLogRecords.getPicture());

        holder.name.setText(callLogRecords.getCallerName());
        holder.phoneNumber.setText(callLogRecords.getCallDisplayNumber());
        holder.date.setText(callLogRecords.getCallDayTime());
        holder.duration.setText(callLogRecords.getCallDuration());

        switch (callLogRecords.getCallType()) {
            case Constants.INCOMING_CALL:
                holder.callTypeImage.setImageResource(R.drawable.incoming);
                break;

            case Constants.OUTGOING_CALL:
                holder.callTypeImage.setImageResource(R.drawable.outgoing);
                break;

            case Constants.MISSED_CALL:
                holder.callTypeImage.setImageResource(R.drawable.missed_call);
                break;
        }

        return view;
    }

    static class CallLogsHolder {
        ImageView contactImage;
        ImageView callTypeImage;
        TextView name;
        TextView phoneNumber;
        TextView date;
        TextView duration;
    }
}