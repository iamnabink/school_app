package com.swipecrafts.library.sectionrecycler;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swipecrafts.school.R;

/**
 * Created by Madhusudan Sapkota on 7/5/2018.
 */
public abstract class SectionedAdapter <VH extends RecyclerView.ViewHolder> extends SectionRecyclerAdapter<HeaderViewHolder,
        VH, RecyclerView.ViewHolder>{

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(getLayoutResource(), parent, false);
        HeaderViewHolder holder = new HeaderViewHolder(view, getTitleTextID());

        return holder;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, int section) {
        String title = getSectionHeaderTitle(section);
        holder.setHeader(title);
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {}

    /**
     * Provides a layout identifier for the header. Override it to change the appearance of the
     * header view.
     */
    protected @LayoutRes int getLayoutResource(){
        return R.layout.sectioned_header;
    }

    /**
     * Provides the identifier of the TextView to render the section header title. Override it if
     * you provide a custom layout for a header.
     */
    protected @IdRes int getTitleTextID(){
        return R.id.title_text;
    }

    /**
     * Returns the title for a given section
     */
    protected abstract String getSectionHeaderTitle(int section);
}