package cn.licht.mobile.anim;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;


public class RippleLinearLayout extends LinearLayout implements IRippleLayout {
    public RippleLinearLayout(Context context) {
        super(context);
    }

    public RippleLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        RippleUtil.onDispatch(this,event);
        return super.dispatchTouchEvent(event);
    }

}
