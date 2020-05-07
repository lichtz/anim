package cn.licht.mobile.anim.widget.keyboard;

import android.content.Context;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.regex.Pattern;

import cn.licht.mobile.anim.R;
import cn.licht.mobile.anim.WindowUtils;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by ly on 2018/4/13.
 */

public class KeyboardSafeView extends LinearLayout implements View.OnTouchListener {

    public static final int BASE_SCREEN_WIDTH = 750;
    public static final int BASE_SCREEN_HEIGHT = 1334;
    public static final int KEY_TEXT_SIZE = 20;
    public static final int KEY_TEXT_SPECIAL_SIZE = 18;
    public static final int KEY_HEIGHT = 84;
    public static final int KEY_WIDTH = 63;
    public static final int KEY_MARGIN_LEFT = 12;
    public static final int KEY_ROW_MARGIN_TOP = 20;
    public static final int KEY_ROW_MARGIN_BOTTOM = 8;
    public static final int KEY_123_SIZE = 250;
    public static final int KEY_SYMBOL_SIZE = 290;
    public static final int KEY_FINISH_SIZE = 175;
    public static final int KEY_DEL_SIZE = 84;
    public static final int KEY_CAP_SIZE = 84;
    public static final int KEY_DEL_MARGIN_LEFT = 28;
    public static final int KEY_SYMBOL_FINISH = 138;
    // 数字键盘宽高
    public static final int KEY_NUM_WIDTH = 234;
    public static final int KEY_NUM_HEIGHT = 91;
    public static final int KEY_NUM_MARGIN = 12;
    public static final int KEY_NUM_SPECIAL = 111;
    public static final int KEY_NUM_SIZE = 25;

    private static final String[][] littleLetter = {{"q", "w", "e", "r", "t", "y", "u", "i", "o", "p"},
            {"a", "s", "d", "f", "g", "h", "j", "k", "l"},
            {KeyTpye.KEY_UP, "z", "x", "c", "v", "b", "n", "m", KeyTpye.KEY_DEL},
            {KeyTpye.KEY_123, KeyTpye.KEY_SYMBOL, KeyTpye.KEY_FINISH}};

    private static final String[][] capitalLetter = {{"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"},
            {"A", "S", "D", "F", "G", "H", "J", "K", "L"},
            {KeyTpye.KEY_UP_CAP, "Z", "X", "C", "V", "B", "N", "M", KeyTpye.KEY_DEL},
            {KeyTpye.KEY_123, KeyTpye.KEY_SYMBOL, KeyTpye.KEY_FINISH}};

    private static final String[][] symbolLetter = {
            {"~", "`", "!", "@", "#", "$", "%", "^", "&", "*"},
            {"(", ")", "_", "-", "+", "=", "{", "}", "[", "]"},
            {"|", "\\", ":", ";", "\"", "'", "<", ",", ">", "."},
            {"?", "/", KeyTpye.KEY_123, KeyTpye.KEY_ABC, KeyTpye.KEY_DEL, KeyTpye.KEY_FINISH}};

    private static final String[][] numKey = {
            {"1", "2", "3"},
            {"4", "5", "6"},
            {"7", "8", "9"},
            {KeyTpye.KEY_SYMBOL, KeyTpye.KEY_ABC, "0", KeyTpye.KEY_DEL, KeyTpye.KEY_FINISH}};

    // 左边第一个字符
    private static final String[] leftFirstLetter = {"q","Q", "~", "(", "|", "?"};
    // 右边第一个字符
    private static final String[] rightFirstLetter = {"p","P", "*", "]", "."};

    private boolean isVibrator = true;
    private View perview;
    private WindowManager mWindowManager;
    private OnKeyEvent onKeyChangeListener;
    private String isSpecialPassWordRequired = "true";
    private boolean isRandomNum = false;
    private boolean isSecure = false;

    public KeyboardSafeView(Context context){
        super(context);
        initView(context);
    }

