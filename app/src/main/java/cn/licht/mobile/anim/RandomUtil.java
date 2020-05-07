package cn.licht.mobile.anim;

import android.text.TextUtils;


import java.security.SecureRandom;

/**
 * Created by ly on 2018/4/25.
 */

public final class RandomUtil {

    private static RandomUtil instance;
    private int driftRandomNum = 0;

    public RandomUtil(){
        getRandomNumber();
    }

    public static RandomUtil getInstance(){
        if (instance == null){
            instance = new RandomUtil();
        }
        return instance;
    }

    // 生成随机数；
    public int getRandomNumber() {
        if (driftRandomNum == 0) {
            SecureRandom random = new SecureRandom();
            while (driftRandomNum == 0) {
                driftRandomNum = Math.abs(random.nextInt()) % 10;
                if (driftRandomNum != 0) {
                    break;
                }
            }
        }
        return driftRandomNum;
    }

    public String getMoveString(String str) {

        StringBuffer sb = new StringBuffer();

        if (!TextUtils.isEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                try {
                    char c1 = str.charAt(i);
                    int n = 0;
                    if (c1 >= 33 && c1 <= 47) {
                        n = driftRandomNum + c1;
                        if (n > 47) {
                            n = 32 + n - 47;
                        }
                    } else if (c1 >= 58 && c1 <= 64) {
                        if (driftRandomNum > 7) {
                            n = 7 + c1;
                        } else {
                            n = driftRandomNum + c1;
                        }
                        if (n > 64) {
                            n = 57 + n - 64;
                        }
                    } else if (c1 >= 91 && c1 <= 96) {
                        if (driftRandomNum > 6) {
                            n = 6 + c1;
                        } else {
                            n = driftRandomNum + c1;
                        }
                        if (n > 96) {
                            n = 90 + n - 96;
                        }
                    } else if (c1 >= 123 && c1 <= 126) {
                        if (driftRandomNum > 4) {
                            n = 4 + c1;
                        } else {
                            n = driftRandomNum + c1;
                        }
                        if (n > 126) {
                            n = 122 + n - 126;
                        }
                    } else if (c1 >= 48 && c1 <= 57) {
                        n = driftRandomNum + c1;
                        if (n > 57) {
                            n = 47 + n - 57;
                        }
                    } else if (c1 >= 65 && c1 <= 90) {
                        n = driftRandomNum + c1;
                        if (n > 90) {
                            n = 64 + n - 90;
                        }
                    } else if (c1 >= 97 && c1 <= 122) {
                        n = driftRandomNum + c1;
                        if (n > 122) {
                            n = 96 + n - 122;
                        }
                    }

                    char c2 = (char) n;
                    sb.append(String.valueOf(c2));
                } catch (Exception e) {
                }
            }
        }
        return sb.toString();
    }

    public String getDecMoveString(String value) {
        String s = "";
        try {
            if (value != null && !value.equals("")) {
                for (int i = 0; i < value.length(); i++) {
                    char c1 = value.charAt(i);
                    int n = 0;
                    if (c1 >= 33 && c1 <= 47) {
                        n = c1 - driftRandomNum;
                        if (n < 33) {
                            n = 47 - (32 - n);
                        }
                    } else if (c1 >= 58 && c1 <= 64) {
                        if (driftRandomNum > 7) {
                            n = c1 - 7;
                        } else {
                            n = c1 - driftRandomNum;
                        }
                        if (n < 58) {
                            n = 64 - (57 - n);
                        }
                    } else if (c1 >= 91 && c1 <= 96) {
                        if (driftRandomNum > 6) {
                            n = c1 - 6;
                        } else {
                            n = c1 - driftRandomNum;
                        }
                        if (n < 91) {
                            n = 96 - (90 - n);
                        }
                    } else if (c1 >= 123 && c1 <= 126) {
                        if (driftRandomNum > 4) {
                            n = c1 - 4;
                        } else {
                            n = c1 - driftRandomNum;
                        }
                        if (n < 123) {
                            n = 126 - (122 - n);
                        }
                    } else if (c1 >= 48 && c1 <= 57) {
                        n = c1 - driftRandomNum;
                        if (n < 48) {
                            n = 57 - (47 - n);
                        }
                    } else if (c1 >= 65 && c1 <= 90) {
                        n = c1 - driftRandomNum;
                        if (n < 65) {
                            n = 90 - (64 - n);
                        }
                    } else if (c1 >= 97 && c1 <= 122) {
                        n = c1 - driftRandomNum;
                        if (n < 97) {
                            n = 122 - (96 - n);
                        }
                    }

                    char c2 = (char) n;
                    s = s + String.valueOf(c2);
                }
            }
            value = s;
            s = "";
        } catch (Exception e) {
        }
        return value;
    }
}
