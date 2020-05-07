package cn.licht.mobile.anim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

public class MaxScrollView extends ScrollView {
    private static final String TAG = "MaxScrollView";
    public static final int COVER_STATE_GONE = 0;
    public static final int COVER_STATE_SHOW = 1;
    public static final int COVER_STATE_SHOW_TOP = 2;
    public static final int COVER_STATE_SHOW_BOTTOM = 3;
    private int maxHeight;
    private int coverWidth;
    private int coverLastPostionHeight;
    private IShowCoverListener iShowCoverListener;
    private int realHeight;
    private boolean isMax;


    public MaxScrollView(Context context) {
        super(context);
    }

    public MaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MaxScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (context != null && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaxScrollView);
            maxHeight = typedArray.getDimensionPixelSize(R.styleable.MaxScrollView_maxHeight, 0);
            typedArray.recycle();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        realHeight = MeasureSpec.getSize(heightMeasureSpec);
        coverWidth = MeasureSpec.getSize(widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: "+realHeight+"AAA"+maxHeight);
        if (maxHeight < realHeight) {
            int makeMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, makeMeasureSpec);
            isMax = true;
            if (iShowCoverListener != null){
                iShowCoverListener.showCover(COVER_STATE_SHOW_BOTTOM);
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            isMax = false;
            if (iShowCoverListener != null){
                iShowCoverListener.showCover(COVER_STATE_GONE);
            }
        }
    }


    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (iShowCoverListener != null && isMax) {
                if (scrollY <= 10) {
                    iShowCoverListener.showCover(COVER_STATE_SHOW_BOTTOM);
                    return;
                }
                if (clampedY){
                    iShowCoverListener.showCover(COVER_STATE_SHOW_TOP);
                    return;
                }

                iShowCoverListener.showCover(COVER_STATE_SHOW);
        }
    }


    public void setiShowCoverListener(IShowCoverListener iShowCoverListener) {
        this.iShowCoverListener = iShowCoverListener;
    }

    public interface IShowCoverListener {
        void showCover(int coverState);
    }

}
