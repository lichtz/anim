package cn.licht.mobile.anim.widget.keyboard;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by ly on 2018/9/17.
 */

public class KeyboardUtils {

    /**
     * 获得随机数字1-9
     */
    public static String[] getRandomNum(boolean bl) {
        String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        if (bl) {
//            if (!BuildConfig.isMonkey) {
            String temp;
            Random random = new SecureRandom();
            for (int i = 0; i < 10; i++) {
                int rnd = Math.abs(random.nextInt()) % 10;
                if (rnd < arr.length) {
                    temp = arr[rnd];
                    arr[rnd] = arr[0];
                    arr[0] = temp;
                }
            }
//            }
        }
        return arr;
    }
}
