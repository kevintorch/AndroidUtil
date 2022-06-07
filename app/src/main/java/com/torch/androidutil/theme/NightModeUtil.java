package com.torch.androidutil.theme;

import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

public class NightModeUtil {
    public static final int PLATFORM_DEFAULT_NIGHT_MODE;

    static {
        int defMode = AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            defMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
        PLATFORM_DEFAULT_NIGHT_MODE = defMode;
    }
}
