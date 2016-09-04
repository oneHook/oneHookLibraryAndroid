package com.onehook.app.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onehookinc.androidlib.R;

/**
 * Created by eaglediaotomore on 2016-09-03.
 */

public class IconTitleListAdapter extends BaseAdapter {

    private static class ViewHolder {

        private ImageView mIconView;

        private TextView mTitleView;

        private View itemView;

        public ViewHolder(final ViewGroup parent) {
            this.itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_icon_title, parent, false);
            this.itemView.setTag(this);
            mIconView = (ImageView) this.itemView.findViewById(R.id.item_row_icon_title_icon_image_view);
            mTitleView = (TextView) this.itemView.findViewById(R.id.item_row_icon_title_title_text_view);
        }

        public void bind(final int drawableRes, final int titleRes) {
            mIconView.setImageResource(drawableRes);
            mTitleView.setText(titleRes);
        }

    }

    private int[] mDrawablesRes;

    private int[] mTitlesRes;

    public IconTitleListAdapter(final int[] drawables, final int[] titles) {
        mDrawablesRes = drawables;
        mTitlesRes = titles;
        if (drawables.length != titles.length) {
            throw new RuntimeException("Two array must be equal length");
        }
    }

    @Override
    public int getCount() {
        return mTitlesRes.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder(viewGroup);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.bind(mDrawablesRes[i], mTitlesRes[i]);
        return holder.itemView;
    }
}
