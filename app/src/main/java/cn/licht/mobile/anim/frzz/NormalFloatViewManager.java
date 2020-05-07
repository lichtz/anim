package cn.licht.mobile.anim.frzz;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.didichuxing.doraemonkit.kit.core.AbsDokitView;
import com.didichuxing.doraemonkit.kit.core.DokitIntent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.licht.mobile.anim.R;

public class NormalFloatViewManager implements FloatViewManagerInterface {
    private Map<Activity, Map<String, AbsFloatView>> mActivityFloatViews;
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
        if (mGlobalFloatViewInfoMap == null) {
            return;
        }
        for (GlobalFloatViewInfo globalFloatViewInfo : mGlobalFloatViewInfoMap.values()) {
            FloatViewWraper floatViewWraper = new FloatViewWraper(globalFloatViewInfo.mAbsFloatView,activity);
            attach(floatViewWraper);
        }

    }

    @Override
    public void onActivityResume(Activity activity) {
        if (mActivityFloatViews == null || activity == null) {
            return;
        }
        Map<String, AbsFloatView> floatViewMap = mActivityFloatViews.get(activity);
        if (mGlobalFloatViewInfoMap != null && mGlobalFloatViewInfoMap.size() > 0) {
            for (GlobalFloatViewInfo globalFloatViewInfo : mGlobalFloatViewInfoMap.values()) {
                AbsFloatView absFloatView = null;
                if (floatViewMap != null && floatViewMap.isEmpty()) {
                    absFloatView = floatViewMap.get(globalFloatViewInfo.mTag);
                }
                if (absFloatView != null && absFloatView.getRootView() != null) {
                    absFloatView.getRootView().setVisibility(View.VISIBLE);
                    absFloatView.updateViewLayout(absFloatView.mTag,true);
                    absFloatView.onResume();
                }else {
                    FloatViewWraper floatViewWraper = new FloatViewWraper(globalFloatViewInfo.mAbsFloatView,activity);
                    attach(floatViewWraper);
                }
            }
        }
    }

    @Override
    public void attach(FloatViewWraper floatViewData) {
        try {
            if (floatViewData == null || floatViewData.getActivity() == null) {
                return;
            }
            if (floatViewData.getFloatView() == null) {
                return;
            }


            Map<String, AbsFloatView> floatViewMap;
            if (mActivityFloatViews.get(floatViewData.getActivity()) == null) {
                floatViewMap = new HashMap<>();
                mActivityFloatViews.put(floatViewData.getActivity(), floatViewMap);
            } else {
                floatViewMap = mActivityFloatViews.get(floatViewData.getActivity());
            }
            if (floatViewMap.get(floatViewData.getTag()) != null) {
                floatViewMap.get(floatViewData.getTag()).updateViewLayout(floatViewData.getTag(), true);
                return;
            }
            final AbsFloatView absFloatView = floatViewData.getFloatView().newInstance();
            floatViewMap.put(floatViewData.getTag(), absFloatView);
            absFloatView.mTag = floatViewData.getTag();
            absFloatView.setActivity(floatViewData.getActivity());
            absFloatView.performCreate(mContext);
            if (mGlobalFloatViewInfoMap != null) {
                mGlobalFloatViewInfoMap.put(floatViewData.getTag(), createGlobalFloatViewInfo(absFloatView));
            }
            FrameLayout decorView = (FrameLayout) floatViewData.getActivity().getWindow().getDecorView();
            if (absFloatView.getNormalLayoutParams() != null && absFloatView.getRootView() != null) {
                getFloatViewRootContebtView(floatViewData.getActivity(), decorView).addView(absFloatView.getRootView(), absFloatView.getNormalLayoutParams());
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

    private FrameLayout getFloatViewRootContebtView(final Activity bindActivity, FrameLayout decorView) {
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
        return new GlobalFloatViewInfo(absFloatView.getClass(), absFloatView.mTag);
    }

    @Override
    public void detach(AbsFloatView absFloatView) {

    }

    @Override
    public void detach(Activity activity, AbsFloatView absFloatView) {

    }

    @Override
    public void detach(String tag) {
        if (mActivityFloatViews == null) {
            return;
        }
        //移除每个activity中指定的dokitView
        for (Activity activityKey : mActivityFloatViews.keySet()) {
            Map<String, AbsFloatView> floatViewMap = mActivityFloatViews.get(activityKey);
            //定位到指定dokitView
            AbsFloatView floatView = floatViewMap.get(tag);
            if (floatView == null) {
                continue;
            }
            if (floatView.getRootView() != null) {
                floatView.getRootView().setVisibility(View.GONE);
                getFloatViewRootContebtView(floatView.getActivity(), (FrameLayout) activityKey.getWindow().getDecorView()).removeView(floatView.getRootView());
            }

            //移除指定UI
            //请求重新绘制
            activityKey.getWindow().getDecorView().requestLayout();
            //执行dokitView的销毁
            floatView.performDestroy();
            //移除map中的数据
            floatViewMap.remove(tag);

        }
        //同步移除全局指定类型的dokitView
        if (mGlobalFloatViewInfoMap != null && mGlobalFloatViewInfoMap.containsKey(tag)) {
            mGlobalFloatViewInfoMap.remove(tag);
        }

    }

    @Override
    public void detach(Activity activity, String tag) {
        if (activity == null) {
            return;
        }
        Map<String, AbsFloatView> floatViews = mActivityFloatViews.get(activity);
        if (floatViews == null) {
            return;
        }

        AbsFloatView absFloatView = floatViews.get(tag);
        if (absFloatView  == null){
            return;
        }
        if (absFloatView.getRootView() != null){
            absFloatView.getRootView().setVisibility(View.GONE);
            getFloatViewRootContebtView(activity,(FrameLayout)activity.getWindow().getDecorView()).removeView(absFloatView.getRootView());
        }
        activity.getWindow().getDecorView().requestLayout();
        absFloatView.performDestroy();
        floatViews.remove(tag);
        if (mGlobalFloatViewInfoMap != null && mGlobalFloatViewInfoMap.containsKey(tag)) {
            mGlobalFloatViewInfoMap.remove(tag);
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
        if (mActivityFloatViews == null){
            return;
        }
        for (Activity activitiekey : mActivityFloatViews.keySet()){
            Map<String, AbsFloatView> floatViewMap = mActivityFloatViews.get(activitiekey);
            getFloatViewRootContebtView(activitiekey,(FrameLayout)activitiekey.getWindow().getDecorView()).removeAllViews();
            floatViewMap.clear();
        }
        if (mGlobalFloatViewInfoMap != null){
            mGlobalFloatViewInfoMap.clear();
        }


    }

    @Override
    public AbsFloatView getFloatView(Activity activity, String tag) {
        if (TextUtils.isEmpty(tag) || activity == null) {
            return null;
        }
        if (mActivityFloatViews == null) {
            return null;
        }
        if (mActivityFloatViews.get(activity) == null) {
            return null;
        }
        return mActivityFloatViews.get(activity).get(tag);
    }

    @Override
    public Map<String, AbsFloatView> getFloatViews(Activity activity) {
        if (activity == null) {
            return Collections.emptyMap();
        }
        if (mActivityFloatViews == null) {
            return Collections.emptyMap();
        }
        Map<String, AbsFloatView> floatViewMap = mActivityFloatViews.get(activity);
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
        mActivityFloatViews.remove(activity);
    }


    @Override
    public void onActivityPause(Activity activity) {
        Map<String, AbsFloatView> floatViews = getFloatViews(activity);
        for (AbsFloatView absFloatView : floatViews.values()) {
            absFloatView.onPause();
        }
    }


}
