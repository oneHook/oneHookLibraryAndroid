package com.onehook.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

/**
 * Created by EagleDiao on 2016-04-30.
 */
public class ButtonWithBadge extends Button {

    public ButtonWithBadge(Context context) {
        super(context);
        commonInit();
    }

    public ButtonWithBadge(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit();
    }

    public ButtonWithBadge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit();
    }

    @TargetApi(21)
    public ButtonWithBadge(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit();
    }

    private Paint mPaint;

    private Paint mTextPaint;

    private String mText;

    /**
     * Rect for measuring text( so we can center the text).
     */
    private static Rect sTextRect = new Rect();

    private void commonInit() {
        setWillNotDraw(false);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);

        mText = "";
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mText.length() > 0) {
            final float width = getMeasuredWidth();
            final float height = getMeasuredHeight();
            final float length = Math.min(width, height);
            final float radius = length * 0.3f;

            Log.d("OptimityBadge", "should draw badge " + mText + " width " + width + " , height " + height);
            canvas.drawCircle(width - radius, radius, radius, mPaint);

            mTextPaint.setTextSize(radius);
            mTextPaint.getTextBounds(mText, 0, mText.length(), sTextRect);
            canvas.drawText(mText,
                    width - radius - sTextRect.width() / 2,
                    radius + sTextRect.height() / 2,
                    mTextPaint);
        }
    }

    public void setBadgeCount(final int count) {
        if (count <= 0) {
            mText = "";
        } else if (count > 99) {
            mText = "99+";
        } else {
            mText = String.valueOf(count);
        }
        invalidate();
    }
}
