package cn.licht.mobile.anim.frzz;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FloatFramelayout extends FrameLayout  {

    public FloatFramelayout(@NonNull Context context) {
        super(context);
    }

    public FloatFramelayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatFramelayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }


    private FloatDispatchTouchProxy floatDispatchTouchProxy;



    public void  setOnTouchProxy (FloatDispatchTouchProxy floatDispatchTouchProxy){
        this.floatDispatchTouchProxy = floatDispatchTouchProxy;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (floatDispatchTouchProxy != null){
            boolean b = floatDispatchTouchProxy.onTouchEvent(this, event);
            if (b){
                return  super.dispatchTouchEvent(event);
            }else {
                return false;
            }
        }
        return  super.dispatchTouchEvent(event);
    }

}

