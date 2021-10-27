package com.swipecrafts.school.utils.renderer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
/**
 * Created by Madhusudan Sapkota on 3/22/2018.
 */

public abstract class BaseViewRenderer<M extends BaseModel, VH extends RecyclerView.ViewHolder> {

    private int mType;
    private ClickListener<M> mClickListener;

    public BaseViewRenderer(int mType, ClickListener<M> listener) {
        this.mType = mType;
        this.mClickListener = listener;
    }

    public abstract void bindView(@NonNull M model, @NonNull VH holder);

    @NonNull
    public abstract VH createViewHolder(@Nullable ViewGroup parent);

    @NonNull
    public int getType() {
        return mType;
    }

    public ClickListener<M> getClickListener() {
        return mClickListener;
    }
}
