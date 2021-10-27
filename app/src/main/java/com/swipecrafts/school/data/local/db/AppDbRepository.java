package com.swipecrafts.school.data.local.db;

import com.swipecrafts.school.data.local.db.dao.BusDriverDAO;
import com.swipecrafts.school.data.local.db.dao.ClassSectionDAO;
import com.swipecrafts.school.data.local.db.dao.FeaturesImageDAO;
import com.swipecrafts.school.data.local.db.dao.HomeDAO;
import com.swipecrafts.school.data.local.db.dao.ImageAlbumContentsDAO;
import com.swipecrafts.school.data.local.db.dao.AlbumDAO;
import com.swipecrafts.school.data.local.db.dao.BookDAO;
import com.swipecrafts.school.data.local.db.dao.BusDAO;
import com.swipecrafts.school.data.local.db.dao.BusRouteDAO;
import com.swipecrafts.school.data.local.db.dao.ChapterDAO;
import com.swipecrafts.school.data.local.db.dao.EventDAO;
import com.swipecrafts.school.data.local.db.dao.GenderDAO;
import com.swipecrafts.school.data.local.db.dao.GradeDAO;
import com.swipecrafts.school.data.local.db.dao.MenuDAO;
import com.swipecrafts.school.data.local.db.dao.NotificationDAO;
import com.swipecrafts.school.data.local.db.dao.ParentDAO;
import com.swipecrafts.school.data.local.db.dao.SchoolDetailsDAO;
import com.swipecrafts.school.data.local.db.dao.SchoolScheduleDAO;
import com.swipecrafts.school.data.local.db.dao.StudentDAO;
import com.swipecrafts.school.data.local.db.dao.SubjectDAO;
import com.swipecrafts.school.data.local.db.dao.TeacherDAO;
import com.swipecrafts.school.data.local.db.dao.UserBookDAO;
import com.swipecrafts.school.data.local.db.dao.UserDAO;
import com.swipecrafts.school.data.local.db.dao.UserParentDAO;

import javax.inject.Inject;

/**
 * Created by Madhusudan Sapkota on 2/23/2018.
 */

public class AppDbRepository {
    private SchoolAppDatabase db;
    @Inject
    public AppDbRepository(SchoolAppDatabase db) {
        this.db = db;
    }

    public NotificationDAO getNotificationDAO() {
        return db.notificationDAO();
    }

    public EventDAO getEventDAO() {
        return db.eventDAO();
    }

    public UserDAO getUserDAO() {
        return db.userDAO();
    }

    public MenuDAO getMenuDAO() {
        return db.menuDAO();
    }

    public GradeDAO getGradeDAO() {
        return db.dcGradeDAO();
    }

    public SubjectDAO getSubjectDAO() {
        return db.dcSubjectDAO();
    }

    public ChapterDAO getChapterDAO() {
        return db.dcChapterDAO();
    }

    public SchoolScheduleDAO getSchoolScheduleDAO() {
        return db.schoolScheduleDAO();
    }

    public BusDAO getBusDAO() {
        return db.busDAO();
    }

    public BusRouteDAO getBusRouteDAO() {
        return db.busRouteDAO();
    }

    public GenderDAO getGenderDAO() {
        return db.genderDAO();
    }

    public AlbumDAO getAlbumDAO() { return db.albumDAO(); }

    public ImageAlbumContentsDAO getAlbumContentsDAO() {
        return db.albumContentsDAO();
    }

    public BookDAO getBookDAO(){ return db.bookDAO();}

    public UserBookDAO getUserBookDAO(){ return db.userBookDAO();}

    public StudentDAO getStudentDAO(){ return db.studentDAO();}

    public ParentDAO getParentDAO(){ return db.parentDAO();}

    public UserParentDAO getUserParentDAO(){ return db.userParentDAO();}

    public SchoolDetailsDAO getSchoolDetailsDAO(){ return db.schoolDetailsDAO();}

    public HomeDAO getHomeDAO(){ return db.homeDAO();}

    public FeaturesImageDAO getFeaturesImageDAO(){ return db.featuresImageDAO();}

    public TeacherDAO getTeacherDAO() {
        return db.teacherDAO();
    }

    public ClassSectionDAO getClassSectionDAO() {
        return db.classSectionDAO();
    }

    public BusDriverDAO getBusDriverDAO() {
        return db.busDriverDAO();
    }
}
