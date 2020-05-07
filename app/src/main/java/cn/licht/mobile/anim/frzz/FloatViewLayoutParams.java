package cn.licht.mobile.anim.frzz;

import android.view.ViewGroup;
import android.view.WindowManager;

public class FloatViewLayoutParams {
    /**
     * 悬浮窗不能获取焦点
     */
    public static int FLAG_NOT_FOCUSABLE = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    /**
     * 悬浮窗不能获取焦点并且不相应触摸
     */
    public static int FLAG_NOT_FOCUSABLE_AND_NOT_TOUCHABLE = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

    public static int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;


    /**
     * 只针对系统悬浮窗起作用 值基本上为以上2个
     */
    public int flags;
    /**
     * 只针对系统悬浮窗起作用 值基本上为Gravity
     */
    public int gravity;
    public int x;
    public int y;
    public int width;
    public int height;

}
