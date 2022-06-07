package com.torch.androidutil.theme;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.view.ActionProvider;
import androidx.core.view.MenuItemCompat;

import com.google.android.material.button.MaterialButton;

public class MaterialButtonActionProvider extends ActionProvider {

    private static final int DEFAULT_PADDING = Math.round(DensityUtils.dpToPx(12));
    private final MaterialButton materialButton;
    private View.OnClickListener onButtonClickListener;

    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     * @param materialButton
     */
    public MaterialButtonActionProvider(Context context,
                                        MaterialButton materialButton,
                                        View.OnClickListener onClickListener) {
        super(context);
        this.materialButton = materialButton;
        this.onButtonClickListener = onClickListener;
        this.materialButton.setPadding(DEFAULT_PADDING, 0, DEFAULT_PADDING, 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.materialButton.setLayoutParams(layoutParams);
    }

    public MenuItem attachTo(MenuItem menuItem) {
        return MenuItemCompat.setActionProvider(menuItem, this);
    }

    public void setOnButtonClickListener(View.OnClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    @Override
    public View onCreateActionView() {
        prepareButton();
        return materialButton;
    }

    private void prepareButton() {
        materialButton.setOnClickListener(onButtonClickListener);
    }

    public void setButtonText(CharSequence charSequence) {
        if (materialButton != null) {
            materialButton.setText(charSequence);
        }
    }
}
