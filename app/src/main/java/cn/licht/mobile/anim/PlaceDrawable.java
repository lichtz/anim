package cn.licht.mobile.anim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PlaceDrawable extends Drawable {
    private Bitmap bitmap;
    public PlaceDrawable(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        if (bounds == null){
            return;
        }
        int width = bounds.width();
        int height = bounds.height();
        if (width<=0 || height <=0){
            return;
        }

        Log.i("zyl", bitmap.isRecycled()+"NIMEIDE");
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawRect(new RectF(0,0,width,height),paint);
        if (width>bitmap.getWidth()){
            int letf = (width - bitmap.getWidth()) / 2;
            int top = (height - bitmap.getHeight()) / 2;
            canvas.drawBitmap(bitmap, letf, top, paint);
        }

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
