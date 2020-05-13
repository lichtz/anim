package cn.licht.mobile.anim.frzz;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
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

    private View.OnClickListener  onFoldContainClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startUnfoldAnim();
        }
    };
    private View.OnClickListener  onFoldButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startFoldAnim();
        }
    };
    private View.OnClickListener onCloseIconClickListner =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            detach();
        }
    };

    private View unFolfContain;
    private View unFolfContainAlpha;
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
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) foldContain.getLayoutParams();
        layoutParams.gravity = getFoldViewAside() == 0 ? Gravity.START : Gravity.END;
        foldContain.setLayoutParams(layoutParams);
        View unFloldView = createUnFoldView(rootView);
        unFolfContain = unFloldView.findViewById(R.id.unfold_contain);
        unFolfContainAlpha = unFloldView.findViewById(R.id.unfold_alpha_fm);
        View foldButton = unFloldView.findViewById(R.id.fold_button);
        View closeIcon = unFloldView.findViewById(R.id.close);
        closeIcon.setOnClickListener(onCloseIconClickListner);
        foldContain.setOnClickListener(onFoldContainClickListener);
        foldButton.setOnClickListener(onFoldButtonClickListener);
    }

    View leftUnFoldView = null;
    View rightFoldView = null;
    View unFoldView = null;

    private View createUnFoldView(FrameLayout rootView) {
        int state = getFoldViewAside();
        if (state == 0) {
            if (leftUnFoldView == null) {
                leftUnFoldView = LayoutInflater.from(getContext()).inflate(R.layout.include_unfold_speak_leftfloat_view, rootView, false);
            }
            if (unFoldView == leftUnFoldView) {
                return unFoldView;
            }
            FrameLayout spearkContain = rootView.findViewById(R.id.speark_contain);
            spearkContain.removeView(unFoldView);
            unFoldView = leftUnFoldView;
            spearkContain.addView(unFoldView);
        } else {
            if (rightFoldView == null) {
                rightFoldView = LayoutInflater.from(getContext()).inflate(R.layout.include_unfold_speak_rightfloat_view, rootView, false);
            }
            if (unFoldView == rightFoldView) {
                return unFoldView;
            }

            FrameLayout spearkContain = rootView.findViewById(R.id.speark_contain);
            spearkContain.removeView(unFoldView);
            unFoldView = rightFoldView;
            spearkContain.addView(unFoldView);

        }
        return unFoldView;
    }


    private void startFoldAnim() {
        onViewCreated(getRootView());
        lastDiff =0;
        foldContain.setVisibility(View.VISIBLE);
        int foldViewAside = getFoldViewAside();
        ValueAnimator valueAnimator;
        if (foldViewAside == 0) {
            int endLeft = getUnfoldViewWidth() - getfoldViewWidth();
            final int leftMargin = getUnFoldParams().leftMargin;
            valueAnimator = ValueAnimator.ofInt(endLeft);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatedValue = (int) animation.getAnimatedValue();
                    getUnFoldParams().leftMargin = leftMargin - animatedValue;
                    invalidateLeftUnFoldView();
                }
            });


        } else {
            int leftDiff = getUnfoldViewWidth() - getfoldViewWidth();
            valueAnimator = ValueAnimator.ofInt(leftDiff);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatedValue = (int) animation.getAnimatedValue();
                    invalidateRightAnimView(animatedValue - lastDiff  );
                    lastDiff = animatedValue;
                }
            });

        }
        Animator alphaUnFoldAnimator = ObjectAnimator.ofFloat(unFolfContainAlpha, "alpha", 1, 0);
        alphaUnFoldAnimator.setDuration(200);

        Animator alphaFoldAnimator = ObjectAnimator.ofFloat(foldContain, "alpha", 0, 1);
        alphaFoldAnimator.setDuration(200);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(alphaUnFoldAnimator, alphaFoldAnimator);


        valueAnimator.setDuration(280);
        valueAnimator.setInterpolator(new EaseCubicInterpolator(1, 0, 0.83f, 1));
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animatorSet.start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setFoldViewStyle();
                unFolfContain.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        valueAnimator.start();


    }

    private int lastDiff = 0;

    private void startUnfoldAnim() {
        onViewCreated(getRootView());
        lastDiff = 0;
        foldContain.setBackgroundResource(R.drawable.shape_speak_float_view_unstroke_bg);
        unFolfContain.setVisibility(View.VISIBLE);
        int foldViewAside = getFoldViewAside();
        final ValueAnimator valueAnimator;
        if (foldViewAside == 0) {
            final int startLeft = getUnfoldViewWidth() - getfoldViewWidth();
            FrameLayout.LayoutParams unFoldParams = getUnFoldParams();
            unFoldParams.leftMargin = -startLeft;
            unFoldParams.rightMargin = 0;
            valueAnimator = ValueAnimator.ofInt(startLeft);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatedValue = (int) animation.getAnimatedValue();
                    getUnFoldParams().leftMargin = animatedValue - startLeft;
                    invalidateLeftUnFoldView();
                }
            });

        } else {
             int leftDiff = getUnfoldViewWidth() - getfoldViewWidth();
            valueAnimator = ValueAnimator.ofInt(leftDiff);

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    int animatedValue = (int) animation.getAnimatedValue();
                    invalidateRightAnimView(lastDiff - animatedValue);
                    lastDiff = animatedValue;
                }
            });
        }
        valueAnimator.setInterpolator(new EaseCubicInterpolator(1, 0, 0.83f, 1));
        valueAnimator.setDuration(280);
        Animator alphaAnimator = ObjectAnimator.ofFloat(foldContain, "alpha", 1, 0);
        alphaAnimator.setDuration(200);
        alphaAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                valueAnimator.start();

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

        Animator alphaUnfoldAnimator = ObjectAnimator.ofFloat(unFolfContainAlpha, "alpha", 0, 1);
        alphaUnfoldAnimator.setDuration(200);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(alphaAnimator, alphaUnfoldAnimator);
        animatorSet.start();


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

    private int getUnfoldViewWidth() {
        int width = 0;
        if (unFolfContain != null) {
            width = unFolfContain.getMeasuredWidth();
            if (width == 0) {
                width = Utils.dp2px(getContext(), 313.5f);
            }
        }
        return width;
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

    @Override
    public boolean canDrag() {
        return unFolfContain.getVisibility() == View.GONE;
    }


    private boolean isAnimRunning() {
        return unFoldView.getVisibility() == View.VISIBLE;

    }

    /**
     * 0 左边 1 右边
     *
     * @return
     */
    private int getFoldViewAside() {
        boolean isLeft  = true;
        if (FloatViewConstant.isSystemFloatMode()){
            if (getSystemLayoutParams() != null) {

                if (getSystemLayoutParams().x > 10) {
                    isLeft = false;
                }
            }
        }else {
            if (getNormalLayoutParams() != null) {
                if (getNormalLayoutParams().leftMargin > 10) {
                    isLeft = false;
                }
            }
        }
        return isLeft?0:1;
    }

    private FrameLayout.LayoutParams getUnFoldParams() {
        return (FrameLayout.LayoutParams) unFolfContain.getLayoutParams();
    }

    private void invalidateLeftUnFoldView() {
        unFolfContain.setLayoutParams(getUnFoldParams());
    }

    private void invalidateRightAnimView(int dx) {
        if (isSystemMode()) {
            getSystemLayoutParams().x += dx;
        } else {
            getNormalLayoutParams().leftMargin += dx;
        }
        invalidate();

    }


    private void setFoldViewStyle(){
        if (foldContain != null){
            int foldViewAside = getFoldViewAside();
            if (foldViewAside  == 0){
                foldContain.setBackgroundResource(R.drawable.shape_speak_float_view_left_bg);
            }else {
                foldContain.setBackgroundResource(R.drawable.shape_speak_float_view_right_bg);
            }
        }
    }


}
