package cn.licht.mobile.anim;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SC extends TextView {
    public SC(Context context) {
        super(context);
    }

    public SC(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SC(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SC(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);



    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, 0, right, bottom-top);
        Log.i("zyl text","left: "+left+"top: "+top+"right: "+right+"bottom: "+bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int compoundPaddingLeft = getCompoundPaddingLeft();
        final int compoundPaddingTop = getCompoundPaddingTop();
        final int compoundPaddingRight = getCompoundPaddingRight();
        final int compoundPaddingBottom = getCompoundPaddingBottom();
        Log.i("zyl text",compoundPaddingLeft +"~"+compoundPaddingTop+"~"+compoundPaddingRight+"~"+compoundPaddingBottom);
    }
}
