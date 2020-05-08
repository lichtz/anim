package cn.licht.mobile.anim.frzz;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.licht.mobile.anim.R;

public class NormalFloatViewManager implements FloatViewManagerInterface {
    private Map<String, Map<String, AbsFloatView>> mActivityFloatViews;
    private Map<String, GlobalFloatViewInfo> mGlobalFloatViewInfoMap;
    private Context mContext;

    public NormalFloatViewManager(Context context) {
        mContext = context.getApplicationContext();
        mActivityFloatViews = new HashMap<>();
        mGlobalFloatViewInfoMap = new HashMap<>();
    }

    @Override
    public void notifyBackground() {
        if (mActivityFloatViews != null) {
            for (Map<String, AbsFloatView> floatViewMap : mActivityFloatViews.values()) {
                for (AbsFloatView absFloatView : floatViewMap.values()) {
                    absFloatView.onAppBackground();
                }
            }
        }
    }

    @Override
    public void notifyForeground() {
        if (mActivityFloatViews != null) {
            for (Map<String, AbsFloatView> floatViewMap : mActivityFloatViews.values()) {
                for (AbsFloatView absFloatView : floatViewMap.values()) {
                    absFloatView.onAppForeground();
                }
            }
        }

    }

    @Override
    public void resumeAndAttachDokitViews(Activity activity) {
        if (mActivityFloatViews == null) {
            return;
        }

        ActivityLifecycleInfo activityLifecycleInfo = FloatViewConstant.ACTIVITY_LIFECYCLE_INFOS.get(activity.getClass().getCanonicalName());
        if (activityLifecycleInfo == null) {
            return;
        }
        if (activityLifecycleInfo.activityLifeCycleCount == ActivityLifecycleInfo.ACTIVITY_LIFECYCLE_CREATE2RESUME) {
            onActivityCreate(activity);
            return;
        }

        if (activityLifecycleInfo.activityLifeCycleCount > ActivityLifecycleInfo.ACTIVITY_LIFECYCLE_CREATE2RESUME) {
            onActivityResume(activity);
        }

    }


    @Override
    public void onActivityCreate(Activity activity) {
        if (activity == null || mGlobalFloatViewInfoMap == null) {
            return;
        }
        for (GlobalFloatViewInfo globalFloatViewInfo : mGlobalFloatViewInfoMap.values()) {
            FloatViewWraper floatViewWraper = new FloatViewWraper(globalFloatViewInfo.mAbsFloatView, activity.getClass().getCanonicalName());
            attach(activity, floatViewWraper);
        }

    }

    @Override
    public void onActivityResume(Activity activity) {
        if (mActivityFloatViews == null || activity == null) {
            return;
        }
        Map<String, AbsFloatView> floatViewMap = mActivityFloatViews.get(activity.getClass().getCanonicalName());
        if (mGlobalFloatViewInfoMap != null && mGlobalFloatViewInfoMap.size() > 0) {
            for (GlobalFloatViewInfo globalFloatViewInfo : mGlobalFloatViewInfoMap.values()) {
                AbsFloatView absFloatView = null;
                if (floatViewMap != null && !floatViewMap.isEmpty()) {
                    absFloatView = floatViewMap.get(globalFloatViewInfo.mFloatViewName);
                }
                if (absFloatView != null && absFloatView.getRootView() != null) {
                    absFloatView.getRootView().setVisibility(View.VISIBLE);
                    absFloatView.updateViewLayout(absFloatView.mFloatViewName, true);
                    absFloatView.onResume();
                } else {
                    FloatViewWraper floatViewWraper = new FloatViewWraper(globalFloatViewInfo.mAbsFloatView, activity.getClass().getCanonicalName());
                    attach(activity, floatViewWraper);
                }
            }
        }
    }

