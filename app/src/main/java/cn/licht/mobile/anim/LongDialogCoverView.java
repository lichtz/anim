package cn.licht.mobile.anim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Util;



public class LongDialogCoverView extends TextView implements MaxScrollView.IShowCoverListener {
    private static final String TAG = "XXFrameLayout";

    private int width;
    private int height;
    private Bitmap topBitmap;
    private Bitmap bottomBitmap;
    private Rect topReact;
    private Rect bottomReact;
    private Paint paint;

    private int coverState = MaxScrollView.COVER_STATE_GONE;

    public LongDialogCoverView(Context context) {
        super(context);
        init();
    }


    public LongDialogCoverView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LongDialogCoverView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        topBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.s);
        bottomBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.m);
        paint = new Paint();
        topReact = new Rect();
        bottomReact = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        height = MeasureSpec.getSize(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw:");
        if (coverState != MaxScrollView.COVER_STATE_GONE && topBitmap != null && !topBitmap.isRecycled() && bottomBitmap != null && !bottomBitmap.isRecycled()) {
            topReact.set(0, 0, width, 100);
            bottomReact.set(0, height - 100, width, height);
            if (coverState ==MaxScrollView .COVER_STATE_SHOW || coverState ==MaxScrollView. COVER_STATE_SHOW_TOP) {
                canvas.drawBitmap(topBitmap, null, topReact, paint);
            }
            if (coverState ==MaxScrollView. COVER_STATE_SHOW || coverState ==MaxScrollView. COVER_STATE_SHOW_BOTTOM) {
                canvas.drawBitmap(bottomBitmap, null, bottomReact, paint);
            }
        }

    }

    @Override
    public void showCover(int coverState) {
        if (this.coverState != coverState){
            this.coverState = coverState;
            postInvalidate();
        }

        Log.i(TAG,"coverState :" +coverState);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        bottomBitmap = null;
        bottomReact = null;
    }
}
