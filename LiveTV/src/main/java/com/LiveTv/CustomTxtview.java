package com.LiveTv;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by creativeinfoway2 on 28/11/16.
 */

public class CustomTxtview extends TextView {
    public CustomTxtview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTxtview(Context context) {
        super(context);
        init();
    }

    public CustomTxtview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomTxtview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/MontserratRegular.ttf");

            setTypeface(tf);
        }
    }
}
