package com.swipecrafts.school.ui.dashboard.uesrlist;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.User;
import com.swipecrafts.library.views.CircleImageView;
import com.swipecrafts.school.utils.listener.ImageLoader;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/14/2018.
 */

public class StudentListAdapter extends BaseAdapter {
    private final ImageLoader<User> mImgLoader;
    private Context mContext;
    private List<User> studentList;
    private OnStudentChangeListener mListener;
    private User activeUser;

    public StudentListAdapter(Context mContext, List<User> studentList, OnStudentChangeListener mListener, ImageLoader<User> imageLoader) {
        this.mContext = mContext;
        this.studentList = studentList;
        this.mListener = mListener;
        this.mImgLoader = imageLoader;
    }

    @Override
    public int getCount() {
        return studentList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.studentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        User student = studentList.get(position);

        if (view == null)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);

        CircleImageView userProfileCIV = (CircleImageView) view.findViewById(R.id.profileIV);

        if (!TextUtils.isEmpty(student.getRemoteImgUrl()))
            mImgLoader.loadImage(userProfileCIV, student);

        TextView studentName = (TextView) view.findViewById(R.id.studentNameTV);
        studentName.setText(student.getName());

        View activeUserView = (View) view.findViewById(R.id.activeUserView);
        if (student.isActive() == 1){
            activeUserView.setVisibility(View.VISIBLE);
            activeUser = student;
        } else{
            activeUserView.setVisibility(View.GONE);
        }

        View finalView = view;
        view.setOnClickListener(it -> {
            if (activeUser == student) return;
            mListener.onStudentChanged(finalView, activeUser, student);
        });

        return view;
    }

    public void updateStudentList(List<User> students) {
        this.studentList = students;
        notifyDataSetChanged();
    }

    public void removeUser(User activeUser) {
        int index  = studentList.indexOf(activeUser);

        if (index != -1){
            studentList.remove(index);
            notifyDataSetChanged();
        }
    }

    public interface OnStudentChangeListener{
        void onStudentChanged(View currentView, User previousUser, User currentUser);
    }
}