package com.swipecrafts.school.data.remote;

import com.google.gson.JsonElement;
import com.swipecrafts.school.data.model.api.AttendanceResponse;
import com.swipecrafts.school.data.model.db.Album;
import com.swipecrafts.school.data.model.db.Book;
import com.swipecrafts.school.data.model.db.Bus;
import com.swipecrafts.school.data.model.db.BusDriver;
import com.swipecrafts.school.data.model.db.BusRoute;
import com.swipecrafts.school.data.model.db.Chapter;
import com.swipecrafts.school.data.model.db.DashboardItem;
import com.swipecrafts.school.data.model.db.DressCode;
import com.swipecrafts.school.data.model.db.Event;
import com.swipecrafts.school.data.model.db.Grade;
import com.swipecrafts.school.data.model.db.Notification;
import com.swipecrafts.school.data.model.db.SchoolDetails;
import com.swipecrafts.school.data.model.db.SchoolSchedule;
import com.swipecrafts.school.data.model.db.Student;
import com.swipecrafts.school.data.model.db.DCSubject;
import com.swipecrafts.school.data.model.db.Teacher;
import com.swipecrafts.school.data.model.db.User;
import com.swipecrafts.school.ui.dashboard.assignment.model.Assignment;
import com.swipecrafts.school.ui.dashboard.attendance.model.StudentAttendance;
import com.swipecrafts.school.ui.dashboard.examroutine.model.ExamRoutineRes;
import com.swipecrafts.school.ui.dashboard.livebus.model.LiveBus;
import com.swipecrafts.school.ui.dashboard.message.MessageModel;
import com.swipecrafts.school.ui.dashboard.schedule.model.ClassResponse;
import com.swipecrafts.school.ui.dashboard.videos.VideoAlbum;
import com.swipecrafts.school.ui.dc.Video.VideoFeed;
import com.swipecrafts.school.ui.dc.dcfeed.ContentFeed;
import com.swipecrafts.school.ui.splash.SchoolId;
import com.swipecrafts.library.imageSlider.FeaturesImage;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Madhusudan Sapkota on 2/21/2018.
 */

public interface RestApi {

    @FormUrlEncoded
    @POST("school/getid")
    Call<List<SchoolId>> getSchoolId(@Header("apikey") String apiKey, @Field("none") String none);

    @FormUrlEncoded
    @POST("dc/grade")
    Call<List<Grade>> getGradeList(
            @Header("apikey") String apiKey,

            @Field("nothing") int dummy);


    @FormUrlEncoded
    @POST("dc/subject")
    Call<List<DCSubject>> getSubjectList(
            @Header("apikey") String apiKey,

            @Field("dc_grade_id") String gradeId);

    @FormUrlEncoded
    @POST("dc/chapter")
    Call<List<Chapter>> getChapterList(
            @Header("apikey") String apiKey,

            @Field("dc_subject_id") String subjectId);

    @FormUrlEncoded
    @POST("dc/digital_content")
    Call<List<VideoFeed>> getVideoFeeds(
            @Header("apikey") String apiKey,

            @Field("dc_grade_id") long gradeId,
            @Field("dc_subject_id") long subjectId);


    @FormUrlEncoded
    @POST("dc/content")
    Call<List<ContentFeed>> getContentFeeds(
            @Header("apikey") String apiKey,

            @Field("dc_grade_id") long gradeId,
            @Field("dc_subject_id") long subjectId,
            @Field("dc_chapter_id") long chapterId);


    @Streaming
    @GET
    Call<ResponseBody> getImage(@Url String url);

    @Streaming
    @GET
    Call<ResponseBody> getPDF(@Url String url);

    @FormUrlEncoded
    @POST("notice/get")
    Call<List<Notification>> getNotifications(
            @Header("apikey") String apiKey,

            @Field("school_id") String schoolId);

    @FormUrlEncoded
    @POST("notice/teacher")
    Call<List<Notification>> getNotifications(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId,
            @Field("usertype") String usertype);

