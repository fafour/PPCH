package co.th.nister.libraryproject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.Display;

public class BitmapHelper 
{

	/**
	 * Created by Nontachai Suptawepong (nontachai@nister.co.th)
	 * 
	 * This method used for get bitmap image from assets by provide path to image in assets
	 * 
	 * @param context
	 * @param strName
	 * @return
	 */
	public static Bitmap getBitmapFromAsset( Context context, String strName)
	{
		try {
		    AssetManager assetManager = context.getAssets();
		    InputStream istr = assetManager.open(strName);
		    
		    BitmapFactory.Options o = new BitmapFactory.Options();
		    o.inJustDecodeBounds = true;
		    BitmapFactory.decodeStream( istr, null, o );
		    
		    Display mDisplay= ((Activity)context).getWindowManager().getDefaultDisplay();
		    int width= mDisplay.getWidth();
		    
		    if ( width > 400 ) width = 400;
		    else width -= 100;
		    
		    final int REQUIRED_SIZE = width;
		    
		    int scale = 1;
		    while ( o.outWidth/scale/2 >= REQUIRED_SIZE && o.outHeight/scale/2 >= REQUIRED_SIZE ) {
		    	scale *= 2;
		    }
		    
		    BitmapFactory.Options o2 = new BitmapFactory.Options();
		    o2.inSampleSize = scale;
		    
		    
		    //Bitmap bitmap = BitmapFactory.decodeStream(istr);
		    Bitmap bitmap = BitmapFactory.decodeStream( istr, null, o2 );
		    return bitmap;
		} catch ( IOException e ) {
			e.printStackTrace();
			return null;
		} catch ( OutOfMemoryError e ) {
			e.printStackTrace();
			return null;
		}
	 }
	
	
	/**
	 * This method used for get bitmap from url (But must call this method in asynctask because of android version 4.0 up restrict)
	 * @param src
	 * @return
	 */
	public static Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	
	public static Bitmap getRoundedCornerBitmap ( Bitmap bitmap, int pixels ) {
		return getRoundedCornerBitmap(bitmap, pixels, Config.RGB_565 );
	}
	
	/**
	 * Convert original image to rounded corner bitmap image
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels, Bitmap.Config bitmapConfig) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.RGB_565);
        
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
	
	
	
	/**
	 * This method used for scale image aspect ratio with providing pixel unit width
	 * @param bitmap
	 * @param px
	 * @return
	 */
	public static Bitmap scaleImagePixel( Context context, Bitmap bitmap, int px )
	{
	    // Get current dimensions AND the desired bounding box
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    int bounding = px;

	    // Determine how much to scale: the dimension requiring less scaling is
	    // closer to the its side. This way the image always stays inside your
	    // bounding box AND either x/y axis touches it.  
	    float xScale = ((float) bounding) / width;
	    float yScale = ((float) bounding) / height;
	    float scale = (xScale <= yScale) ? xScale : yScale;

	    // Create a matrix for the scaling and add the scaling data
	    Matrix matrix = new Matrix();
	    matrix.postScale(scale, scale);

	    // Create a new bitmap and convert it to a format understood by the ImageView 
	    Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	    
	    return scaledBitmap;
	}
	
	/**
	 * This method used for scale image aspect ratio with providing dp unit width
	 * @param bitmap
	 * @param px
	 * @return
	 */
	public static Bitmap scaleImageDP( Context context, Bitmap bitmap, int dp )
	{
	    // Get current dimensions AND the desired bounding box
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    int bounding = SizeHelper.dpToPx( context, dp);

	    // Determine how much to scale: the dimension requiring less scaling is
	    // closer to the its side. This way the image always stays inside your
	    // bounding box AND either x/y axis touches it.  
	    float xScale = ((float) bounding) / width;
	    float yScale = ((float) bounding) / height;
	    float scale = (xScale <= yScale) ? xScale : yScale;

	    // Create a matrix for the scaling and add the scaling data
	    Matrix matrix = new Matrix();
	    matrix.postScale(scale, scale);

	    // Create a new bitmap and convert it to a format understood by the ImageView 
	    Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	    
	    return scaledBitmap;
	}
	
	
	public static Bitmap getBitmapFromFile ( String filePath )
	{
		return getBitmapFromFile(filePath, Bitmap.Config.ARGB_8888);
	}
	
