package com.swipecrafts.school.ui.dashboard.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.ui.dashboard.attendance.model.StudentAttendance;
import com.swipecrafts.school.utils.listener.AdapterListener;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 4/4/2018.
 */
public class UserListAdapter extends BaseAdapter {

    private final AdapterListener<Long> listener;
    private Context mContext;
    private List<StudentAttendance> userList;

    public UserListAdapter(Context mContext, List<StudentAttendance> studentList, AdapterListener<Long> listener) {
        this.mContext = mContext;
        this.userList = studentList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        StudentAttendance user = userList.get(position);

        if (view == null)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_user_item, parent, false);

        TextView studentName = (TextView) view.findViewById(R.id.userProfileName);
        studentName.setText(user.getName());

        view.setOnClickListener(it -> {
            if (listener != null) {
                listener.onItemClicked(user.getStudentId());
            }
        });

        return view;
    }

    public void updateStudentList(List<StudentAttendance> students) {
        this.userList = students;
        notifyDataSetChanged();
    }
}
