package cn.licht.mobile.anim;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import cn.licht.mobile.anim.widget.popupwindow.BasePopupwindow;


/**
 * Created by ly on 2018/4/28.
 */

public final class SharePopupwindow extends BasePopupwindow implements View.OnClickListener {
    private OnShareEventListener onShareEventListener;
    private String showFlags;


    public SharePopupwindow(Activity activity, String showFlags) {
        this.showFlags = showFlags;
        init(activity);
    }

    @Override
    protected void init(Activity activity) {
        if (activity == null || activity.getApplication() == null){
            return;
        }
        super.init(activity);
//        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.popupwindow_share_pad_layout, null);
        LinearLayout lnContainView = conentView.findViewById(R.id.ln_contain);
        if (TextUtils.isEmpty(showFlags)) {
            showFlags = "1|2|3|4";
        }
        String[] split = showFlags.split("\\|");
        for (String flag : split) {
            if ("12345".contains(flag)) {
                LinearLayout ln = (LinearLayout) inflater.inflate(R.layout.include_sharepad_item, null);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                ImageView imageView = ln.findViewById(R.id.img);
                TextView textView = ln.findViewById(R.id.tv);
                ln.setTag(flag);
                ln.setOnClickListener(this);
//                switch (flag) {
//                    case "1":
////                        textView.setText(R.string.share_wechat);
////                        imageView.setBackgroundResource(R.mipmap.ic_wechat);
//                        break;
//                    case "2":
//                        textView.setText(R.string.share_wechat_friends);
//                        imageView.setBackgroundResource(R.mipmap.ic_wechat_friends);
//                        break;
//                    case "3":
//                        textView.setText(R.string.share_weibo);
//                        imageView.setBackgroundResource(R.mipmap.ic_weibo);
//                        break;
//                    case "4":
//                        textView.setText(R.string.share_sms);
//                        imageView.setBackgroundResource(R.mipmap.ic_sms);
//                        break;
//                    case "5":
//                        textView.setText(R.string.share_qr);
//                        imageView.setBackgroundResource(R.mipmap.qr);
//                        break;
//                    default:
//                        break;
//
//                }
                lnContainView.addView(ln,layoutParams);


            }
        }


        TextView tvCancel = (TextView) conentView.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
//        conentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        setClippingEnabled(false);
        setContentView(conentView);


    }

    @Override
    public void onClick(View v) {

        if (onShareEventListener == null) {
            return;
        }
        Object tag = v.getTag();
        if (tag == null) {
            return;
        }
        String sTag = (String) tag;
        switch (sTag) {
            case "1":
                onShareEventListener.onShareWechat();
                break;
            case "2":
                onShareEventListener.onShareWechatFriends();
                break;
            case "3":
                onShareEventListener.onShareWeibo();
                break;
            case "4":
                onShareEventListener.onShareSms();
                break;
            case "5":
                onShareEventListener.onShareQr();
                break;
            default:
                break;
        }
        dismiss(true);
    }

    public void setShareEventListener(OnShareEventListener onShareEventListener) {
        this.onShareEventListener = onShareEventListener;
    }

    public interface OnShareEventListener {
        void onShareWechat();

        void onShareWechatFriends();

        void onShareWeibo();

        void onShareSms();

        void onShareQr();
    }


}
