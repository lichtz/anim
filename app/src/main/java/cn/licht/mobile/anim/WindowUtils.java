package cn.licht.mobile.anim;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;


import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class WindowUtils {
    private static final String TAG = "WindowUtils";

    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        return context.getResources().getDimensionPixelSize(tv.resourceId);

    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);

            //刘海屏增加高度
            if (isConcaveScreens(context)) {
                if (Build.BRAND.contains("huawei")) {
                    int[] ret = getNotchSize(context);
                    int height = ret[1];
                    if (height > statusBarHeight) {
                        statusBarHeight = height;
                    }
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public static int getStatusBarHeight1(Context context){
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        //刘海屏增加高度
        if (isConcaveScreens(context)) {
            if (Build.BRAND.contains("huawei")) {
                int[] ret = getNotchSize(context);
                int height = ret[1];
                if (height > statusBarHeight) {
                    statusBarHeight = height;
                }
            }
        }

        return statusBarHeight;
    }

    public static int getMeasureHeightOfView(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    public static int getMeasureWidthOfView(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredWidth();
    }

    public static int getRealHeight (Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenHeight = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics dm = new DisplayMetrics();
            display.getRealMetrics(dm);
            screenHeight = dm.heightPixels;
        } else {
            try {
                screenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception e) {
                DisplayMetrics dm = new DisplayMetrics();
                display.getMetrics(dm);
                screenHeight = dm.heightPixels;
            }
        }

        return screenHeight;
    }

    public static int getRealWidth (Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics dm = new DisplayMetrics();
            display.getRealMetrics(dm);
            screenWidth = dm.widthPixels;
        } else {
            try {
                screenWidth = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception e) {
                DisplayMetrics dm = new DisplayMetrics();
                display.getMetrics(dm);
                screenWidth = dm.widthPixels;
            }
        }

        return screenWidth;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static float getScalelSizeForWidth(int src, int baseScreen, Context context){
        if (context != null){
            float f = src * 1.0f /baseScreen;
            return f*getScreenWidth(context);
        }
        return 0;
    }

    public static float getScalelSizeForHeight(int src, int baseScreen, Context context){
        if (context != null){
            float f = src * 1.0f /baseScreen;
            return f*getScreenHeight(context);
        }
        return 0;
    }

    public static boolean isConcaveScreens(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return false;
        }

        String model = Build.BRAND.toLowerCase();
        if (context == null || context.getPackageManager() == null || TextUtils.isEmpty(model)) {
            return false;
        }

        if (TextUtils.equals(model, "oppo")) {
            return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
        } else if (TextUtils.equals(model, "vivo")) {
            return checkVivoConcave();
        } else if (TextUtils.equals(model, "huawei")) {
            return checkHuaweiConcave(context);
        }

        return false;
    }

    private static boolean checkVivoConcave() {
        try {
            Class clazz = Class.forName("android.util.FtFeature");
            Object obj = clazz.newInstance();
            Class[] cArgs = new Class[1];
            cArgs[0] = int.class;
            Method method = clazz.getDeclaredMethod("isFeatureSupport", cArgs);
            return (boolean) method.invoke(obj, 0x00000020);
        } catch (Throwable e) {
        }
        return false;
    }

    private static boolean checkHuaweiConcave(Context context) {
        try {
            ClassLoader cl = context.getClassLoader();
            Class hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = hwNotchSizeUtil.getMethod("hasNotchInScreen");
            boolean hasNotchInScreen = (boolean) get.invoke(hwNotchSizeUtil);
            boolean isDisplayNotch = isDisplayNotch(context);
            return hasNotchInScreen && isDisplayNotch;
        } catch (Throwable e) {
        }
        return false;
    }

    private static boolean isDisplayNotch(Context context) {
        int setting = Settings.Secure.getInt(context.getContentResolver(), "display_notch_status", 0);
        return setting == 0;
    }

    public static int[] getNotchSize(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = hwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(hwNotchSizeUtil);
        } catch (Throwable e) {
        }
        return ret;
    }
}
