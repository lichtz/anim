package cn.licht.mobile.anim.frzz;


import android.graphics.Point;

import java.util.HashMap;
import java.util.Map;

public class FloatViewConstant {
    public static Map<String, ActivityLifecycleInfo> ACTIVITY_LIFECYCLE_INFOS = new HashMap<>();
    public static final int MODE_GLOBAL_FLOAT_VIEW = 1;
    public static final int MODE_REGIONAL_FLOAT_VIEW = 2;


    public static boolean IS_SYSTEM_FLOAT_MODE = true;
    public static  boolean isSystemFloatMode(){
        return  IS_SYSTEM_FLOAT_MODE;
    }

}