    public KeyboardSafeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public KeyboardSafeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        if (context != null){
            setOrientation(VERTICAL);
            mWindowManager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
        }
    }

    public void setKeyboardType(String type){
        if (!TextUtils.isEmpty(type)){
            if (type.equals(KeyboardType.TYPE_NUM_KEYBOARD)){
                initKeyNum(getContext());
            }else if (type.equals(KeyboardType.TYPE_LITTLE_LETTER_KEYBOARD)){
                initKeyLetter(getContext(), littleLetter, true);
            }else if (type.equals(KeyboardType.TYPE_CAP_LETTER_KEYBOARD)){
                initKeyLetter(getContext(), capitalLetter, false);
            }else if (type.equals(KeyboardType.TYPE_SYMBOL_KEYBOARD)){
                initKeySymbol(getContext());
            }else{
                initKeyNum(getContext());
            }
        }else{
            initKeyNum(getContext());
        }
    }

    private int getScalelSizeForWidth(int src){
        return (int) WindowUtils.getScalelSizeForWidth(src, BASE_SCREEN_WIDTH, getContext());
    }

    private int getScalelSizeForHeight(int src){
        return (int) WindowUtils.getScalelSizeForHeight(src, BASE_SCREEN_HEIGHT, getContext());
    }

    public void setIsRandomNum(boolean isRandomNum) {
        this.isRandomNum = isRandomNum;
    }

    public boolean getIsRandomNum() {
        return isRandomNum;
    }

    /**
     * 设置是否需要特殊符号输入
     * @param isSpecialPassWordRequired
     */
    public void setSpecialPassWordRequired(String isSpecialPassWordRequired){
        this.isSpecialPassWordRequired = isSpecialPassWordRequired;
    }

    public void setVibrator(boolean isVibrator){
        this.isVibrator = isVibrator;
    }

    public void setOnKeyChangeListener(OnKeyEvent onKeyChangeListener){
        this.onKeyChangeListener = onKeyChangeListener;
    }

    private boolean isSpecialLetter(String str){
        if (!TextUtils.isEmpty(str)){
            if (str.equals(KeyTpye.KEY_123)
                    || str.equals(KeyTpye.KEY_DEL)
                    || str.equals(KeyTpye.KEY_FINISH)
                    || str.equals(KeyTpye.KEY_SYMBOL)
                    || str.equals(KeyTpye.KEY_UP)
                    || str.equals(KeyTpye.KEY_UP_CAP)
                    || str.equals(KeyTpye.KEY_ABC)){
                return true;
            }
        }
        return false;
    }

    private boolean isLeftFirstLetter(String str){
        for (int i = 0; i < leftFirstLetter.length; i++) {
            if (leftFirstLetter[i].equals(str)) {
                return true;
            }
        }
        return false;
    }

    private boolean isRightFirstLetter(String str){
        for (int i = 0; i < rightFirstLetter.length; i++) {
            if (rightFirstLetter[i].equals(str)) {
                return true;
            }
        }
        return false;
    }

    private boolean isFirstLetter(String str){
        if (!TextUtils.isEmpty(str)){
            if (str.equals("q")
                    || str.equals("a")
                    || str.equals("z")
                    || str.equals("Q")
                    || str.equals("A")
                    || str.equals("Z")
                    || str.equals("~")
                    || str.equals("(")
                    || str.equals("|")
                    || str.equals("?")){
                return true;
            }
        }
        return false;
    }

    private void initKeyLetter(Context context, String[][] letter, boolean isLittle){
        removeAllViews();
        setGravity(Gravity.CENTER_HORIZONTAL);
        if (context != null && letter != null && letter.length > 0){
            for (int i = 0; i < letter.length; i++) {
                LinearLayout row = new LinearLayout(context);
                row.setOrientation(HORIZONTAL);
                row.setGravity(Gravity.CENTER_VERTICAL);
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                lp.topMargin = getScalelSizeForHeight(KEY_ROW_MARGIN_TOP);
                if (i == 3){
                    lp.bottomMargin = getScalelSizeForHeight(KEY_ROW_MARGIN_BOTTOM);
                }

                row.setLayoutParams(lp);

                for (int j = 0; j < letter[i].length; j++) {

                    if (!isSpecialLetter(letter[i][j])){
                        LayoutParams keyLp = new LayoutParams(getScalelSizeForWidth(KEY_WIDTH),
                                getScalelSizeForHeight(KEY_HEIGHT));

                        if (!isFirstLetter(letter[i][j])){
                            keyLp.leftMargin = getScalelSizeForWidth(KEY_MARGIN_LEFT);
                        }else{
                            if (letter[i][j].equals("z") || letter[i][j].equals("Z")){
                                keyLp.leftMargin = getScalelSizeForWidth(KEY_DEL_MARGIN_LEFT);
                            }
                        }

                        TextView key = new TextView(context);
                        key.setLayoutParams(keyLp);
                        key.setTextColor(context.getResources().getColor(R.color.text_color_333333));
                        key.setTextSize(KEY_TEXT_SIZE);
                        key.setGravity(Gravity.CENTER);
                        key.setBackgroundResource(R.drawable.shape_safe_key_normal_bg);
                        key.setTag(letter[i][j]);
                        key.setText(letter[i][j]);
                        key.setOnTouchListener(this);
                        row.addView(key);
                    }else{

                        if (letter[i][j].equals(KeyTpye.KEY_UP)
                                || letter[i][j].equals(KeyTpye.KEY_UP_CAP)){                                                                        // 大小写按键
                            LayoutParams imgLp = new LayoutParams(getScalelSizeForWidth(KEY_CAP_SIZE),
                                    getScalelSizeForHeight(KEY_CAP_SIZE));
                            ImageView keyImage = new ImageView(context);
                            keyImage.setLayoutParams(imgLp);
                            keyImage.setScaleType(ImageView.ScaleType.CENTER);

                            if (isLittle) {
                                keyImage.setBackgroundResource(R.drawable.shape_safe_key_gray_bg);
                                keyImage.setImageResource(R.drawable.ic_key_up);
                            }else{
                                keyImage.setBackgroundResource(R.drawable.shape_safe_key_normal_bg);
                                keyImage.setImageResource(R.drawable.ic_key_up_cap);
                            }

                            keyImage.setTag(letter[i][j]);
                            keyImage.setOnTouchListener(this);
                            row.addView(keyImage);
                        }else if (letter[i][j].equals(KeyTpye.KEY_DEL)){                                                               // 删除按键
                            LayoutParams imgLp = new LayoutParams(getScalelSizeForWidth(KEY_DEL_SIZE),
                                    getScalelSizeForHeight(KEY_DEL_SIZE));
                            imgLp.leftMargin = getScalelSizeForWidth(KEY_DEL_MARGIN_LEFT);
                            ImageView keyImage = new ImageView(context);
                            keyImage.setLayoutParams(imgLp);
                            keyImage.setScaleType(ImageView.ScaleType.CENTER);

                            if (isLittle) {
                                keyImage.setBackgroundResource(R.drawable.shape_safe_key_gray_bg);
                                keyImage.setImageResource(R.drawable.ic_key_safe_del);
                            }else{
                                keyImage.setBackgroundResource(R.drawable.shape_safe_key_normal_bg);
                                keyImage.setImageResource(R.drawable.ic_key_safe_del_cap);
                            }

                            keyImage.setTag(letter[i][j]);
                            keyImage.setOnTouchListener(this);
                            row.addView(keyImage);
                        }else if (letter[i][j].equals(KeyTpye.KEY_123)){                                                               // 数字键盘
                            LayoutParams keyLp = new LayoutParams(getScalelSizeForWidth(KEY_123_SIZE), getScalelSizeForHeight(KEY_HEIGHT));
                            TextView key = new TextView(context);
                            key.setLayoutParams(keyLp);
                            key.setTextColor(context.getResources().getColor(R.color.text_color_333333));
                            key.setTextSize(KEY_TEXT_SPECIAL_SIZE);
                            key.setGravity(Gravity.CENTER);
                            key.setBackgroundResource(R.drawable.shape_safe_key_gray_bg);
                            key.setTag(letter[i][j]);
                            key.setText(letter[i][j]);
                            key.setOnTouchListener(this);
                            row.addView(key);
                        }else if (letter[i][j].equals(KeyTpye.KEY_SYMBOL)){                                                           // 字符
                            LayoutParams keyLp = new LayoutParams(getScalelSizeForWidth(KEY_SYMBOL_SIZE), getScalelSizeForHeight(KEY_HEIGHT));
                            keyLp.leftMargin = getScalelSizeForWidth(KEY_MARGIN_LEFT);
                            TextView key = new TextView(context);
                            key.setLayoutParams(keyLp);
                            key.setTextColor(context.getResources().getColor(R.color.text_color_333333));
                            key.setTextSize(KEY_TEXT_SPECIAL_SIZE);
                            key.setGravity(Gravity.CENTER);
                            key.setBackgroundResource(R.drawable.shape_safe_key_gray_bg);
                            key.setTag(letter[i][j]);
                            key.setText("符");
                            key.setOnTouchListener(this);
                            row.addView(key);
                        }else if (letter[i][j].equals(KeyTpye.KEY_FINISH)){                                                          // 完成
                            LayoutParams keyLp = new LayoutParams(getScalelSizeForWidth(KEY_FINISH_SIZE), getScalelSizeForHeight(KEY_HEIGHT));
                            keyLp.leftMargin = getScalelSizeForWidth(KEY_MARGIN_LEFT);
                            TextView key = new TextView(context);
                            key.setLayoutParams(keyLp);
                            key.setTextColor(context.getResources().getColor(R.color.text_color_ffffff));
                            key.setTextSize(KEY_TEXT_SPECIAL_SIZE);
                            key.setGravity(Gravity.CENTER);
                            key.setBackgroundResource(R.drawable.shape_safe_key_finish_bg);
                            key.setTag(letter[i][j]);
                            key.setText("完成");
                            key.setOnTouchListener(this);
                            row.addView(key);
                        }
                    }
                }

                addView(row);
            }
        }
    }

    /**
     * 字符键盘
     * @param context
     */
    private void initKeySymbol(Context context){
        removeAllViews();
        setGravity(Gravity.CENTER_HORIZONTAL);
        if (context != null && symbolLetter != null && symbolLetter.length > 0){
            for (int i = 0; i < symbolLetter.length; i++) {
                LinearLayout row = new LinearLayout(context);
                row.setOrientation(HORIZONTAL);
                row.setGravity(Gravity.CENTER_VERTICAL);
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                lp.topMargin = getScalelSizeForHeight(KEY_ROW_MARGIN_TOP);
                if (i == 3){
                    lp.bottomMargin = getScalelSizeForHeight(KEY_ROW_MARGIN_BOTTOM);
                }

                row.setLayoutParams(lp);

                for (int j = 0; j < symbolLetter[i].length; j++) {

                    if (!isSpecialLetter(symbolLetter[i][j])){
                        LayoutParams keyLp = new LayoutParams(getScalelSizeForWidth(KEY_WIDTH),
                                getScalelSizeForHeight(KEY_HEIGHT));

                        if (!isFirstLetter(symbolLetter[i][j])){
                            keyLp.leftMargin = getScalelSizeForWidth(KEY_MARGIN_LEFT);
                        }

                        TextView key = new TextView(context);
                        key.setLayoutParams(keyLp);
                        key.setTextColor(context.getResources().getColor(R.color.text_color_333333));
                        key.setTextSize(KEY_TEXT_SIZE);
                        key.setGravity(Gravity.CENTER);
                        key.setBackgroundResource(R.drawable.shape_safe_key_normal_bg);
                        key.setTag(symbolLetter[i][j]);
                        key.setText(symbolLetter[i][j]);
                        key.setOnTouchListener(this);
                        row.addView(key);
                    }else{

                        if (symbolLetter[i][j].equals(KeyTpye.KEY_DEL)){                                                               // 删除按键
                            LayoutParams imgLp = new LayoutParams(getScalelSizeForWidth(KEY_SYMBOL_FINISH),
                                    getScalelSizeForHeight(KEY_DEL_SIZE));
                            imgLp.leftMargin = getScalelSizeForWidth(KEY_MARGIN_LEFT);
                            ImageView keyImage = new ImageView(context);
                            keyImage.setLayoutParams(imgLp);
                            keyImage.setScaleType(ImageView.ScaleType.CENTER);
                            keyImage.setBackgroundResource(R.drawable.shape_safe_key_gray_bg);
                            keyImage.setImageResource(R.drawable.ic_key_safe_del);
                            keyImage.setTag(symbolLetter[i][j]);
                            keyImage.setOnTouchListener(this);
                            row.addView(keyImage);
                        }else if (symbolLetter[i][j].equals(KeyTpye.KEY_123)){                                                               // 数字键盘
                            LayoutParams keyLp = new LayoutParams(getScalelSizeForWidth(KEY_SYMBOL_FINISH), getScalelSizeForHeight(KEY_HEIGHT));
                            keyLp.leftMargin = getScalelSizeForWidth(KEY_MARGIN_LEFT);
                            TextView key = new TextView(context);
                            key.setLayoutParams(keyLp);
                            key.setTextColor(context.getResources().getColor(R.color.text_color_333333));
                            key.setTextSize(KEY_TEXT_SPECIAL_SIZE);
                            key.setGravity(Gravity.CENTER);
                            key.setBackgroundResource(R.drawable.shape_safe_key_gray_bg);
                            key.setTag(symbolLetter[i][j]);
                            key.setText(symbolLetter[i][j]);
                            key.setOnTouchListener(this);
                            row.addView(key);
                        }else if (symbolLetter[i][j].equals(KeyTpye.KEY_ABC)){                                                           // ABC
                            LayoutParams keyLp = new LayoutParams(getScalelSizeForWidth(KEY_SYMBOL_FINISH), getScalelSizeForHeight(KEY_HEIGHT));
                            keyLp.leftMargin = getScalelSizeForWidth(KEY_MARGIN_LEFT);
                            TextView key = new TextView(context);
                            key.setLayoutParams(keyLp);
                            key.setTextColor(context.getResources().getColor(R.color.text_color_333333));
                            key.setTextSize(KEY_TEXT_SPECIAL_SIZE);
                            key.setGravity(Gravity.CENTER);
                            key.setBackgroundResource(R.drawable.shape_safe_key_gray_bg);
                            key.setTag(symbolLetter[i][j]);
                            key.setText("ABC");
                            key.setOnTouchListener(this);
                            row.addView(key);
                        }else if (symbolLetter[i][j].equals(KeyTpye.KEY_FINISH)){                                                          // 完成
                            LayoutParams keyLp = new LayoutParams(getScalelSizeForWidth(KEY_SYMBOL_FINISH), getScalelSizeForHeight(KEY_HEIGHT));
                            keyLp.leftMargin = getScalelSizeForWidth(KEY_MARGIN_LEFT);
                            TextView key = new TextView(context);
                            key.setLayoutParams(keyLp);
                            key.setTextColor(context.getResources().getColor(R.color.text_color_ffffff));
                            key.setTextSize(KEY_TEXT_SPECIAL_SIZE);
                            key.setGravity(Gravity.CENTER);
                            key.setBackgroundResource(R.drawable.shape_safe_key_finish_bg);
                            key.setTag(symbolLetter[i][j]);
                            key.setText("完成");
                            key.setOnTouchListener(this);
                            row.addView(key);
                        }
                    }
                }

                addView(row);
            }
        }
    }

    private void initKeyNum(Context context){
        removeAllViews();
        setGravity(Gravity.CENTER_HORIZONTAL);

        String[] randNum = KeyboardUtils.getRandomNum(isRandomNum);

        if (randNum != null && randNum.length == 10) {
            for (int i = 0; i < randNum.length; i++) {
                int row = i / 3;
                int column = i - row * 3;

                if (row == 3 && i == 9) {
                    numKey[3][2] = randNum[i];
                } else {
                    numKey[row][column] = randNum[i];
                }
            }
        }

        if (context != null && numKey != null && numKey.length > 0){
            for (int i = 0; i < numKey.length; i++) {
                LinearLayout row = new LinearLayout(context);
                row.setOrientation(HORIZONTAL);
                row.setGravity(Gravity.CENTER_VERTICAL);
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                lp.topMargin = getScalelSizeForHeight(KEY_NUM_MARGIN);
                if (i == 3){
                    lp.bottomMargin = getScalelSizeForHeight(KEY_NUM_MARGIN);
                }

                row.setLayoutParams(lp);

                for (int j = 0; j < numKey[i].length; j++) {

                    if (!isSpecialLetter(numKey[i][j])){
                        LayoutParams keyLp = new LayoutParams(getScalelSizeForWidth(KEY_NUM_WIDTH),
                                getScalelSizeForHeight(KEY_NUM_HEIGHT));

                        if (j != 0){
                            keyLp.leftMargin = getScalelSizeForWidth(KEY_NUM_MARGIN);
                        }

                        TextView key = new TextView(context);
                        key.setLayoutParams(keyLp);
                        key.setTextColor(context.getResources().getColor(R.color.text_color_333333));
                        key.setTextSize(KEY_NUM_SIZE);
                        key.setGravity(Gravity.CENTER);
                        key.setBackgroundResource(R.drawable.selector_safe_key_num_bg);
                        key.setTag(numKey[i][j]);
                        key.setText(numKey[i][j]);
                        key.setOnTouchListener(this);
                        row.addView(key);
                    }else{

                        if (numKey[i][j].equals(KeyTpye.KEY_DEL)){                                                               // 删除按键
                            LayoutParams imgLp = new LayoutParams(getScalelSizeForWidth(KEY_NUM_SPECIAL),
                                    getScalelSizeForHeight(KEY_NUM_HEIGHT));
                            imgLp.leftMargin = getScalelSizeForWidth(KEY_NUM_MARGIN);
                            ImageView keyImage = new ImageView(context);
                            keyImage.setLayoutParams(imgLp);
                            keyImage.setScaleType(ImageView.ScaleType.CENTER);
                            keyImage.setBackgroundResource(R.drawable.shape_safe_key_gray_bg);
                            keyImage.setImageResource(R.drawable.ic_key_safe_del);
                            keyImage.setTag(numKey[i][j]);
                            keyImage.setOnTouchListener(this);
                            row.addView(keyImage);
                        }else if (numKey[i][j].equals(KeyTpye.KEY_SYMBOL)){                                                               // 符号键盘
                            LayoutParams keyLp = new LayoutParams(getScalelSizeForWidth(KEY_NUM_SPECIAL), getScalelSizeForHeight(KEY_NUM_HEIGHT));
                            TextView key = new TextView(context);
                            key.setLayoutParams(keyLp);
                            key.setTextColor(context.getResources().getColor(R.color.text_color_333333));
                            key.setTextSize(KEY_TEXT_SPECIAL_SIZE);
                            key.setGravity(Gravity.CENTER);
                            key.setBackgroundResource(R.drawable.shape_safe_key_gray_bg);
                            key.setTag(numKey[i][j]);
                            key.setText("符");
                            key.setOnTouchListener(this);
                            row.addView(key);
                        }else if (numKey[i][j].equals(KeyTpye.KEY_ABC)){                                                           // ABC
                            LayoutParams keyLp = new LayoutParams(getScalelSizeForWidth(KEY_NUM_SPECIAL), getScalelSizeForHeight(KEY_NUM_HEIGHT));
                            keyLp.leftMargin = getScalelSizeForWidth(KEY_NUM_MARGIN);
                            TextView key = new TextView(context);
                            key.setLayoutParams(keyLp);
                            key.setTextColor(context.getResources().getColor(R.color.text_color_333333));
                            key.setTextSize(KEY_TEXT_SPECIAL_SIZE);
                            key.setGravity(Gravity.CENTER);
                            key.setBackgroundResource(R.drawable.shape_safe_key_gray_bg);
                            key.setTag(numKey[i][j]);
                            key.setText("ABC");
                            key.setOnTouchListener(this);
                            row.addView(key);
                        }else if (numKey[i][j].equals(KeyTpye.KEY_FINISH)){                                                          // 完成
                            LayoutParams keyLp = new LayoutParams(getScalelSizeForWidth(KEY_NUM_SPECIAL), getScalelSizeForHeight(KEY_NUM_HEIGHT));
                            keyLp.leftMargin = getScalelSizeForWidth(KEY_NUM_MARGIN);
                            TextView key = new TextView(context);
                            key.setLayoutParams(keyLp);
                            key.setTextColor(context.getResources().getColor(R.color.text_color_ffffff));
                            key.setTextSize(KEY_TEXT_SPECIAL_SIZE);
                            key.setGravity(Gravity.CENTER);
                            key.setBackgroundResource(R.drawable.shape_safe_key_finish_bg);
                            key.setTag(numKey[i][j]);
                            key.setText("完成");
                            key.setOnTouchListener(this);
                            row.addView(key);
                        }
                    }
                }

                addView(row);
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (view.getTag() != null) {
                if (!isSpecialLetter((String) view.getTag())) {
                    if (isNumber((String) view.getTag())){
                        view.setBackgroundResource(R.drawable.shape_safe_key_num_press_bg);
                    }else {
                        if (isLeftFirstLetter((String) view.getTag())) {
                            addLeftPerview(view);
                        } else if (isRightFirstLetter((String) view.getTag())) {
                            addRightPerview(view);
                        } else {
                            addMiddlePerview(view);
                        }
                    }

                    if (onKeyChangeListener != null){
                        onKeyChangeListener.onChangeListener((String) view.getTag());
                    }
                }else{
                    if (view.getTag().equals(KeyTpye.KEY_UP)){
                        initKeyLetter(getContext(), capitalLetter, false);
                    }else if (view.getTag().equals(KeyTpye.KEY_UP_CAP)){
                        initKeyLetter(getContext(), littleLetter, true);
                    }else if (view.getTag().equals(KeyTpye.KEY_SYMBOL)){
                        if (!TextUtils.isEmpty(isSpecialPassWordRequired) &&
                                (isSpecialPassWordRequired.equals("true")
                                || isSpecialPassWordRequired.equals("1"))) {
                            initKeySymbol(getContext());
                        }
                    }else if (view.getTag().equals(KeyTpye.KEY_ABC)){
                        initKeyLetter(getContext(), littleLetter, true);
                    }else if (view.getTag().equals(KeyTpye.KEY_123)){
                        initKeyNum(getContext());
                    }else if (view.getTag().equals(KeyTpye.KEY_DEL)
                            || view.getTag().equals(KeyTpye.KEY_FINISH)){
                        if (onKeyChangeListener != null){
                            onKeyChangeListener.onChangeListener((String) view.getTag());
                        }
                    }
                }
            }

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

        }else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
            if (isNumber((String) view.getTag())){
                view.setBackgroundResource(R.drawable.shape_safe_key_normal_bg);
            }
            removePerview(true);
        }
        return true;
    }

    /**
     * 添加左边第一个字符点击的预览图
     * @param view
     */
    private void addLeftPerview(View view){
        if (view != null){

            if (mWindowManager == null){
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
            int height=perview.getMeasuredHeight();
            int width=perview.getMeasuredWidth();

            // 获取状态栏高度
            int statusBarHeight = WindowUtils.getStatusBarHeight(getContext());

            WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
            wlp.flags =  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

            if (isSecure) {
                wlp.flags = wlp.flags | WindowManager.LayoutParams.FLAG_SECURE;
            }

            wlp.format = PixelFormat.TRANSLUCENT;
            wlp.gravity = Gravity.LEFT | Gravity. TOP;
            wlp.x = source[0];
            wlp.y = source[1] + srcH - height - statusBarHeight;
            wlp.width = width;
            wlp.height = height;
            mWindowManager.addView(perview, wlp);
        }
    }

    /**
     * 添加右边第一个字符点击的预览图
     * @param view
     */
    private void addRightPerview(View view){
        if (view != null){

            if (mWindowManager == null){
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
            int height=perview.getMeasuredHeight();
            int width=perview.getMeasuredWidth();

            // 获取状态栏高度
            int statusBarHeight = WindowUtils.getStatusBarHeight(getContext());

            WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
            wlp.format = PixelFormat.TRANSLUCENT;
            wlp.flags =  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

            if (isSecure) {
                wlp.flags = wlp.flags | WindowManager.LayoutParams.FLAG_SECURE;
            }

            wlp.gravity = Gravity.LEFT | Gravity. TOP;
            wlp.x = source[0] + srcW - width;
            wlp.y = source[1] + srcH - height - statusBarHeight;
            wlp.width = width;
            wlp.height = height;
            mWindowManager.addView(perview, wlp);
        }
    }

    private void addMiddlePerview(View view){
        if (view != null){

            if (mWindowManager == null){
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
            int height=perview.getMeasuredHeight();
            int width=perview.getMeasuredWidth();

            // 获取状态栏高度
            int statusBarHeight = WindowUtils.getStatusBarHeight(getContext());

            WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
            wlp.format = PixelFormat.TRANSLUCENT;
            wlp.flags =  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

            if (isSecure) {
                wlp.flags = wlp.flags | WindowManager.LayoutParams.FLAG_SECURE;
            }

            wlp.gravity = Gravity.LEFT | Gravity. TOP;
            wlp.x = source[0] + srcW/2 - width/2;
            wlp.y = source[1] + srcH - height - statusBarHeight;
            wlp.width = width;
            wlp.height = height;
            mWindowManager.addView(perview, wlp);
        }
    }

    public boolean isNumber(String str) {
        Pattern intPattern = Pattern.compile("^[0-9]*");//整数样式匹配
        return intPattern.matcher(str).matches();
    }

    private void removePerview(boolean isDelay){
        if (perview != null) {
            if (mWindowManager == null) {
                mWindowManager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
            }
            mWindowManager.removeViewImmediate(perview);
            perview = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removePerview(false);
    }

    public void setSecure(boolean isSecure) {
        this.isSecure = isSecure;
    }
}
