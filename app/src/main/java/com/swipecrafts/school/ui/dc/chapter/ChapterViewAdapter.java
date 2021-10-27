package com.swipecrafts.school.ui.dc.chapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.Chapter;
import com.swipecrafts.school.utils.listener.AdapterListener;

import java.util.List;


public class ChapterViewAdapter extends RecyclerView.Adapter<ChapterViewAdapter.ViewHolder> {

    private List<Chapter> chapterList;
    private final AdapterListener<Chapter> mListener;

    public ChapterViewAdapter(List<Chapter> items, AdapterListener<Chapter> listener) {
        chapterList = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Chapter item = chapterList.get(position);
        holder.mItem = item;
        holder.mChapterTitleTV.setText(item.getDcChapterTitle());

        holder.mChapterViewImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onItemClicked(holder.mItem);
                }
            }
        });
    }

    public void setChapterList(List<Chapter> chapterList) {
        if (chapterList == null ) return;
        this.chapterList.clear();

        this.chapterList = chapterList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mChapterTitleTV;
        public final ImageView mChapterViewImgBtn;

        public Chapter mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mChapterTitleTV = (TextView) view.findViewById(R.id.chapterTitleTV);
            mChapterViewImgBtn = (ImageView) view.findViewById(R.id.chapterIcon);
        }
    }
}
