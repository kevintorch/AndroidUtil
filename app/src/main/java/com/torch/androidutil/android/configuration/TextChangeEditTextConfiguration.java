package com.torch.androidutil.android.configuration;

import android.text.TextWatcher;
import android.widget.EditText;

import com.torch.androidutil.interfaces.EditTextConfiguration;

public class TextChangeEditTextConfiguration implements EditTextConfiguration {
    private final TextWatcher textChangedListener;

    public TextChangeEditTextConfiguration(TextWatcher textChangedListener) {
        this.textChangedListener = textChangedListener;
    }

    @Override
    public void configure(EditText editText) {
        editText.addTextChangedListener(textChangedListener);
    }
}
