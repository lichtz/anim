package cn.licht.mobile.anim.frzz;

public class ActivityLifecycleInfo {
    /**
     * activityLifeCycleCount = 1 页面创建调用onResume
     * activityLifeCycleCount > 1 页面创建返回onResume
     */
    public static final int ACTIVITY_LIFECYCLE_CREATE2RESUME = 1;
    /**
     * 生命周期是否已经调用过stop 交叉判断是第一次调用resume还是页面返回调用resume
     */
    public boolean invokeStopMethod = false;
    public String activityName;
    /**
     * activityLifeCycleCount = 1 页面创建调用onResume
     * activityLifeCycleCount > 1 页面创建返回onResume
     */
    public int activityLifeCycleCount = 0;
}
