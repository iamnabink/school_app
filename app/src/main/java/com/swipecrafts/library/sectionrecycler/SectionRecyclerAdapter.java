package com.swipecrafts.library.sectionrecycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Madhusudan Sapkota on 6/17/2018.
 */
public abstract class SectionRecyclerAdapter<H extends HeaderViewHolder, C extends RecyclerView.ViewHolder, F extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int SECTION_TYPE_HEADER = -1;
    protected static final int SECTION_TYPE_FOOTER = -2;
    protected static final int SECTION_TYPE_CONTENT = -3;

    int _count = 0;
    private int[] _sectionForPosition = null;
    private int[] _positionWithinSection = null;
    private boolean[] _isHeader = null;
    private boolean[] _isFooter = null;

    public SectionRecyclerAdapter() {
        super();
        registerAdapterDataObserver(new SectionRecyclerObserver(this));
    }

    private void setupIndices() {
        _count = countItems();
        allocateAuxiliaryArrays(_count);
        precomputeIndices();
    }

    private int countItems() {
        int count = 0;
        int sections = getSectionCount();

        for (int i = 0; i < sections; i++) {
            count += 1 + getItemCountForSection(i) + (hasFooterInSection(i) ? 1 : 0);
        }
        return count;
    }

    private void precomputeIndices() {
        int sections = getSectionCount();
        int index = 0;

        for (int i = 0; i < sections; i++) {
            setPrecomputedItem(index, true, false, i, 0);
            index++;

            for (int j = 0; j < getItemCountForSection(i); j++) {
                setPrecomputedItem(index, false, false, i, j);
                index++;
            }

            if (hasFooterInSection(i)) {
                setPrecomputedItem(index, false, true, i, 0);
                index++;
            }
        }
    }

    private void allocateAuxiliaryArrays(int count) {
        _sectionForPosition = new int[count];
        _positionWithinSection = new int[count];
        _isHeader = new boolean[count];
        _isFooter = new boolean[count];
    }

    private void setPrecomputedItem(int index, boolean isHeader, boolean isFooter, int section, int position) {
        this._isHeader[index] = isHeader;
        this._isFooter[index] = isFooter;
        _sectionForPosition[index] = section;
        _positionWithinSection[index] = position;
    }

    protected int getSectionHeaderViewType(int section) {
        return SECTION_TYPE_HEADER;
    }

    protected int getSectionFooterViewType(int section) {
        return SECTION_TYPE_FOOTER;
    }

    protected int getSectionItemViewType(int section, int position) {
        return SECTION_TYPE_CONTENT;
    }


    /**
     * Returns true if the argument position corresponds to a header
     */
    public boolean isSectionHeaderPosition(int position) {
        if (_isHeader == null) {
            setupIndices();
        }
        return _isHeader[position];
    }

    /**
     * Returns true if the argument position corresponds to a footer
     */
    public boolean isSectionFooterPosition(int position) {
        if (_isFooter == null) {
            setupIndices();
        }
        return _isFooter[position];
    }

    protected boolean isSectionHeaderViewType(int viewType) {
        return viewType == SECTION_TYPE_HEADER;
    }

    protected boolean isSectionFooterViewType(int viewType) {
        return viewType == SECTION_TYPE_FOOTER;
    }

    /**
     * Returns the number of sections in the RecyclerView
     */
    protected abstract int getSectionCount();

    /**
     * Returns the number of items for a given section
     */
    protected abstract int getItemCountForSection(int section);

    /**
     * Returns true if a given section should have a footer
     */
    protected abstract boolean hasFooterInSection(int section);

    /**
     * Creates a ViewHolder of class H for a Header
     */
    protected abstract H onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType);

    /**
     * Creates a ViewHolder of class F for a Footer
     */
    protected abstract F onCreateSectionFooterViewHolder(ViewGroup parent, int viewType);

    /**
     * Creates a ViewHolder of class VH for an Item
     */
    protected abstract C onCreateContentViewHolder(ViewGroup parent, int viewType);

    /**
     * Binds data to the header view of a given section
     */
    protected abstract void onBindSectionHeaderViewHolder(H holder, int section);

    /**
     * Binds data to the footer view of a given section
     */
    protected abstract void onBindSectionFooterViewHolder(F holder, int section);

    /**
     * Binds data to the item view for a given position within a section
     */
    protected abstract void onBindContentViewHolder(C holder, int section, int position);

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setupIndices();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        if (isSectionHeaderViewType(viewType)) {
            viewHolder = onCreateSectionHeaderViewHolder(parent, viewType);
        } else if (isSectionFooterViewType(viewType)) {
            viewHolder = onCreateSectionFooterViewHolder(parent, viewType);
        } else {
            viewHolder = onCreateContentViewHolder(parent, viewType);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int section = _sectionForPosition[position];
        int index = _positionWithinSection[position];

        if (isSectionHeaderPosition(position)) {
            onBindSectionHeaderViewHolder((H) holder, section);
        } else if (isSectionFooterPosition(position)) {
            onBindSectionFooterViewHolder((F) holder, section);
        } else {
            onBindContentViewHolder((C) holder, section, index);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (_sectionForPosition == null) {
            setupIndices();
        }

        int section = _sectionForPosition[position];
        int index = _positionWithinSection[position];

        if (isSectionHeaderPosition(position)) {
            return getSectionHeaderViewType(section);
        } else if (isSectionFooterPosition(position)) {
            return getSectionFooterViewType(section);
        } else {
            return getSectionItemViewType(section, index);
        }

    }

    @Override
    public int getItemCount() {
        return _count;
    }


    private static class SectionRecyclerObserver extends RecyclerView.AdapterDataObserver {

        private SectionRecyclerAdapter _adapter;

        private SectionRecyclerObserver(SectionRecyclerAdapter _adapter) {
            this._adapter = _adapter;
        }

        @Override
        public void onChanged() {
            _adapter.setupIndices();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            _adapter.setupIndices();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            _adapter.setupIndices();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            _adapter.setupIndices();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            _adapter.setupIndices();
        }
    }
}
