package app.contacts.com.contacts.utilities;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;


import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatAutoCompleteTextView;
import com.cengalabs.flatui.views.FlatCheckBox;
import com.cengalabs.flatui.views.FlatEditText;
import com.cengalabs.flatui.views.FlatRadioButton;
import com.cengalabs.flatui.views.FlatTextView;

import app.contacts.com.contacts.R;
import app.contacts.com.contacts.common.Constants;


public class ThemeUtil {

    private static int appTheme = Constants.BLACK_THEME;

    public static int getAppTheme(){
        return appTheme;
    }

    public static void setAppTheme(int appTheme){
        ThemeUtil.appTheme = appTheme;
    }

    public static void setFlatUITheme(){
        FlatUI.setEditTextStyle(FlatUI.EDITTEXT_TRANSPARENT);
        FlatUI.setUseFlatTextColor(false);
    }

    public  static void setTextColor(int color){
        FlatAutoCompleteTextView.setColor(color);
        FlatEditText.setColor(color);
        FlatTextView.setColor(color);
        FlatCheckBox.setColor(color);
        FlatRadioButton.setColor(color);
    }

    public static void onActivityCreateSetTheme(Activity activity, int theme) {

        Log.d("ThemeUtil", "Set the current theme");
        //View view = CommonUtil.getActivityRoot(activity);
        switch (theme) {
            case Constants.BLACK_THEME:
                //view.setBackgroundResource(R.drawable.rounded_black_old);
                activity.setTheme(R.style.DefaultTheme);
                setTextColor(Color.WHITE);
                break;

            case Constants.BLUE_THEME:
                //view.setBackgroundResource(R.drawable.rounded_blue);
                activity.setTheme(R.style.BlueTheme);
                setTextColor(Color.BLACK);
                break;
            case Constants.WHITE_THEME:
                //view.setBackgroundResource(R.drawable.rounded_white);
                activity.setTheme(R.style.WhiteTheme);
                setTextColor(Color.BLACK);
                break;
        }

        ThemeUtil.setFlatUITheme();
        FlatUI.setUseFlatTextColor(false);
    }
}
