package com.torch.androidutil.android.configuration;

import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.torch.androidutil.interfaces.EditTextConfiguration;
import com.torch.androidutil.interfaces.OnObjectSelectedListener;

import java.util.List;

public class ConfirmationDialogEditTextConfiguration<T> implements EditTextConfiguration {
    private List<T> items;
    private int selectedItemPosition = -1;
    private OnObjectSelectedListener<T> listener;
    private CharSequence[] charItems;

    public ConfirmationDialogEditTextConfiguration(List<T> items) {
        setObjects(items);
    }

    @Override
    public void configure(EditText editText) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(editText.getContext());
        builder.setSingleChoiceItems(charItems, selectedItemPosition, (dialog, which) -> this.selectedItemPosition = which);
        builder.setPositiveButton("Ok", (dialog, which) -> {
            T selectedItem = selectedItemPosition == -1 ? null : items.get(selectedItemPosition);
            editText.setText(selectedItem != null ? selectedItem.toString() : null);
            notifyUpdate(selectedItem);
        });
        builder.setNegativeButton("Cancel", null);
        editText.setOnClickListener(v -> builder.show());
    }

    public void setObjects(List<T> items) {
        this.items = items;
        this.charItems = items.stream().map(Object::toString).toArray(CharSequence[]::new);
    }

    private void notifyUpdate(T o) {
        if (listener != null) {
            listener.onSelect(o);
        }
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
    }

    public void setListener(OnObjectSelectedListener<T> listener) {
        this.listener = listener;
    }

}
