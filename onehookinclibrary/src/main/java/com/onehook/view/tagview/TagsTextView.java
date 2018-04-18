package com.onehook.view.tagview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.List;

/**
 * Created by eaglediaotomore on 2016-07-20.
 */

public class TagsTextView extends TextView {

    public TagsTextView(Context context) {
        super(context);
        commonInit(null);
    }

    public TagsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit(attrs);
    }

    public TagsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(attrs);
    }

    @TargetApi(21)
    public TagsTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit(attrs);
    }

    private int mTagBackgroundColor;

    private int mTextPadding;

    private int mCornerRadius;

    private void commonInit(final AttributeSet attributeSet) {
        mTagBackgroundColor = Color.RED;
        mTextPadding = (int) convertDpToPx(getContext(), 2);
        mCornerRadius = (int) convertDpToPx(getContext(), 4);
        if (attributeSet != null) {

        }
    }

    public void setTagBackgroundColor(@ColorInt int tagBackgroundColor) {
        mTagBackgroundColor = tagBackgroundColor;
    }

    public void setTagBackgroundColorRes(@ColorRes int colorRes) {
        mTagBackgroundColor = ContextCompat.getColor(getContext(), colorRes);
    }

    public void setTags(final String[] tokens) {
        final RoundedCornersBackgroundSpan.TextPartsBuilder builder = new RoundedCornersBackgroundSpan.TextPartsBuilder(getContext())
                .setTextPadding(mTextPadding)
                .setCornersRadius(mCornerRadius)
                .setSeparator("  ");

        for (int i = 0; i < tokens.length; i++) {
            builder.addTextPart(tokens[i], mTagBackgroundColor);
        }
        setText(builder.build());
    }

    public void setTags(final List<String> tokens) {
        final RoundedCornersBackgroundSpan.TextPartsBuilder builder = new RoundedCornersBackgroundSpan.TextPartsBuilder(getContext())
                .setTextPadding(mTextPadding)
                .setCornersRadius(mCornerRadius)
                .setSeparator("  ");

        for (int i = 0; i < tokens.size(); i++) {
            builder.addTextPart(tokens.get(i), mTagBackgroundColor);
        }
        setText(builder.build());
    }

    private static float convertDpToPx(Context context, float dp) {
        return context.getResources().getDisplayMetrics().density * dp;
    }
}
