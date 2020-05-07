package cn.licht.mobile.anim;

import android.content.Context;
import android.content.res.Configuration;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.licht.mobile.anim.dialog.BaseDialog;


public class UserProtocolDialog extends BaseDialog {

    private static final String TAG = "UserProtocolDialog";

    public UserProtocolDialog(Context context, String title, String content, String left, String right, DialogOnClickListener dialogOnClickListener) {
        super(context, R.style.dialog);
        init(context, title, content, left, right, dialogOnClickListener);
    }

    private void init(Context context, String title, String content, String left, String right, final DialogOnClickListener dialogOnClickListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_share_protocal_layout, null);
        TextView tvTitle = (TextView) layout.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) layout.findViewById(R.id.tv_content);
        TextView tvLeft = (TextView) layout.findViewById(R.id.tv_left);
        TextView tvRight = (TextView) layout.findViewById(R.id.tv_right);
        MaxScrollView maxScrollView = layout.findViewById(R.id.maxscroll);
        final LongDialogCoverView xxFrameLayout = layout.findViewById(R.id.xxf);
        maxScrollView.setiShowCoverListener(new MaxScrollView.IShowCoverListener() {
            @Override
            public void showCover(int coverState) {
                xxFrameLayout.showCover(coverState);
            }
        });

        tvTitle.setText(title);

        try {
            if (!TextUtils.isEmpty(content)) {
//                String html = StringEscapeUtils.unescapeHtml4(content);
//                html = TextUtils.isEmpty(html) ? "" : html.replace("\r\n", "<br/>");
//                html = TextUtils.isEmpty(html) ? "" : html.replace("\n", "<br/>");
//                CharSequence charSequence = Html.fromHtml(html);
                tvContent.setText(content);
            }
        } catch (Exception e) {
        }

        tvLeft.setText(left);
        tvRight.setText(right);

        if (getWindow() != null) {
            getWindow().setDimAmount(0.8f);
        }

        setCancelable(false);
        setCanceledOnTouchOutside(false);
        int width = defaultToScreen750(context, 670);
        addContentView(layout, new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogOnClickListener != null) {
                    dialogOnClickListener.leftOnClickListener(UserProtocolDialog.this, v);
                }
            }
        });

        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogOnClickListener != null) {
                    dialogOnClickListener.rightOnClickListener(UserProtocolDialog.this, v);
                }
            }
        });
    }

    public interface DialogOnClickListener{
        void leftOnClickListener(BaseDialog dialog, View view);
        void rightOnClickListener(BaseDialog dialog, View view);
    }

    public static int defaultToScreen750(Context context, int value) {
        double resultD = (double)(value * 320) / 750.0D;
        long resultL = Math.round(resultD);
        return getScaledValueX(context, (int)resultL);
    }
    public static int getScaledValueX(Context context, int num) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        Configuration mConfiguration = context.getResources().getConfiguration();
        int ori = mConfiguration.orientation;
        if (ori == 2) {
            screenWidth = dm.heightPixels;
        } else if (ori == 1) {
            screenWidth = dm.widthPixels;
        }

        float scaleX = (float)((double)((float)screenWidth) / 320.0D);
        float numtemp = scaleX * (float)num;
        return (int)numtemp;
    }

}
