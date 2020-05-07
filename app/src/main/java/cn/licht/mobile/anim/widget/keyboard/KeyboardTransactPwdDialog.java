package cn.licht.mobile.anim.widget.keyboard;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;



import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import cn.licht.mobile.anim.R;
import cn.licht.mobile.anim.WindowUtils;
import cn.licht.mobile.anim.dialog.BaseDialog;

/**
 * Created by ly on 2018/4/23.
 */

public class KeyboardTransactPwdDialog extends BaseDialog implements View.OnClickListener, OnKeyEvent {

    private TransactPwdEditText tpetTransactionPassword;
    private StringBuffer realValue;
    private String ps = "";
    private String mobileNo;
    private String money;
    private JSONArray contents;
    private JSONArray cardNums;
    private Map<String, Object> inputMap = new HashMap<>();
    private KeyboardFullSafeWindow.OnKeyChangeListener onKeyChangeListener;
    private String styleType; //通讯录转帐 应行卡转帐
    private String accountName; //转帐目标人名称
    private String accountNum;//转帐目标电话好吗
    private String accountBankLogo; //转帐目标银行logo
    private boolean isRandomNum = false;

    public KeyboardTransactPwdDialog(Context context, JSONObject data) {
        super(context, R.style.InputTransactPasswordDialog);
        init(context, data);
    }

