package cn.licht.mobile.anim.widget.keyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import cn.licht.mobile.anim.KeyboardStyleBean;
import cn.licht.mobile.anim.R;
import cn.licht.mobile.anim.WindowUtils;


/**
 * Created by ly on 2018/4/12.
 */

public class KeyboardNumView extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {
    private static final int KEY_HEIGHT = 59;
    private static final int KEY_TEXT_SIZE = 25;
    private static final int KEY_TEXT_SPECIAL_SIZE = 18;
    public static final int BASE_SCREEN_WIDTH = 750;
    public static final int BASE_SCREEN_HEIGHT = 1334;
    //取款键盘宽高
    public static final int KEY_CODE_HEIGHT = 105;
    private static HashMap<String, KeyboardStyleBean> keyboardStyleHashMap;
    private static final String[][] idcardKey = {{"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", "9"}, {"X", "0", KeyTpye.KEY_DEL, KeyTpye.KEY_FINISH}};

    private static final String[][] codeKey = {
            {"1", "2", "3"},
            {"4", "5", "6"},
            {"7", "8", "9"},
            {KeyTpye.KEY_NULL, "0", KeyTpye.KEY_DEL, KeyTpye.KEY_FINISH}};

    private static final String[][] numKey = {
            {"1", "2", "3"},
            {"4", "5", "6"},
            {"7", "8", "9"},
            {KeyTpye.KEY_NULL, "0", KeyTpye.KEY_DEL}};

    private static final String[][] amountKey = {
            {"1", "2", "3"},
            {"4", "5", "6"},
            {"7", "8", "9"},
            {".", "0", KeyTpye.KEY_NULL}};

    private static final String[] amountControlKey =
            {KeyTpye.KEY_DEL, KeyTpye.KEY_FINISH};

    private boolean isVibrator = true;
    private boolean isRandomNum = false;
    private OnKeyEvent onKeyChangeListener;


    public KeyboardNumView(Context context) {
        super(context);
        initView(context, KeyboardType.KEY_NUM_TYPE);
    }

    public KeyboardNumView(Context context, String type) {
        super(context);
        initView(context, type);
    }

