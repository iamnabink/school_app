package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.swipecrafts.school.data.model.db.Student;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/21/2018.
 */

@Dao
public abstract class StudentDAO implements BaseDAO<Student>{

    @Query("SELECT COUNT(*) FROM students")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM students")
    public abstract int countStatic();

    @Query("SELECT * from students")
    public abstract LiveData<List<Student>> getStudentList();

    @Query("SELECT * from students")
    public abstract List<Student> getStaticStudentList();

    @Query("SELECT students.* FROM students INNER JOIN users ON users.id = students.user_id  WHERE users.active_user = 1")
    public abstract LiveData<Student> getActiveStudentDetails();

    @Query("SELECT * FROM students WHERE id = :id")
    public abstract LiveData<Student> getStudentById(long id);

    @Query("DELETE FROM students")
    public abstract void deleteAll();

    @Transaction
    public Long deleteAndInsert(Student student) {
        delete(student);
       return insert(student);
    }
}
