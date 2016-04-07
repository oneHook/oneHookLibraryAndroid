package com.onehook.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

/**
 * Created by EagleDiao on 2016-02-16.
 */
public class TextDrawable extends Drawable {

    private final String mText;

    private final Paint mPaint;

    /**
     * Rect for measuring text( so we can center the text).
     */
    private static Rect sTextRect = new Rect();

    /**
     * Matrix for calculation.
     */
    private static Matrix sMatrix = new Matrix();

    private static float[] sValues = new float[9];

    public TextDrawable(String text, final Typeface typeFace) {
        mText = text;
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTypeface(typeFace);
        mPaint.setAntiAlias(true);
    }

    public TextDrawable(String text, final Typeface typeFace, final int color) {
        mText = text;
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setTypeface(typeFace);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.getMatrix(sMatrix);
        sMatrix.getValues(sValues);
        mPaint.setTextSize(canvas.getWidth() / 2);
        mPaint.getTextBounds(mText, 0, mText.length(), sTextRect);
        canvas.translate(-sValues[2] - sTextRect.left, -sValues[2] - sTextRect.bottom);
        canvas.drawText(mText,
                canvas.getWidth() / 2 - sTextRect.width() / 2,
                canvas.getHeight() / 2 + sTextRect.height() / 2,
                mPaint);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}