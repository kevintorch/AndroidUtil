package com.torch.androidutil.android;

import android.content.Context;
import android.util.TypedValue;

import androidx.annotation.AnyRes;
import androidx.annotation.AttrRes;
import androidx.core.content.res.ResourcesCompat;

public class ResourcesUtil {

    @AnyRes
    public static int getResource(Context context, @AttrRes int resId) {
        TypedValue type = resolveAttr(context, resId);
        return type != null ? type.resourceId : ResourcesCompat.ID_NULL;
    }

    public static int getInteger(Context context, @AttrRes int resId, int defaultValue) {
        TypedValue typedValue = resolveAttr(context, resId);
        return typedValue != null && typedValue.type == TypedValue.TYPE_INT_DEC ?
                typedValue.data : defaultValue;
    }

    public static float getDimension(Context context, @AttrRes int attr, float defaultValue) {
        TypedValue typedValue = resolveAttr(context, attr);
        return typedValue != null && typedValue.type == TypedValue.TYPE_DIMENSION ?
                typedValue.getDimension(context.getResources().getDisplayMetrics()) : defaultValue;
    }

    public static TypedValue resolveAttr(Context context, @AttrRes int resId) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(resId, typedValue, true)) {
            return typedValue;
        }
        return null;
    }
}
