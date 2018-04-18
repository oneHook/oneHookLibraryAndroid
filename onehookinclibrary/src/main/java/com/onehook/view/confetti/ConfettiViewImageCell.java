package com.onehook.view.confetti;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by Eagle Diao.
 */
public class ConfettiViewImageCell extends AppCompatImageView {

    public int colorRes;

    public ConfettiViewImageCell(Context context) {
        super(context);
        commonInit();
    }

    public ConfettiViewImageCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit();
    }

    public ConfettiViewImageCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit();
    }

    private void commonInit() {

    }

    @Override
    protected void onDraw(Canvas canvas){
        final Drawable drawable = getDrawable();
        DrawableCompat.setTint(drawable, ContextCompat.getColor(getContext(), colorRes));
        setImageDrawable(drawable);
        super.onDraw(canvas);
    }
}
