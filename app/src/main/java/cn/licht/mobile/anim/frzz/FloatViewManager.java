package cn.licht.mobile.anim.frzz;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

public class FloatViewManager  implements FloatViewManagerInterface{

    private Context mContext;
    private Map<String, FloatViewPosInfo> mFloatViewPosInfoMaps;
    private  Map<String, Point> mFloatViewPos;
    private FloatViewManagerInterface mFloatViewManager;


    private static class Holder {
        private static FloatViewManager INSTANCE = new FloatViewManager();
    }

    public static FloatViewManager getInstance() {
        return Holder.INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        if (FloatViewConstant.IS_SYSTEM_FLOAT_MODE) {
            mFloatViewManager = new SystemFloatViewManager(context);
        }else {
            mFloatViewManager = new NormalFloatViewManager(context);
        }
        mFloatViewPos = new HashMap<>();
        mFloatViewPosInfoMaps = new HashMap<>();
    }




    @Override
    public void attach(FloatViewWraper floatViewData) {
        if (mFloatViewManager != null){
            mFloatViewManager.attach(floatViewData);
        }

    }

    @Override
    public void detach(AbsFloatView absFloatView) {
        if (mFloatViewManager != null){
            mFloatViewManager.detach(absFloatView);
        }
    }

    @Override
    public void detach(Activity activity, AbsFloatView absFloatView) {
        if (mFloatViewManager != null){
            mFloatViewManager.detach(activity,absFloatView);
        }
    }

    @Override
    public void detach(String tag) {
        if (mFloatViewManager != null){
            mFloatViewManager.detach(tag);
        }
    }

    @Override
    public void detach(Activity activity, String tag) {
        if (mFloatViewManager != null){
            mFloatViewManager.detach(activity,tag);
        }
    }

    @Override
    public void detach(Class<? extends AbsFloatView> absFlowView) {
        if (mFloatViewManager != null){
            mFloatViewManager.detach(absFlowView);
        }
    }

    @Override
    public void detach(Activity activity, Class<? extends AbsFloatView> absFlowView) {
        if (mFloatViewManager != null){
            mFloatViewManager.detach(activity,absFlowView);
        }
    }

    @Override
    public void detachAll() {
        if (mFloatViewManager != null){
            mFloatViewManager.detachAll();
        }
    }

    @Override
    public AbsFloatView getFloatView(Activity activity, String tag) {
        if (mFloatViewManager != null){
            return  mFloatViewManager.getFloatView(activity,tag);
        }
        return null;
    }

    @Override
    public Map<String, AbsFloatView> getFloatViews(Activity activity) {
        if (mFloatViewManager != null){
            return  mFloatViewManager.getFloatViews(activity);
        }
        return null;
    }

    @Override
    public void notifyBackground() {
        if (mFloatViewManager != null){
            mFloatViewManager.notifyBackground();
        }

    }

    @Override
    public void notifyForeground() {
        if (mFloatViewManager != null) {
            mFloatViewManager.notifyForeground();
        }
    }

    @Override
    public void onActivityDestroy(Activity activity) {
        if (mFloatViewManager != null){
            mFloatViewManager.onActivityDestroy(activity);
        }
    }

    @Override
    public void onActivityCreate(Activity activity) {

    }

    @Override
    public void onActivityResume(Activity activity) {

    }

    @Override
    public void onActivityPause(Activity activity) {
        if (mFloatViewManager != null){
            mFloatViewManager.onActivityPause(activity);
        }

    }

    @Override
    public void resumeAndAttachDokitViews(Activity activity) {
        if (mFloatViewManager != null){
            mFloatViewManager.resumeAndAttachDokitViews(activity);
        }
    }



    WindowManager getWindowManager() {
        return (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    public FloatViewPosInfo getFloatViewPosInfo(String key) {
        if (mFloatViewPosInfoMaps == null) {
            return null;
        }
        return mFloatViewPosInfoMaps.get(key);
    }

    public void removeFloatViewPosInfo(String key) {
        if (mFloatViewPosInfoMaps != null) {
            mFloatViewPosInfoMaps.remove(key);
        }
    }

    public void saveFloatViewPosInfo(String key, FloatViewPosInfo floatViewPosInfo) {
        if (mFloatViewPosInfoMaps == null ) {
            mFloatViewPosInfoMaps = new HashMap<>();
        }
            if (!TextUtils.isEmpty(key)) {
                mFloatViewPosInfoMaps.put(key, floatViewPosInfo);
            }
    }

    public Point getFloatViewPos(String tag){
        if (mFloatViewPos == null){
            return  null;
        }

        return  mFloatViewPos.get(tag);
    }
    public void saveFloatViewPos(String tag, int marginLeft, int marginTop) {
        if (mFloatViewPos == null) {
            mFloatViewPos = new HashMap<>();
        }

        if (mFloatViewPos.get(tag) == null) {
            Point point = new Point(marginLeft, marginTop);
            mFloatViewPos.put(tag, point);
        } else {
            Point point = mFloatViewPos.get(tag);
            if (point != null) {
                point.set(marginLeft, marginTop);
            }
        }
    }


    /**
     * 系统悬浮窗需要调用
     *
     * @param listener
     */
    void addFloatViewAttachedListener(FloatViewAttachedListener listener) {
        if (mFloatViewManager != null) {
            if (FloatViewConstant.IS_SYSTEM_FLOAT_MODE && mFloatViewManager instanceof SystemFloatViewManager) {
                ((SystemFloatViewManager) mFloatViewManager).addListener(listener);
            }
        }

    }

    void removeDokitViewAttachedListener(FloatViewAttachedListener listener) {
        if (mFloatViewManager != null) {
            if (FloatViewConstant.IS_SYSTEM_FLOAT_MODE && mFloatViewManager instanceof SystemFloatViewManager) {
                ((SystemFloatViewManager) mFloatViewManager).removeListener(listener);
            }
        }
    }


    public interface FloatViewAttachedListener {
        void onFloatViewAdd(AbsFloatView absFloatView);
    }
}