    @Override
    public void attach(Activity activity, FloatViewWraper floatViewData) {
        try {
            if (activity == null || floatViewData == null || floatViewData.getBindActivityCanonicalName() == null) {
                return;
            }
            if (floatViewData.getFloatView() == null) {
                return;
            }

            Map<String, AbsFloatView> floatViewMap;
            if (mActivityFloatViews.get(floatViewData.getBindActivityCanonicalName()) == null) {
                floatViewMap = new HashMap<>();
                mActivityFloatViews.put(floatViewData.getBindActivityCanonicalName(), floatViewMap);
            } else {
                floatViewMap = mActivityFloatViews.get(floatViewData.getBindActivityCanonicalName());
            }
            if (floatViewMap == null) {
                return;
            }
            AbsFloatView floatView = floatViewMap.get(floatViewData.getFloatViewName());
            if (floatView != null) {
                floatView.updateViewLayout(floatViewData.getFloatViewName(), true);
                return;
            }
            final AbsFloatView absFloatView = floatViewData.getFloatView().newInstance();
            floatViewMap.put(floatViewData.getFloatViewName(), absFloatView);
            absFloatView.mFloatViewName = floatViewData.getFloatViewName();
            absFloatView.setActivity(activity);
            absFloatView.performCreate(mContext);
            if (floatViewData.mode == FloatViewConstant.MODE_GLOBAL_FLOAT_VIEW && mGlobalFloatViewInfoMap != null && !mGlobalFloatViewInfoMap.containsKey(floatViewData.getFloatViewName())) {
                mGlobalFloatViewInfoMap.put(floatViewData.getFloatViewName(), createGlobalFloatViewInfo(absFloatView));
            }
            FrameLayout decorView = (FrameLayout) activity.getWindow().getDecorView();
            if (absFloatView.getNormalLayoutParams() != null && absFloatView.getRootView() != null) {
                getFloatViewRootContentView(activity, decorView).addView(absFloatView.getRootView(), absFloatView.getNormalLayoutParams());
                absFloatView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        absFloatView.onResume();
                    }
                }, 100);
            }


        } catch (Exception e) {

        }

    }

    private FrameLayout getFloatViewRootContentView(final Activity bindActivity, FrameLayout decorView) {
        FrameLayout floatRootView = decorView.findViewById(R.id.floatview_content_id);
        if (floatRootView != null) {
            return (FrameLayout) floatRootView;
        }
        floatRootView = new FloatFramelayout(mContext);
        floatRootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Map<String, AbsFloatView> floatViews = getFloatViews(bindActivity);
                    if (floatViews == null || floatViews.size() == 0) {
                        return false;
                    }
                    for (AbsFloatView floatView : floatViews.values()) {
                        if (floatView.shouldDealBackKey()) {
                            return floatView.onBackPressed();
                        }

                    }
                    return false;
                }
                return false;
            }
        });
        floatRootView.setClipChildren(true);
        floatRootView.setFocusable(true);
        floatRootView.setFocusableInTouchMode(true);
        floatRootView.requestFocus();
        floatRootView.setId(R.id.floatview_content_id);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        floatRootView.setLayoutParams(layoutParams);
        decorView.addView(floatRootView);
        return floatRootView;
    }


    private GlobalFloatViewInfo createGlobalFloatViewInfo(AbsFloatView absFloatView) {
        return new GlobalFloatViewInfo(absFloatView.getClass(), absFloatView.mFloatViewName);
    }

    @Override
    public void detach(AbsFloatView absFloatView) {
        detach(absFloatView.mFloatViewName);

    }

    @Override
    public void detach(Activity activity, AbsFloatView absFloatView) {
        detach(activity,absFloatView.mFloatViewName);

    }

    @Override
    public void detach(String floatViewName) {
        if (mActivityFloatViews == null || TextUtils.isEmpty(floatViewName)) {
            return;
        }
        //移除每个activity中指定的dokitView
        for (String activityCanonicalName : mActivityFloatViews.keySet()) {
            Map<String, AbsFloatView> floatViewMap = mActivityFloatViews.get(activityCanonicalName);
            try {
                if (floatViewMap == null){
                    return;
                }
                AbsFloatView floatView = floatViewMap.get(floatViewName);
                if (floatView == null) {
                    continue;
                }
                if (floatView.getActivity() != null && !floatView.getActivity().isFinishing()) {
                if (floatView.getRootView() != null) {
                    floatView.getRootView().setVisibility(View.GONE);
                    if (floatView.getActivity() != null) {
                        getFloatViewRootContentView(floatView.getActivity(), (FrameLayout) floatView.getActivity().getWindow().getDecorView()).removeView(floatView.getRootView());
                        floatView.getActivity().getWindow().getDecorView().requestLayout();
                    }
                }

                }
                floatView.performDestroy();

            } catch (Exception e) {

            }

            floatViewMap.remove(floatViewName);
        }
        //同步移除全局指定类型的dokitView
        if (mGlobalFloatViewInfoMap != null ) {
            mGlobalFloatViewInfoMap.remove(floatViewName);
        }

    }

    @Override
    public void detach(Activity activity, String floatViewName) {
        if (activity == null || TextUtils.isEmpty(floatViewName)) {
            return;
        }
        Map<String, AbsFloatView> floatViews = mActivityFloatViews.get(activity.getClass().getCanonicalName());
        if (floatViews == null) {
            return;
        }

        AbsFloatView absFloatView = floatViews.get(floatViewName);
        if (absFloatView == null) {
            return;
        }

        if (!activity.isFinishing()) {
            if (absFloatView.getRootView() != null) {
                absFloatView.getRootView().setVisibility(View.GONE);
                getFloatViewRootContentView(activity, (FrameLayout) activity.getWindow().getDecorView()).removeView(absFloatView.getRootView());
            }
            activity.getWindow().getDecorView().requestLayout();
        }
        absFloatView.performDestroy();
        floatViews.remove(floatViewName);
        if (mGlobalFloatViewInfoMap != null ) {
            mGlobalFloatViewInfoMap.remove(floatViewName);
        }
    }

    @Override
    public void detach(Class<? extends AbsFloatView> absFlowView) {
        detach(absFlowView.getSimpleName());
    }

    @Override
    public void detach(Activity activity, Class<? extends AbsFloatView> absFlowView) {
        detach(activity, absFlowView.getSimpleName());

    }

    @Override
    public void detachAll() {
        if (mActivityFloatViews == null) {
            return;
        }
        for (String activityCanonicalName : mActivityFloatViews.keySet()) {
            Map<String, AbsFloatView> floatViewMap = mActivityFloatViews.get(activityCanonicalName);
            if (floatViewMap != null && floatViewMap.size() != 0) {
                for (AbsFloatView absFloatView : floatViewMap.values()) {
                    Activity activity = absFloatView.getActivity();
                    if (activity != null && !activity.isFinishing()) {
                        Window window = activity.getWindow();
                        if (window != null) {
                            View decorView = window.getDecorView();
                            FrameLayout floatViewRootContentView = getFloatViewRootContentView(activity, (FrameLayout) decorView);
                            if (floatViewRootContentView != null) {
                                floatViewRootContentView.removeAllViews();
                            }

                        }
                    }
                }
                floatViewMap.clear();
            }

        }
        if (mGlobalFloatViewInfoMap != null) {
            mGlobalFloatViewInfoMap.clear();
        }


    }

    @Override
    public AbsFloatView getFloatView(Activity activity, String floatViewName) {
        if (TextUtils.isEmpty(floatViewName) || activity == null) {
            return null;
        }
        if (mActivityFloatViews == null) {
            return null;
        }
        Map<String, AbsFloatView> absFloatViewMap = mActivityFloatViews.get(activity.getClass().getCanonicalName());
        if ( absFloatViewMap== null || absFloatViewMap.size() ==0) {
            return null;
        }
        return absFloatViewMap.get(floatViewName);
    }

    @Override
    public Map<String, AbsFloatView> getFloatViews(Activity activity) {
        if (activity == null) {
            return Collections.emptyMap();
        }
        if (mActivityFloatViews == null) {
            return Collections.emptyMap();
        }
        Map<String, AbsFloatView> floatViewMap = mActivityFloatViews.get(activity.getClass().getCanonicalName());
        if (floatViewMap == null) {
            return Collections.emptyMap();
        } else {
            return floatViewMap;
        }

    }


    @Override
    public void onActivityDestroy(Activity activity) {
        if (mActivityFloatViews == null) {
            return;
        }
        Map<String, AbsFloatView> dokitViewMap = getFloatViews(activity);
        if (dokitViewMap == null) {
            return;
        }
        for (AbsFloatView dokitView : dokitViewMap.values()) {
            dokitView.performDestroy();
        }
        mActivityFloatViews.remove(activity.getClass().getCanonicalName());
    }


    @Override
    public void onActivityPause(Activity activity) {
        Map<String, AbsFloatView> floatViews = getFloatViews(activity);
        for (AbsFloatView absFloatView : floatViews.values()) {
            absFloatView.onPause();
        }
    }


}
