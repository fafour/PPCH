package co.th.nister.libraryproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import android.text.Html;

public class StringHelper {
	
	/**
	 * Created by Nontachai Suptawepong (nontachai@nister.co.th)
	 * 
	 * This method used for encrypt password text into md5 format
	 * @param s
	 * @return
	 */
	public static String md5(String s) 
	{
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        
	        digest.update(s.getBytes()); 
	        byte messageDigest[] = digest.digest();
	        
	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i = 0; i < messageDigest.length; i++) {
	            String h = Integer.toHexString(0xFF & messageDigest[i]);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        return hexString.toString();
	        
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
	
	/**
	 * Convert input stream to string object
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is)
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try  {
			while ((line = reader.readLine()) != null) 
           sb.append(line + "\n");
		}  catch (IOException e)  {
			e.printStackTrace();
		} 
		finally  {
			try  {
				is.close();
			}  catch (IOException e)  {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	
	public static String stripHTML ( String html ) {
		return Html.fromHtml( html ).toString().replace( "&nbsp;", " " );
	}
	
	public static String randomString ( int length ) 
	{
		String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

		final Random random=new Random();
  
		final StringBuilder sb=new StringBuilder();
  
		for ( int i = 0; i < length; i++ ) {
			sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
		}
    
		return sb.toString();
	}
}
