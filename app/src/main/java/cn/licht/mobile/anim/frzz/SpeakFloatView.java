package cn.licht.mobile.anim.frzz;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentManager;

import cn.licht.mobile.anim.R;
import cn.licht.mobile.anim.Utils;

public class SpeakFloatView extends AbsFloatView implements ISpeakFloatViewControl {

    private static final String TAG = "IFlySpeakFloatView";

    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            saveFloatViewPos();
            setFoldViewStyle();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            saveFloatViewPos();
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private View.OnClickListener onFoldContainClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startUnfoldAnim();
        }
    };


    private View foldContain;


    @Override
    public void onCreate(Context context) {

    }

    @Override
    public View onCreateView(Context context, FrameLayout rootView) {
        return LayoutInflater.from(context).inflate(R.layout.ifly_speak_layout, rootView, false);
    }


    @Override
    public void onViewCreated(FrameLayout rootView) {
        foldContain = rootView.findViewById(R.id.fold_contain);
        foldContain.setOnClickListener(onFoldContainClickListener);

    }


    private void attachUnFloatView() {
        int state = getFoldViewAside();
        if (state == 0) {
            AbsFloatView floatView = FloatViewManager.getInstance().getFloatView(getActivity(), SpeakFloatRightView.class.getSimpleName());
            if (floatView != null) {
                FloatViewManager.getInstance().detach(SpeakFloatRightView.class);
            }

            FloatViewWraper floatViewWraper = new FloatViewWraper(SpeakFloatLeftView.class, getActivity().getClass().getCanonicalName());
            FloatViewManager.getInstance().attach(getActivity(), floatViewWraper);

        } else {
            AbsFloatView floatView = FloatViewManager.getInstance().getFloatView(getActivity(), SpeakFloatLeftView.class.getSimpleName());
            if (floatView != null) {
                FloatViewManager.getInstance().detach(SpeakFloatLeftView.class);
            }
            FloatViewWraper floatViewWraper = new FloatViewWraper(SpeakFloatRightView.class, getActivity().getClass().getCanonicalName());
            FloatViewManager.getInstance().attach(getActivity(), floatViewWraper);

        }


    }




    private void startUnfoldAnim() {
        attachUnFloatView();
        Animator alphaAnimator = ObjectAnimator.ofFloat(foldContain, "alpha", 1, 0);
        alphaAnimator.setDuration(2000);
        alphaAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                foldContain.setBackgroundResource(R.drawable.shape_speak_float_view_unstroke_bg);
                int foldViewAside = getFoldViewAside();
                AbsFloatView floatView = null;
                if (foldViewAside == 0) {
                    floatView = FloatViewManager.getInstance().getFloatView(getActivity(), SpeakFloatLeftView.class.getSimpleName());

                } else {
                    floatView = FloatViewManager.getInstance().getFloatView(getActivity(), SpeakFloatRightView.class.getSimpleName());
                }
                if (floatView instanceof ISpeakFloatViewControl) {
                    ((ISpeakFloatViewControl) floatView).onUnFoldViewAnimStart();
                }


            }

            @Override
            public void onAnimationEnd(Animator animation) {
                foldContain.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        alphaAnimator.start();

    }

    private void startFoldAnim() {
        attachUnFloatView();
        foldContain.setVisibility(View.VISIBLE);
        Animator alphaFoldAnimator = ObjectAnimator.ofFloat(foldContain, "alpha", 0, 1);
        alphaFoldAnimator.setDuration(2000);
        alphaFoldAnimator.start();
    }



    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void initFloatViewLayoutParams(FloatViewLayoutParams params) {
        Log.d(TAG, "initFloatViewLayoutParams: " + params.toString());
        int foldViewAside = getFoldViewAside();

        params.x = foldViewAside == 0 ? 0 : Utils.getAppScreenWidth(getContext()) - getfoldViewWidth();
        params.y = 700;
        params.width = FloatViewLayoutParams.WRAP_CONTENT;
        params.height = FloatViewLayoutParams.WRAP_CONTENT;

    }

    @Override
    public void onMove(int x, int y, int dx, int dy) {
        super.onMove(x, y, dx, dy);
        foldContain.setBackgroundResource(R.drawable.shape_speak_float_view_move_bg);
    }

    @Override
    public void onUp(int x, int y) {
        int appScreenWidth = Utils.getAppScreenWidth(getContext());
        int measuredWidth = getRootView().getMeasuredWidth();
        if (isSystemMode()) {
            WindowManager.LayoutParams systemLayoutParams = getSystemLayoutParams();
            if (systemLayoutParams.x > (appScreenWidth - measuredWidth) / 2) {
                startAnim(systemLayoutParams.x, appScreenWidth - measuredWidth, animatorListener);
            } else {

                startAnim(systemLayoutParams.x, 0, animatorListener);
            }

        } else {
            FrameLayout.LayoutParams normalLayoutParams = getNormalLayoutParams();
            if (normalLayoutParams.leftMargin > (appScreenWidth - measuredWidth) / 2) {

                startAnim(normalLayoutParams.leftMargin, appScreenWidth - measuredWidth, animatorListener);
            } else {
                startAnim(normalLayoutParams.leftMargin, 0, animatorListener);
            }
        }
    }


    private void startAnim(int startLeftMargin, int stopLeftMargin, Animator.AnimatorListener animatorListener) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startLeftMargin, stopLeftMargin);
        valueAnimator.setDuration(250);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isSystemMode()) {
                    getSystemLayoutParams().x = (int) animation.getAnimatedValue();
                } else {
                    getNormalLayoutParams().leftMargin = (int) animation.getAnimatedValue();
                }
                invalidate();
            }
        });
        valueAnimator.addListener(animatorListener);
        valueAnimator.start();

    }


    private int getfoldViewWidth() {
        int width = 0;
        if (foldContain != null) {
            width = foldContain.getMeasuredWidth();
            if (width == 0) {
                width = Utils.dp2px(getContext(), 60.5f);
            }
        }
        return width;
    }


    /**
     * 0 左边 1 右边
     *
     * @return
     */
    private int getFoldViewAside() {
        boolean isLeft = true;
        if (FloatViewConstant.isSystemFloatMode()) {
            if (getSystemLayoutParams() != null) {

                if (getSystemLayoutParams().x > 10) {
                    isLeft = false;
                }
            }
        } else {
            if (getNormalLayoutParams() != null) {
                if (getNormalLayoutParams().leftMargin > 10) {
                    isLeft = false;
                }
            }
        }
        return isLeft ? 0 : 1;
    }


    private void setFoldViewStyle() {
        if (foldContain != null) {
            int foldViewAside = getFoldViewAside();
            if (foldViewAside == 0) {
                foldContain.setBackgroundResource(R.drawable.shape_speak_float_view_left_bg);
            } else {
                foldContain.setBackgroundResource(R.drawable.shape_speak_float_view_right_bg);
            }
        }
    }




    @Override
    public void onFoldViewAnimStart() {
        startFoldAnim();

    }

    @Override
    public void onFoldViewAnimStop() {
        setFoldViewStyle();

    }

    @Override
    public void onUnFoldViewAnimStart() {
        startUnfoldAnim();

    }

    @Override
    public void onUnFoldViewAnimStop() {

    }

    @Override
    public void onClose() {
        detach();
    }
}
