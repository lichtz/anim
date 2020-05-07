package cn.licht.mobile.anim;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class CU extends ImageView {

    private int maxHeight;
    public CU(Context context) {
        super(context);
    }

    public CU(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CU(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void init(Context context, AttributeSet attrs) {
        if (context != null && attrs != null) {
//            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FXM);
//            maxHeight = typedArray.getDimensionPixelSize(R.styleable.FXM_maxHeight, 0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i("zyl","ASSS"+event.getAction()+"files"+hasOnClickListeners());

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(Color.RED);
                break;
            case MotionEvent.ACTION_UP:

                setBackgroundColor(Color.TRANSPARENT);
                break;
        }
        return super.dispatchTouchEvent(event);

    }
}
