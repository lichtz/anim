package cn.licht.mobile.anim.frzz;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import cn.licht.mobile.anim.R;
import cn.licht.mobile.anim.Utils;

public class SpeakFloatRightView extends AbsFloatView implements  ISpeakFloatViewControl {
    private static final String TAG = SpeakFloatRightView.class.getSimpleName();
    private View unFolfContain;
    private View playIcon;
    private View foldButton;
    private View.OnClickListener onFoldButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startFoldAnim();
        }
    };
    private View.OnClickListener onCloseIconClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AbsFloatView floatView = FloatViewManager.getInstance().getFloatView(getActivity(), SpeakFloatView.class.getSimpleName());
            if (floatView instanceof  ISpeakFloatViewControl){
                ((ISpeakFloatViewControl)floatView).onClose();
            }
            detach();
        }
    };


    @Override
    public void onCreate(Context context) {

    }

    @Override
    public View onCreateView(Context context, FrameLayout rootView) {
        return LayoutInflater.from(context).inflate(R.layout.include_unfold_speak_rightfloat_view, rootView,false);
    }

    @Override
    public void onViewCreated(FrameLayout rootView) {
        unFolfContain = rootView.findViewById(R.id.unfold_contain);
        playIcon = rootView.findViewById(R.id.play_icon);
        foldButton = rootView.findViewById(R.id.fold_button);
        View foldButton = rootView.findViewById(R.id.fold_button);
        View closeIcon = rootView.findViewById(R.id.close);
        closeIcon.setOnClickListener(onCloseIconClickListner);
        foldButton.setOnClickListener(onFoldButtonClickListener);
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
        startFoldAnim();
    }

    @Override
    public void initFloatViewLayoutParams(FloatViewLayoutParams params) {
        Point floatViewPos = FloatViewManager.getInstance().getFloatViewPos(SpeakFloatView.class.getSimpleName());
        if (floatViewPos != null) {
            params.y = floatViewPos.y;
            params.x = Utils.getAppScreenWidth(getContext()) - getUnfoldViewWidth();
            params.width = FloatViewLayoutParams.WRAP_CONTENT;
            params.height = FloatViewLayoutParams.WRAP_CONTENT;
        }else {
            params.x = Utils.getAppScreenWidth(getContext()) - getUnfoldViewWidth();
            params.y = 700;
            params.width = FloatViewLayoutParams.WRAP_CONTENT;
            params.height = FloatViewLayoutParams.WRAP_CONTENT;
        }

    }

    private EaseCubicInterpolator easeCubicInterpolator = new EaseCubicInterpolator(1, 0, 0.83f, 1);
    private ObjectAnimator unFoldTranslationxAnimator = null;
    private Animator unFoldPlayIconAlphaAnimator = null;
    private Animator unFoldFoldButtonAlphaAnimator = null;
    private AnimatorSet unFoldAnimatorSet = null;


    private void startUnfoldAnim() {
        unFolfContain.setVisibility(View.VISIBLE);
        if (unFoldTranslationxAnimator != null && unFoldTranslationxAnimator.isRunning()) {
            return;
        }
        int startLeft =  getUnfoldViewWidth() - getfoldViewWidth();
        unFolfContain.setTranslationX(startLeft);
        if (unFoldTranslationxAnimator == null) {
            unFoldTranslationxAnimator = ObjectAnimator.ofFloat(unFolfContain, "translationX", startLeft, 0);
            unFoldTranslationxAnimator.setDuration(280);
            unFoldTranslationxAnimator.setInterpolator(easeCubicInterpolator);
        }
        int startLeft2 = getfoldViewWidth() - getUnfoldViewWidth();

        if (unFoldPlayIconAlphaAnimator == null) {
            unFoldPlayIconAlphaAnimator = ObjectAnimator.ofFloat(playIcon, "alpha", 0, 1);
            unFoldPlayIconAlphaAnimator.setDuration(200);
        }

        if (unFoldFoldButtonAlphaAnimator == null) {
            unFoldFoldButtonAlphaAnimator = ObjectAnimator.ofFloat(foldButton, "alpha", 0, 1);
            unFoldFoldButtonAlphaAnimator.setDuration(200);
        }


        if (unFoldAnimatorSet == null) {
            unFoldAnimatorSet = new AnimatorSet();
            unFoldAnimatorSet.playTogether(unFoldPlayIconAlphaAnimator, unFoldFoldButtonAlphaAnimator);
            unFoldAnimatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    AbsFloatView floatView = FloatViewManager.getInstance().getFloatView(getActivity(), SpeakFloatView.class.getSimpleName());
                    if (floatView instanceof ISpeakFloatViewControl) {
                        ((ISpeakFloatViewControl) floatView).onUnFoldViewAnimStop();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }


        unFoldTranslationxAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                AbsFloatView floatView = FloatViewManager.getInstance().getFloatView(getActivity(), SpeakFloatView.class.getSimpleName());
                if (floatView instanceof ISpeakFloatViewControl) {
                    ((ISpeakFloatViewControl) floatView).onUnFoldViewAnimStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                unFoldAnimatorSet.start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        unFoldTranslationxAnimator.start();


    }

    private Animator foldPlayIconAlphaAnimator = null;
    private Animator foldFoldButtonAlphaAnimator = null;
    private ObjectAnimator foldTranslationxAnimator = null;
    private AnimatorSet foldAnimatorSet = null;
    private void startFoldAnim() {
        attachFloatView();
        if (foldTranslationxAnimator != null && foldTranslationxAnimator.isRunning()){
            return;
        }

        final AbsFloatView floatView = FloatViewManager.getInstance().getFloatView(getActivity(), SpeakFloatView.class.getSimpleName());
        if (foldPlayIconAlphaAnimator == null) {
            foldPlayIconAlphaAnimator = ObjectAnimator.ofFloat(playIcon, "alpha", 1, 0);
            foldPlayIconAlphaAnimator.setDuration(200);
        }
        if (foldFoldButtonAlphaAnimator == null) {
            foldFoldButtonAlphaAnimator = ObjectAnimator.ofFloat(foldButton, "alpha", 1, 0);
            foldFoldButtonAlphaAnimator.setDuration(200);
        }
        if (foldAnimatorSet == null) {
            foldAnimatorSet = new AnimatorSet();
            foldAnimatorSet.playTogether(foldPlayIconAlphaAnimator, foldFoldButtonAlphaAnimator);

        }


        if (foldTranslationxAnimator == null) {
            int startLeft =getUnfoldViewWidth() - getfoldViewWidth()  ;
            foldTranslationxAnimator = ObjectAnimator.ofFloat(unFolfContain, "translationX", 0, startLeft);
            foldTranslationxAnimator.setDuration(280);
            foldTranslationxAnimator.setInterpolator(easeCubicInterpolator);

            foldTranslationxAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                    if (floatView instanceof ISpeakFloatViewControl) {
                        ((ISpeakFloatViewControl) floatView).onFoldViewAnimStop();
                    }
                    unFolfContain.setVisibility(View.GONE);
                    detach();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }




        foldAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                foldTranslationxAnimator.start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        foldAnimatorSet.start();

        int startLeft =getUnfoldViewWidth() - getfoldViewWidth()  ;
        Log.d(TAG, "startFoldAnim: startleftXX :" +startLeft);

    }




    @Override
    public void onFoldViewAnimStart() {

    }

    @Override
    public void onFoldViewAnimStop() {

    }

    @Override
    public void onUnFoldViewAnimStart() {
        Log.d(TAG, "onUnFoldViewAnimStart: ");
        startUnfoldAnim();
    }

    @Override
    public void onUnFoldViewAnimStop() {

    }

    @Override
    public boolean canDrag() {
        return false;
    }

    @Override
    public void onClose() {

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
        return Utils.dp2px(getContext(), 60.5f);
    }


    private void attachFloatView() {
        AbsFloatView floatView = FloatViewManager.getInstance().getFloatView(getActivity(), SpeakFloatView.class.getSimpleName());
        if (floatView == null) {
            FloatViewWraper floatViewWraper = new FloatViewWraper(SpeakFloatView.class, getActivity().getClass().getCanonicalName());
            FloatViewManager.getInstance().attach(getActivity(), floatViewWraper);
        }

    }

}
