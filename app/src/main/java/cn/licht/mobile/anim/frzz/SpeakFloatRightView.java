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
    private View unFolfContainAlpha;
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
        unFolfContainAlpha = rootView.findViewById(R.id.unfold_alpha_fm);
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

    }

    @Override
    public void initFloatViewLayoutParams(FloatViewLayoutParams params) {
        Point floatViewPos = FloatViewManager.getInstance().getFloatViewPos(SpeakFloatView.class.getSimpleName());
        if (floatViewPos != null) {
            params.x = floatViewPos.x - getUnfoldViewWidth() +getfoldViewWidth();
            params.y = floatViewPos.y;
            params.width = FloatViewLayoutParams.WRAP_CONTENT;
            params.height = FloatViewLayoutParams.WRAP_CONTENT;
        }

    }


    private void startUnfoldAnim() {
        unFolfContain.setVisibility(View.VISIBLE);
        int startLeft =  getUnfoldViewWidth() - getfoldViewWidth();
        unFolfContain.setTranslationX(startLeft);
        ObjectAnimator translationxAnimator = ObjectAnimator.ofFloat(unFolfContain, "translationX", startLeft, 0);
        translationxAnimator.setDuration(2800);
        translationxAnimator.setInterpolator(new EaseCubicInterpolator(1, 0, 0.83f, 1));


        Animator alphaUnfoldAnimator = ObjectAnimator.ofFloat(unFolfContainAlpha, "alpha", 0, 1);
        alphaUnfoldAnimator.setDuration(2000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationxAnimator, alphaUnfoldAnimator);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                AbsFloatView  floatView = FloatViewManager.getInstance().getFloatView(getActivity(), SpeakFloatView.class.getSimpleName());
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
        animatorSet.start();




    }


    private void startFoldAnim() {
        final AbsFloatView floatView = FloatViewManager.getInstance().getFloatView(getActivity(), SpeakFloatView.class.getSimpleName());


        Animator alphaUnFoldAnimator = ObjectAnimator.ofFloat(unFolfContainAlpha, "alpha", 1, 0);
        alphaUnFoldAnimator.setDuration(2000);
        int startLeft =getUnfoldViewWidth() - getfoldViewWidth()  ;
        ObjectAnimator translationxAnimator = ObjectAnimator.ofFloat(unFolfContain, "translationX", 0, startLeft);
        translationxAnimator.setDuration(2800);
        translationxAnimator.setInterpolator(new EaseCubicInterpolator(1, 0, 0.83f, 1));
        translationxAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (floatView instanceof ISpeakFloatViewControl) {
                    ((ISpeakFloatViewControl) floatView).onFoldViewAnimStart();
                }
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


        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(alphaUnFoldAnimator, translationxAnimator);
        animatorSet.start();


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
}
