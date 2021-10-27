package com.swipecrafts.library.sectionrecycler;

import android.support.v7.widget.GridLayoutManager;

/**
 * Created by Madhusudan Sapkota on 6/17/2018.
 */
public class SectionedSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    protected final SectionRecyclerAdapter<?, ?, ?> _adapter;
    protected final GridLayoutManager _layoutManager;

    public SectionedSpanSizeLookup(SectionRecyclerAdapter<?, ?, ?> adapter, GridLayoutManager layoutManager) {
        this._adapter = adapter;
        this._layoutManager = layoutManager;
    }

    @Override
    public int getSpanSize(int position) {
        if (_adapter.isSectionHeaderPosition(position) || _adapter.isSectionFooterPosition(position)) {
            return _layoutManager.getSpanCount();
        } else {
            return 1;
        }

    }
}
