package cn.licht.mobile.anim;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.Nullable;


public class RippleTextView extends TextView implements  IRippleLayout{

    public RippleTextView(Context context) {
        super(context);
    }

    public RippleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        RippleUtil.onDispatch(this,event);
        return super.dispatchTouchEvent(event);
    }
}
