package com.onehook.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

import com.onehook.util.StringUtil;
import com.onehookinc.androidlib.R;

/**
 * Created by Eagle Diao on 2016-04-30.
 */
public class ButtonWithBadge extends AppCompatButton {

    public int ALIGNMENT_LEFT = 0;
    public int ALIGNMENT_RIGHT = 1;

    public ButtonWithBadge(Context context) {
        super(context);
        commonInit(context, null);
    }

    public ButtonWithBadge(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    public ButtonWithBadge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(context, attrs);
    }

    private Paint mPaint;
    private Paint mTextPaint;

    private String mText = "";
    private int mBadgeTextColor = Color.WHITE;
    private int mBadgeBackgroundColor = Color.RED;
    private float mBadgeSizeRatio = 0.25f;
    private int mBadgeAlignment = ALIGNMENT_LEFT;

    /**
     * Rect for measuring text( so we can center the text).
     */
    private Rect mTextRect = new Rect();

    private void commonInit(@NonNull final Context context,
                            @Nullable final AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ButtonWithBadge);
            mText = a.getString(R.styleable.ButtonWithBadge_badge_text);
            if (!StringUtil.isEmpty(mText)) {
                mText = mText.toUpperCase();
            }
            mBadgeBackgroundColor = a.getColor(R.styleable.ButtonWithBadge_badge_background_color,
                    mBadgeBackgroundColor);
            mBadgeTextColor = a.getColor(R.styleable.ButtonWithBadge_badge_text_color,
                    mBadgeTextColor);
            mBadgeSizeRatio = a.getFloat(R.styleable.ButtonWithBadge_badge_size_ratio,
                    mBadgeSizeRatio);
            mBadgeAlignment = a.getInt(R.styleable.ButtonWithBadge_badge_alignment,
                    mBadgeAlignment);
            a.recycle();
        }

        setWillNotDraw(false);
        mPaint = new Paint();
        mPaint.setColor(mBadgeBackgroundColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint();
        mTextPaint.setColor(mBadgeTextColor);
        mTextPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mText.length() > 0) {
            final float width = getMeasuredWidth();
            final float height = getMeasuredHeight();
            final float length = Math.min(width, height);
            final float radius = length * mBadgeSizeRatio;

            /* Measure the text size */
            mTextPaint.setTextSize(radius * 0.80f);
            mTextRect.setEmpty();
            mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);

            mPaint.setStrokeWidth(radius);

            if (mBadgeAlignment == ALIGNMENT_LEFT) {
                canvas.drawLine(radius, radius / 2,
                        mTextRect.width() + radius, radius / 2, mPaint);
                canvas.drawText(mText,
                        radius,
                        mTextRect.height() + (radius - mTextRect.height()) / 2,
                        mTextPaint);
            } else {
                canvas.drawLine(width - mTextRect.width() - radius, radius / 2, width - radius, radius / 2, mPaint);
                canvas.drawText(mText,
                        width - mTextRect.width() - radius,
                        mTextRect.height() + (radius - mTextRect.height()) / 2,
                        mTextPaint);
            }
        }
    }

    /**
     * Set the badge text using a number.
     *
     * @param count    count
     * @param collapse if true and count > 99, the text will be 99+
     */
    public void setBadgeCount(final int count, final boolean collapse) {
        if (count <= 0) {
            mText = "";
        } else if (count > 99 && collapse) {
            mText = "99+";
        } else {
            mText = String.valueOf(count);
        }
        invalidate();
    }

    /**
     * Set badge text.
     *
     * @param text text
     */
    public void setBadgeText(final String text) {
        mText = text.toUpperCase();
        invalidate();
    }

    /* public setting */

    public void setBadgeTextColor(int badgeTextColor) {
        mBadgeTextColor = badgeTextColor;
        invalidate();
    }

    public void setBadgeBackgroundColor(int badgeBackgroundColor) {
        mBadgeBackgroundColor = badgeBackgroundColor;
        invalidate();
    }

    public void setBadgeSizeRatio(float badgeSizeRatio) {
        mBadgeSizeRatio = badgeSizeRatio;
        invalidate();
    }
}
