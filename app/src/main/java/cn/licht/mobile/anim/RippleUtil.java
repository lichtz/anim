package cn.licht.mobile.anim;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class RippleUtil {
    public static int defaultPressedColor = Color.parseColor("#EFEFEF");
    public static int defaultUnPressedColor = Color.TRANSPARENT;

    public static void onDispatch(View view, MotionEvent event) {
        if (view == null) {
            return;
        }
        onDispatch(view, event, defaultPressedColor, defaultUnPressedColor);

    }

    public static void onDispatch(View view, MotionEvent event, int pressedColor, int upPressColor) {
        if (view == null) {
            return;
        }
        if (view.hasOnClickListeners()) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setTouchBackgroundColor(view, pressedColor);
                    break;
                case MotionEvent.ACTION_UP:
                    setTouchBackgroundColor(view, upPressColor);
                    break;
                default:
                    break;
            }

        }
    }


    private static void setTouchBackgroundColor(View view, int color) {
        boolean hasRippleChilden = false;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            if (childCount > 0) {
             hasRippleChilden =    isHasRippleChilden(color,hasRippleChilden, viewGroup);
                if (!hasRippleChilden) {
                    view.setBackgroundColor(color);
                }
            } else {
                view.setBackgroundColor(color);
            }
        } else {
            view.setBackgroundColor(color);
        }

    }

    private static boolean isHasRippleChilden(int color,boolean hasRippleChilden, ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
               hasRippleChilden =   isHasRippleChilden(color,hasRippleChilden, (ViewGroup) child);
            } else {
                if (child instanceof IRippleLayout) {
                    child.setBackgroundColor(color);
                    hasRippleChilden = true;
                }
            }
        }
        return hasRippleChilden;
    }
}
