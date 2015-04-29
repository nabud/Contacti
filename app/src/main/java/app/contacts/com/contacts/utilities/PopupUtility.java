package app.contacts.com.contacts.utilities;

import android.app.ActionBar;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import app.contacts.com.contacts.R;

public class PopupUtility {

    /**
     * Get popup window
     * @param context
     * @param layoutIndex
     * @param style
     * @return
     */
    public static PopupWindow getPopupWindow(Context context, int layoutIndex, int style) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(layoutIndex, null);
        PopupWindow popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);

        if(style != 0) popupWindow.setAnimationStyle(style);
//        if (layoutIndex != R.layout.blur_layout) dismissPopup(popupWindow, R.id.cancel);
        return popupWindow;
    }

    /**
     * Get popup window
     * @param context
     * @param layoutIndex
     * @param style
     * @return
     */
    public static PopupWindow getPopupWindowWithBlur(Context context, int layoutIndex, int style) {

        final PopupWindow blurWindow = showBlurLayout(context);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(layoutIndex, null);
        PopupWindow popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        if(style != 0) popupWindow.setAnimationStyle(style);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                blurWindow.dismiss();
            }
        });
        return popupWindow;
    }

    /**
     * Dismiss popup
     * @param popupWindow
     * @param id
     */
    public static void dismissPopup(final PopupWindow popupWindow, int id){

        popupWindow.getContentView().findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    public static PopupWindow showBlurLayout(Context context) {

        PopupWindow blurWindow = getPopupWindow(context, R.layout.blur_layout, 0);

        //TODO fix animation
//        ViewAnimations.fadeInAnimation(blurWindow.getContentView());
        blurWindow.showAtLocation(blurWindow.getContentView(), Gravity.FILL, 0, 0);
        return blurWindow;
    }

    /**
     * Show Confirm Response Layout
     *
     * @param title
     * @param message
     * @return
     */
    public static PopupWindow showConfirmActionWindowWithBlur(Context context, int drawableId, String title, String message) {
        LogUtility.debug("CommonUtil", "Show confirm window with blur");
        return showConfirmLayout(context, R.layout.confirm_response_layout, drawableId, title, message, showBlurLayout(context));
    }

    /**
     * Show Confirm Layout
     * @param layoutId
     * @param title
     * @param message
     * @return
     */
    private static PopupWindow showConfirmLayout(Context context, int layoutId, int drawableId, String title, String message, final PopupWindow blurWindow) {

        PopupWindow confirmWindow = getPopupWindow(context, layoutId, R.style.Animation_AppCompat_DropDownUp);
        ((ImageView) confirmWindow.getContentView().findViewById(R.id.confirmation_image)).setImageResource(drawableId);
        ((TextView) confirmWindow.getContentView().findViewById(R.id.title)).setText(title);
        ((TextView) confirmWindow.getContentView().findViewById(R.id.message)).setText(message);
        confirmWindow.showAtLocation(confirmWindow.getContentView(), Gravity.CENTER, 0, 0);
        closeWindowWithBlurWindow(confirmWindow, blurWindow, R.id.cancel);

        confirmWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                blurWindow.dismiss();
            }
        });
        return confirmWindow;
    }

    /**
     * Closing actual window and blur window
     *
     * @param popupWindow
     * @param blurWindow
     * @param index
     */
    public static void closeWindowWithBlurWindow(final PopupWindow popupWindow, final PopupWindow blurWindow, int index) {

        LogUtility.debug("CommonUtil", "Closing actual window and blur window");

        (popupWindow.getContentView().findViewById(index)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                blurWindow.dismiss();
            }
        });
    }

}
