package co.th.nister.libraryproject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputHelper 
{
	
	/**
	 * Created by Nontachai Suptawepong (nontachai@nister.co.th)
	 * 
	 * This method use for hidden keyboard interface
	 * 
	 * @param context
	 */
	public static void hiddenKeyboard( Context context )
	{
		InputMethodManager inputManager = 
		        (InputMethodManager) context.
		            getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(
				((Activity) context).getCurrentFocus().getWindowToken(),
		        InputMethodManager.HIDE_NOT_ALWAYS); 
		
	}
	
	/**
	 * Created by Nontachai Suptawepong (nontachai@nister.co.th)
	 * 
	 * This method use for show keyboard interface with view that want to focus
	 * 
	 * @param context
	 * @param txtFocus
	 */
	public static void showKeyboard ( Context context, View txtFocus ) {
		InputMethodManager imm =  (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txtFocus, InputMethodManager.SHOW_IMPLICIT);
	}
}
