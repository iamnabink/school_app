package com.swipecrafts.school.data.local.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.swipecrafts.school.data.local.db.converter.DateConverter;
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
import com.swipecrafts.school.data.model.db.Album;
import com.swipecrafts.school.data.model.db.BusDriver;
import com.swipecrafts.school.data.model.db.ClassRoutine;
import com.swipecrafts.school.data.model.db.DCSubject;
import com.swipecrafts.school.data.model.db.Day;
import com.swipecrafts.school.data.model.db.ImageAlbumContent;
import com.swipecrafts.school.data.model.db.Book;
import com.swipecrafts.school.data.model.db.Bus;
import com.swipecrafts.school.data.model.db.BusRoute;
import com.swipecrafts.school.data.model.db.Chapter;
import com.swipecrafts.school.data.model.db.DashboardItem;
import com.swipecrafts.school.data.model.db.Event;
import com.swipecrafts.school.data.model.db.Gender;
import com.swipecrafts.school.data.model.db.Grade;
import com.swipecrafts.school.data.model.db.Notification;
import com.swipecrafts.school.data.model.db.Parent;
import com.swipecrafts.school.data.model.db.SchoolClass;
import com.swipecrafts.school.data.model.db.SchoolDetails;
import com.swipecrafts.school.data.model.db.SchoolSchedule;
import com.swipecrafts.school.data.model.db.SchoolSection;
import com.swipecrafts.school.data.model.db.Student;
import com.swipecrafts.school.data.model.db.Teacher;
import com.swipecrafts.school.data.model.db.User;
import com.swipecrafts.school.data.model.db.relation.ClassSection;
import com.swipecrafts.school.data.model.db.relation.UserBook;
import com.swipecrafts.school.data.model.db.relation.UserParent;
import com.swipecrafts.library.imageSlider.FeaturesImage;

/**
 * Created by Madhusudan Sapkota on 2/20/2018.
 */


@Database(entities = {
        Notification.class,
        Event.class,
        User.class,
        DashboardItem.class,
        Grade.class,
        DCSubject.class,
        Chapter.class,
        SchoolSchedule.class,
        Bus.class,
        BusRoute.class,
        Gender.class,
        Album.class,
        ImageAlbumContent.class,
        Book.class,
        UserBook.class,
        Student.class,
        Parent.class,
        UserParent.class,
        SchoolDetails.class,
        FeaturesImage.class,
        Teacher.class,
        SchoolClass.class,
        SchoolSection.class,
        ClassSection.class,
        Day.class,
        ClassRoutine.class,
        BusDriver.class
        }, version = 10, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class SchoolAppDatabase extends RoomDatabase{

    public abstract NotificationDAO notificationDAO();

    public abstract EventDAO eventDAO();

    public abstract UserDAO userDAO();

    public abstract MenuDAO menuDAO();

    public abstract GradeDAO dcGradeDAO();

    public abstract SubjectDAO dcSubjectDAO();

    public abstract ChapterDAO dcChapterDAO();


    public abstract SchoolScheduleDAO schoolScheduleDAO();

    public abstract BusDAO busDAO();

    public abstract BusRouteDAO busRouteDAO();

    public abstract GenderDAO genderDAO();

    public abstract AlbumDAO albumDAO();

    public abstract ImageAlbumContentsDAO albumContentsDAO();

    public abstract BookDAO bookDAO();

    public abstract UserBookDAO userBookDAO();

    public abstract StudentDAO studentDAO();

    public abstract ParentDAO parentDAO();

    public abstract UserParentDAO userParentDAO();

    public abstract SchoolDetailsDAO schoolDetailsDAO();

    public abstract HomeDAO homeDAO();

    public abstract FeaturesImageDAO featuresImageDAO();

    public abstract TeacherDAO teacherDAO();

    public abstract ClassSectionDAO classSectionDAO();

    public abstract BusDriverDAO busDriverDAO();
}
