package cn.licht.mobile.anim.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        final View view = rootView.findViewById(R.id.bg);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Utils.dp2px(getContext(), 200);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                final int width = layoutParams.width;
                ValueAnimator valueAnimator = ValueAnimator.ofInt(i);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int animatedValue = (int) animation.getAnimatedValue();
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.width = width + animatedValue;
                        view.setLayoutParams(layoutParams);
                    }
                });
                valueAnimator.setDuration(280);
                valueAnimator.start();
            }
        });


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
