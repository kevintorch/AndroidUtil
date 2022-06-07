package com.torch.androidutil.android.repository;

import android.os.Handler;
import android.os.Looper;

public interface FakeRepository {
    int SERVICE_DELAY = 1500;

    default void afterFakeCall(Runnable runnable, int delay) {
        new Handler(Looper.getMainLooper()).postDelayed(runnable, delay);
    }

    default void afterFakeCall(Runnable runnable) {
        afterFakeCall(runnable, SERVICE_DELAY);
    }
}
