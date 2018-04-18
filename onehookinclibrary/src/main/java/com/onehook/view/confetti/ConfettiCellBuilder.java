package com.onehook.view.confetti;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

public class ConfettiCellBuilder {

    private Context mContext;

    public ConfettiCellBuilder(final Context context) {
        mContext = context;
    }

    @Nullable
    public View imageCell(@DrawableRes final int drawableRes,
                          @ColorRes final int colorRes) {
        final Drawable d = ContextCompat.getDrawable(mContext, drawableRes);
        if (d == null) {
            return null;
        }

        final ConfettiViewImageCell cell = new ConfettiViewImageCell(mContext);
        cell.colorRes = colorRes;
        cell.setImageDrawable(DrawableCompat.wrap(d));
        return cell;
    }

    @Nullable
    public View emojiCell(@StringRes final int stringRes,
                          @ColorInt final int color) {
        final AppCompatTextView tv = new AppCompatTextView(mContext);
        tv.setText(stringRes);
        tv.setTextColor(color);
        return tv;
    }
}
