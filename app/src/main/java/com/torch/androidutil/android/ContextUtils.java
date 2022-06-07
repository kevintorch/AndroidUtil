package com.torch.androidutil.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public final class ContextUtils {

    public static int getThemeAttrColor(Context context, int color) {
        TypedArray a = context.obtainStyledAttributes(new int[]{color});
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }

    @SuppressLint("Recycle")
    public static Interpolator getThemeInterpolator(Context context, int inter) {
        int defaultInterpolator = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            defaultInterpolator = android.R.interpolator.fast_out_slow_in;
        }
        return AnimationUtils.loadInterpolator(context, context
                .obtainStyledAttributes(new int[]{inter}).getResourceId(0, defaultInterpolator));
    }
}
