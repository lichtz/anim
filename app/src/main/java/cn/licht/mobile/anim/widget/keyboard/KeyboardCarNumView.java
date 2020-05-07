package cn.licht.mobile.anim.widget.keyboard;

import android.content.Context;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.HashMap;
import java.util.regex.Pattern;

import cn.licht.mobile.anim.KeyboardStyleBean;
import cn.licht.mobile.anim.R;
import cn.licht.mobile.anim.WindowUtils;

import static android.content.Context.WINDOW_SERVICE;

public class KeyboardCarNumView extends LinearLayout implements View.OnTouchListener, View.OnLongClickListener {

    private static int KEY_ROW_MARGIN_BOTTOM_PX = 24;
    private static int KEY_MARGIN_LEFT_PX = 12;
    private static final int KEY_HEIGHT_PX = 84;
    private static final int KEY_WIDTH_PX = 63;


    public static final int BASE_SCREEN_WIDTH = 750;
    public static final int BASE_SCREEN_HEIGHT = 1334;
    public static final int KEY_TEXT_SIZE = 20;
    public static final int KEY_TEXT_SPECIAL_SIZE = 18;
    public static final int KEY_TEXT_FINISH_SPECIAL_SIZE = 16;


    public static final int KEY_123_SIZE = 250;
    public static final int KEY_SYMBOL_SIZE = 290;
    public static final int KEY_FINISH_SIZE = 175;
    public static final int KEY_DEL_SIZE = 84;
    public static final int KEY_CAP_SIZE = 84;
    public static final int KEY_SYMBOL_FINISH = 138;
    // 数字键盘宽高
    public static final int KEY_NUM_WIDTH = 234;
    public static final int KEY_NUM_HEIGHT = 91;
    public static final int KEY_NUM_MARGIN = 12;
    public static final int KEY_NUM_SPECIAL = 111;
    public static final int KEY_NUM_SIZE = 25;

    private static HashMap<String, KeyboardStyleBean> keyboardStyleHashMap;
    private static final String[][] carNum = {{"京", "津", "渝", "沪", "冀", "晋", "辽", "吉", "黑", "苏"}, {"浙", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘", "粤", "琼"}, {"川", "贵", "云", "陕", "甘", "青", "蒙", "桂", "宁", "新"}, {"藏", "使", "领", "警", "学", "港", "澳", KeyTpye.KEY_DEL, KeyTpye.KEY_FINISH}};
    private static final String[][] capitalLetter = {{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"}, {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"},
            {"A", "S", "D", "F", "G", "H", "J", "K", "L"},
            {"Z", "X", "C", "V", "B", "N", "M", KeyTpye.KEY_DEL, KeyTpye.KEY_FINISH}};

    // 左边第一个字符
    private static final String[] leftFirstLetter = {"1", "Q", "Z", "京", "浙", "川", "藏"};
    // 右边第一个字符
    private static final String[] rightFirstLetter = {"0", "苏", "琼", "新"};

    public static Pattern intPattern = Pattern.compile("^[0-9]*");

    private View perview;
    private WindowManager mWindowManager;
    private OnKeyEvent onKeyChangeListener;
    private int keyWidth;
    private int keyHeight;
    private int keyMarginLeft;
    private int lineMargin;


    public KeyboardCarNumView(Context context) {
        super(context);
        initView(context);
    }

    public KeyboardCarNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public KeyboardCarNumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        if (context != null) {
            keyWidth = getScalelSizeForWidth(KEY_WIDTH_PX);
            keyHeight = (int) (keyWidth * 1.3);
            keyMarginLeft = getScalelSizeForWidth(KEY_MARGIN_LEFT_PX);
            lineMargin = (int) (keyMarginLeft * 1.85);
            setOrientation(VERTICAL);
            mWindowManager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
        }
    }

    public void setKeyboardType(String type) {
        if (!TextUtils.isEmpty(type)) {
            applyStyle(type);
            switch (type) {
                case KeyboardType.KEY_CAR_NUM_TYPE:
                case KeyboardType.KEY_CAR_NONUM_TYPE:
                    initCarInputView(getContext(), type, capitalLetter);
                    break;
                case KeyboardType.KEY_CAR_PRV_NUM_TYPE:
                    initCarInputView(getContext(), type, carNum);
                    break;
                default:
                    break;
            }

        }
    }

    private int getScalelSizeForWidth(int src) {
        return (int) WindowUtils.getScalelSizeForWidth(src, BASE_SCREEN_WIDTH, getContext());
    }

    private int getScalelSizeForHeight(int src) {
        return (int) WindowUtils.getScalelSizeForHeight(src, BASE_SCREEN_HEIGHT, getContext());
    }


    public void setOnKeyChangeListener(OnKeyEvent onKeyChangeListener) {
        this.onKeyChangeListener = onKeyChangeListener;
    }

    private boolean isSpecialLetter(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (str.equals(KeyTpye.KEY_123)
                    || str.equals(KeyTpye.KEY_DEL)
                    || str.equals(KeyTpye.KEY_FINISH)
                    || str.equals(KeyTpye.KEY_SYMBOL)
                    || str.equals(KeyTpye.KEY_UP)
                    || str.equals(KeyTpye.KEY_UP_CAP)
                    || str.equals(KeyTpye.KEY_ABC)) {
                return true;
            }
        }
        return false;
    }


