package com.swipecrafts.school.data.repository;

import android.arch.lifecycle.LiveData;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.remote.AppApiRepository;
import com.swipecrafts.school.ui.dashboard.attendance.model.StudentAttendance;
import com.swipecrafts.school.ui.dashboard.message.MessageModel;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 6/6/2018.
 */
public class MessageRepository extends BaseRepository{

    public MessageRepository(AppApiRepository webService, AppDbRepository dbService, AppPreferencesRepository preferencesRepository) {
        super(webService, dbService, preferencesRepository);
    }

    public LiveData<ApiResponse<List<StudentAttendance>>> getUser(long classId, long sectionId) {
       return webService.getStudentsByClassAndOrSection(classId, sectionId);
    }

    public LiveData<ApiResponse<List<MessageModel>>> loadInbox() {
        return webService.getInboxMessage();
    }

    public LiveData<ApiResponse<List<MessageModel>>> loadSentMessage() {
        return webService.getSentMessage();
    }

    public LiveData<ApiResponse<List<StudentAttendance>>> loadTeachers() {
        return webService.getTeacherList();
    }

    public LiveData<ApiResponse<String>> sendMessage(String message, long to) {
        return webService.postMessage(message, to);
    }
    public String getActiveUserType() {
        return preferencesService.getUserType();
    }

}
