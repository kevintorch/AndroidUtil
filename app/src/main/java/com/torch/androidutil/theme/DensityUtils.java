package com.torch.androidutil.theme;

import android.content.res.Resources;
import android.util.TypedValue;

public class DensityUtils {

    public static float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static float pxToDp(float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, Resources.getSystem().getDisplayMetrics());
    }

    public static float txtPxToSp(float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, px, Resources.getSystem().getDisplayMetrics());
    }

}
