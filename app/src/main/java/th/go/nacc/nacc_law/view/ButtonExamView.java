package th.go.nacc.nacc_law.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import th.go.nacc.nacc_law.R;

/**
 * Created by nontachai on 9/11/15 AD.
 */
public class ButtonExamView extends View {

    private final Drawable logo;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public ButtonExamView(Context context) {
        super(context);
        logo = context.getResources().getDrawable(R.drawable.btn_exam);
        int sdk = Build.VERSION.SDK_INT;
        if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(logo);
        } else {
            setBackground(logo);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public ButtonExamView(Context context, AttributeSet attrs) {
        super(context, attrs);
        logo = context.getResources().getDrawable(R.drawable.btn_exam);
        int sdk = Build.VERSION.SDK_INT;
        if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(logo);
        } else {
            setBackground(logo);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public ButtonExamView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        logo = context.getResources().getDrawable(R.drawable.btn_exam);
        int sdk = Build.VERSION.SDK_INT;
        if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(logo);
        } else {
            setBackground(logo);
        }
    }

    @Override protected void onMeasure(int widthMeasureSpec,
                                       int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width * logo.getIntrinsicHeight() / logo.getIntrinsicWidth();
        setMeasuredDimension(width, height);
    }
}
