package cn.licht.mobile.anim;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FXM extends FrameLayout {

    private int maxHeight;

    public FXM(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public FXM(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public FXM(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }
    private void init(Context context, AttributeSet attrs) {
        if (context != null && attrs != null) {
//            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FXM);
//            maxHeight = typedArray.getDimensionPixelSize(R.styleable.FXM_maxHeight, 0);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (size > maxHeight){
            int makeMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, makeMeasureSpec);
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        Log.i("zyl FMX",maxHeight+"~~~"+size);
    }
}
