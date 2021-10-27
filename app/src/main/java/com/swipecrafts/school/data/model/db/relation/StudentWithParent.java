package com.swipecrafts.school.data.model.db.relation;

import android.arch.persistence.room.Embedded;

import com.swipecrafts.school.data.model.db.Parent;
import com.swipecrafts.school.data.model.db.Student;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 6/4/2018.
 */
public class StudentWithParent {

    @Embedded
    public final Student student;

    @Embedded
    public final List<Parent> parents;

    public StudentWithParent(Student student, List<Parent> parents) {
        this.student = student;
        this.parents = parents;
    }
}
