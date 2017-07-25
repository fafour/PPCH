package co.th.nister.libraryproject;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility {
	
	
	/**
	 * Create by Nontachai Suptawepong (nontachai@nister.co.th)
	 * 
	 * This method use for fit listview aspect height (Helpful in case of listview is in a scrollview)
	 * 
	 * @param listView
	 */
    public static void setListViewHeightBasedOnChildren(ListView listView) 
    {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    
    
}