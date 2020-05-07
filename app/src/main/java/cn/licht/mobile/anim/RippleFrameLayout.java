package cn.licht.mobile.anim;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class RippleFrameLayout extends FrameLayout implements  IRippleLayout {

    public RippleFrameLayout(@NonNull Context context) {
        super(context);
    }

    public RippleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        RippleUtil.onDispatch(this,event);
        return super.dispatchTouchEvent(event);
    }

}
