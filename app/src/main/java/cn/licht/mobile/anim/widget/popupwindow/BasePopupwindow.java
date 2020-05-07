package cn.licht.mobile.anim.widget.popupwindow;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.core.content.ContextCompat;

import cn.licht.mobile.anim.R;


/**
 * Created by ly on 2018/4/28.
 */

public class BasePopupwindow extends PopupWindow {

    private static final String TAG = "BasePopWindow";
    private Activity activity;
    private ValueAnimator valueAnimator;
    private OnSelectListener onSelectListener;
    private boolean isFinish;

    protected void init(Activity activity) {
        this.activity = activity;
        try {
//            HideSoftInputUtil.hideKeyboard(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);////获取焦点
        setTouchable(true);
        setOutsideTouchable(false);//设置popupwindow外部可点击
        ColorDrawable dw = new ColorDrawable(0x7f000000);// 实例化一个ColorDrawable颜色为半透明
//        setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(activity,R.color.permissions_bg_endcolor)));// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        int identifier = activity.getApplication().getResources().getIdentifier("PopupWindowAnimation", "style", activity.getPackageName());
        setAnimationStyle(identifier);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                hideBackground();

                if (onSelectListener != null && !isFinish) {
                    isFinish = true;
                    onSelectListener.onCancel();
                }
            }
        });
    }

    @Override
    public void dismiss() {
        dismiss(false);
    }

    public void dismiss(boolean isFinish) {
        this.isFinish = isFinish;
        BasePopupwindow.super.dismiss();

    }

    public void showPopupWindow() {
        try {
            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            showBackground();
        } catch (Exception e) {
        }
    }

    @Override
    public void showAtLocation(final View parent, final int gravity, final int x, final int y) {
        BasePopupwindow.super.showAtLocation(parent, gravity, x, y);

    }

    @Override
    public void showAsDropDown(final View anchor) {
        BasePopupwindow.super.showAsDropDown(anchor);

    }

    @Override
    public void showAsDropDown(final View anchor, final int xoff, final int yoff) {
        BasePopupwindow.super.showAsDropDown(anchor, xoff, yoff);

    }

    @Override
    public void showAsDropDown(final View anchor, final int xoff, final int yoff, final int gravity) {
        BasePopupwindow.super.showAsDropDown(anchor, xoff, yoff, gravity);

    }


    protected void showBackground() {
        backgroundAlpha(0.2f);
//        if (valueAnimator != null && valueAnimator.isRunning()) {
//            valueAnimator.end();
//        }
//        valueAnimator = ValueAnimator.ofFloat(1.0f, 0.5f);
//        valueAnimator.setDuration(300);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                Float value = (Float) animation.getAnimatedValue();
//                backgroundAlpha(value);
//            }
//        });
//        valueAnimator.start();
    }

    public void hideBackground() {

        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.end();
        }
        valueAnimator = ValueAnimator.ofFloat(0.2f, 1.0f);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                backgroundAlpha(value);
            }
        });
        valueAnimator.start();
    }

    protected void backgroundAlpha(float bgAlpha) {
        if (activity == null) {
            return;
        }
//        activity.getWindow().setBackgroundDrawable();
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public OnSelectListener getOnSelectListener() {
        return onSelectListener;
    }

    public interface OnSelectListener {
        void onListener(View view, int position);

        void onCancel();
    }
}
