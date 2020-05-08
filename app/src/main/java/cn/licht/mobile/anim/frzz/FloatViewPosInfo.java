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

    public void setLeftMargin(int screenWiidth, int leftMargin) {
        if (screenWiidth == 0) {
            leftMarginPercent = 0;
        } else {
            this.leftMarginPercent = (float) leftMargin / screenWiidth;
        }
    }

    public void setTopMargin(int screenHeight, int topMargin) {
        if (screenHeight == 0) {
            topMarginPercent = 0;
        } else {
            this.topMarginPercent = (float) topMargin / screenHeight;
        }
    }

    public void setPortrait(boolean isPortrait) {
        this.isPortrait = isPortrait;
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
