package co.th.nister.libraryproject;

import android.graphics.Color;

public class ColorHelper {

	public static int darker ( int color, float ratio ) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[2] *= (1.0f - ratio); // value component
		color = Color.HSVToColor(hsv);
		
		return color;
	}
	
	public static float[] convertToHSV ( int color ) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		
		return hsv;
	}
	
	public static int darker ( int color ) {
		return darker(color, 0.8f);
	}
	
	
}
