package cn.licht.mobile.anim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class CoustomImageView extends androidx.appcompat.widget.AppCompatImageView {

    private boolean isPress;

    public CoustomImageView(Context context) {
        super(context);
    }

    public CoustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CoustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        isPress = true;
        setPressed(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isPress) {
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            canvas.drawRect(new Rect(0, 0, 200, 200), paint);
        }
        super.onDraw(canvas);
    }


}
