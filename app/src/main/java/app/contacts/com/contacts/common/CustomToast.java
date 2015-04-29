package app.contacts.com.contacts.common;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.contacts.com.contacts.R;

public class CustomToast {

    /**
     * Show toast
     *
     * @param context
     * @param text
     * @param duration
     * @param drawableResponse
     */
    public static void showToast(Context context, String text, int duration, int drawableResponse) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.toast_layout, null);

        ((TextView) layout.findViewById(R.id.toast_text)).setText(text);

        switch (drawableResponse) {
            case Constants.SUCCESS:
                ((ImageView) layout.findViewById(R.id.toast_image)).setImageResource(R.drawable.check_mark);
                break;

            case Constants.Failure:
                ((ImageView) layout.findViewById(R.id.toast_image)).setImageResource(R.drawable.close);
                break;

            case Constants.WARNING:
                ((ImageView) layout.findViewById(R.id.toast_image)).setImageResource(R.drawable.warning);
                break;
        }

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }
}
