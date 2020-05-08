package cn.licht.mobile.anim.frzz;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import cn.licht.mobile.anim.R;
import cn.licht.mobile.anim.Utils;

public class IFlySpeakFloatView extends AbsFloatView {

    private static final String TAG = "IFlySpeakFloatView";
    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            saveFloatViewPos();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            saveFloatViewPos();
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    @Override
    public void onCreate(Context context) {

    }

    @Override
    public View onCreateView(Context context, FrameLayout rootView) {
        return LayoutInflater.from(context).inflate(R.layout.ifly_speak_layout, rootView, false);
    }

    @Override
    public void onViewCreated(FrameLayout rootView) {
        final FrameLayout control = rootView.findViewById(R.id.voice_control);
        final View viewById = rootView.findViewById(R.id.control);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                control.setVisibility(View.VISIBLE);
            }
        });
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                viewById.setVisibility(View.VISIBLE);
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
        Log.d(TAG, "initFloatViewLayoutParams: "+params.toString());
        params.x = 0;
        params.y = 700;
        params.width = FloatViewLayoutParams.WRAP_CONTENT;
        params.height = FloatViewLayoutParams.WRAP_CONTENT;

    }

    @Override
    public void onUp(int x, int y) {
        int appScreenWidth = Utils.getAppScreenWidth(getContext());
        int measuredWidth = getRootView().getMeasuredWidth();
        if (isSystemMode()){
            WindowManager.LayoutParams systemLayoutParams = getSystemLayoutParams();
            if (    systemLayoutParams.x> (appScreenWidth - measuredWidth) / 2) {
                startAnim( systemLayoutParams.x, appScreenWidth - measuredWidth,animatorListener);
            } else {

                startAnim( systemLayoutParams.x, 0,animatorListener);
            }

        }else {
            FrameLayout.LayoutParams normalLayoutParams = getNormalLayoutParams();
            if (normalLayoutParams.leftMargin > (appScreenWidth - measuredWidth) / 2) {

                startAnim(normalLayoutParams.leftMargin, appScreenWidth - measuredWidth,animatorListener);
            } else {
                startAnim( normalLayoutParams.leftMargin, 0,animatorListener);
            }
        }
    }


    private void startAnim( int startLeftMargin, int stopLeftMargin,Animator.AnimatorListener animatorListener) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startLeftMargin, stopLeftMargin);
        valueAnimator.setDuration(250);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isSystemMode()){
                    getSystemLayoutParams().x =  (int) animation.getAnimatedValue();
                }else {
                    getNormalLayoutParams().leftMargin = (int) animation.getAnimatedValue();
                }
                invalidate();
            }
        });
        valueAnimator.addListener(animatorListener);
        valueAnimator.start();

    }

}
