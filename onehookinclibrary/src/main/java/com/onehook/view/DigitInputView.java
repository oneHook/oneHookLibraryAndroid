package com.onehook.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

public class DigitInputView extends StackLayout implements View.OnClickListener {

    public DigitInputView(@NonNull Context context) {
        super(context);
        commonInit(context, null);
    }

    public DigitInputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    public DigitInputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(context, attrs);
    }

    @TargetApi(21)
    public DigitInputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit(context, attrs);
    }

    InputMethodManager inputMethodManager;
    Paint paint;

    private void commonInit(@NonNull final Context context, @Nullable AttributeSet attrs) {
        // 设置可以接受到焦点
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        // 获取 InputMethodManager
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
        paint.setTextSize(textSize);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 弹出、关闭输入法
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                System.out.println("oneHook " + keyEvent.getKeyCode());
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {
        requestFocus();

    }

    // Here is where the magic happens
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;
        outAttrs.imeOptions = EditorInfo.IME_ACTION_GO;
        return null;
    }

}
