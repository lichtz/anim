package cn.licht.mobile.anim.frzz;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class FloatDispatchTouchProxy {
    private static final String TAG = "FloatTouchProxy";
    private int mLastX;
    private int mLastY;
    private int mStartX;
    private int mStartY;
    private int mTouchSlop;
    private int tapTimeout;
    private OnDispatchTouchEventListener mEventListener;
    private boolean dispatch;

    public interface OnDispatchTouchEventListener {
        void onMove(int x, int y, int dx, int dy);

        void onUp(int x, int y);

        void onDown(int x, int y);
    }

    public FloatDispatchTouchProxy( FloatDispatchTouchProxy.OnDispatchTouchEventListener eventListener) {
        mEventListener = eventListener;
    }

    private void initConfig(Context context) {
        if (mTouchSlop != 0){
            return;
        }
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        tapTimeout = ViewConfiguration.getTapTimeout();
        Log.d(TAG, "initConfig: tapTimeOut"+ tapTimeout);
        if (mTouchSlop == 0) {

        }
        if (tapTimeout == 0) {
            tapTimeout = 100;
        }
    }

    public boolean dispatchEvent(View view, MotionEvent event){
        initConfig(view.getContext());
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dispatch= true;
                mStartX = x;
                mStartY = y;
                mLastX = x;
                mLastY = y;
                if (mEventListener != null) {
                    mEventListener.onDown(x, y);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(x - mStartX) < mTouchSlop && Math.abs(y - mStartY) < mTouchSlop) {
                    dispatch = true;
                }else {
                    dispatch = false;
                }

                mLastY = y;
                mLastX = x;
                return  dispatch;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

        return dispatch;
    }



    public boolean onTouchEvent(View view, MotionEvent event) {
        initConfig(view.getContext());
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dispatch= true;
                mStartX = x;
                mStartY = y;
                mLastX = x;
                mLastY = y;
                if (mEventListener != null) {
                    mEventListener.onDown(x, y);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(x - mStartX) < mTouchSlop && Math.abs(y - mStartY) < mTouchSlop) {
                    dispatch = true;
                }else {
                    if (mEventListener != null) {
                        mEventListener.onMove(mLastX, mLastY, x - mLastX, y - mLastY);
                    }
                    dispatch = false;
                }

                mLastY = y;
                mLastX = x;
                return  dispatch;
            case MotionEvent.ACTION_UP:
                if (!dispatch) {
                    if (mEventListener != null) {
                        mEventListener.onUp(x, y);
                    }
                }
                break;
            default:
                break;
        }

        return dispatch;
    }

}