	/**
	 * Get bitmap from file
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitmapFromFile ( String filePath, Bitmap.Config bitmapConfig )
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = bitmapConfig;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		
		return bitmap;
	}
	
	
	public static Bitmap getBitmapFromFile ( String filePath, int reqWidth, int reqHeight ) {
		return getBitmapFromFile(filePath, reqWidth, reqHeight, Bitmap.Config.ARGB_8888 );
	}
	
	public static Bitmap getBitmapFromFile ( String filePath, int reqWidth, int reqHeight, Bitmap.Config bitmapConfig )
	{
		// First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    options.inPreferredConfig = bitmapConfig;
	    //BitmapFactory.decodeFile(filePath, options);

	    
	    /*
	     * float ratio = 1.0f;
	    Log.i( "BitmapHelper", "ORIGINAL WIDTH: " + bitmapImage.getWidth() + ", HEIGHT: " + bitmapImage.getHeight() );
	    
	    ratio = (bitmapImage.getWidth() * 1.0f) / (bitmapImage.getHeight() * 1.0f);
	    
	    
	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, (int)(reqWidth / ratio ) );
	     */
	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    Bitmap bitmapImage = BitmapFactory.decodeFile(filePath, options);
	    
	    
	    if ( bitmapImage == null ) {
	    	// delete file
	    	File file = new File( filePath );
	    	file.delete();
	    	return null;
	    }
	    
	    
	    float ratio = 1.0f;
	    Log.i( "BitmapHelper", "ORIGINAL WIDTH: " + bitmapImage.getWidth() + ", HEIGHT: " + bitmapImage.getHeight() );
	    
	    ratio = (bitmapImage.getWidth() * 1.0f) / (bitmapImage.getHeight() * 1.0f);
	    
	    
	    /*
	    if ( bitmapImage.getWidth() > bitmapImage.getHeight() ) {
	    	
	    } else if ( bitmapImage.getWidth() < bitmapImage.getHeight() ) {
	    	
	    }
	    */
	    Log.i( "BitmapHelper", "RATIO: " + ratio );
	    
	    Log.i( "BitmapHelper", "WIDTH: " + reqWidth + ", Height: " + (int)(reqWidth / ratio ) );
	    Bitmap resultBitmap = Bitmap.createScaledBitmap(bitmapImage, reqWidth, (int)(reqWidth / ratio), false );
	    
	    bitmapImage.recycle();
	    bitmapImage = null;
	    
	    return resultBitmap;
	}
	
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	
	    return inSampleSize;
	}
		
	/**
	 * Save bitmap to file
	 */
	public static String saveBitmap ( Bitmap bmp, String filename, String extension, String directory, boolean appendExtension )
	{
		try {
			
			String filePath = directory + File.separator + filename;
			
			if ( appendExtension ) {
				filePath +=   "." + extension;
			}
			
			FileOutputStream out = new FileOutputStream( filePath );
			
			if ( extension.toLowerCase().equals( "png" ) ) 
				bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
			else if ( extension.toLowerCase().equals( "jpg" ) || extension.toLowerCase().equals( "jpeg" ) )
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, out );
			
 			out.close();
			
			return filePath;
		} catch (Exception e) {
			e.printStackTrace();
			
			return "";
		}
		
		
	}
	
	
	public static String saveBitmapWithExtension ( Bitmap bmp, String filename, String extension, String directory )
	{
		return saveBitmap( bmp, filename, extension, directory, true );
	}
	
	public static String saveBitmapWithOutExtension ( Bitmap bmp, String filename, String extension, String directory  )
	{
		return saveBitmap( bmp, filename, extension, directory, false );
	}
}
