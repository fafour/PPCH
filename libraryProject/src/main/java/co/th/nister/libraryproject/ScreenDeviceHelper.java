package co.th.nister.libraryproject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;

public class ScreenDeviceHelper 
{
	
	/**
	 * Created by Nontachai Suptawepong (nontachai@nister.co.th)
	 * 
	 * This method use for check client's device is a tablet or mobile platform
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context) 
	{
	    boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
	    boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
	    return (xlarge || large);
	}
	
	
	public static int width ( Context context )
	{
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
			Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			
			return width;
		} else {
			Display display = ((Activity)context).getWindowManager().getDefaultDisplay(); 
			int width = display.getWidth();  // deprecated
			
			return width;
		}
	}
	
	public static int widthWithDensity ( Context context )
	{
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
			Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			
			//return (int)(width * context.getResources().getDisplayMetrics().density);
			return width;
		} else {
			Display display = ((Activity)context).getWindowManager().getDefaultDisplay(); 
			int width = display.getWidth();  // deprecated
			
			//return (int)(width * context.getResources().getDisplayMetrics().density);
			return width;
		}
		 
	}
	public static int height ( Context context )
	{
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
			Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int height = size.y;
			
			return height;
		} else {
			Display display = ((Activity)context).getWindowManager().getDefaultDisplay(); 
			int height = display.getHeight();
			
			return height;
		}
	}
}
