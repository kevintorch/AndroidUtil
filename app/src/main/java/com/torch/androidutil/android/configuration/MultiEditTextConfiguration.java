package com.torch.androidutil.android.configuration;

import android.widget.EditText;


import com.torch.androidutil.interfaces.EditTextConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MultiEditTextConfiguration implements EditTextConfiguration {
    private final List<EditTextConfiguration> configurations = new ArrayList<>();


    public void addConfiguration(EditTextConfiguration configuration) {
        configurations.add(configuration);
    }

    @Override
    public void configure(EditText editText) {
        for (EditTextConfiguration config : configurations) {
            config.configure(editText);
        }
    }
}
