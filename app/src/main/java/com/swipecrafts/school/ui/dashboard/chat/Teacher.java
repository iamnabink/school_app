package com.swipecrafts.school.ui.dashboard.chat;

import java.util.Date;

/**
 * Created by Madhusudan Sapkota on 3/26/2018.
 */

public class Teacher {

    private Date lastOnline = new Date();
    private String teacherProfile;

    public String getTeacherName() {
        return "Kushal Karki";
    }

    public Date getLastOnline() {
        return lastOnline;
    }

    public Object getTeacherProfile() {
        return teacherProfile;
    }
}
