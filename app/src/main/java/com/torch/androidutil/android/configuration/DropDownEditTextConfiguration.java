package com.torch.androidutil.android.configuration;

import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.torch.androidutil.android.adapters.NoFilterArrayAdapter;
import com.torch.androidutil.interfaces.EditTextConfiguration;

import java.util.ArrayList;
import java.util.List;

public class DropDownEditTextConfiguration<T> implements EditTextConfiguration {
    private AdapterView.OnItemClickListener itemClickListener;
    private List<T> objects;

    public DropDownEditTextConfiguration() {
        this(new ArrayList<>(), null);
    }

    public DropDownEditTextConfiguration(List<T> objects, AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        this.objects = objects;
    }

    public DropDownEditTextConfiguration(List<T> objects) {
        this(objects, null);
    }

    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setObjects(List<T> objects) {
        this.objects = objects;
    }

    @Override
    public void configure(EditText editText) {
        if (editText instanceof AutoCompleteTextView) {
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) editText;
            if (itemClickListener != null)
                autoCompleteTextView.setOnItemClickListener(itemClickListener);
            autoCompleteTextView.setAdapter(new NoFilterArrayAdapter<>(autoCompleteTextView.getContext(),
                                                                       android.R.layout.simple_spinner_dropdown_item, objects));
            autoCompleteTextView.setOnClickListener(v -> ((AutoCompleteTextView) v).showDropDown());
        }
    }
}
