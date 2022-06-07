package com.torch.androidutil.android.adapters;

import android.widget.Filter;

public class NoFilter extends Filter {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        return null;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

    }
}