    private void initCarInputView(Context context, String keyboardType, String[][] letter) {
        removeAllViews();
        setGravity(Gravity.CENTER_HORIZONTAL);
        if (context != null && letter != null && letter.length > 0) {
            for (int i = 0; i < letter.length; i++) {
                LinearLayout row = new LinearLayout(context);
                row.setOrientation(HORIZONTAL);
                row.setGravity(Gravity.CENTER_VERTICAL);
                LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.topMargin = lineMargin;
                if (i == letter.length - 1) {
                    lp.bottomMargin = lineMargin;
                }
                row.setLayoutParams(lp);

                for (int j = 0; j < letter[i].length; j++) {
                    LayoutParams keyLp = new LayoutParams(keyWidth,
                            keyHeight);
                    if (j != 0) {
                        keyLp.leftMargin = getScalelSizeForWidth(12);
                    }
                    View childView = createChildView(context, keyboardType, letter[i][j], keyLp, keyboardStyleHashMap);
                    row.addView(childView);
                }

                addView(row);
            }
        }
    }


    private View createChildView(Context context, String keyboardType, String keyName, LayoutParams childLayoutParams, HashMap<String, KeyboardStyleBean> keyStyleMap) {
        int backgroundColorIds = 0;
        if (keyStyleMap != null) {
            KeyboardStyleBean keyboardStyleBean = keyStyleMap.get(keyName);
            if (keyboardStyleBean != null) {
                if (keyboardStyleBean.weight > 0) {
                    childLayoutParams.weight = keyboardStyleBean.weight;
                    ;
                }
                backgroundColorIds = keyboardStyleBean.backgroundColorIds;
                if (keyboardStyleBean.width > 0) {
                    childLayoutParams.width = keyboardStyleBean.width;
                }
                if (keyboardStyleBean.height > 0) {
                    childLayoutParams.height = keyboardStyleBean.height;
                }
            }
        }

        if (keyName.equals(KeyTpye.KEY_DEL)) {
            FrameLayout frameLayout = new FrameLayout(context);
            frameLayout.setLayoutParams(childLayoutParams);
            frameLayout.setBackgroundResource(backgroundColorIds == 0 ? R.color.white : backgroundColorIds);
            ImageView keyImage = new ImageView(context);
            int i = DensityUtil.dp2px(22);
            FrameLayout.LayoutParams keyImageLayout = new FrameLayout.LayoutParams(i, i);
            keyImageLayout.gravity = Gravity.CENTER;
            keyImage.setLayoutParams(keyImageLayout);
            keyImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            keyImage.setImageResource(R.drawable.selector_key_del_btn_bg);
            frameLayout.addView(keyImage);
            frameLayout.setTag(keyName);
            frameLayout.setOnTouchListener(this);
            frameLayout.setOnLongClickListener(this);
            return frameLayout;
        } else if (keyName.equals(KeyTpye.KEY_FINISH)) {
            TextView key = new TextView(context);
            key.setLayoutParams(childLayoutParams);
            key.setTextColor(context.getResources().getColor(R.color.text_color_ffffff));
            key.setTextSize(KEY_TEXT_SPECIAL_SIZE);
            key.setGravity(Gravity.CENTER);
            key.setBackgroundResource(backgroundColorIds == 0 ? R.color.key_safe_finish : backgroundColorIds);
            key.setTag(keyName);
            key.setText("完成");
            key.setOnTouchListener(this);
            return key;
        } else if (keyName.equals(KeyTpye.KEY_NULL)) {
            TextView key = new TextView(context);
            key.setLayoutParams(childLayoutParams);
            key.setTextColor(context.getResources().getColor(R.color.text_color_ffffff));
            key.setTextSize(KEY_TEXT_SPECIAL_SIZE);
            key.setGravity(Gravity.CENTER);
            key.setBackgroundResource(backgroundColorIds == 0 ? R.color.key_btn_bg_gray : backgroundColorIds);
            key.setTag(keyName);
            key.setText("");
            key.setOnTouchListener(this);
            return key;
        } else {
            TextView key = new TextView(context);
            key.setLayoutParams(childLayoutParams);
            key.setTextColor(context.getResources().getColor(R.color.text_color_000000));
            key.setTextSize(KEY_TEXT_SIZE);
            key.setGravity(Gravity.CENTER);
            key.setBackgroundResource(R.drawable.selector_key_num_btn_bg);
            key.setTag(keyName);
            key.setText(keyName);
            boolean clickEnAble = canClick(keyboardType, keyName);
            if (clickEnAble) {
                key.setOnTouchListener(this);
            } else {
                key.setOnTouchListener(null);
                key.setTextColor(context.getResources().getColor(R.color.text_color_unclick));
            }
            return key;
        }

    }


