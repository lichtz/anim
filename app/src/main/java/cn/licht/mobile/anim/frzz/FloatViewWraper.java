package cn.licht.mobile.anim.frzz;

import android.view.ViewGroup;

public class FloatViewWraper {

    private String activityCanonicalName;
    private Class<? extends AbsFloatView> floatView;
    private String floatViewName;
    public int mode = FloatViewConstant.MODE_GLOBAL_FLOAT_VIEW;

    public FloatViewWraper(Class<? extends AbsFloatView> floatView, String activityCanonicalName) {
        this.floatView = floatView;
        floatViewName = floatView.getSimpleName();
        this.activityCanonicalName = activityCanonicalName;
    }

    public String getFloatViewName() {
        return floatViewName;
    }

    public Class<? extends AbsFloatView> getFloatView() {
        return floatView;
    }

    public String getBindActivityCanonicalName() {
        return activityCanonicalName;
    }
}
