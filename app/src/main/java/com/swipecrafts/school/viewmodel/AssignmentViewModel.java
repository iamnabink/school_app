package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.model.db.SchoolClass;
import com.swipecrafts.school.data.repository.AssignmentRepository;
import com.swipecrafts.school.data.repository.ClassSectionRepository;
import com.swipecrafts.school.data.repository.RepositoryFactory;
import com.swipecrafts.school.data.repository.SubjectRepository;
import com.swipecrafts.school.ui.dashboard.assignment.model.Assignment;
import com.swipecrafts.school.ui.dashboard.assignment.model.Subject;

import java.io.File;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 4/5/2018.
 */
public class AssignmentViewModel extends ViewModel {
    private final AssignmentRepository assignmentRepository;
    private final ClassSectionRepository classSectionRepository;
    private final SubjectRepository subjectRepository;
    private RepositoryFactory dataManager;

    public AssignmentViewModel(RepositoryFactory dataManager) {
        this.dataManager = dataManager;
        this.assignmentRepository = dataManager.create(AssignmentRepository.class);
        this.classSectionRepository = dataManager.create(ClassSectionRepository.class);
        this.subjectRepository = dataManager.create(SubjectRepository.class);
    }


    public LiveData<Resource<List<SchoolClass>>> getClassList() {
        return classSectionRepository.loadSchoolClasses();
    }

    public LiveData<ApiResponse<List<Subject>>> getSubjectLists() {
        return subjectRepository.loadSubjects();
    }

    public LiveData<ApiResponse<String>> postAssignment(String title, int classId, int subjectId, File selectedFile) {
        return assignmentRepository.postAssignment(title, classId, subjectId, selectedFile);
    }

    public LiveData<ApiResponse<List<Assignment>>> loadAssignments(){
        return assignmentRepository.getAssignments();
    }

}
