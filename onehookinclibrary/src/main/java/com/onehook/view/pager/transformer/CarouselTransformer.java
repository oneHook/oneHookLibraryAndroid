package com.onehook.view.pager.transformer;

/**
 * Copyright (C) 2015 Kaelaela
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import android.support.v4.view.ViewPager;
import android.view.View;

public class CarouselTransformer implements ViewPager.PageTransformer {

    private float mOffset;

    public CarouselTransformer(final float offset) {
        mOffset = offset;
    }

    @Override
    public void transformPage(View view, float position) {
        position -= mOffset;
        view.setPivotY(0.5f * view.getMeasuredHeight());
        if (position < -0.5) {
            view.setPivotX(1 * view.getMeasuredWidth());
        } else if (position > 0.5) {
            view.setPivotX(0);
        } else {
            view.setPivotX(0.5f * view.getMeasuredWidth());
        }


        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }
        final float scale = 1 - Math.abs(position) * 0.2f;

        view.setScaleX(scale);
        view.setScaleY(scale);
    }
}
