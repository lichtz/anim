package cn.licht.mobile.anim.frzz;

public class GlobalFloatViewInfo {
    public Class<? extends AbsFloatView> mAbsFloatView;
    public String mFloatViewName;

    public GlobalFloatViewInfo(Class<? extends AbsFloatView> mAbsFloatView, String mFloatViewName) {
        this.mAbsFloatView = mAbsFloatView;
        this.mFloatViewName = mFloatViewName;
    }
}
