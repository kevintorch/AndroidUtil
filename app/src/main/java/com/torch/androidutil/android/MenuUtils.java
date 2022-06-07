package com.torch.androidutil.android;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.ColorInt;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.torch.androidutil.theme.DensityUtils;


public class MenuUtils {

    public static void tintMenuItemsIcon(Menu menu, @ColorInt int color) {
        for (int i = 0; i < menu.size(); i++) {
            tintMenuItemIcon(menu.getItem(i), color);
        }
    }

    private static void tintMenuItemIcon(MenuItem menuItem, @ColorInt int color) {
        if (menuItem == null || menuItem.getIcon() == null) return;

        Drawable icon = DrawableCompat.wrap(menuItem.getIcon());
        DrawableCompat.setTint(icon, color);
        menuItem.setIcon(icon);
    }

    public static void animateMenuItemIcon(MenuItem menuItem) {
        if (menuItem == null || menuItem.getIcon() == null) return;

        Drawable icon = menuItem.getIcon();

        if (icon instanceof Animatable) {
            ((Animatable) icon).start();
        }
    }

    public static CircularProgressDrawable createMenuItemProgressDrawable(Context context) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(4f);
        int SIZE = (int) DensityUtils.dpToPx(20);
        circularProgressDrawable.setBounds(0, 0, SIZE, SIZE);
        return circularProgressDrawable;
    }

    public static void setItemsCheckedExcept(Menu menu, boolean checked, int menuId) {
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() != menuId)
                item.setChecked(checked);
        }
    }
}
