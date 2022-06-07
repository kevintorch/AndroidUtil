package com.torch.androidutil.android;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.core.graphics.drawable.DrawableCompat;

import com.torch.androidutil.java.iterator.ArrayIterator;

import java.util.Collection;
import java.util.Iterator;

public final class ViewUtility {

    public static void setViewVisible(View v, boolean when) {
        v.setVisibility(when ? View.VISIBLE : View.GONE);
    }

    public static void hideViews(View... views) {
        updateViewsVisibility(new ArrayIterator<>(views), View.GONE);
    }

    public static void showViews(View... views) {
        updateViewsVisibility(new ArrayIterator<>(views), View.VISIBLE);
    }

    public static void showViews(Collection<View> views) {
        updateViewsVisibility(views.iterator(), View.VISIBLE);
    }

    public static void hideViews(Collection<View> views) {
        updateViewsVisibility(views.iterator(), View.GONE);
    }

    public static void tintViewBackground(View view, @ColorInt int colorTint) {
        if (view == null || view.getBackground() == null) return;
        Drawable background = tintDrawable(view.getBackground(), colorTint);
        view.setBackground(background);
    }

    public static Drawable tintDrawable(Drawable drawable, int tintColor) {
        Drawable drawable1 = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable1, tintColor);
        return drawable1;
    }

    private static void updateViewsVisibility(Iterator<View> iterator, int visibility) {
        while (iterator.hasNext()) {
            iterator.next().setVisibility(visibility);
        }
    }
}
