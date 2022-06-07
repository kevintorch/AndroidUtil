package com.torch.androidutil.android.recyclerView;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * View types for a sticky header are not supported.
 */
public interface StickyHeaders {

    /**
     * This method gets called by {@link StickyHeaderItemDecorator} to fetch
     * the position of the header item in the adapter that is used for
     * (represents) item at specified position.
     *
     * @param itemPosition int. Adapter's position of the item for which to do
     *                     the search of the position of the header item.
     * @return int. Position of the header for an item in the adapter or
     * {@link RecyclerView#NO_POSITION} (-1) if an item has no header.
     */
    int getHeaderPositionForItem(int itemPosition);

    /**
     * This method gets called by {@link StickyHeaderItemDecorator} to setup the header View.
     *
     * @param holder         RecyclerView.ViewHolder. Holder to bind the data on.
     * @param headerPosition int. Position of the header item in the adapter.
     */
    void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int headerPosition);

    /**
     * Called only twice when {@link StickyHeaderItemDecorator} needs
     * a new {@link RecyclerView.ViewHolder} to represent a sticky header item.
     * Those two instances will be cached and used to represent a current top sticky header
     * and the moving one.
     * <p>
     * You can either create a new View manually or inflate it from an XML layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindHeaderViewHolder(RecyclerView.ViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent The ViewGroup to resolve a layout params.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #onBindHeaderViewHolder(RecyclerView.ViewHolder, int)
     */
    RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent);
}