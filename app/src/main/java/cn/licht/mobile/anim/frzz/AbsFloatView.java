package cn.licht.mobile.anim.frzz;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;

import java.lang.ref.WeakReference;

import cn.licht.mobile.anim.Utils;


public abstract class AbsFloatView implements IFLoatView, FloatDispatchTouchProxy.OnDispatchTouchEventListener {
    private String TAG = this.getClass().getSimpleName();
    public String mFloatViewName = TAG;
    private Handler mHandler;
    public FloatDispatchTouchProxy floatDispatchTouchProxy = new FloatDispatchTouchProxy(this);
    private WindowManager mWindowManager = FloatViewManager.getInstance().getWindowManager();
    private AbsFloatView.InnerReceiver mInnerReceiver;
    private FloatViewPosInfo mFloatViewPosInfo;
    private FloatFramelayout mRootView;
    private View mChildView;
    private WindowManager.LayoutParams mWindowlayoutParams;
    private FrameLayout.LayoutParams mFrameLayoutParams;
    private FloatViewLayoutParams mFloatViewLayoutParams;
    private WeakReference<Activity> mAttachActivity;
    private int floatViewWidth;
    private int floatViewHeight;
    private ViewTreeObserver mViewTreeObserver;
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (mRootView != null) {
                //每次布局发生变动的时候重新赋值
                floatViewWidth = mRootView.getMeasuredWidth();
                floatViewHeight = mRootView.getMeasuredHeight();
                if (mFloatViewPosInfo != null) {
                    mFloatViewPosInfo.setFloatViewWidth(floatViewWidth);
                    mFloatViewPosInfo.setFloatViewHeight(floatViewHeight);
                }
            }
        }
    };

    public AbsFloatView() {
        TAG = getClass().getSimpleName();
        mFloatViewName = TAG;
        if (FloatViewManager.getInstance().getFloatViewPosInfo(mFloatViewName) == null) {
            mFloatViewPosInfo = new FloatViewPosInfo();
            FloatViewManager.getInstance().saveFloatViewPosInfo(mFloatViewName, mFloatViewPosInfo);
        } else {
            mFloatViewPosInfo = FloatViewManager.getInstance().getFloatViewPosInfo(mFloatViewName);
        }
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void performCreate(Context context) {
        try {
            onCreate(context);
            if (isSystemMode()) {
                mRootView = new FloatFramelayout(context) {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_UP && shouldDealBackKey()) {
                            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_HOME) {
                                return onBackPressed();
                            }
                        }
                        return super.dispatchKeyEvent(event);
                    }
                };
            } else {
                mRootView = new FloatFramelayout(context);
            }
            //添加根布局的layout回调
            addViewTreeObserverListener();
            mChildView = onCreateView(context, mRootView);
            mRootView.addView(mChildView);
            mRootView.setOnTouchProxy(floatDispatchTouchProxy);
            onViewCreated(mRootView);
            mFloatViewLayoutParams = new FloatViewLayoutParams();
            if (isSystemMode()) {
                mWindowlayoutParams = new WindowManager.LayoutParams();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mWindowlayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    mWindowlayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                }

                if (!shouldDealBackKey()) {
                    mWindowlayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                    mFloatViewLayoutParams.flags = FloatViewLayoutParams.FLAG_NOT_FOCUSABLE;
                }
                mWindowlayoutParams.format = PixelFormat.TRANSPARENT;
                mWindowlayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                mFloatViewLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;

                //动态注册关闭系统弹窗的广播
                IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                if (mInnerReceiver == null) {
                    mInnerReceiver = new AbsFloatView.InnerReceiver();
                }
                context.registerReceiver(mInnerReceiver, intentFilter);

            } else {
                mFrameLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                mFrameLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                mFloatViewLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
            }
            initFloatViewLayoutParams(mFloatViewLayoutParams);
            if (isSystemMode()) {
                onSystemLayoutParamCreated(mWindowlayoutParams);
            } else {
                onNormalLayoutParamsCreated(mFrameLayoutParams);
            }


        } catch (Exception e) {
            Log.i("zyl",e.getMessage());
        }


    }

    private void addViewTreeObserverListener() {
        if (mViewTreeObserver == null && mRootView != null && mOnGlobalLayoutListener != null) {
            mViewTreeObserver = mRootView.getViewTreeObserver();
            mViewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }

    }

    public void performDestroy() {
        Log.d(TAG, "performDestroy: ");
        if (isSystemMode()) {
            Context context = getContext();
            if (context != null && mInnerReceiver != null) {
                context.unregisterReceiver(mInnerReceiver);
            }
        }
        removeViewTreeObserverListener();
        mHandler = null;
        mRootView = null;
        onDestroy();
    }

    private void removeViewTreeObserverListener() {
        if (mViewTreeObserver != null && mOnGlobalLayoutListener != null) {
            if (mViewTreeObserver.isAlive()) {
                mViewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
            }
        }

    }

    private void onNormalLayoutParamsCreated(FrameLayout.LayoutParams params) {
        params.gravity = mFloatViewLayoutParams.gravity;
        params.width = mFloatViewLayoutParams.width;
        params.height = mFloatViewLayoutParams.height;
        portraitOrLandscape(params);

    }

    private void onSystemLayoutParamCreated(WindowManager.LayoutParams params) {
        params.flags = mFloatViewLayoutParams.flags;
        params.gravity = mFloatViewLayoutParams.gravity;
        params.width = mFloatViewLayoutParams.width;
        params.height = mFloatViewLayoutParams.height;
        Point point = FloatViewManager.getInstance().getFloatViewPos(mFloatViewName);
        if (point != null) {
            params.x = point.x;
            params.y = point.y;
        } else {
            params.x = mFloatViewLayoutParams.x;
            params.y = mFloatViewLayoutParams.y;
        }
    }

    private void portraitOrLandscape(FrameLayout.LayoutParams params) {
        Point point = FloatViewManager.getInstance().getFloatViewPos(mFloatViewName);
        if (point != null) {
            //横竖屏切换兼容
            if (Utils.isPortrait(getContext())) {
                if (mFloatViewPosInfo.isPortrait()) {
                    params.leftMargin = point.x;
                    params.topMargin = point.y;
                } else {
                    params.leftMargin = (int) (point.x * mFloatViewPosInfo.getLeftMarginPercent());
                    params.topMargin = (int) (point.y * mFloatViewPosInfo.getTopMarginPercent());
                }
            } else {
                if (mFloatViewPosInfo.isPortrait()) {
                    params.leftMargin = (int) (point.x * mFloatViewPosInfo.getLeftMarginPercent());
                    params.topMargin = (int) (point.y * mFloatViewPosInfo.getTopMarginPercent());
                } else {
                    params.leftMargin = point.x;
                    params.topMargin = point.y;
                }
            }
        } else {
            //横竖屏切换兼容
            if (Utils.isPortrait(getContext())) {
                if (mFloatViewPosInfo.isPortrait()) {
                    params.leftMargin = mFloatViewLayoutParams.x;
                    params.topMargin = mFloatViewLayoutParams.y;
                } else {
                    params.leftMargin = (int) (mFloatViewLayoutParams.x * mFloatViewPosInfo.getLeftMarginPercent());
                    params.topMargin = (int) (mFloatViewLayoutParams.y * mFloatViewPosInfo.getTopMarginPercent());
                }
            } else {
                if (mFloatViewPosInfo.isPortrait()) {
                    params.leftMargin = (int) (mFloatViewLayoutParams.x * mFloatViewPosInfo.getLeftMarginPercent());
                    params.topMargin = (int) (mFloatViewLayoutParams.y * mFloatViewPosInfo.getTopMarginPercent());
                } else {
                    params.leftMargin = mFloatViewLayoutParams.x;
                    params.topMargin = mFloatViewLayoutParams.y;
                }
            }
        }
        mFloatViewPosInfo.setPortrait(Utils.isPortrait(getContext()));
        mFloatViewPosInfo.setLeftMargin(Utils.getAppScreenWidth(getContext()),params.leftMargin);
        mFloatViewPosInfo.setTopMargin(Utils.getAppScreenHeight(getContext()),params.topMargin);
    }

    public boolean isSystemMode() {
        return FloatViewConstant.IS_SYSTEM_FLOAT_MODE;
    }

    private class InnerReceiver extends BroadcastReceiver {

        final String SYSTEM_DIALOG_REASON_KEY = "reason";

        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";


        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                        onHomeKeyPress();
                    } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                        onRecentAppKeyPress();
                    }
                }

            }


        }
    }

    public void onHomeKeyPress() {

    }

    public void onRecentAppKeyPress() {

    }

    @Override
    public boolean shouldDealBackKey() {
        return false;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onAppBackground() {
        if (isSystemMode() && mRootView != null) {
            mRootView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAppForeground() {
        if (isSystemMode() && mRootView != null) {
            mRootView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMove(int x, int y, int dx, int dy) {
        if (!canDrag()) {
            return;
        }
        if (isSystemMode()) {
            mWindowlayoutParams.x += dx;
            mWindowlayoutParams.y += dy;
            mWindowManager.updateViewLayout(mRootView, mWindowlayoutParams);

        } else {
            mFrameLayoutParams.leftMargin += dx;
            mFrameLayoutParams.topMargin += dy;
            updateViewLayout(mFloatViewName, false);
        }
    }

    @Override
    public void onUp(int x, int y) {
        if (!canDrag()) {
            return;
        }
        saveFloatViewPos();
    }

    public void saveFloatViewPos()
    {
        if (isSystemMode()) {
            FloatViewManager.getInstance().saveFloatViewPos(mFloatViewName, mWindowlayoutParams.x, mWindowlayoutParams.y);
        } else {
            FloatViewManager.getInstance().saveFloatViewPos(mFloatViewName, mFrameLayoutParams.leftMargin, mFrameLayoutParams.topMargin);
        }
    }

    @Override
    public void onDown(int x, int y) {

    }

    public void updateViewLayout(String mTag, boolean isActivityResumeUpdatePos) {
        if (mRootView == null || mChildView == null || mFrameLayoutParams == null || isSystemMode()) {
            return;
        }
        if (isActivityResumeUpdatePos) {
            Point point = FloatViewManager.getInstance().getFloatViewPos(mTag);
            if (point != null) {
                mFrameLayoutParams.leftMargin = point.x;
                mFrameLayoutParams.topMargin = point.y;
            }
        } else {
            mFloatViewPosInfo.setLeftMargin(Utils.getAppScreenWidth(getContext()),mFrameLayoutParams.leftMargin);
            mFloatViewPosInfo.setTopMargin(Utils.getAppScreenHeight(getContext()),mFrameLayoutParams.topMargin);
        }
        checkBorderLine(mFrameLayoutParams);
        mRootView.setLayoutParams(mFrameLayoutParams);
    }

    private void checkBorderLine(FrameLayout.LayoutParams params) {
        if ( isSystemMode()) {
            return;
        }
        if (params.topMargin <= 0) {
            params.topMargin = 0;
        }
        if (Utils.isPortrait(getContext())) {
            int screenLongSideLength = Utils.getScreenLongSideLength(getContext());
            if (params.topMargin >= screenLongSideLength - floatViewHeight) {
                params.topMargin = screenLongSideLength - floatViewHeight;
            }
        } else {
            int screenShortSideLength = Utils.getScreenShortSideLength(getContext());
            if (params.topMargin >=  screenShortSideLength- floatViewHeight) {
                params.topMargin = screenShortSideLength - floatViewHeight;
            }
        }
        if (params.leftMargin <= 0) {
            params.leftMargin = 0;
        }

        if (Utils.isPortrait(getContext())) {
            int screenShortSideLength = Utils.getScreenShortSideLength(getContext());
            if (params.leftMargin >= screenShortSideLength - floatViewWidth) {
                params.leftMargin = screenShortSideLength - floatViewWidth;
            }
        } else{
            int screenLongSideLength = Utils.getScreenLongSideLength(getContext());
            if (params.leftMargin >= screenLongSideLength- floatViewWidth) {
                params.leftMargin =screenLongSideLength - floatViewWidth;
            }
        }

    }



    public FloatFramelayout getRootView() {
        return mRootView;
    }


    public Activity getActivity() {
        if (mAttachActivity != null) {
            return mAttachActivity.get();
        }
        return null;
    }

    public void setActivity(Activity activity) {
        this.mAttachActivity = new WeakReference<>(activity);
    }

    public Context getContext() {
        if (mRootView != null) {
            return mRootView.getContext();
        } else {
            return null;
        }
    }

    @Override
    public boolean canDrag() {
        return true;
    }

    protected <T extends View> T findViewById(@IdRes int id) {
        if (mRootView == null) {
            return null;
        }
        return mRootView.findViewById(id);
    }

    public FrameLayout.LayoutParams getNormalLayoutParams() {
        return mFrameLayoutParams;
    }

    public WindowManager.LayoutParams getSystemLayoutParams() {
        return mWindowlayoutParams;
    }

    public void detach() {
        FloatViewManager.getInstance().detach(this);
    }

    public void post(Runnable r) {
        if (mHandler != null) {
            mHandler.post(r);
        }
    }

    public void postDelayed(Runnable r, long delayMillis) {
        if (mHandler != null) {
            mHandler.postDelayed(r, delayMillis);
        }
    }

    public void setFloatViewNotResponseTouchEvent(View view) {
        if (isSystemMode()) {
            if (view != null) {
                view.setOnTouchListener(null);
            }
        } else {
            if (view != null) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
            }
        }

    }

    public void invalidate() {
        if (isSystemMode()) {
            if (mWindowManager != null && mRootView != null && mWindowlayoutParams != null) {
                mWindowManager.updateViewLayout(mRootView, mWindowlayoutParams);
            }
        } else {
            if (mRootView != null && mFrameLayoutParams != null) {
                mRootView.setLayoutParams(mFrameLayoutParams);
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        FloatViewManager.getInstance().removeFloatViewPosInfo(mFloatViewName);
        mAttachActivity = null;

    }
}
