package cn.licht.mobile.anim;

public class KeyboardStyleBean {
    public float weight = 1;
    public int backgroundColorIds;
    public int width = 0;
    public int height = 0;
    public boolean clickAble = true;

    public KeyboardStyleBean(float weight, int backgroundColorids) {
        this.weight = weight;
        this.backgroundColorIds = backgroundColorids;
    }

    public KeyboardStyleBean(float weight, int backgroundColorIds, int width, int height) {
        this.weight = weight;
        this.backgroundColorIds = backgroundColorIds;
        this.width = width;
        this.height = height;
    }
}