    public KeyboardNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, KeyboardType.KEY_NUM_TYPE);
    }

    public KeyboardNumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, KeyboardType.KEY_NUM_TYPE);
    }

    private int getScalelSizeForWidth(int src) {
        return (int) WindowUtils.getScalelSizeForWidth(src, BASE_SCREEN_WIDTH, getContext());
    }

    private int getScalelSizeForHeight(int src) {
        return (int) WindowUtils.getScalelSizeForHeight(src, BASE_SCREEN_HEIGHT, getContext());
    }

    private void initView(Context context, String type) {
        if (context != null && !TextUtils.isEmpty(type)) {
            setOrientation(VERTICAL);
            applyStyle(type);
            if (type.equals(KeyboardType.KEY_CODE_TYPE)) {
                initKeyCodeNormal(context, codeKey);
            } else if (type.equals(KeyboardType.KEY_NUM_TYPE)) {
                initKeyCodeNormal(context, numKey);
            } else if (type.equals(KeyboardType.KEY_AMOUNT_TYPE)) {
                initKeyAmount(context, amountKey, amountControlKey);
            } else if (type.equals(KeyboardType.KEY_IDCARD_TYPE)) {
                initKeyCodeNormal(context, idcardKey);
            }
        }
    }

    public void setVibrator(boolean isVibrator) {
        this.isVibrator = isVibrator;
    }

    public void setIsRandomNum(boolean isRandomNum) {
        this.isRandomNum = isRandomNum;
    }

    public boolean getIsRandomNum() {
        return isRandomNum;
    }

    public void setKeyType(String type) {
        if (!TextUtils.isEmpty(type)) {
            setOrientation(VERTICAL);
            applyStyle(type);
            if (type.equals(KeyboardType.KEY_CODE_TYPE)) {
                initKeyCodeNormal(getContext(), codeKey);
            } else if (type.equals(KeyboardType.KEY_NUM_TYPE)) {
                initKeyCodeNormal(getContext(), numKey);
            } else if (type.equals(KeyboardType.KEY_AMOUNT_TYPE)) {
                initKeyAmount(getContext(), amountKey, amountControlKey);
            } else if (type.equals(KeyboardType.KEY_IDCARD_TYPE)) {
                initKeyCodeNormal(getContext(), idcardKey);
            }
        }
    }

    private void initKeyCodeNormal(Context context, String[][] codes) {
        removeAllViews();
        String[] randNum = KeyboardUtils.getRandomNum(isRandomNum);
        if (randNum != null && randNum.length == 10) {
            for (int i = 0; i < randNum.length; i++) {
                int row = i / 3;
                int column = i - row * 3;

                if (row == 3 && i == 9) {
                    codes[3][1] = randNum[i];
                } else {
                    codes[row][column] = randNum[i];
                }
            }
        }

        if (context != null) {
            for (int i = 0; i < codes.length; i++) {
                LinearLayout row = new LinearLayout(context);
                row.setOrientation(HORIZONTAL);
                row.setGravity(Gravity.CENTER_VERTICAL);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                for (int j = 0; j < codes[i].length; j++) {
                    LayoutParams keyLp = new LayoutParams(0, DensityUtil.dp2px(100));
                    row.addView(createChildView(context, codes[i][j], keyLp, keyboardStyleHashMap));
                    if (j != codes[i].length - 1) {
                        row.addView( createDividerView(context, 2, 100));
                    }
                }
                addView(row);
                if (i != codes.length - 1) {
                    addView(createDividerView(context,ViewGroup.LayoutParams.MATCH_PARENT,2));
                }
            }
        }
    }

    private void initKeyAmount(Context context, String[][] codes, String[] controlkeys) {
        removeAllViews();
        setOrientation(HORIZONTAL);
        int keyHeightPx = DensityUtil.dp2px(KEY_HEIGHT);
        if (context != null) {
            LinearLayout leftLinearLayout = new LinearLayout(context);
            LayoutParams leftLinearLayoutLp = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            leftLinearLayoutLp.weight = 2.89f;
            leftLinearLayout.setOrientation(VERTICAL);
            leftLinearLayout.setLayoutParams(leftLinearLayoutLp);

            for (int i = 0; i < codes.length; i++) {
                LinearLayout row = new LinearLayout(context);
                row.setOrientation(HORIZONTAL);
                row.setGravity(Gravity.CENTER_VERTICAL);
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, keyHeightPx);
                row.setLayoutParams(lp);
                for (int j = 0; j < codes[i].length; j++) {
                    LayoutParams keyLp = new LayoutParams(0, keyHeightPx);
                    keyLp.weight = 1;
                    View childView = createChildView(context, codes[i][j], keyLp,keyboardStyleHashMap);
                    row.addView(childView);
                    row.addView(createDividerView(context, 1, keyHeightPx));
                }
                leftLinearLayout.addView(row);
                if (i != codes.length - 1) {
                    leftLinearLayout.addView(createDividerView(context, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                }
            }

            LinearLayout rightLinearLayout = new LinearLayout(context);
            LayoutParams rightLinearLayoutLp = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            rightLinearLayoutLp.weight = 1;
            rightLinearLayout.setLayoutParams(rightLinearLayoutLp);
            rightLinearLayout.setOrientation(VERTICAL);
            for (int i = 0; i < controlkeys.length; i++) {
                LayoutParams controlLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                rightLinearLayout.addView(createChildView(context, controlkeys[i], controlLp,keyboardStyleHashMap));
            }
            addView(leftLinearLayout);
            addView(rightLinearLayout);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();

        // 添加软件盘震动效果
        AudioManager audiomanage = (AudioManager) getContext()
                .getSystemService(Context.AUDIO_SERVICE);
        Vibrator vib = (Vibrator) getContext()
                .getSystemService(Context.VIBRATOR_SERVICE);
        if (isVibrator) {
            // 获取手机是否设置为震动模式
            if (audiomanage
                    .getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_OFF) {
                // 不震动
                vib.cancel();
            } else {
                vib.vibrate(100);
            }
        }

        if (this.onKeyChangeListener != null
                && !TextUtils.isEmpty(tag)
                && !tag.equals(KeyTpye.KEY_NULL)
        ) {
            onKeyChangeListener.onChangeListener(tag);
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

    public void setOnKeyChangeListener(OnKeyEvent onKeyChangeListener) {
        this.onKeyChangeListener = onKeyChangeListener;
    }


    private View createChildView(Context context, String keyType, LayoutParams childLayoutParams, HashMap<String, KeyboardStyleBean> keyStyleMap) {
        float keyWeight = 1;
        int backgroundColorIds = 0;
        if (keyStyleMap != null) {
            KeyboardStyleBean keyboardStyleBean = keyStyleMap.get(keyType);
            if (keyboardStyleBean != null) {
                keyWeight = keyboardStyleBean.weight;
                backgroundColorIds = keyboardStyleBean.backgroundColorIds;
            }
        }
        childLayoutParams.weight = keyWeight;
        if (keyType.equals(KeyTpye.KEY_DEL)) {
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
            frameLayout.setTag(keyType);
            frameLayout.setOnClickListener(this);
            frameLayout.setOnLongClickListener(this);
            return frameLayout;
        } else if (keyType.equals(KeyTpye.KEY_FINISH)) {
            TextView key = new TextView(context);
            key.setLayoutParams(childLayoutParams);
            key.setTextColor(context.getResources().getColor(R.color.text_color_ffffff));
            key.setTextSize(KEY_TEXT_SPECIAL_SIZE);
            key.setGravity(Gravity.CENTER);
            key.setBackgroundResource(backgroundColorIds == 0 ? R.color.key_safe_finish : backgroundColorIds);
            key.setTag(keyType);
            key.setText("完成");
            key.setOnClickListener(this);
            return key;
        } else if (keyType.equals(KeyTpye.KEY_NULL)) {
            TextView key = new TextView(context);
            key.setLayoutParams(childLayoutParams);
            key.setTextColor(context.getResources().getColor(R.color.text_color_ffffff));
            key.setTextSize(KEY_TEXT_SPECIAL_SIZE);
            key.setGravity(Gravity.CENTER);
            key.setBackgroundResource(backgroundColorIds == 0 ? R.color.key_btn_bg_gray : backgroundColorIds);
            key.setTag(keyType);
            key.setText("");
            key.setOnClickListener(this);
            return key;
        } else {
            Button key = new Button(context);
            key.setLayoutParams(childLayoutParams);
            key.setTextColor(context.getResources().getColor(R.color.text_color_000000));
            key.setTextSize(KEY_TEXT_SIZE);
            key.setGravity(Gravity.CENTER);
            key.setBackgroundResource(R.drawable.selector_key_num_btn_bg);
            key.setTag(keyType);
            key.setText(keyType);
            key.setOnClickListener(this);
            return key;
        }

    }

    private View createDividerView(Context context, int width, int height) {
        View divider = new View(context);
        LayoutParams dividerLp = new LayoutParams(width, height);
        divider.setLayoutParams(dividerLp);
        divider.setBackgroundColor(context.getResources().getColor(R.color.key_divider));
        return divider;
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
            case KeyboardType.KEY_IDCARD_TYPE:
                keyboardStyleHashMap.put(KeyTpye.KEY_DEL, new KeyboardStyleBean(0.5f, R.color.key_btn_bg_gray));
                keyboardStyleHashMap.put(KeyTpye.KEY_FINISH, new KeyboardStyleBean(0.5f, 0));
                break;
            case KeyboardType.KEY_CODE_TYPE:
                keyboardStyleHashMap.put(KeyTpye.KEY_DEL, new KeyboardStyleBean(0.5f, R.color.white));
                keyboardStyleHashMap.put(KeyTpye.KEY_FINISH, new KeyboardStyleBean(0.5f, 0));
                break;
            case KeyboardType.KEY_AMOUNT_TYPE:
                keyboardStyleHashMap.put(KeyTpye.KEY_DEL, new KeyboardStyleBean(0.5f, R.color.key_btn_bg_white));
                keyboardStyleHashMap.put(KeyTpye.KEY_FINISH, new KeyboardStyleBean(0.5f, 0));
                break;
            default:
                break;
        }
    }
}