    private void applyStyle(String keyboardType) {
        if (keyboardType == null) {
            if (keyboardStyleHashMap != null) {
                keyboardStyleHashMap.clear();
            }
            return;
        }
        if (keyboardStyleHashMap == null) {
            keyboardStyleHashMap = new HashMap<>(3);
        } else {
            keyboardStyleHashMap.clear();
        }
        switch (keyboardType) {
            case KeyboardType.KEY_CAR_NUM_TYPE:
            case KeyboardType.KEY_CAR_PRV_NUM_TYPE:
            case KeyboardType.KEY_CAR_NONUM_TYPE:
                int delKetWidth = getScalelSizeForWidth(100);
                int delHeight = (int) (delKetWidth * 0.84);
                keyboardStyleHashMap.put(KeyTpye.KEY_DEL, new KeyboardStyleBean(0, R.color.key_btn_bg_gray, delKetWidth, delHeight));
                keyboardStyleHashMap.put(KeyTpye.KEY_FINISH, new KeyboardStyleBean(0, 0, delKetWidth, delHeight));
                break;
            default:
                break;

        }

    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removePerview(false);
        if (keyboardStyleHashMap != null) {
            keyboardStyleHashMap.clear();
        }
    }


    private boolean isLeftFirstLetter(String str) {
        for (int i = 0; i < leftFirstLetter.length; i++) {
            if (leftFirstLetter[i].equals(str)) {
                return true;
            }
        }
        return false;
    }

