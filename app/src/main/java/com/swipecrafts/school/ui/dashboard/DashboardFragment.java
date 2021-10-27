package com.swipecrafts.school.ui.dashboard;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.swipecrafts.library.preetydialog.CustomDialog;
import com.swipecrafts.school.BuildConfig;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.manager.Status;
import com.swipecrafts.school.data.model.api.UserType;
import com.swipecrafts.school.data.model.db.DashboardItem;
import com.swipecrafts.school.data.model.db.User;
import com.swipecrafts.school.data.remote.ApiListener;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dashboard.about.AboutFragment;
import com.swipecrafts.school.ui.dashboard.application.ApplicationFragment;
import com.swipecrafts.school.ui.dashboard.assignment.parent.AssignmentFragment;
import com.swipecrafts.school.ui.dashboard.assignment.teacher.TeacherAssignmentFragment;
import com.swipecrafts.school.ui.dashboard.attendance.student.AttendanceFragment;
import com.swipecrafts.school.ui.dashboard.attendance.teacher.TeacherAttendanceFragment;
import com.swipecrafts.school.ui.dashboard.attendance.teacher.schoolclass.ClassFragment;
import com.swipecrafts.school.ui.dashboard.booklist.BookFragment;
import com.swipecrafts.school.ui.dashboard.busroute.BusListFragment;
import com.swipecrafts.school.ui.dashboard.dresscode.DressCodeFragment;
import com.swipecrafts.school.ui.dashboard.examroutine.ExamRoutineFragment;
import com.swipecrafts.school.ui.dashboard.gallery.AlbumFragment;
import com.swipecrafts.school.ui.dashboard.livebus.LiveBusListFragment;
import com.swipecrafts.school.ui.dashboard.message.MessageFragment;
import com.swipecrafts.school.ui.dashboard.profile.StudentProfileFragment;
import com.swipecrafts.school.ui.dashboard.schedule.ClassRoutineFragment;
import com.swipecrafts.school.ui.dashboard.schoolschedule.ScheduleFragment;
import com.swipecrafts.school.ui.dashboard.settings.SettingFragment;
import com.swipecrafts.school.ui.dashboard.suggestion.SuggestionFragment;
import com.swipecrafts.school.ui.dashboard.teachercontact.TeacherContactFragment;
import com.swipecrafts.school.ui.dashboard.uesrlist.StudentListAdapter;
import com.swipecrafts.school.ui.dashboard.videos.VideoAlbumFragment;
import com.swipecrafts.school.ui.login.LoginActivity;
import com.swipecrafts.school.ui.login.LoginDialogFragment;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.LogUtils;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.utils.listener.AdapterListener;
import com.swipecrafts.school.viewmodel.DashboardViewModel;
import com.swipecrafts.school.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.swipecrafts.school.ui.login.LoginDialogFragment.ARG_LOGIN_TYPE;


public class DashboardFragment extends BaseFragment implements AdapterListener<DashboardItem> {

    private static final int LOGIN_RESPONSE_CODE = 1;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DashboardViewModel dashboardViewModel;
    private UserViewModel userViewModel;

    private Toolbar toolbar;

    private RecyclerView mRecyclerView;
    private List<DashboardItem> dashboardItems;
    private DashboardItem updateVersion;
    private DashboardItemAdapter mDashboardItemAdapter;

    private User activeUser;

    // for parent view (user list views)
    private View popupStudentView;
    private ListView studentListView;
    private View addNewStudentView;
    private PopupWindow popupWindow;

    private TextView userProfileName;

