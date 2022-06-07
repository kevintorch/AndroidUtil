package com.torch.androidutil.theme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.ThemeUtils;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;


public final class Colors {

    private Colors() {
    }

    @ColorInt
    public static int getColorAttr(View view, @AttrRes int color) {
        return getColorAttr(view.getContext(), color);
    }

    @SuppressLint("RestrictedApi")
    @ColorInt
    public static int getColorAttr(Context context, @AttrRes int color) {
        return ThemeUtils.getThemeAttrColor(context, color);
    }

    @ColorInt
    public static int getColor(View view, @ColorRes int color) {
        return getColor(view.getContext(), color);
    }

    @ColorInt
    public static int getColor(Context context, @ColorRes int color) {
        return ContextCompat.getColor(context, color);
    }

    public static ColorStateList getColorStateList(@NonNull Context context, @ColorRes int id) {
        return ResourcesCompat.getColorStateList(context.getResources(), id, context.getTheme());
    }

    /**
     * Converts a @ColorRes array to @ColorInt array.
     *
     * @param context context on which to get the color
     * @param colors  an array of @ColorRes int array.
     * @return an array of @ColorInt integers.
     */
    @ColorInt
    public static int[] toColorIntArray(Context context, @ColorRes int[] colors) {
        @ColorInt int[] colorInts = new int[colors.length];
        for (int i = 0; i < colors.length; i++) {
            colorInts[i] = getColor(context, colors[i]);
        }
        return colorInts;
    }
}
