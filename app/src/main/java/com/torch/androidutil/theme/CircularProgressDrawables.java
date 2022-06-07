package com.torch.androidutil.theme;

import android.content.Context;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;


public final class CircularProgressDrawables {

    public static final float CENTER_RADIUS_MEDIUM = DensityUtils.dpToPx(8);
    public static final float STROKE_WIDTH_MEDIUM = DensityUtils.dpToPx(2);

    public static CircularProgressDrawable ofTextInputLayout(Context context) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setCenterRadius(CENTER_RADIUS_MEDIUM);
        circularProgressDrawable.setStrokeWidth(STROKE_WIDTH_MEDIUM);
        return circularProgressDrawable;
    }
}
