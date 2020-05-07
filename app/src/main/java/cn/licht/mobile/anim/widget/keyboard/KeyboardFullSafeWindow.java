package cn.licht.mobile.anim.widget.keyboard;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.licht.mobile.anim.R;
import cn.licht.mobile.anim.RandomUtil;
import cn.licht.mobile.anim.WindowUtils;
import cn.licht.mobile.anim.dialog.BaseDialog;

/**
 * Created by ly on 2018/4/14.
 */

public class KeyboardFullSafeWindow extends BaseDialog implements OnKeyEvent {

    private EditText editText;
    private StringBuffer realValue;
    private String ps = "";
    private boolean isVerify;
    private String mobileNo;
    private String pswIndex;
    private int maxlength;
    private String isSpecialPassWordRequired;
    private String actionName;
    private OnKeyChangeListener onKeyChangeListener;
    private String curText;                                          //  当前输入的文本（用于非加密键盘拼接字符串）
    private String type;                                             //  键盘类型
    private OnKeyEventIntercept onKeyEventIntercept;
    private int keyboardHeight;
    private String certNo;
    private String certType;

    //发现精彩兼任参数
    private boolean isRegister;
    private boolean isVibrator;                                      // 是否为震动模式
    private boolean isTelPwd;
    private boolean isAuto;
    private boolean isNum;
    private boolean isRandomNum = true;
    private int isTelPwdLen = 4; //数字密码规则长度
    private boolean isNeedVerify;                                   // 兼容发现精彩

    public KeyboardFullSafeWindow(Activity activity, JSONObject data) {
        super(activity, R.style.Transact_dialog_style);
        initData(data);
        initView(activity);
    }

    public KeyboardFullSafeWindow(Activity activity, EditText editText, JSONObject data) {
        super(activity, R.style.Transact_dialog_style);
        initData(data);
        initView(activity);
        this.editText = editText;
    }

    public String getCurText() {
        if (realValue != null) {
            return realValue.toString();
        }
        return null;
    }

    public void setCurText(String value) {
        realValue = new StringBuffer(value);
    }

    private void initData(JSONObject data) {
        type =KeyboardType.KEY_CAR_NONUM_TYPE;
        if (data != null) {
//            actionName = H5Utils.getString(data, "actionName", "");
//            isSpecialPassWordRequired = H5Utils.getString(data, "isSpecialPassWordRequired", "");
//            ps = H5Utils.getString(data, "ps", "AE0");
//            isVerify = H5Utils.getBoolean(data, "isVerify", false);
//            mobileNo = H5Utils.getString(data, "mobileNo", "");
//            pswIndex = H5Utils.getString(data, "pswIndex", "");
//            type = H5Utils.getString(data, "type", "0");

//            curText = H5Utils.getString(data, "curText", "");
//            maxlength = H5Utils.getInt(data, "maxSize", Integer.MAX_VALUE);

//            isRegister = H5Utils.getBoolean(data, "isRegister", false);
//            isVibrator = H5Utils.getBoolean(data, "isVibrator", true);
//            isTelPwd = H5Utils.getBoolean(data, "isTelPwd", false);
//            isTelPwdLen = H5Utils.getInt(data, "isTelPwdLen", 4);
//            isAuto = H5Utils.getBoolean(data, "isAuto", false);
//            isNum = H5Utils.getBoolean(data, "isNum", false);
//            isRandomNum = H5Utils.getBoolean(data, "isRandomNum", true);
//            certNo = H5Utils.getString(data, "certNo", "");
//            certType = H5Utils.getString(data, "certType", "");
//            isNeedVerify = H5Utils.getBoolean(data, "isNeedVerify", false);
        }

        realValue = new StringBuffer();
        //if (!TextUtils.isEmpty(curText))
        //realValue.append(curText);
    }

