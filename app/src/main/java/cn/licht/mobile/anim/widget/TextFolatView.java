package cn.licht.mobile.anim.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import cn.licht.mobile.anim.R;
import cn.licht.mobile.anim.Utils;
import cn.licht.mobile.anim.frzz.AbsFloatView;
import cn.licht.mobile.anim.frzz.FloatViewLayoutParams;

public class TextFolatView extends AbsFloatView {
    @Override
    public void onCreate(Context context) {

    }

    @Override
    public View onCreateView(Context context, FrameLayout rootView) {
        return LayoutInflater.from(context).inflate(R.layout.texts_layout,rootView,false);
    }

    @Override
    public void onViewCreated(FrameLayout rootView) {
        rootView.setTranslationX(-70);

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void initFloatViewLayoutParams(FloatViewLayoutParams params) {

        params.x = 0;
        params.y = 700;
        params.width = FloatViewLayoutParams.WRAP_CONTENT;
        params.height = FloatViewLayoutParams.WRAP_CONTENT;
    }

}
