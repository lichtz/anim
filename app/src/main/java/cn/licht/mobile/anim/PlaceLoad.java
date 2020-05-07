package cn.licht.mobile.anim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PlaceLoad  extends ImageView {
    public PlaceLoad(Context context) {
        super(context);
        init();
    }

    public PlaceLoad(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlaceLoad(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        Log.i("zyl","imageResourec"+resId);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        Log.i("zyl","setImageBitmap");
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        setBackgroundColor(Color.TRANSPARENT);
        super.setImageDrawable(drawable);
        Log.i("zyl","setImageDrawable");
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        Log.i("zyl","setImageURI");
    }
}