    private void initView(Activity activity) {

        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams windowManagerLayoutParams = window.getAttributes();
            windowManagerLayoutParams.gravity = Gravity.BOTTOM;
            windowManagerLayoutParams.width = WindowUtils.getScreenWidth(activity);
            windowManagerLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            int identifier = getContext().getResources().getIdentifier("PopupWindowAnimation", "style", getContext().getPackageName());
            window.setWindowAnimations(identifier);

            windowManagerLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

            window.setAttributes(windowManagerLayoutParams);
        }

//        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        setFocusable(false);
//        final ColorDrawable dw = new ColorDrawable(0x7f000000);// 实例化一个ColorDrawable颜色为半透明
//        setBackgroundDrawable(dw);// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
//        int identifier = activity.getApplication().getResources().getIdentifier("PopupWindowAnimation", "style", activity.getPackageName());
//        setAnimationStyle(identifier);//设置显示和消失动画
        LayoutInflater inflater = LayoutInflater.from(activity);
        View conentView;
        if (type.equals(KeyboardType.TYPE_NUM_KEYBOARD)
                || type.equals(KeyboardType.TYPE_SYMBOL_KEYBOARD)
                || type.equals(KeyboardType.TYPE_LITTLE_LETTER_KEYBOARD)
                || type.equals(KeyboardType.TYPE_CAP_LETTER_KEYBOARD)) {

            conentView = inflater.inflate(R.layout.keyboard_safe_layout, null);
            KeyboardSafeView llKeyboard = (KeyboardSafeView) conentView.findViewById(R.id.k_keyboard);
            llKeyboard.setIsRandomNum(isRandomNum);
            llKeyboard.setKeyboardType(type);
            llKeyboard.setVibrator(isVibrator);
            llKeyboard.setOnKeyChangeListener(this);

            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
                llKeyboard.setSecure(true);
            }
        } else if (KeyboardType.KEY_CODE_TYPE.equals(type)) {
            conentView = inflater.inflate(R.layout.keyboard_safe_num_layout, null);
            KeyboardNumView llKeyboard = (KeyboardNumView) conentView.findViewById(R.id.k_keyboard);
            llKeyboard.setIsRandomNum(isRandomNum);
            llKeyboard.setKeyType(KeyboardType.KEY_CODE_TYPE);
            llKeyboard.setVibrator(isVibrator);
            llKeyboard.setOnKeyChangeListener(this);

            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
            }
        } else if (KeyboardType.KEY_NUM_TYPE.equals(type)
                || KeyboardType.KEY_AMOUNT_TYPE.equals(type)
                || KeyboardType.KEY_IDCARD_TYPE.equals(type)) {
            conentView = inflater.inflate(R.layout.keyboard_idcard, null);
            KeyboardNumView llKeyboard = (KeyboardNumView) conentView.findViewById(R.id.ll_keyboard);
            llKeyboard.setVibrator(isVibrator);
            llKeyboard.setIsRandomNum(true);
            llKeyboard.setOnKeyChangeListener(this);
            llKeyboard.setKeyType(type);

        } else if (KeyboardType.KEY_CAR_NUM_TYPE.equals(type) || KeyboardType.KEY_CAR_NONUM_TYPE.equals(type) || KeyboardType.KEY_CAR_PRV_NUM_TYPE.equals(type)){
            conentView = inflater.inflate(R.layout.keyboard_car_input_layout, null);
            KeyboardCarNumView keyboardCarNumView = conentView.findViewById(R.id.ll_keyboard);
            keyboardCarNumView.setOnKeyChangeListener(this);
            keyboardCarNumView.setKeyboardType(type);
        }else {
            conentView = inflater.inflate(R.layout.keyboard_safe_layout, null);
        }
//        ImageView ivDown = (ImageView) conentView.findViewById(R.id.iv_down);
        ViewGroup.LayoutParams viewGroupLayoutParams = new ViewGroup.LayoutParams(WindowUtils.getScreenWidth(getContext()), -2);
        setContentView(conentView, viewGroupLayoutParams);

//        if (isAuto) {
//            setOutsideTouchable(true);
//        }

        setCancelable(true);
        setCanceledOnTouchOutside(isAuto);
        // 计算键盘高度
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        conentView.measure(width, height);
        keyboardHeight = conentView.getMeasuredHeight();

