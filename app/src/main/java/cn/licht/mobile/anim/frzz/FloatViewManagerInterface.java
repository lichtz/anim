package cn.licht.mobile.anim.frzz;

import android.app.Activity;

import java.util.Map;

public interface FloatViewManagerInterface {

    void attach(FloatViewWraper floatViewData);

    void detach(AbsFloatView absFloatView);

    void detach(Activity activity,AbsFloatView absFloatView);

    void detach(String tag);

    void detach(Activity activity,String tag);

    void detach(Class<? extends AbsFloatView> absFlowView);

    void detach(Activity activity, Class<? extends AbsFloatView> absFlowView);

    void detachAll();

   AbsFloatView getFloatView(Activity activity, String tag);


    Map<String, AbsFloatView> getFloatViews(Activity activity);

    void notifyBackground();

    void notifyForeground();

    void onActivityDestroy(Activity activity);

    void onActivityCreate(Activity activity);

    void onActivityResume(Activity activity);

    void onActivityPause(Activity activity);

    void resumeAndAttachDokitViews(Activity activity);
}