    @FormUrlEncoded
    @POST("event/list")
    Call<List<Event>> getEvents(
            @Header("apikey") String apiKey,

            @Field("school_id") String schoolId);

    @FormUrlEncoded
    @POST("menu/get")
    Call<List<DashboardItem>> getMenuItems(
            @Header("apikey") String apiKey,

            @Field("nothing") int dummy);


    @FormUrlEncoded
    @POST("featured/img")
    Call<List<FeaturesImage>> getFeatureImages(
            @Header("apikey") String apiKey,

            @Field("school_id") String schoolId);

    @FormUrlEncoded
    @POST("school/schedule")
    Call<List<SchoolSchedule>> getSchoolSchedule(
            @Header("apikey") String apiKey,

            @Field("school_id") String schoolId);


    @FormUrlEncoded
    @POST("bus/list")
    Call<List<Bus>> getBusList(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,

            @Field("student_id") String student_id,
            @Field("school_id") String schoolId);

    @FormUrlEncoded
    @POST("station/getbybus")
    Call<List<BusRoute>> getBusRouteList(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,

            @Field("student_id") String student_id,
            @Field("school_id") String schoolId,
            @Field("app_bus_id") String busId);

    @FormUrlEncoded
    @POST("dress_code/show")
    Call<List<DressCode>> getDressCode(
            @Header("apikey") String apiKey,

            @Field("school_id") String schoolId);


    @FormUrlEncoded
    @POST("gallery/img")
    Call<List<Album>> getAlbumDetails(
            @Header("apikey") String apiKey,

            @Field("school_id") String schoolId);

    @FormUrlEncoded
    @POST("gallery/video")
    Call<List<VideoAlbum>> getVideoAlbums(
            @Header("apikey") String apiKey,

            @Field("school_id") String schoolId);

    @FormUrlEncoded
    @POST("school/detail")
    Call<List<SchoolDetails>> getSchoolDetails(
            @Header("apikey") String apiKey,

            @Field("school_id") String schoolId);

    @FormUrlEncoded
    @POST("app/login")
    Call<List<User>> loginStudent(
            @Header("apikey") String apiKey,

            @Field("school_id") String schoolId,
            @Field("username") String username,
            @Field("passkey") String password,
            @Field("usertype") String userType,
            @Field("fcm_key") String fcmKey);


    @FormUrlEncoded
    @POST("book/list")
    Call<List<Book>> getBooks(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId,
            @Header("sesusr") String userType);

    @FormUrlEncoded
    @POST("student/profile")
    Call<List<Student>> getStudentDetails(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId,
            @Header("sesusr") String userType);

    @FormUrlEncoded
    @POST("class/routine")
    Call<List<ClassResponse>> getClassRoutine(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId,
            @Header("sesusr") String userType);


    @FormUrlEncoded
    @POST("app/logout")
    Call<JsonElement> logOut(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,

            @Field("user_id") String userId,
            @Header("sesusr") String userType);

    @FormUrlEncoded
    @POST("post/suggestion")
    Call<JsonElement> postSuggestion(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,


            @Field("school_id") String schoolId,
            @Field("user_id") String userId,
            @Header("sesusr") String userType,
            @Field("stitle") String title,
            @Field("sbody") String body,
            @Field("anonymous") int anonymous);

    @FormUrlEncoded
    @POST("post/appreciation")
    Call<JsonElement> postAppreciation(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,


            @Field("school_id") String schoolId,
            @Field("user_id") String userId,
            @Header("sesusr") String userType,
            @Field("atitle") String title,
            @Field("abody") String body,
            @Field("anonymous") int anonymous);

    @FormUrlEncoded
    @POST("attendance/clist")
    Call<List<ClassResponse>> getClassSections(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId
    );


    @FormUrlEncoded
    @POST("student/listbyclass")
    Call<List<StudentAttendance>> getStudentsByClass(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId,
            @Field("class_id") String classId
    );

