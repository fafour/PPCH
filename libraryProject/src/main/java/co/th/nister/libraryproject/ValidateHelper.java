package co.th.nister.libraryproject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateHelper 
{
	
	/**
	 * Created by Nontachai Suptawepong (nontachai@nister.co.th)
	 * 
	 * This method use for check valid email
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmailValid(String email) 
	{
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
	
}
