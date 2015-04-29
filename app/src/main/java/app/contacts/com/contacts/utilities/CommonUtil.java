package app.contacts.com.contacts.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class CommonUtil {

    /**
     * Get Activity Root
     * @param context
     * @return
     */
    public static View getActivityRoot(Context context){
        return ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
    }

}
