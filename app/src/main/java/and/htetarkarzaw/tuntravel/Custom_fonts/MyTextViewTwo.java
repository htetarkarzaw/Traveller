package and.htetarkarzaw.tuntravel.Custom_fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by one on 3/12/15.
 */
public class MyTextViewTwo extends AppCompatTextView {

    public MyTextViewTwo(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewTwo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewTwo(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Meera_Inimai_regular.ttf");
            setTypeface(tf);
        }
    }

}