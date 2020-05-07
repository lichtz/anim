package cn.licht.mobile.anim.frzz;

public class FloatViewPosInfo {
    private boolean isPortrait = true;
    private int floatViewWidth;
    private int floatViewHeight;
    private float leftMarginPercent;
    private float topMarginPercent;

    public int getFloatViewWidth() {
        return floatViewWidth;
    }

    public void setFloatViewWidth(int floatViewWidth) {
        this.floatViewWidth = floatViewWidth;
    }

    public int getFloatViewHeight() {
        return floatViewHeight;
    }

    public void setFloatViewHeight(int floatViewHeight) {
        this.floatViewHeight = floatViewHeight;
    }

    public void setLeftMargin(int leftMargin) {
        this.leftMarginPercent = (float) leftMargin / 1080;
    }

    public void setTopMargin(int topMargin) {
        this.topMarginPercent = (float) topMargin / 1940;
    }

    public void setPortrait() {
        isPortrait = false;
    }
    public boolean isPortrait() {
        return isPortrait;
    }

    public float getLeftMarginPercent() {
        return leftMarginPercent;
    }

    public float getTopMarginPercent() {
        return topMarginPercent;
    }
}
