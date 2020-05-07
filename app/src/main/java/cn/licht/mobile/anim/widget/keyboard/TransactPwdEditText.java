package cn.licht.mobile.anim.widget.keyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.EditText;

import com.scwang.smartrefresh.layout.util.DensityUtil;

import cn.licht.mobile.anim.R;

/**
 * 交易密码框
 */
public class TransactPwdEditText extends EditText {

    /**
     * 背景颜色
     */
    private int backgroundColor;

    /**
     * 密码长度
     */
    private int passwordTextLength = 6;

    /**
     * 密码文字颜色
     */
    private int passwordTextColor;

    /**
     * 密码框颜色
     */
    private int passwordBoxColor;

    /**
     * 背景的画笔
     */
    private Paint backgroundPaint;

    /**
     * 密码的画笔
     */
    private TextPaint passwordTextPaint;

    /**
     * 框的画笔
     */
    private Paint boxPaint;

    /**
     * 控件宽度
     */
    private int width;

    /**
     * 控件高度
     */
    private int height;

    private int boxStrokeWidth;
    private Drawable drawableBg;

    /**
     * 输入密码的长度
     */
    private int inputPasswordTextLength;

    public TransactPwdEditText(Context context) {
        super(context);

        init(context, null);
    }

    public TransactPwdEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public TransactPwdEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TransactPwdEditText);

        if (null != typedArray) {
            backgroundColor = typedArray.getColor(R.styleable.TransactPwdEditText_tpet_background_color, getResources().getColor(R.color.white));
            passwordTextLength = typedArray.getInt(R.styleable.TransactPwdEditText_tpet_password_text_length, 6);
            passwordTextColor = typedArray.getColor(R.styleable.TransactPwdEditText_tpet_password_text_color, getResources().getColor(R.color.text_color_333333));
            passwordBoxColor = typedArray.getColor(R.styleable.TransactPwdEditText_tpet_password_box_color, getResources().getColor(R.color.text_color_333333));
            typedArray.recycle();
        }

        setTextSize(0);
        setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(passwordTextLength)
        });
        setCursorVisible(false);
        setLongClickable(false);

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setDither(true);
        backgroundPaint.setColor(backgroundColor);

        drawableBg = context.getResources().getDrawable(R.drawable.shape_transact_pwd_edittext_bg);

        passwordTextPaint = new TextPaint();
        passwordTextPaint.setAntiAlias(true);
        passwordTextPaint.setDither(true);
        passwordTextPaint.setColor(passwordTextColor);
        passwordTextPaint.setTextAlign(Paint.Align.CENTER);
        passwordTextPaint.setTextSize(DensityUtil.px2dp( 15));

        boxStrokeWidth = DensityUtil.dp2px( 0.5f);
        boxPaint = new Paint();
        boxPaint.setAntiAlias(true);
        boxPaint.setDither(true);
        boxPaint.setColor(passwordBoxColor);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(boxStrokeWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));

        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBackground(canvas);
        drawPasswordText(canvas);
        drawBox(canvas);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        inputPasswordTextLength = text.toString().trim().length();

        invalidate();
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result;

        if (MeasureSpec.EXACTLY == specMode) {
            result = specSize;
        } else {
            result = getPaddingLeft() + getPaddingRight() + DensityUtil.dp2px( 300);

            if (MeasureSpec.AT_MOST == specMode) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result;

        if (MeasureSpec.EXACTLY == specMode) {
            result = specSize;
        } else {
            result = getPaddingTop() + getPaddingBottom() + DensityUtil.dp2px( 40) + boxStrokeWidth;

            if (MeasureSpec.AT_MOST == specMode) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, backgroundPaint);
    }

    private void drawPasswordText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = passwordTextPaint.getFontMetrics();
        float offsetY = -fontMetrics.ascent * 1 / 6;
        float baseLineY = (-fontMetrics.top + fontMetrics.bottom) / 2 - fontMetrics.bottom + offsetY;

        Rect rect = new Rect();
        passwordTextPaint.getTextBounds("●", 0, 1, rect);

        for (int i = 0; i < inputPasswordTextLength; i++) {
            float x = (i + 0.5f) * width / 6;
            float y = boxStrokeWidth + height / 2 + rect.height()/2;
            canvas.drawText("●", x, y, passwordTextPaint);
        }
    }

    private void drawBox(Canvas canvas) {
        drawableBg.setBounds(0, 0, width, height);
        drawableBg.draw(canvas);

        for (int i = 0; i < passwordTextLength; i++) {
            if (i != passwordTextLength -1) {
                float x = (i + 1) * width / 6;
                float y = height;
                canvas.drawLine(x, 0, x, y, boxPaint);
            }
        }
//        canvas.drawRoundRect(getPaddingLeft(), boxStrokeWidth, getPaddingLeft() + width - boxStrokeWidth, height,
//                DensityUtil.dp2px(getContext(), 4), DensityUtil.dp2px(getContext(), 4), boxPaint);
//        for (int i = 0; i < passwordTextLength; i++) {
//            float left = getPaddingLeft() + boxOffset / 2 + i * (boxWidth + boxOffset);
//            float top = boxStrokeWidth;      /** 若top为0，矩形框上边缘不完整  */
//            float right = left + boxWidth;
//            float bottom = boxHeight + top;
//
//            canvas.drawRect(left, top, right, bottom, boxPaint);
//        }
    }

    public void reset() {
        setText("");

        invalidate();
    }

}