//        ivDown.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hideAction();
//                dismiss();
//            }
//        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hideAction();
            }
        });
    }

    public int getHeight() {
        return keyboardHeight;
    }

    private void hideAction() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("action", "hide");
        inputMap.put("isHidden", true);
        inputMap.put("isSecure", true);

        if (onKeyChangeListener != null) {
            onKeyChangeListener.onChange(inputMap);
        }
    }

    private void finish() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("action", "finish");
        inputMap.put("isHidden", true);
        inputMap.put("isSecure", true);

        if (onKeyChangeListener != null) {
            onKeyChangeListener.onChange(inputMap);
        }
    }

    private void delete() {
        if (realValue.length() > 0) {
            realValue.deleteCharAt(realValue.length() - 1);
        }

        int complexFlag = 0;
//        if (isVerify) {
//            complexFlag = Hsmcli.checkWeakPassword(realValue.toString(), mobileNo);
//        } else if (isRegister) {
//            complexFlag = Hsmcli.checkWeakPassword(realValue.toString(), mobileNo);                          // 发现精彩兼容
//        } else if (isTelPwd) {
//            complexFlag = Hsmcli.checkNumWeakPassword(realValue.toString(), isTelPwdLen, certNo, certType);  // 兼容发现精彩弱密码判断增加生日规则
//        }

        String valuetrue = realValue.toString();
        String moveValue = RandomUtil.getInstance().getMoveString(valuetrue);

        Map<String, Object> inputMap = new HashMap<>();

        if (type.equals(KeyboardType.KEY_NUM_TYPE)
                || type.equals(KeyboardType.KEY_AMOUNT_TYPE)
                || type.equals(KeyboardType.KEY_IDCARD_TYPE)) {
            inputMap.put("curText", KeyTpye.KEY_DEL);
            // 发现精彩兼任
            inputMap.put("delete", true);
            inputMap.put("pswRealValue", realValue.toString());
            //realValue.setLength(0);
        } else {
            String[] pins = getPinEx(valuetrue, ps);
            inputMap.put("curText", "");
            inputMap.put("complexFlag", complexFlag + "");
            inputMap.put("apin", pins[0]);
            inputMap.put("epin", pins[1]);
            inputMap.put("randomStr", moveValue);
            // 发现精彩兼任
            inputMap.put("psw", moveValue.toString());
        }
        inputMap.put("len", valuetrue.length() + "");
        inputMap.put("pswIndex", pswIndex);
        inputMap.put("action", "input");

        // 发现精彩兼任
        inputMap.put("actionName", actionName);

        if (onKeyChangeListener != null) {
            onKeyChangeListener.onChange(inputMap);
        }
    }

    private void addChar(String str) {
        if (realValue.length() < maxlength && !TextUtils.isEmpty(str)) {
            String temp = realValue.toString();
            String temp1_1 = "";
            String temp1_2 = "";
            int size = temp.length();
            for (int i = 0; i < temp.length(); i++) {
                temp1_1 = temp1_1.concat("●");
            }
            for (int i = temp.length(); i < size; i++) {
                temp1_2 = temp1_2.concat("●");
            }

            realValue.append(str.trim());

            int complexFlag = 0;
//            if (isVerify) {
//                complexFlag = Hsmcli.checkWeakPassword(realValue.toString(), mobileNo);
//            } else if (isRegister) {
//                complexFlag = Hsmcli.checkWeakPassword(realValue.toString(), mobileNo);                          // 发现精彩兼容
//            } else if (isTelPwd) {
//                complexFlag = Hsmcli.checkNumWeakPassword(realValue.toString(), isTelPwdLen, certNo, certType);  // 兼容发现精彩弱密码判断增加生日规则
//            }

            String valuetrueinp = realValue.toString();
            String moveValue = RandomUtil.getInstance().getMoveString(valuetrueinp);


            Map<String, Object> inputMap = new HashMap<>();

            if (type.equals(KeyboardType.KEY_NUM_TYPE)
                    || type.equals(KeyboardType.KEY_AMOUNT_TYPE)
                    || type.equals(KeyboardType.KEY_IDCARD_TYPE)) {
                inputMap.put("curText", str);
                // 发现精彩兼任
                inputMap.put("psw", str);
                inputMap.put("pswRealValue", realValue.toString());
                //realValue.setLength(0);
            } else {
                String[] pins = getPinEx(valuetrueinp, ps);
                inputMap.put("curText", "");
                inputMap.put("complexFlag", complexFlag + "");
                inputMap.put("apin", pins[0]);
                inputMap.put("epin", pins[1]);
                inputMap.put("randomStr", moveValue);
                // 发现精彩兼任
                inputMap.put("psw", moveValue);
            }

            inputMap.put("len", valuetrueinp.length() + "");
            inputMap.put("pswIndex", pswIndex);
            inputMap.put("action", "input");
            // 发现精彩兼任
            inputMap.put("actionName", actionName);

            if (onKeyChangeListener != null) {
                onKeyChangeListener.onChange(inputMap);
            }
        }
    }

    /**
     * 可以根据ps参数选择是否使用国寿公钥
     * @param valuetrue
     * @param ps
     * @return
     */
    public static String[] getPinEx(String valuetrue, String ps) {
        String[] pins = new String[2];

//        pins[0] = Hsmcli.PkEncryptAPin(valuetrue, ps);
//        pins[1] = Hsmcli.PkEncryptEPin(valuetrue, ps);

        return pins;
    }

    public static String[] getPin(String valuetrue) {
        String[] pins = new String[2];

//        pins[0] = Hsmcli.PkEncryptAPin(valuetrue, "");
//        pins[1] = Hsmcli.PkEncryptEPin(valuetrue, "");

        return pins;
    }

    public static String[] getPin(String moveValue, String random) {
        if (!TextUtils.isEmpty(moveValue) && !TextUtils.isEmpty(random)) {
            String value = RandomUtil.getInstance().getDecMoveString(moveValue);
//            value = Hsmcli.doEncryptByRandom(value, random);
            String[] pins = getPin(value);
            return pins;
        }

        return null;
    }

    public static String oneTimePadding(JSONObject data) {
//        if (data == null)
//            return "";
//
//        String name = H5Utils.getString(data, "name");
//        String value = H5Utils.getString(data, "pwd");
//        String encryptMode = H5Utils.getString(data, "ps");
//
        String content = "";
//        if ("01".equals(encryptMode)) {// 动态加密
//            content = value;
//        } else if ("A0".equals(encryptMode)) {// APIN加密
//            content = Hsmcli.PkEncryptAPin(value, "");
//        } else if ("A1".equals(encryptMode)) {// APIN加密与动态加密
//            content = Hsmcli.PkEncryptAPin(value, "");
//        } else if ("E0".equals(encryptMode)) {// EPIN加密
//            content = Hsmcli.PkEncryptEPin(value, "");
//        } else if ("E1".equals(encryptMode)) {// EPIN加密与动态加密
//            content = Hsmcli.PkEncryptEPin(value, "");
//        } else if ("AE0".equals(encryptMode)) {// APIN、EPIN加密
//            content = Hsmcli.PkEncryptAPin(value, "")
//                    + Hsmcli.PkEncryptEPin(value, "");
//        } else if ("AE1".equals(encryptMode)) {// APIN、EPIN加密与动态加密
//            try {
//                content = Hsmcli.PkEncryptAPin(value, "")
//                        + Hsmcli.PkEncryptEPin(value, "");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        return content;
    }

    public static String[] decodePin(String encryptPwd) {
        String[] pwd = {"", ""};
//        if (StringUtils.emptyString(encryptPwd)) {
//            return pwd;
//        }
        int pwdLen = encryptPwd.length();
        if (pwdLen == 256) {
            pwd[0] = encryptPwd;
        } else if (pwdLen == 512) {
            pwd[0] = encryptPwd.substring(0, 256);
            pwd[1] = encryptPwd.substring(256, pwdLen - 1);
        } else if (pwdLen == 260) {
            pwd[0] = encryptPwd.substring(0, 256);
        } else if (pwdLen == 516) {
            pwd[0] = encryptPwd.substring(0, 512);
        } else if (pwdLen == 520) {
            pwd[0] = encryptPwd.substring(0, 256);
            pwd[1] = encryptPwd.substring(260, pwdLen - 4);
        } else if (pwdLen == 1032) {
            pwd[0] = encryptPwd.substring(0, 512);
            pwd[1] = encryptPwd.substring(516, pwdLen - 4);
        }
        return pwd;
    }

    /**
     * 显示密码键盘
     *
     * @param act
     */
    public void showKeyBoard(final Activity act) {
        if (act != null) {
            if (!act.isFinishing()) {
//                showAtLocation(act.getWindow()
//                                .getDecorView().findViewById(android.R.id.content),
//                        Gravity.BOTTOM, 0, 0);
                show();
            }
        }
    }

    public void hideKeyboard() {
        if (isShowing()) {
            dismiss();
        }
    }

    public void setOnKeyChangeListener(OnKeyChangeListener onKeyChangeListener) {
        this.onKeyChangeListener = onKeyChangeListener;
    }

    @Override
    public void onChangeListener(String change) {
        if (!TextUtils.isEmpty(change)) {
            if (onKeyEventIntercept != null) {
                if (onKeyEventIntercept.onKeyIntercept(change)) {
                    return;
                }
            }

            if (change.equals(KeyTpye.KEY_DEL)) {
                delete();

                if (editText != null) {
                    int index = editText.getSelectionStart();
                    if (index > 0) {
                        editText.getText().delete(index - 1, index);
                    }
                }
            } else if (change.equals(KeyTpye.KEY_FINISH)) {
                finish();
                dismiss();
            } else {
                addChar(change);

                if (editText != null) {
                    int index = editText.getSelectionStart();
                    editText.getText().insert(index, change);
                }
            }
        }
    }

    @Override
    public void onDelAllListener() {

    }

    public void setOnKeyEventIntercept(OnKeyEventIntercept onKeyEventIntercept) {
        this.onKeyEventIntercept = onKeyEventIntercept;
    }

    public interface OnKeyChangeListener {
        void onChange(Map<String, Object> data);

        void onDismiss();
    }

    public interface OnKeyEventIntercept {
        boolean onKeyIntercept(String key);
    }


}
