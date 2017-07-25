package co.th.nister.libraryproject;

import android.content.Context;

public class SizeHelper {
	
	/**
	 * Convert dp unit to pixel unit
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dpToPx( Context context, int dp)
	{
	    float density = context.getResources().getDisplayMetrics().density;
	    return Math.round((float)dp * density);
	}

}
