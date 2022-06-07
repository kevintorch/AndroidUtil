package com.torch.androidutil.interfaces;

import android.view.View;

public interface ViewConfiguration<T extends View> {
    void configure(T view);
}
