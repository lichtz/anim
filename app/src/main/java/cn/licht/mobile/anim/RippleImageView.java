package cn.licht.mobile.anim;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;


public class RippleImageView extends AppCompatImageView implements  IRippleLayout {

    public RippleImageView(Context context) {
        super(context);
    }

    public RippleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        RippleUtil.onDispatch(this,event);
        return super.dispatchTouchEvent(event);
    }
}
