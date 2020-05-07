package cn.licht.mobile.anim.frzz;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SystemFloatViewManager implements FloatViewManagerInterface {
    private static final String TAG = "FloatPageManager";
    private WindowManager mWindowManager = FloatViewManager.getInstance().getWindowManager();
    private Context mContext;
    private List<AbsFloatView> mFloatViews = new ArrayList<>();
    private List<FloatViewManager.FloatViewAttachedListener> mListeners = new ArrayList<>();

    public SystemFloatViewManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void attach(FloatViewWraper floatViewData) {
        try {
            if (mFloatViews == null) {
                return;
            }
            if (floatViewData == null || floatViewData.getFloatView() == null){
                return;
            }
            for (AbsFloatView floatView :mFloatViews){
                if (floatViewData.getFloatView().isInstance(floatView)){
                    return;
                }
            }
            AbsFloatView floatView = floatViewData.getFloatView().newInstance();
            mFloatViews.add(floatView);
            floatView.performCreate(mContext);
            mWindowManager.addView(floatView.getRootView(),floatView.getSystemLayoutParams());
            floatView.onResume();
            if (FloatViewConstant.IS_SYSTEM_FLOAT_MODE){
                for (FloatViewManager.FloatViewAttachedListener floatViewAttachedListener :mListeners) {
                    floatViewAttachedListener.onFloatViewAdd(floatView);
                }
            }

        } catch (Exception e) {

        }

    }

    @Override
    public void detach(AbsFloatView absFloatView) {
        detach(absFloatView.getClass().getSimpleName());
    }

    @Override
    public void detach(Activity activity, AbsFloatView absFloatView) {

    }

    @Override
    public void detach(String tag) {
        if (TextUtils.isEmpty(tag) || mWindowManager == null || mFloatViews == null) {
            return;
        }

        for (Iterator<AbsFloatView> it = mFloatViews.iterator(); it.hasNext(); ) {
            AbsFloatView floatView = it.next();
            if (tag.equals(floatView.mTag)) {
                mWindowManager.removeView(floatView.getRootView());
                floatView.performDestroy();
                it.remove();
                return;
            }
        }

    }

    @Override
    public void detach(Activity activity, String tag) {

    }

    @Override
    public void detach(Class<? extends AbsFloatView> absFlowView) {

    }

    @Override
    public void detach(Activity activity, Class<? extends AbsFloatView> absFlowView) {

    }

    @Override
    public void detachAll() {
        if (mFloatViews == null) {
            return;
        }
        for (Iterator<AbsFloatView> it = mFloatViews.iterator(); it.hasNext(); ) {
            AbsFloatView floatView = it.next();
            mWindowManager.removeView(floatView.getRootView());
            floatView.performDestroy();
            it.remove();
        }
    }

    @Override
    public AbsFloatView getFloatView(Activity activity, String tag) {
        if (mFloatViews == null) {
            return null;
        }
        if (TextUtils.isEmpty(tag)) {
            return null;
        }
        for (AbsFloatView dokitView : mFloatViews) {
            if (tag.equals(dokitView.mTag)) {
                return dokitView;
            }
        }
        return null;
    }

    @Override
    public Map<String, AbsFloatView> getFloatViews(Activity activity) {
        if (mFloatViews == null) {
            return new HashMap<>();
        }
        Map<String, AbsFloatView> floatViewMap = new HashMap<>();
        for (AbsFloatView floatView : mFloatViews) {
            floatViewMap.put(floatView.mTag, floatView);
        }
        return floatViewMap;
    }

    @Override
    public void notifyBackground() {
        if (mFloatViews == null) {
            return;
        }
        for (AbsFloatView floatView : mFloatViews) {
            floatView.onAppBackground();
        }
    }

    @Override
    public void notifyForeground() {
        if (mFloatViews == null) {
            return;
        }
        for (AbsFloatView floatView : mFloatViews) {
            floatView.onAppForeground();
        }
    }

    @Override
    public void onActivityDestroy(Activity activity) {

    }

    @Override
    public void onActivityCreate(Activity activity) {
    }

    @Override
    public void onActivityResume(Activity activity) {

    }

    @Override
    public void onActivityPause(Activity activity) {
        Map<String, AbsFloatView> floatViews = getFloatViews(activity);
        for (AbsFloatView floatView : floatViews.values()) {
            floatView.onPause();
        }
    }

    @Override
    public void resumeAndAttachDokitViews(Activity activity) {
        ActivityLifecycleInfo activityLifecycleInfo = FloatViewConstant.ACTIVITY_LIFECYCLE_INFOS.get(activity.getClass().getCanonicalName());
        //新建Activity
        if (activityLifecycleInfo != null && activityLifecycleInfo.activityLifeCycleCount == ActivityLifecycleInfo.ACTIVITY_LIFECYCLE_CREATE2RESUME) {
            onActivityCreate(activity);
        }

        //activity resume
        if (activityLifecycleInfo != null && activityLifecycleInfo.activityLifeCycleCount > ActivityLifecycleInfo.ACTIVITY_LIFECYCLE_CREATE2RESUME) {
            onActivityResume(activity);
        }

        //生命周期回调
        Map<String, AbsFloatView> floatViews = getFloatViews(activity);
        for (AbsFloatView floatView : floatViews.values()) {
            floatView.onResume();
        }

    }

    void addListener(FloatViewManager.FloatViewAttachedListener listener) {
        mListeners.add(listener);
    }

    void removeListener(FloatViewManager.FloatViewAttachedListener listener) {
        mListeners.remove(listener);
    }
}
