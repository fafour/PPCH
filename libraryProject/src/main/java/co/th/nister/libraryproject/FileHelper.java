package co.th.nister.libraryproject;

import java.io.File;

public class FileHelper {
	
	/**
	 * Convert file size in byte to readable format
	 * @param bytes
	 * @param si
	 * @return
	 */
	public static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	
	
	/**
	 * Calculate directory size (recursive)
	 * @param dir
	 * @return
	 */
	public static long dirSize ( File dir ) {
		
		if (dir.exists()) {
	        long result = 0;
	        File[] fileList = dir.listFiles();
	        for(int i = 0; i < fileList.length; i++) {
	            // Recursive call if it's a directory
	            if(fileList[i].isDirectory()) {
	                result += dirSize(fileList [i]);
	            } else {
	                // Sum the file size in bytes
	                result += fileList[i].length();
	            }
	        }
	        return result; // return the file size
	    }
	    return 0;
	}
}
