package co.th.nister.libraryproject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class AssetHelper {
	
	// just only prototype for copy all assets to sdcard path (root path), 
	// if you not want copy to root path just add parameter and pass to getExternalFileDir() method
	public static void copyAssets( Context context) {
		
	    AssetManager assetManager = context.getAssets();
	    String[] files = null;
	    try {
	        files = assetManager.list("");
	    } catch (IOException e) {
	        Log.e("tag", "Failed to get asset file list.", e);
	    }
	    for(String filename : files) {
	        InputStream in = null;
	        OutputStream out = null;
	        try {
	          in = assetManager.open(filename);
	          File outFile = new File( context.getExternalFilesDir(null), filename);
	          out = new FileOutputStream(outFile);
	          copyFile(in, out);
	          in.close();
	          in = null;
	          out.flush();
	          out.close();
	          out = null;
	        } catch(IOException e) {
	            Log.e("tag", "Failed to copy asset file: " + filename, e);
	        }       
	    }
	}

	
	public static void copyAsset ( Context context, String asset_path, String sdcard_path ) {
	    AssetManager assetManager = context.getAssets();
	    String[] files = null;
        InputStream in = null;
        OutputStream out = null;

        try {
          in = assetManager.open(asset_path);
          
          
          //File outFile = new File( context.getExternalFilesDir(null), "test2.epub" );
          File outFile = new File( "/storage/sdcard0/" + sdcard_path );
          Log.i( "AssetHelper", "Output File: " + outFile.getAbsolutePath() );
          out = new FileOutputStream(outFile);
          copyFile(in, out);
          in.close();
          in = null;
          out.flush();
          out.close();
          out = null;
        } catch(IOException e) {
        	e.printStackTrace();
            Log.e("tag", "Failed to copy asset file: " + asset_path, e);
        }       
	}
	
	public static void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
}
