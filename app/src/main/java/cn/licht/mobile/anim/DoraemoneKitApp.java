package cn.licht.mobile.anim;

import android.app.Application;

import cn.licht.mobile.anim.frzz.FloatViewActivityLifecycleCallbacks;
import cn.licht.mobile.anim.frzz.FloatViewManager;

public class DoraemoneKitApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new FloatViewActivityLifecycleCallbacks());
        FloatViewManager.getInstance().init(this);
    }
}
