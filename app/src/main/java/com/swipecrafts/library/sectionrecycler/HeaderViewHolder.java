package com.swipecrafts.library.sectionrecycler;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Madhusudan Sapkota on 6/17/2018.
 */
public class HeaderViewHolder extends RecyclerView.ViewHolder {
    protected final TextView titleText;
    public HeaderViewHolder(View itemView, @IdRes int titleID) {
        super(itemView);
        titleText = (TextView) itemView.findViewById(titleID);
    }

    public void setHeader(String title){
        titleText.setText(title);
    }
}