    private List<User> loggedInUsersList;
    private StudentListAdapter studentListAdapter;
    private LoginDialogFragment loginDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //Set up and subscribe (observe) to the ViewModel
        dashboardViewModel = ViewModelProviders.of(this, viewModelFactory).get(DashboardViewModel.class);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);

        dashboardViewModel.getMenuItem().observe(this, dashboardItems -> {
            if (dashboardItems == null || dashboardItems.isEmpty()) return;

            DashboardItem dashboardItem = new DashboardItem();
            dashboardItem.id = 26;
            dashboardItem.setOrder(10);
            dashboardItem.menuName = "Live Bus";
            dashboardItems.add(dashboardItem);
            updateRecyclerViewData(dashboardItems);
        });
        userViewModel.getUsers().observe(this, this::setLoggedInUsers);
        userViewModel.getActiveUser().observe(this, this::setActiveUser);
        checkAppVersion();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        popupStudentView = inflater.inflate(R.layout.student_popup_view, null);
        studentListView = popupStudentView.findViewById(R.id.studentList);
        addNewStudentView = inflater.inflate(R.layout.add_new_student, studentListView, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.dashboardToolbar);
        userProfileName = toolbar.findViewById(R.id.userProfileName);
        mRecyclerView = view.findViewById(R.id.dashboardListRecycler);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        // Set the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.color.colorGrey));
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if (dashboardItems == null) dashboardItems = new ArrayList<>();
        mDashboardItemAdapter = new DashboardItemAdapter(
                dashboardItems,
                false,
                (imageView, model) -> {
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.progressbar)
                            .error(R.drawable.ic_default);

                    Glide.with(this)
                            .load(model.getRemoteIconResource())
                            .apply(options)
                            .into(imageView);
                },
                this);
        mRecyclerView.setAdapter(mDashboardItemAdapter);
    }

    private void updateRecyclerViewData(List<DashboardItem> items) {
        this.dashboardItems = items;

        UserType userType = UserType.PARENT;
        if (activeUser != null) userType = UserType.from(activeUser.getUserType());

        List<DashboardItem> dashboardItems = new ArrayList<>();
        for (DashboardItem item : items) {
            long id = item.id;

            if (id == 25) {
                this.updateVersion = item;
                continue;
            }

            if (id == 16 || id == 3 || id == 5 || id == 23) {
                if (userType != UserType.TEACHER) {
                    dashboardItems.add(item);
                }
            } else {
                dashboardItems.add(item);
            }
        }

        mDashboardItemAdapter.showOnlyLoggedInData(dashboardItems, activeUser != null);
        mDashboardItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(DashboardItem item) {
        long id = item.getId();

        if (id == 1) {
            // Dress-Code can be viewed to both teacher/parent
            replaceFragment(
                    R.id.main_container,
                    new DressCodeFragment(),
                    DressCodeFragment.class.getSimpleName(),
                    "Dashboard");

        } else if (id == 2) {
            // Assignment can be viewed by parent/teacher
            if (activeUser == null) {
                showWarningDialog(null, getString(R.string.error_dialog_title), getString(R.string.dashboard_login_message), getString(R.string.dialog_ok));
                return;
            }

            // Attendance can be viewed by parents!!
            if (activeUser.getUserType().equalsIgnoreCase("parent")) {
                replaceFragment(
                        R.id.main_container,
                        new AssignmentFragment(),
                        AssignmentFragment.class.getSimpleName(),
                        "Dashboard");
                return;
            }
            // Attendance can be posted by teacher
            if (activeUser.getUserType().equalsIgnoreCase("teacher"))
                // do something
                replaceFragment(
                        R.id.main_container,
                        new TeacherAssignmentFragment(),
                        TeacherAttendanceFragment.class.getSimpleName(),
                        "Dashboard");

        } else if (id == 3) {

            // Book lists can be viewed to both parent
            if (activeUser == null) {
                showWarningDialog(null, getString(R.string.error_dialog_title), getString(R.string.dashboard_login_message), getString(R.string.dialog_ok));
                return;
            }
            replaceFragment(
                    R.id.main_container,
                    new BookFragment(),
                    BookFragment.class.getSimpleName(),
                    "Dashboard");

        } else if (id == 4) {

        } else if (id == 5) {

            // ExamRoutineRes Routine can be viewed to both parent
            if (activeUser == null) {
                showWarningDialog(null, getString(R.string.error_dialog_title), getString(R.string.dashboard_login_message), getString(R.string.dialog_ok));
                return;
            }

            if (activeUser.getUserType().equalsIgnoreCase("parent"))
                replaceFragment(
                        R.id.main_container,
                        new ExamRoutineFragment(),
                        ExamRoutineFragment.class.getSimpleName(),
                        "Dashboard");

        } else if (id == 6) {

        } else if (id == 7) {

            // School- Bus Route can be viewed to both teacher/parent/driver
            replaceFragment(
                    R.id.main_container,
                    new BusListFragment(),
                    BusListFragment.class.getSimpleName(),
                    "Dashboard");

        } else if (id == 8) {
            if (activeUser == null) {
                showWarningDialog(null, getString(R.string.error_dialog_title), getString(R.string.dashboard_login_message), getString(R.string.dialog_ok));
                return;
            }

            replaceFragment(
                    R.id.main_container,
                    new TeacherContactFragment(),
                    TeacherContactFragment.class.getSimpleName(),
                    "Dashboard");

        } else if (id == 9) {

        } else if (id == 10) {

        } else if (id == 11) {
            if (activeUser == null) {
                showWarningDialog(null, "Sorry...", getString(R.string.dashboard_login_message), getString(R.string.dialog_ok));
                return;
            }

            // Message can be viewed to both teacher/parent
            MessageFragment fragment = MessageFragment.newInstance(MessageFragment.INBOX_TYPE);

            replaceFragment(
                    R.id.main_container,
                    fragment,
                    MessageFragment.class.getSimpleName(),
                    "Dashboard");

        } else if (id == 12) {

        } else if (id == 13) {

            // School-Image Album can be viewed to both teacher/parent/driver
            replaceFragment(
                    R.id.main_container,
                    new AlbumFragment(),
                    AlbumFragment.class.getSimpleName(),
                    "Dashboard");

        } else if (id == 14) {

        } else if (id == 15) {

            // School-Video Album can be viewed to both teacher/parent/driver
            replaceFragment(
                    R.id.main_container,
                    new VideoAlbumFragment(),
                    VideoAlbumFragment.class.getSimpleName(),
                    "Dashboard");

        } else if (id == 16) {

            // Student-Profile can be viewed to both teacher/parent/driver
            if (activeUser == null) {
                showWarningDialog(null, getString(R.string.error_dialog_title), getString(R.string.dashboard_login_message), getString(R.string.dialog_ok));
                return;
            }
            replaceFragment(
                    R.id.main_container,
                    new StudentProfileFragment(),
                    StudentProfileFragment.class.getSimpleName(),
                    "Dashboard");

        } else if (id == 17) {

            // School-Schedule can be viewed to both teacher/parent/driver
            replaceFragment(
                    R.id.main_container,
                    new ScheduleFragment(),
                    ScheduleFragment.class.getSimpleName(),
                    "Dashboard");

        } else if (id == 18) {

            // Suggestion can be posted by both teacher/parent/driver
            if (activeUser == null) {
                showWarningDialog(null, getString(R.string.error_dialog_title), getString(R.string.dashboard_login_message), getString(R.string.dialog_ok));
                return;
            }
            SuggestionFragment fragment = SuggestionFragment.newInstance(SuggestionFragment.ARG_SUGGESTION_TYPE);
            replaceFragment(
                    R.id.main_container,
                    fragment,
                    SuggestionFragment.class.getSimpleName(),
                    "Dashboard");

        } else if (id == 19) {

            // Appreciation can be posted by teacher/parent/driver
            if (activeUser == null) {
                showWarningDialog(null, getString(R.string.error_dialog_title), getString(R.string.dashboard_login_message), getString(R.string.dialog_ok));
                return;
            }
            SuggestionFragment fragment = SuggestionFragment.newInstance(SuggestionFragment.ARG_APPRECIATION_TYPE);
            replaceFragment(
                    R.id.main_container,
                    fragment,
                    SuggestionFragment.class.getSimpleName(),
                    "Dashboard");

        } else if (id == 20) {

            // School-Profile can be viewed to both teacher/parent/driver
            replaceFragment(
                    R.id.main_container,
                    new AboutFragment(),
                    AboutFragment.class.getSimpleName(),
                    "Dashboard");

        } else if (id == 21) {
            // Attendance can be viewed by parent

            if (activeUser == null) {
                showWarningDialog(null, getString(R.string.error_dialog_title), getString(R.string.dashboard_login_message), getString(R.string.dialog_ok));
                return;
            }

            // Attendance can be viewed by parents!!
            if (activeUser.getUserType().equalsIgnoreCase("parent")) {
                replaceFragment(
                        R.id.main_container,
                        new AttendanceFragment(),
                        AttendanceFragment.class.getSimpleName(),
                        "Dashboard");
                return;
            }
            // Attendance can be posted by teacher
            if (activeUser.getUserType().equalsIgnoreCase("teacher"))
                // do something
                replaceFragment(
                        R.id.main_container,
                        new ClassFragment(),
                        ClassFragment.class.getSimpleName(),
                        "Dashboard");

        } else if (id == 22) {

            // leave application
            if (activeUser == null) {
                showWarningDialog(null, getString(R.string.error_dialog_title), getString(R.string.dashboard_login_message), getString(R.string.dialog_ok));
                return;
            }

            // Leave Application can be posted by parents/teacher!!
            replaceFragment(
                    R.id.main_container,
                    new ApplicationFragment(),
                    ApplicationFragment.class.getSimpleName(),
                    "Dashboard");

        } else if (id == 23) {

            if (activeUser == null) {
                showWarningDialog(null, getString(R.string.error_dialog_title), getString(R.string.dashboard_login_message), getString(R.string.dialog_ok));
                return;
            }

            // Class Routine can be viewed to both parent(student)
            if (activeUser.getUserType().equalsIgnoreCase("parent") || activeUser.getUserType().equalsIgnoreCase("teacher"))
                replaceFragment(
                        R.id.main_container,
                        new ClassRoutineFragment(),
                        ClassRoutineFragment.class.getSimpleName(),
                        "Dashboard");
//            else if (activeUser.getValue().equalsIgnoreCase("teacher"))
//                ;

        } else if (id == 24) {

            if (activeUser == null) {
                showWarningDialog(null, getString(R.string.error_dialog_title), getString(R.string.dashboard_login_message), getString(R.string.dialog_ok));
                return;
            }

            //  teacher/parent/driver can log out from system
            CustomDialog dialog = showProgressDialog(null, "logout...");
            UserType userType = UserType.from(activeUser.getUserType());

            userViewModel.logOutUser().observe(this, response -> {
                if (response == null) return;

                if (response.status == Status.SUCCESS) {
                    if (userType == UserType.TEACHER) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.TEACHER_NOTIFICATION);
                    }
                    showSuccessDialog(dialog, getString(R.string.logout_dialog_title), getString(R.string.logout_message), getString(R.string.dialog_ok));
//                    activeUser = response.data;
                } else if (response.status == Status.ERROR) {
                    String errorMessage = (response.code() == 1001 ? Utils.isOnline(getContext()) ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : response.message);
                    showErrorDialog(dialog, getString(R.string.error_dialog_title), errorMessage, getString(R.string.dialog_ok), null);
                }
            });

        } else if (id == 25) {

            final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }

        } else if (id == 26) {
            if (activeUser == null) {
                showWarningDialog(null, getString(R.string.error_dialog_title), getString(R.string.dashboard_login_message), getString(R.string.dialog_ok));
                return;
            }

            // Class Routine can be viewed to both parent(student)
//            if (activeUser.getUserType().equalsIgnoreCase("parent"))
            replaceFragment(
                    R.id.main_container,
                    new LiveBusListFragment(),
                    LiveBusListFragment.class.getSimpleName(),
                    "Dashboard");

        } else if (id == 27) {
            replaceFragment(
                    R.id.main_container,
                    new SettingFragment(),
                    SettingFragment.class.getSimpleName(),
                    "Dashboard");
        }
    }

    private void setActiveUser(User user) {
        this.activeUser = user;
        mDashboardItemAdapter.showOnlyLoggedInData(dashboardItems, activeUser != null);
        mDashboardItemAdapter.notifyDataSetChanged();

        if (this.activeUser == null) {
            userProfileName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            userProfileName.setText(getResources().getString(R.string.login));
            userProfileName.setOnClickListener(view -> {
                // go to the login page
//                loginDialog = LoginDialogFragment.newInstance(UserType.GENERAL);
//                loginDialog.setTargetFragment(this, LOGIN_RESPONSE_CODE);
//                loginDialog.show(getFragmentManager(), "General Login");

                Intent intent = new Intent(getContext(), LoginActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ARG_LOGIN_TYPE, UserType.GENERAL.getValue());
                intent.putExtras(bundle);
                startActivityForResult(intent, LOGIN_RESPONSE_CODE);
            });
        } else {
            Log.e("Active User", activeUser.getName());
            UserType userType = UserType.from(activeUser.getUserType());

            if (userType == UserType.PARENT) {
                userProfileName.setText(activeUser.getName());
                userProfileName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
                userProfileName.setOnClickListener(it -> popupWindow.showAsDropDown(userProfileName, 50, 0));
            } else if (userType == UserType.TEACHER) {
                userProfileName.setText(activeUser.getName());
                userProfileName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                userProfileName.setOnClickListener(null);
            }
        }

        updateRecyclerViewData(dashboardItems);
    }

    public void setLoggedInUsers(Resource<List<User>> resource) {
        if (resource == null || resource.data == null || resource.data.isEmpty()) return;

        Log.e("Users", resource.data.size() + "");
        if (resource.status == Status.SUCCESS) {
            this.loggedInUsersList = resource.data;
            if (studentListAdapter == null) {
                setUpUserViewList();
            } else {
                studentListAdapter.updateStudentList(resource.data);
            }
        }
    }

    private void setUpUserViewList() {
        popupWindow = new PopupWindow(popupStudentView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));

        studentListAdapter = new StudentListAdapter(
                getContext(),
                loggedInUsersList,
                (view, previousUser, currentUser) -> {
                    userViewModel.changeActiveUser(currentUser);
                    popupWindow.dismiss();
                },
                (imageView, user) -> {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.progressbar)
                            .error(R.drawable.ic_person_placeholder);

                    Glide.with(this)
                            .load(user.getRemoteImgUrl())
                            .apply(options)
                            .into(imageView);
                });

        studentListView.addFooterView(addNewStudentView);
        studentListView.setAdapter(studentListAdapter);
        TextView textView = addNewStudentView.findViewById(R.id.addNewStudentTV);
        textView.setText(R.string.add_student);
        textView.setTextColor(getResources().getColor(R.color.colorPrimaryText));

        addNewStudentView.setOnClickListener(view -> {
            // go to the login page
            LoginDialogFragment loginDialog = LoginDialogFragment.newInstance(UserType.PARENT);
            loginDialog.show(getFragmentManager(), "Parent Login");
            popupWindow.dismiss();
        });
    }

    private void checkAppVersion() {
        dashboardViewModel.checkNewAppVersion(new ApiListener<String>() {
            @Override
            public void onSuccess(String version) {
                if (version == null) return;
                LogUtils.errorLog("AppVersion", version);
                int latestVersion = (int) Double.parseDouble(version);
                int appVersion = BuildConfig.VERSION_CODE;

                if (appVersion < latestVersion) {
                    if (updateVersion != null)
                        mDashboardItemAdapter.addNewItem(updateVersion);
                }
            }

            @Override
            public void onFailed(String message, int code) {
                Log.e("AppVersion", message);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 0) {
                // onBackPress
            }
        }
    }
}
