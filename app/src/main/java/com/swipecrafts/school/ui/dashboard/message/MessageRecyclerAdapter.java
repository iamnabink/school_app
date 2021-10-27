package com.swipecrafts.school.ui.dashboard.message;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swipecrafts.school.R;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 4/4/2018.
 */
public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder> {

    private List<MessageModel> messageList;

    public MessageRecyclerAdapter(List<MessageModel> messageList) {
        this.messageList = messageList;
    }

    @Override
    public MessageRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageRecyclerAdapter.ViewHolder holder, int position) {
        holder.model = messageList.get(position);

        Log.e("UserD", holder.model.getName()+" message "+ holder.model.getMessage()+" time "+ holder.model.getTime());
        holder.nameTV.setText(holder.model.getName());
        holder.lastMsgDateTV.setText(holder.model.getTime());
        holder.messageTV.setText(holder.model.getMessage());

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void setMessageList(List<MessageModel> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View view;
        private MessageModel model;

        private final TextView nameTV;
        private final TextView lastMsgDateTV;
        private final TextView messageTV;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;

            nameTV = (TextView) view.findViewById(R.id.messengerNameTV);
            lastMsgDateTV = (TextView) view.findViewById(R.id.lastMessageDateTV);
            messageTV = (TextView) view.findViewById(R.id.messageTV);

        }
    }
}
