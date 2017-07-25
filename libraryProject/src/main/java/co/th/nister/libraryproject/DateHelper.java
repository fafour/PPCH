package co.th.nister.libraryproject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateHelper {
	
	public static final int COMPARE_LEFT_AFTER = 1;
	public static final int COMPARE_RIGHT_AFTER = -1;
	public static final int COMPARE_EQUAL = 0;
	
	/**
	 * Convert date form from input format to output format
	 * @param inputFormatString
	 * @param outputFormatString
	 * @param date
	 * @return
	 * 
	 * example: convertDateFormat ( "yyyy-MM-dd HH:mm:ii", "MMM dd, yyyy HH:mm:ii", createDate );
	 */
	public static String convertDateFormat( String inputFormatString, String outputFormatString, String date)
	{
		// get format string to dateformat object
	    DateFormat inputFormat = new SimpleDateFormat( inputFormatString );
	    
	    // set timezone if needed
	    //inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	    
	    // get output format string to dateformat object
	    DateFormat outputFormat = new SimpleDateFormat( outputFormatString );
	    
	    // initial date object
	    Date parsed = new Date();
	    try
	    {
	    	// parse date string to date object using input format
	        parsed = inputFormat.parse(date);
	    }
	    catch (ParseException e)
	    {
	    	// in case of parse error (format wrong)
	        e.printStackTrace();
	    }
	    
	    // parse from input format to output format as an string
	    String outputText = outputFormat.format(parsed);
	    
	    // return output format string
	    return outputText;
	}
	
	
	
	public static int compare ( String date1, String date2, String dateFormatString )
	{
		SimpleDateFormat sdf = new SimpleDateFormat( dateFormatString );
		
		if ( date1 == null ) return COMPARE_RIGHT_AFTER;
		else if ( date2 == null ) return COMPARE_LEFT_AFTER;
		
		try {
			Date objDate1 = sdf.parse( date1 );
			Date objDate2 = sdf.parse( date2 );
			
			if ( objDate1.after( objDate2 ) ) {
				return COMPARE_LEFT_AFTER;
			} else if ( objDate1.before( objDate2 )) {
				return COMPARE_RIGHT_AFTER;
			} else {
				return COMPARE_EQUAL;
			}
		} catch ( ParseException e ) {
			e.printStackTrace();
			return COMPARE_RIGHT_AFTER;
		}
	}
	
	/**
	 * Compare between 2 date, is equal or not
	 */
	public static boolean isEqual ( String date1, String date2, String dateFormatString )
	{
		SimpleDateFormat sdf = new SimpleDateFormat( dateFormatString );
		
		try {
			Date objDate1 = sdf.parse( date1 );
			Date objDate2 = sdf.parse( date2 );
			
			if ( objDate1.after( objDate2 ) || objDate1.before( objDate2 ) ) {
				return false;
			} else {
				return true;
			}
		} catch ( ParseException e ) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	public static String getTimeAsStringAgo ( Date date )
	{
		long diffInMis = (new Date()).getTime() - date.getTime();

		long dateDiff = TimeUnit.MILLISECONDS.toSeconds(diffInMis);
		
		
	    
	    int nrSeconds = (int) dateDiff;//components.second;
	    int nrMinutes = nrSeconds / 60;
	    int nrHours = nrSeconds / 3600;
	    int nrDays = Math.round ( dateDiff / 86400 ); //components.day;
	    
	    String time;
	    if (nrDays > 5){
	    	SimpleDateFormat dateFormat = new SimpleDateFormat( "MMM dd, yyyy" );
	    	
	    	time = dateFormat.format(date);
	    } else {
	        // days=1-5
	        if (nrDays > 0) {
	            if (nrDays == 1) {
	                time = "1 day ago";
	            } else {
	                time = String.format( "%d days ago", nrDays );
	            }
	        } else {
	            if (nrHours == 0) {
	                if (nrMinutes < 2) {
	                    time = "just now";
	                } else {
	                    time = String.format( "%d minutes ago", nrMinutes );
	                }
	            } else { // days=0 hours!=0
	                if (nrHours == 1) {
	                    time = "1 hour ago";
	                } else {
	                    time = String.format("%d hours ago", nrHours);
	                }
	            }
	        }
	    }
	    
	    return time;
	}
	
	public static Date getYesterdayDate()
	{
		Calendar cal = Calendar.getInstance();
		cal.add ( Calendar.DATE, -1 );
		return cal.getTime();
	}
	
	public static Date getDateBefore( int dateBefore)
	{
		Calendar cal = Calendar.getInstance();
		cal.add ( Calendar.DATE, -dateBefore );
		return cal.getTime();
	}
}