    private boolean isRightFirstLetter(String str) {
        for (int i = 0; i < rightFirstLetter.length; i++) {
            if (rightFirstLetter[i].equals(str)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (view.getTag() != null) {
                if (isLeftFirstLetter((String) view.getTag())) {
                    addLeftPerview(view);
                } else if (isRightFirstLetter((String) view.getTag())) {
                    addRightPerview(view);
                } else {
                    addMiddlePerview(view);
                }

                if (onKeyChangeListener != null) {
                    onKeyChangeListener.onChangeListener((String) view.getTag());
                }
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            removePerview(true);
        }
        return true;
    }

    /**
     * 添加左边第一个字符点击的预览图
     *
     * @param view
     */
    private void addLeftPerview(View view) {
        if (view != null) {

            if (mWindowManager == null) {
                mWindowManager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
            }

            //获取被点击的view相对屏幕的坐标和宽高
            int[] source = new int[2];
            int srcW = view.getWidth();
            int srcH = view.getHeight();
            view.getLocationOnScreen(source);

            removePerview(false);
            // 获取左边perview的view
            perview = LayoutInflater.from(getContext()).inflate(R.layout.keyboard_perview_left_layout, null);
            TextView tvName = (TextView) perview.findViewById(R.id.tv_name);
            tvName.setText((CharSequence) view.getTag());
            perview.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int height = perview.getMeasuredHeight();
            int width = perview.getMeasuredWidth();

            // 获取状态栏高度
            int statusBarHeight = WindowUtils.getStatusBarHeight(getContext());

            WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
            wlp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

            wlp.format = PixelFormat.TRANSLUCENT;
            wlp.gravity = Gravity.LEFT | Gravity.TOP;
            wlp.x = source[0];
            wlp.y = source[1] + srcH - height - statusBarHeight;
            wlp.width = width;
            wlp.height = height;
            mWindowManager.addView(perview, wlp);
        }
    }

    /**
     * 添加右边第一个字符点击的预览图
     *
     * @param view
     */
    private void addRightPerview(View view) {
        if (view != null) {

            if (mWindowManager == null) {
                mWindowManager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
            }

            //获取被点击的view相对屏幕的坐标和宽高
            int[] source = new int[2];
            int srcW = view.getWidth();
            int srcH = view.getHeight();
            view.getLocationOnScreen(source);

            removePerview(false);
            // 获取右边perview的view
            perview = LayoutInflater.from(getContext()).inflate(R.layout.keyboard_perview_right_layout, null);
            TextView tvName = (TextView) perview.findViewById(R.id.tv_name);
            tvName.setText((CharSequence) view.getTag());
            perview.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int height = perview.getMeasuredHeight();
            int width = perview.getMeasuredWidth();

            // 获取状态栏高度
            int statusBarHeight = WindowUtils.getStatusBarHeight(getContext());

            WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
            wlp.format = PixelFormat.TRANSLUCENT;
            wlp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

            wlp.gravity = Gravity.LEFT | Gravity.TOP;
            wlp.x = source[0] + srcW - width;
            wlp.y = source[1] + srcH - height - statusBarHeight;
            wlp.width = width;
            wlp.height = height;
            mWindowManager.addView(perview, wlp);
        }
    }

    private void addMiddlePerview(View view) {
        if (view != null) {

            if (mWindowManager == null) {
                mWindowManager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
            }

            //获取被点击的view相对屏幕的坐标和宽高
            int[] source = new int[2];
            int srcW = view.getWidth();
            int srcH = view.getHeight();
            view.getLocationOnScreen(source);

            removePerview(false);
            // 获取中间perview的view
            perview = LayoutInflater.from(getContext()).inflate(R.layout.keyboard_perview_middle_layout, null);
            TextView tvName = (TextView) perview.findViewById(R.id.tv_name);
            tvName.setText((CharSequence) view.getTag());
            perview.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int height = perview.getMeasuredHeight();
            int width = perview.getMeasuredWidth();

            // 获取状态栏高度
            int statusBarHeight = WindowUtils.getStatusBarHeight(getContext());

            WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
            wlp.format = PixelFormat.TRANSLUCENT;
            wlp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

            wlp.gravity = Gravity.LEFT | Gravity.TOP;
            wlp.x = source[0] + srcW / 2 - width / 2;
            wlp.y = source[1] + srcH - height - statusBarHeight;
            wlp.width = width;
            wlp.height = height;
            mWindowManager.addView(perview, wlp);
        }
    }


    private void removePerview(boolean isDelay) {
        if (perview != null) {
            if (mWindowManager == null) {
                mWindowManager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
            }
            if (mWindowManager != null) {
                mWindowManager.removeViewImmediate(perview);
            }
            perview = null;
        }

    }

    @Override
    public boolean onLongClick(View v) {
        String tag = (String) v.getTag();

        if (this.onKeyChangeListener != null && tag.equals(KeyTpye.KEY_DEL)) {
            onKeyChangeListener.onDelAllListener();
        }
        return false;
    }

    private boolean canClick(String type, String keyName) {
        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(keyName)) {
            return true;
        }
        switch (type) {
            case KeyboardType.KEY_CAR_NONUM_TYPE:
                if (isNumber(keyName)) {
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    private boolean isNumber(String str) {
        return intPattern.matcher(str).matches();
    }

}
