package com.torch.androidutil.android;

import android.graphics.drawable.Drawable;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.android.material.textfield.TextInputLayout;
import com.torch.androidutil.interfaces.OnSuccessListener;
import com.torch.androidutil.theme.CircularProgressDrawables;

public class TextInputLayoutHelper {

    public TextInputLayoutHelper() {
    }

    public void asyncLoadingIn(TextInputLayout textInputLayout, AsyncWorkListener asyncWorkListener) {
        Drawable prevDrawable = textInputLayout.getEndIconDrawable();
        setLoadingAppearance(textInputLayout);
        asyncWorkListener.onAsyncWork(completed -> {
            if (completed) {
                setTextInputStateIcon(textInputLayout,
                        prevDrawable,
                        true);
            }
        });
    }

    public void setLoadingAppearance(TextInputLayout textInputLayout) {
        CircularProgressDrawable circularProgressDrawable =
                CircularProgressDrawables.ofTextInputLayout(textInputLayout.getContext());
        setTextInputStateIcon(textInputLayout,
                circularProgressDrawable,
                false);
        circularProgressDrawable.start();
    }

    private void setTextInputStateIcon(TextInputLayout textInputLayout,
                                       Drawable circularProgressDrawable, boolean enabled) {
        textInputLayout.setEndIconDrawable(circularProgressDrawable);
        textInputLayout.setEnabled(enabled);
    }

    @FunctionalInterface
    public interface AsyncWorkListener {
        void onAsyncWork(OnSuccessListener<Boolean> finishWork);
    }
}
