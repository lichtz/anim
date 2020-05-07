package cn.licht.mobile.anim.frzz;


import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;


public class FloatTouchProxy {
    private int mLastX;
    private int mLastY;
    private int mStartX;
    private int mStartY;
    private FloatTouchProxy.OnTouchEventListener mEventListener;
    private FloatTouchProxy.TouchState mState = FloatTouchProxy.TouchState.STATE_STOP;
    private int mTouchSlop;
    private int tapTimeout;

    public FloatTouchProxy( FloatTouchProxy.OnTouchEventListener eventListener) {
        mEventListener = eventListener;
    }

    private void initConfig(Context context) {
        if (mTouchSlop != 0){
            return;
        }
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        tapTimeout = ViewConfiguration.getTapTimeout();
        if (mTouchSlop == 0) {

        }
        if (tapTimeout == 0) {
            tapTimeout = 100;
        }
    }

    public void setEventListener(FloatTouchProxy.OnTouchEventListener eventListener) {
        mEventListener = eventListener;
    }

    private enum TouchState {
        STATE_MOVE,
        STATE_STOP
    }

    public boolean onTouchEvent(View view, MotionEvent event) {
        initConfig(view.getContext());
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
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
                    if (mState == FloatTouchProxy.TouchState.STATE_STOP) {
                        break;
                    }
                }else if (mState != TouchState.STATE_MOVE){
                    mState = TouchState.STATE_MOVE;
                }
                if (mEventListener != null) {
                    mEventListener.onMove(mLastX, mLastY, x - mLastX, y - mLastY);
                }
                mLastY = y;
                mLastX = x;
                mState = FloatTouchProxy.TouchState.STATE_MOVE;
                break;
            case MotionEvent.ACTION_UP:
                if (mEventListener != null) {
                    mEventListener.onUp(x, y);
                }
                if (mState !=TouchState.STATE_MOVE && event.getEventTime()- event.getDownTime()<tapTimeout){
                    view.performClick();
                }
                mState = TouchState.STATE_STOP;
                break;
            default:
                break;
        }

        return true;
    }

    public interface OnTouchEventListener {
        void onMove(int x, int y, int dx, int dy);

        void onUp(int x, int y);

        void onDown(int x, int y);
    }
}


