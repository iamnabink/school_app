package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.db.Teacher;
import com.swipecrafts.school.data.repository.RepositoryFactory;
import com.swipecrafts.school.data.repository.TeacherRepository;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 5/22/2018.
 */
public class TeacherViewModel extends ViewModel {
    private final TeacherRepository teacherRepository;
    private RepositoryFactory factory;

    public TeacherViewModel(RepositoryFactory factory) {
        this.factory = factory;
        this.teacherRepository = factory.create(TeacherRepository.class);
    }

    public LiveData<Resource<List<Teacher>>> loadTeachers(boolean fetchFromRemote) {
        return teacherRepository.loadTeachers(fetchFromRemote);
    }
}
