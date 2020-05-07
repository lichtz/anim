package cn.licht.mobile.anim;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;


public class RippleRelativeLayout extends RelativeLayout implements  IRippleLayout{
    public RippleRelativeLayout(Context context) {
        super(context);
    }

    public RippleRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        RippleUtil.onDispatch(this,event);
        return super.dispatchTouchEvent(event);
    }

}
