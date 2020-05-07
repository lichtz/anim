package cn.licht.mobile.anim.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * Created by jinpingchen on 2018/7/20.
 */

public class MaxEmsTextView extends TextView {

    private String targetText;
    private int maxEmsLength = 0;

    public MaxEmsTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaxEmsTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MaxEmsTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (getText() != null) {
            targetText = getText().toString();
        }
    }

    public void setMaxEmsLength(int length) {
        this.maxEmsLength = length;
    }

    public void setTargetText(String text) {
        this.targetText = text;
        if (text.length() > maxEmsLength) {
            String tagText = text.substring(0, maxEmsLength);
            setText(tagText + "...");
        } else {
            setText(text);
        }
    }

    public String getTargetText() {
        return targetText;
    }

}
