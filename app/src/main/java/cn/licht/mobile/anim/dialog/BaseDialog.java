package cn.licht.mobile.anim.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.util.DensityUtil;

import cn.licht.mobile.anim.R;
import cn.licht.mobile.anim.widget.CGBCheckbox;


/**
 * Created by ly on 2018/4/13.
 */

public class BaseDialog extends Dialog {
    private static final String TAG = "BaseDialog";
    private boolean mDismissByClicBg;
    private Context mContext;

    public BaseDialog(Context context) {
        super(context);
        mContext = context;
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    public static class Builder {
        private Context context;
        private BaseDialog dialog;
        private String title;
        private String content;
        private String buttonLeftText;
        private String buttonRightText;
        private OnClickListener leftlistener;
        private OnClickListener rightlistener;
        private Boolean mClickBgToHide = false;
        private OnClickBgToHideListener mHideListener;
        private String tipsVisibilityType;
        private OnTipsCheckedChangeListener onTipsCheckedChangeListener;
        private CGBCheckbox checkbox;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            try {
                if (context != null) {
                    this.title = context.getResources().getString(title);
                }
            } catch (Exception e) {
            }
            return this;

        }

        public Builder setClickBgToHide(Boolean hasHide) {
            this.mClickBgToHide = hasHide;
            return this;
        }

        public Builder setClicBgToHideListener(OnClickBgToHideListener hideListener) {
            this.mHideListener = hideListener;
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(int content) {
            if (context != null) {
                this.content = context.getResources().getString(content);
            }
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setLeftButton(String text, OnClickListener listener) {
            buttonLeftText = text;
            this.leftlistener = listener;
            return this;
        }

        public Builder setLeftButton(int textId, OnClickListener listener) {
            if (context != null) {
                buttonLeftText = context.getResources().getString(textId);
                this.leftlistener = listener;
            }
            return this;
        }

        public Builder setRightButton(String text, OnClickListener listener) {
            buttonRightText = text;
            this.rightlistener = listener;
            return this;
        }

        public Builder setRightButton(int textId, OnClickListener listener) {
            if (context != null) {
                buttonRightText = context.getResources().getString(textId);
                this.rightlistener = listener;
            }
            return this;
        }

        /**
         * 显示不再提醒
         *
         * @param visibilityType "0" 隐藏 "1"显示
         * @return
         */
        public Builder isShowTips(String visibilityType) {
            tipsVisibilityType = visibilityType;
            return this;
        }

        public Builder setOnTipsCheckedChangeListener(OnTipsCheckedChangeListener onTipsCheckedChangeListener) {
            this.onTipsCheckedChangeListener = onTipsCheckedChangeListener;
            return this;
        }

        public  String getTipsVisibilityType(){
            if (checkbox !=null){
               return checkbox.getCheckBoxstate();
            }

            return "-1";
        }

        public BaseDialog create() {
            if (context != null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.dialog_base_layout, null);
                final TextView tvTitleName = (TextView) layout.findViewById(R.id.tv_title_name);
                TextView tvContent = (TextView) layout.findViewById(R.id.tv_content);
                TextView tvLeft = (TextView) layout.findViewById(R.id.tv_left);
                TextView tvRight = (TextView) layout.findViewById(R.id.tv_right);
                View vDivider = layout.findViewById(R.id.v_divider);
                checkbox = layout.findViewById(R.id.cgb_checkbox);
                LinearLayout checkboxLn = layout.findViewById(R.id.ln_tip);
                //设置 <不再提示>
                if (!TextUtils.isEmpty(tipsVisibilityType) && tipsVisibilityType.equals("1")) {
                    checkboxLn.setVisibility(View.VISIBLE);
                    checkbox.setOncheckedListener(new CGBCheckbox.OncheckedListener() {
                        @Override
                        public void isChecked(boolean isChecked) {
                            if (onTipsCheckedChangeListener != null) {
                                onTipsCheckedChangeListener.onCheckedChanged(isChecked);
                            }

                        }
                    });
                } else {
                    checkboxLn.setVisibility(View.GONE);
                }


                //判断是否需要标题
                if (TextUtils.isEmpty(title)) {
                    tvTitleName.setVisibility(View.GONE);
                } else {
                    tvTitleName.setVisibility(View.VISIBLE);
                    tvTitleName.setText(title);
                }


                // 判断是否要内容
                if (TextUtils.isEmpty(content)) {
                    tvContent.setVisibility(View.GONE);
                } else {
                    tvContent.setVisibility(View.VISIBLE);
                    tvContent.setText(content);
                }

                // 判断是否有分割线
                if (!TextUtils.isEmpty(buttonLeftText) && !TextUtils.isEmpty(buttonRightText)) {
                    vDivider.setVisibility(View.VISIBLE);
                } else {
                    vDivider.setVisibility(View.GONE);
                }

                if (TextUtils.isEmpty(buttonLeftText)) {
                    tvLeft.setVisibility(View.GONE);
                } else {
                    tvLeft.setVisibility(View.VISIBLE);
                    tvLeft.setText(buttonLeftText);
                    tvLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (leftlistener != null) {
                                dialog.mDismissByClicBg = false;
                                leftlistener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                            }
                        }
                    });
                }

                if (TextUtils.isEmpty(buttonRightText)) {
                    tvRight.setVisibility(View.GONE);
                } else {
                    tvRight.setVisibility(View.VISIBLE);
                    tvRight.setText(buttonRightText);
                    tvRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (rightlistener != null) {
                                dialog.mDismissByClicBg = false;
                                rightlistener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                            }
                        }
                    });
                }
                dialog = new BaseDialog(context, R.style.base_dialog);
                Display display = dialog.getWindow().getWindowManager().getDefaultDisplay();
                int screenWidth = display.getWidth();
                final int screenHeight = (int) (display.getHeight() * 0.75f);
                int width = screenWidth - DensityUtil.dp2px( 105);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(mClickBgToHide);
                if (screenHeight < screenWidth) {
                    dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                } else {
                    dialog.addContentView(layout, new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                dialog.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (dialog.mDismissByClicBg) {
                            if (mHideListener != null) {
                                mHideListener.onClickToHide();
                            }
                        }
                    }
                });
            }
            return dialog;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        View mView = getWindow().getDecorView().findViewById(R.id.ll_root);
        if (mView != null) {
            int location[] = new int[2];
            mView.getLocationOnScreen(location);
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (y < location[1] || y > location[1] + mView.getHeight() || x < location[0] || x > location[0] + mView.getWidth()) {
                mDismissByClicBg = true;
            } else {
                mDismissByClicBg = false;
            }
        }
        return super.onTouchEvent(event);
    }


    public interface OnClickBgToHideListener {
        void onClickToHide();
    }

    @Override
    public void show() {
        BaseDialog.super.show();

    }

    @Override
    public void dismiss() {
        BaseDialog.super.dismiss();

    }

    public interface OnTipsCheckedChangeListener {
        void onCheckedChanged(boolean isChecked);

    }
}
