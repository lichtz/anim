package cn.licht.mobile.anim.widget;

import android.content.Context;

import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.licht.mobile.anim.R;


/**
 * @author licht
 * @version v 0.1 2018/12/10 Mon 10:56
 */
public class CGBCheckbox  extends FrameLayout{

    private OncheckedListener oncheckedListener;
    /**
     * -1 无状态 0 未选择 1 选中
     */
    private  String state = "-1";
    public CGBCheckbox(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CGBCheckbox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CGBCheckbox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private  void  init(final Context context){
        state = "-1";
        final ImageView imageView = new ImageView(context);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
         imageView.setLayoutParams(layoutParams);
         imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(R.drawable.checkbox_selected_false);
         imageView.setTag("0");
         imageView.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (imageView.getTag() !=null && "0".equals(imageView.getTag())) {
                     imageView.setTag("1");
                     state ="1";
                     imageView.setImageResource(R.drawable.checkbox_selected_true);
                     if (oncheckedListener != null){
                         oncheckedListener.isChecked(true);
                     }
                 }else {
                     imageView.setImageResource(R.drawable.checkbox_selected_false);
                     imageView.setTag("0");
                     state ="0";
                     if (oncheckedListener != null){
                         oncheckedListener.isChecked(false);
                     }
                 }
             }
         });

        addView(imageView);
    }

    public void setOncheckedListener(OncheckedListener oncheckedListener) {
        this.oncheckedListener = oncheckedListener;
    }
    public  String getCheckBoxstate(){
        return state;
    }

    public  interface  OncheckedListener{
        void isChecked(boolean isChecked);
    }

}
