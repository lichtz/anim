package cn.licht.mobile.anim.frzz;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.licht.mobile.anim.Utils;


public class FloatViewActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {


    private int startedActivityCounts = 1;


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        recordActivityLifeCycleStatus(activity, LIFE_CYCLE_STATUS_CREATE);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

        if (startedActivityCounts == 0) {
            FloatViewManager.getInstance().notifyForeground();

        }
        startedActivityCounts++;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        recordActivityLifeCycleStatus(activity, LIFE_CYCLE_STATUS_RESUME);
        resumeAndAttachDokitViews(activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        FloatViewManager.getInstance().onActivityPause(activity);
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        recordActivityLifeCycleStatus(activity, LIFE_CYCLE_STATUS_STOPPED);
        startedActivityCounts--;
        //通知app退出到后台
        if (startedActivityCounts == 0) {
            FloatViewManager.getInstance().notifyBackground();
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        recordActivityLifeCycleStatus(activity, LIFE_CYCLE_STATUS_DESTROY);
        FloatViewManager.getInstance().onActivityDestroy(activity);
    }

    private void resumeAndAttachDokitViews(Activity activity) {
        if (FloatViewConstant.isSystemFloatMode()) {
            //系统模式
            //悬浮窗权限 vivo 华为可以不需要动态权限 小米需要
            if (Utils.canDrawOverlays(activity)) {
                FloatViewManager.getInstance().resumeAndAttachDokitViews(activity);
            } else {
                //请求悬浮窗权限
                Utils.requestDrawOverlays(activity);
            }

        } else {
            FloatViewManager.getInstance().resumeAndAttachDokitViews(activity);
        }


    }

    private void recordActivityLifeCycleStatus(Activity activity, int lifeCycleStatus) {
        ActivityLifecycleInfo activityLifecaycleInfo = FloatViewConstant.ACTIVITY_LIFECYCLE_INFOS.get(activity.getClass().getCanonicalName());
        if (activityLifecaycleInfo == null) {
            activityLifecaycleInfo = new ActivityLifecycleInfo();
            activityLifecaycleInfo.activityName = activity.getClass().getCanonicalName();
            if (lifeCycleStatus == LIFE_CYCLE_STATUS_CREATE) {
                activityLifecaycleInfo.activityLifeCycleCount = 0;
            } else if (lifeCycleStatus == LIFE_CYCLE_STATUS_RESUME) {
                activityLifecaycleInfo.activityLifeCycleCount = activityLifecaycleInfo.activityLifeCycleCount + 1;
            } else if (lifeCycleStatus == LIFE_CYCLE_STATUS_STOPPED) {
                activityLifecaycleInfo.invokeStopMethod = true;
            }
            FloatViewConstant.ACTIVITY_LIFECYCLE_INFOS.put(activity.getClass().getCanonicalName(), activityLifecaycleInfo);
        } else {
            activityLifecaycleInfo.activityName = activity.getClass().getCanonicalName();
            if (lifeCycleStatus == LIFE_CYCLE_STATUS_CREATE) {
                activityLifecaycleInfo.activityLifeCycleCount = 0;
            } else if (lifeCycleStatus == LIFE_CYCLE_STATUS_RESUME) {
                activityLifecaycleInfo.activityLifeCycleCount = activityLifecaycleInfo.activityLifeCycleCount + 1;
            } else if (lifeCycleStatus == LIFE_CYCLE_STATUS_STOPPED) {
                activityLifecaycleInfo.invokeStopMethod = true;
            } else if (lifeCycleStatus == LIFE_CYCLE_STATUS_DESTROY) {
                FloatViewConstant.ACTIVITY_LIFECYCLE_INFOS.remove(activity.getClass().getCanonicalName());
            }
        }
    }

    /**
     * Activity 创建
     */
    private static int LIFE_CYCLE_STATUS_CREATE = 100;
    /**
     * Activity resume
     */
    private static int LIFE_CYCLE_STATUS_RESUME = 101;
    /**
     * Activity stop
     */
    private static int LIFE_CYCLE_STATUS_STOPPED = 102;
    /**
     * Activity destroy
     */
    private static int LIFE_CYCLE_STATUS_DESTROY = 103;
}
