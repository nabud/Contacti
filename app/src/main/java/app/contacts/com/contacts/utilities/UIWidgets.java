package app.contacts.com.contacts.utilities;

import android.widget.ArrayAdapter;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;


public class UIWidgets {

    // animate a list view, NOTE: LIST VIEW MUST HAVE HEIGHT AND WIDTH SET TO MATCH_PARENT
    // otherwise this does not work
    public static void setAnimatedArrayAdapter(DynamicListView listView, ArrayAdapter<?> adapter){

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);
    }
}
