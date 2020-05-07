package cn.licht.mobile.anim.frzz;

public class GlobalFloatViewInfo {
    public Class<? extends AbsFloatView> mAbsFloatView;
    public String mTag;

    public GlobalFloatViewInfo(Class<? extends AbsFloatView> mAbsFloatView, String mTag) {
        this.mAbsFloatView = mAbsFloatView;
        this.mTag = mTag;
    }
}
