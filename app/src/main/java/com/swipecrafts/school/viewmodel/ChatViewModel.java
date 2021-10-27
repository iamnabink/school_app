package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.model.db.SchoolClass;
import com.swipecrafts.school.data.model.db.SchoolSection;
import com.swipecrafts.school.data.repository.ClassSectionRepository;
import com.swipecrafts.school.data.repository.MessageRepository;
import com.swipecrafts.school.data.repository.RepositoryFactory;
import com.swipecrafts.school.ui.dashboard.attendance.model.StudentAttendance;
import com.swipecrafts.school.ui.dashboard.message.MessageFragment;
import com.swipecrafts.school.ui.dashboard.message.MessageModel;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/27/2018.
 */

public class ChatViewModel extends ViewModel {
    private final ClassSectionRepository classSectionRepository;
    private final MessageRepository messageRepository;
    private RepositoryFactory dataManager;

    public ChatViewModel(RepositoryFactory dataManager) {
        this.dataManager = dataManager;
        this.classSectionRepository = dataManager.create(ClassSectionRepository.class);
        this.messageRepository = dataManager.create(MessageRepository.class);
    }

    public LiveData<Resource<List<SchoolClass>>> getClassSections() {
        return classSectionRepository.loadSchoolClasses();
    }

    public LiveData<List<SchoolSection>> getSections(long classId) {
        return classSectionRepository.getSectionsFromDB(classId);
    }

    public LiveData<ApiResponse<List<StudentAttendance>>> getUsersList(long classId, long sectionId) {
        return messageRepository.getUser(classId, sectionId);
    }

    public LiveData<ApiResponse<List<MessageModel>>> getMessages(String msgType) {
        if (msgType.equalsIgnoreCase(MessageFragment.INBOX_TYPE)){
            return messageRepository.loadInbox();
        } else{
            return messageRepository.loadSentMessage();
        }
    }

    public LiveData<ApiResponse<String>> sendMessage(String message, long toUser) {
        return messageRepository.sendMessage(message, toUser);
    }

    public String getUserType() {
        return messageRepository.getActiveUserType();
    }

    public LiveData<ApiResponse<List<StudentAttendance>>> getTeacherList() {
        return messageRepository.loadTeachers();
    }
}
