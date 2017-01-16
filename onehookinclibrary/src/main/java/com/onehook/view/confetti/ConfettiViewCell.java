package com.onehook.view.confetti;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by EagleDiao on 2016-04-05.
 */
public class ConfettiViewCell extends ImageView {

    public int color;

    public ConfettiViewCell(Context context) {
        super(context);
        commonInit();
    }

    public ConfettiViewCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit();
    }

    public ConfettiViewCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit();
    }

    @TargetApi(21)
    public ConfettiViewCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit();
    }

    private void commonInit() {
//        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas){
        final Drawable drawable = getDrawable();
        DrawableCompat.setTint(drawable, getResources().getColor(color));
        setImageDrawable(drawable);
        super.onDraw(canvas);
    }



}