    @FormUrlEncoded
    @POST("student/listbyclass")
    Call<List<StudentAttendance>> getStudentsByClassSections(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId,
            @Field("class_id") String classId,
            @Field("section_id") String sectionId
    );

    @FormUrlEncoded
    @POST("student/doattendance")
    Call<List<AttendanceResponse>> getAttendanceData(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId,
            @Field("class_id") String classId,
            @Field("section_id") String sectionId
    );


    @Multipart
    @POST("student/attendance")
    Call<JsonElement> postStudentAttendance(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Part("school_id") RequestBody schoolId,
            @Part("user_id") RequestBody userId,
            @Part("attendance_details") RequestBody attendanceData
    );


    @FormUrlEncoded
    @POST("lnotice/post")
    Call<JsonElement> postLeaveApplication(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId,

            @Field("atitle") String title,
            @Field("asubject") String subject,
            @Field("abody") String body

    );

    @FormUrlEncoded
    @POST("student/getattendance")
    Call<List<StudentAttendance>> getMyAttendance(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("subject/list")
    Call<List<com.swipecrafts.school.ui.dashboard.assignment.model.Subject>> getSchoolSubjectsList(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId
    );


    @FormUrlEncoded
    @POST("student/msg/inbox")
    Call<List<MessageModel>> getStudentInboxMessage(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("teacher/msg/inbox")
    Call<List<MessageModel>> getTeacherInboxMessage(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("student/msg/outbox")
    Call<List<MessageModel>> getStudentSentMessage(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("teacher/msg/outbox")
    Call<List<MessageModel>> getTeacherSentMessage(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId
    );


    @FormUrlEncoded
    @POST("student/msg/send")
    Call<JsonElement> postMessageFromStudent(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId,
            @Field("msg") String message,
            @Field("teacher_id") long receiverId
    );

    @FormUrlEncoded
    @POST("teacher/msg/send")
    Call<JsonElement> postMessageFromTeacher(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId,
            @Field("msg") String message,
            @Field("student_id") long receiverId
    );

    @FormUrlEncoded
    @POST("teacher/list")
    Call<List<StudentAttendance>> getTeachersList(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("teacher/clist")
    Call<List<Teacher>> getTeacherContactList(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId
    );


    @Multipart
    @POST("assignment/post")
    Call<JsonElement> postAssignment(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Part("school_id") RequestBody schoolId,
            @Part("user_id") RequestBody userId,
            @Part("atitle") RequestBody title,
            @Part MultipartBody.Part file,
            @Part("class_id") RequestBody classId,
            @Part("section_id") RequestBody sectionId,
            @Part("subject_id") RequestBody subjectId

    );

    @FormUrlEncoded
    @POST("assignment/listbyclass")
    Call<List<Assignment>> getAssignment(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("exam/routine")
    Call<List<ExamRoutineRes>> getExamRoutine(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("app/version/get")
    Call<List<HashMap<String, String>>> checkAppVersion(@Header("apikey") String apiKey, @Field("school_id") String schoolId);

    @FormUrlEncoded
    @POST("app/password")
    Call<JsonElement> updatePassword(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("user_id") String userId,
            @Field("passkey") String oldPassword,
            @Field("newpasskey") String newPassword,
            @Field("confirmpass") String confirmPassword
    );

    @FormUrlEncoded
    @POST("bus/list")
    Call<List<LiveBus>> getLiveMapBusList(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("student_id") String student_id,
            @Field("school_id") String schoolId
    );

    @FormUrlEncoded
    @POST("bus/get_by_driver")
    Call<List<BusDriver>> getBusDetails(
            @Header("apikey") String apiKey,
            @Header("sshkey") String sshKey,
            @Header("seskey") String sesKey,
            @Header("sesnid") String sesNId,
            @Header("sesuid") String sesUId,
            @Header("sesusr") String userType,

            @Field("school_id") String schoolId,
            @Field("driver_id")String driverId);

}