    private void init(Context context, JSONObject data) {

        if (data != null) {
//            contents = H5Utils.getJSONArray(data, "contents", null);
//            money = H5Utils.getString(data, "money", "");
//            cardNums = H5Utils.getJSONArray(data, "cardNum", null);
//            ps = H5Utils.getString(data, "ps", "AE0");
//            mobileNo = H5Utils.getString(data, "mobileNo", "");
//            styleType = H5Utils.getString(data, "styleType", "");
//            accountName = H5Utils.getString(data, "accountName", "");
//            accountNum = H5Utils.getString(data, "accountPhoneNumOrCardId", "");
//            accountBankLogo = H5Utils.getString(data, "accountBankLogo", "");
//            isRandomNum = H5Utils.getBoolean(data, "isRandomNum", true);
        }

        realValue = new StringBuffer();

        Window window = getWindow();
        if (window != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
            WindowManager.LayoutParams windowManagerLayoutParams = window.getAttributes();
            windowManagerLayoutParams.gravity = Gravity.BOTTOM;
            int identifier = getContext().getResources().getIdentifier("PopupWindowAnimation", "style", getContext().getPackageName());
            window.setWindowAnimations(identifier);
            window.setAttributes(windowManagerLayoutParams);
        }

        setCanceledOnTouchOutside(false);
        View contentView = getLayoutInflater().inflate(R.layout.dialog_transact_pwd_layout, null);
        TextView tvAmount = (TextView) contentView.findViewById(R.id.tv_amount);
        TextView tvCardNum = (TextView) contentView.findViewById(R.id.tv_card_num);
        TextView tvTitle = (TextView) contentView.findViewById(R.id.zz_title_tv);
        ImageView bankLogo = (ImageView) contentView.findViewById(R.id.bank_logo);
        LinearLayout transactUserInfoLinear = (LinearLayout) contentView.findViewById(R.id.transact_user_info_linear); //通讯录转帐 银行卡转帐要显示的信息
        TextView subTitle = (TextView) contentView.findViewById(R.id.phone_num_or_ic_card_tv);
        ViewGroup.LayoutParams viewGroupLayoutParams = new ViewGroup.LayoutParams(WindowUtils.getScreenWidth(getContext()), -2);
        setContentView(contentView, viewGroupLayoutParams);

        if (!TextUtils.isEmpty(money)) {
            BigDecimal bigDecimal = new BigDecimal(money);
            String amount = "¥" + bigDecimal.doubleValue();
            SpannableString spannableString = new SpannableString(amount);
            spannableString.setSpan(new AbsoluteSizeSpan(13, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new AbsoluteSizeSpan(24, true), 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvAmount.setText(spannableString);
        } else if (contents != null && contents.size() > 0) {
            SpannableStringBuilder amountSpannableStringBuilder = createSpannableStringBuilder(contents);
            if (amountSpannableStringBuilder != null) {
                tvAmount.setText(amountSpannableStringBuilder);
            }
        }else {
            tvAmount.setVisibility(View.GONE);
        }

        //处理cardNum
        SpannableStringBuilder spannableStringBuilder = createSpannableStringBuilder(cardNums);
        if (spannableStringBuilder != null){
            tvCardNum.setText(spannableStringBuilder);
        }

        //通讯录转帐 银行卡转帐
        if (TextUtils.isEmpty(accountNum) || TextUtils.isEmpty(styleType)){
            transactUserInfoLinear.setVisibility(View.GONE);
        }else {
            transactUserInfoLinear.setVisibility(View.VISIBLE);
        }
        if (accountName != null){
            tvTitle.setText(accountName);
        }
        if (accountNum != null){
            subTitle.setText(accountNum);

        }

        //小图标展示
        if (styleType != null) {
            if (styleType.equals("0")) {
                //通讯录转帐
                bankLogo.setImageResource(R.drawable.ic_pwd_dailog_phone);
            } else if (styleType.equals("1")) {
                //银行卡转帐
                if (TextUtils.isEmpty(accountBankLogo)) {
                    bankLogo.setImageResource(R.drawable.ic_pwd_diloag_bank_default);
                } else {
                    if (!TextUtils.isEmpty(accountBankLogo) && accountBankLogo.equals("CGB")){
                        bankLogo.setImageResource(R.drawable.ic_logo_keyboard);
                    }else {
                        Glide.with(context).load(accountBankLogo).dontAnimate().error(R.drawable.ic_pwd_diloag_bank_default).into(bankLogo);
                    }
                }
            }
        }
        contentView.findViewById(R.id.iv_close).setOnClickListener(this);
        KeyboardNumView llKeyboard = (KeyboardNumView) contentView.findViewById(R.id.k_keyboard);
        llKeyboard.setIsRandomNum(isRandomNum);
        llKeyboard.setKeyType(KeyboardType.KEY_NUM_TYPE);
        llKeyboard.setOnKeyChangeListener(this);

        tpetTransactionPassword = (TransactPwdEditText) contentView.findViewById(R.id.tpet_transact_password);

        // 隐藏系统键盘
        if (Build.VERSION.SDK_INT <= 10) {
            tpetTransactionPassword.setInputType(InputType.TYPE_NULL);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            Class<EditText> editTextClass = EditText.class;
            try {
                Method method = editTextClass.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(tpetTransactionPassword, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        tpetTransactionPassword.reset();
        tpetTransactionPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String transactPassword = tpetTransactionPassword.getText().toString().trim();

                if (transactPassword.length() == 6) {
                    if (onKeyChangeListener != null) {
                        onKeyChangeListener.onChange(inputMap);
                    }

                    dismiss();
                }
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (onKeyChangeListener != null){
                    onKeyChangeListener.onDismiss();
                }
            }
        });
    }

    private SpannableStringBuilder createSpannableStringBuilder(JSONArray contents) {
        if (contents == null){
            return null;
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        for (int i = 0; i < contents.size(); i++) {
            JSONObject contentJson = contents.getJSONObject(i);
            String content = contentJson.getString("content");
            int size = contentJson.getInteger("size");
            String color = contentJson.getString("color");
            int colorValue = 0;

            if (!TextUtils.isEmpty(color)) {
                try {
                    colorValue = Color.parseColor(color);
                } catch (Exception e) {
                }
            }

            if (!TextUtils.isEmpty(content)) {
                SpannableString spannableString = new SpannableString(content);
                spannableString.setSpan(new AbsoluteSizeSpan(size/2, true), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                if (colorValue != 0) {
                    spannableString.setSpan(new ForegroundColorSpan(colorValue), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                ssb.append(spannableString);
            }
        }

        return ssb;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_close) {
            close();

        }
    }

    @Override
    public void onChangeListener(String change) {
        if (!TextUtils.isEmpty(change)) {

            if (change.equals(KeyTpye.KEY_DEL)) {
                delete();

                if (tpetTransactionPassword != null) {
                    int index = tpetTransactionPassword.getSelectionStart();
                    if (index > 0) {
                        tpetTransactionPassword.getText().delete(index - 1, index);
                    }
                }
            } else if (change.equals(KeyTpye.KEY_FINISH)) {
                dismiss();
            } else {
                addChar(change);

                if (tpetTransactionPassword != null) {
                    int index = tpetTransactionPassword.getSelectionStart();
                    tpetTransactionPassword.getText().insert(index, change);
                }
            }
        }
    }

    @Override
    public void onDelAllListener() {

    }

    private void close() {
        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put("action", "hide");

//        if (onKeyChangeListener != null){
//            onKeyChangeListener.onChange(inputMap);
//        }

        dismiss();
    }

    private void delete() {
        if (realValue.length() > 0) {
            realValue.deleteCharAt(realValue.length() - 1);
        }

        String valuetrue = realValue.toString();

        String[] pins = KeyboardFullSafeWindow.getPin(valuetrue);

        inputMap.put("apin", pins[0]);
        inputMap.put("epin", pins[1]);
    }

    private void addChar(String str) {
        if (realValue.length() < 6 && !TextUtils.isEmpty(str)) {
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

            String valuetrueinp = realValue.toString();

            String[] pins = KeyboardFullSafeWindow.getPin(valuetrueinp);

            inputMap.put("apin", pins[0]);
            inputMap.put("epin", pins[1]);
        }
    }

    public void setOnKeyChangeListener(KeyboardFullSafeWindow.OnKeyChangeListener onKeyChangeListener) {
        this.onKeyChangeListener = onKeyChangeListener;
    }
}
