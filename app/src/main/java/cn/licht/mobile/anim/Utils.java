package cn.licht.mobile.anim;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.view.WindowManager;

import com.didichuxing.doraemonkit.util.LogHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Utils {

    public static boolean isPortrait(Context context){
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }

    public static int getScreenShortSideLength(Context context) {
        if (isPortrait(context)) {
            return getAppScreenWidth(context);
        } else {
            return getAppScreenHeight(context);
        }
    }

    /**
     * 获取屏幕长边的长度 不包含statusBar
     *
     * @return
     */
    public static int getScreenLongSideLength(Context context) {
        if (isPortrait(context)) {
            //ScreenUtils.getScreenHeight(); 包含statusBar
            //ScreenUtils.getAppScreenHeight(); 不包含statusBar
            return getAppScreenHeight(context);
        } else {
            return getAppScreenWidth(context);
        }
    }




    /**
     * 判断是否具有悬浮窗权限
     * @param context
     * @return
     */
    public static boolean canDrawOverlays(Context context) {
        //android 6.0及以上的判断条件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }
        //android 4.4~6.0的判断条件
        return checkOp(context, 24);
    }

    private static boolean checkOp(Context context, int op) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Class clazz = AppOpsManager.class;
            try {
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Process.myUid(), context.getPackageName());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    public static void requestDrawOverlays(Context context) {
        Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + context.getPackageName()));
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
        }
    }

    public static int getAppScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }

    public static int getAppScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }
}
