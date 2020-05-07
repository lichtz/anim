package cn.licht.mobile.anim.frzz;

import android.app.Activity;

import com.didichuxing.doraemonkit.kit.core.AbsDokitView;

public class FloatViewWraper {
    private Activity bindActivity;
    private Class<? extends AbsFloatView> floatView;
    private String tag;

    public FloatViewWraper(Class<? extends AbsFloatView> floatView,Activity activity) {
        this.floatView = floatView;
        tag = floatView.getSimpleName();
        this.bindActivity = activity;
    }

    public String getTag() {
        return tag;
    }

    public Class<? extends AbsFloatView> getFloatView() {
        return floatView;
    }

    public Activity getActivity() {
        return bindActivity;
    }
}
