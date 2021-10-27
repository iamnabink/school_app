package com.swipecrafts.school.di.component;

import android.app.Application;

import com.swipecrafts.school.di.module.ApplicationModule;
import com.swipecrafts.school.di.module.NetModule;
import com.swipecrafts.school.di.module.PrefModule;
import com.swipecrafts.school.di.module.RoomModule;
import com.swipecrafts.school.ui.MainActivity;
import com.swipecrafts.school.ui.calendar.CalendarFragment;
import com.swipecrafts.school.ui.dashboard.DashboardFragment;
import com.swipecrafts.school.ui.dashboard.about.AboutFragment;
import com.swipecrafts.school.ui.dashboard.application.ApplicationFragment;
import com.swipecrafts.school.ui.dashboard.assignment.parent.AssignmentFragment;
import com.swipecrafts.school.ui.dashboard.assignment.teacher.TeacherAssignmentFragment;
import com.swipecrafts.school.ui.dashboard.attendance.student.AttendanceFragment;
import com.swipecrafts.school.ui.dashboard.attendance.teacher.TeacherAttendanceFragment;
import com.swipecrafts.school.ui.dashboard.attendance.teacher.schoolclass.ClassFragment;
import com.swipecrafts.school.ui.dashboard.attendance.teacher.schoolsection.SectionFragment;
import com.swipecrafts.school.ui.dashboard.booklist.BookFragment;
import com.swipecrafts.school.ui.dashboard.busroute.BusRouteFragment;
import com.swipecrafts.school.ui.dashboard.busroute.map.BusRouteMapsActivity;
import com.swipecrafts.school.ui.dashboard.chat.ChatUserFragment;
import com.swipecrafts.school.ui.dashboard.dresscode.DressCodeFragment;
import com.swipecrafts.school.ui.dashboard.examroutine.ExamRoutineFragment;
import com.swipecrafts.school.ui.dashboard.gallery.AlbumFragment;
import com.swipecrafts.school.ui.dashboard.gallery.GalleryFragment;
import com.swipecrafts.school.ui.dashboard.livebus.LiveBusListFragment;
import com.swipecrafts.school.ui.dashboard.livebus.livemap.LiveBusFragment;
import com.swipecrafts.school.ui.dashboard.message.MessageFragment;
import com.swipecrafts.school.ui.dashboard.profile.StudentProfileFragment;
import com.swipecrafts.school.ui.dashboard.schedule.ClassRoutineFragment;
import com.swipecrafts.school.ui.dashboard.schoolschedule.ScheduleFragment;
import com.swipecrafts.school.ui.dashboard.settings.SettingFragment;
import com.swipecrafts.school.ui.dashboard.suggestion.SuggestionFragment;
import com.swipecrafts.school.ui.dashboard.teachercontact.TeacherContactFragment;
import com.swipecrafts.school.ui.dashboard.videos.VideoAlbumFragment;
import com.swipecrafts.school.ui.dashboard.videos.VideoGalleryFragment;
import com.swipecrafts.school.ui.dc.DigitalClassFragment;
import com.swipecrafts.school.ui.dc.Video.VideoFragment;
import com.swipecrafts.school.ui.dc.chapter.ChapterFragment;
import com.swipecrafts.school.ui.dc.dcfeed.ContentFeedFragment;
import com.swipecrafts.school.ui.dc.subject.SubjectFragment;
import com.swipecrafts.school.ui.driver.DriverDashboard;
import com.swipecrafts.school.ui.driver.DriverDashboardFragment;
import com.swipecrafts.school.ui.driver.bus.AssociatedBusFragment;
import com.swipecrafts.school.ui.driver.live.BusLocationFragment;
import com.swipecrafts.school.ui.home.HomeFragment;
import com.swipecrafts.school.ui.login.ChangePasswordDFragment;
import com.swipecrafts.school.ui.login.LoginActivity;
import com.swipecrafts.school.ui.login.LoginDialogFragment;
import com.swipecrafts.school.ui.notification.NoticeListFragment;
import com.swipecrafts.school.ui.notification.NotificationFragment;
import com.swipecrafts.school.ui.splash.SplashActivity;
import com.swipecrafts.school.utils.service.LocationService;
import com.swipecrafts.school.utils.service.LocationTracker;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Annotated as a Singelton since we don't want to have multiple instances of a Single Database,
 * <p>
 * Created by Madhusudan Sapkota.
 */

@Singleton
@Component(modules = {ApplicationModule.class, RoomModule.class, PrefModule.class, NetModule.class})
public interface ApplicationComponent {

    void inject(SplashActivity activity);

    void inject(MainActivity activity);

    void inject(AboutFragment fragment);

    void inject(LiveBusFragment fragment);

    void inject(ClassRoutineFragment fragment);

    void inject(ExamRoutineFragment fragment);

    void inject(LiveBusListFragment fragment);

    void inject(StudentProfileFragment fragment);

    void inject(BookFragment fragment);

    void inject(AlbumFragment fragment);

    void inject(GalleryFragment fragment);

    void inject(VideoAlbumFragment fragment);

    void inject(VideoGalleryFragment fragment);

    void inject(LoginActivity fragment);
    void inject(LoginDialogFragment fragment);
    void inject(ChangePasswordDFragment fragment);

    void inject(ScheduleFragment fragment);

    void inject(NoticeListFragment fragment);

    void inject(NotificationFragment fragment);

    void inject(CalendarFragment fragment);

    void inject(DashboardFragment fragment);

    void inject(DigitalClassFragment fragment);

    void inject(SubjectFragment fragment);

    void inject(ChapterFragment fragment);
    void inject(VideoFragment fragment);

    void inject(ContentFeedFragment fragment);

    void inject(BusRouteFragment fragment);

    void inject(com.swipecrafts.school.ui.dashboard.busroute.BusListFragment fragment);

    void inject(DressCodeFragment fragment);

    void inject(BusRouteMapsActivity fragment);

    void inject(ChatUserFragment fragment);

    void inject(MessageFragment fragment);

    void inject(SuggestionFragment fragment);

    void inject(TeacherAttendanceFragment fragment);

    void inject(ClassFragment fragment);

    void inject(SectionFragment fragment);

    void inject(AttendanceFragment fragment);

    void inject(ApplicationFragment fragment);

    void inject(TeacherAssignmentFragment fragment);

    void inject(AssignmentFragment fragment);

    void inject(HomeFragment fragment);

    void inject(SettingFragment fragment);

    void inject(TeacherContactFragment fragment);

    void inject(LocationService service);
    void inject(LocationTracker service);

    Application application();

    void inject(BusLocationFragment busLocationFragment);

    void inject(DriverDashboard driverDashboard);

    void inject(DriverDashboardFragment fragment);

    void inject(AssociatedBusFragment fragment);

}
