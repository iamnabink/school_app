package com.swipecrafts.school.data.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonElement;
import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.model.api.AttendanceResponse;
import com.swipecrafts.school.data.model.api.UserType;
import com.swipecrafts.school.data.model.db.Album;
import com.swipecrafts.school.data.model.db.Book;
import com.swipecrafts.school.data.model.db.Bus;
import com.swipecrafts.school.data.model.db.BusDriver;
import com.swipecrafts.school.data.model.db.BusRoute;
import com.swipecrafts.school.data.model.db.Chapter;
import com.swipecrafts.school.data.model.db.DCSubject;
import com.swipecrafts.school.data.model.db.DashboardItem;
import com.swipecrafts.school.data.model.db.DressCode;
import com.swipecrafts.school.data.model.db.Event;
import com.swipecrafts.school.data.model.db.Grade;
import com.swipecrafts.school.data.model.db.Notification;
import com.swipecrafts.school.data.model.db.SchoolDetails;
import com.swipecrafts.school.data.model.db.SchoolSchedule;
import com.swipecrafts.school.data.model.db.Student;
import com.swipecrafts.school.data.model.db.Teacher;
import com.swipecrafts.school.data.model.db.User;
import com.swipecrafts.school.data.repository.SessionRepository;
import com.swipecrafts.library.imageSlider.FeaturesImage;
import com.swipecrafts.school.ui.dashboard.assignment.model.Assignment;
import com.swipecrafts.school.ui.dashboard.assignment.model.Subject;
import com.swipecrafts.school.ui.dashboard.attendance.model.StudentAttendance;
import com.swipecrafts.school.ui.dashboard.examroutine.model.ExamRoutineRes;
import com.swipecrafts.school.ui.dashboard.livebus.model.LiveBus;
import com.swipecrafts.school.ui.dashboard.message.MessageModel;
import com.swipecrafts.school.ui.dashboard.schedule.model.ClassResponse;
import com.swipecrafts.school.ui.dashboard.videos.VideoAlbum;
import com.swipecrafts.school.ui.dc.Video.VideoFeed;
import com.swipecrafts.school.ui.dc.dcfeed.ContentFeed;
import com.swipecrafts.school.ui.home.HomeModel;
import com.swipecrafts.school.ui.splash.SchoolId;
import com.swipecrafts.school.utils.ErrorsUtil;
import com.swipecrafts.school.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Madhusudan Sapkota on 2/22/2018.
 */

public class AppApiRepository {

    private final String TAG = AppApiRepository.class.getSimpleName();

    private final AppPreferencesRepository prefRepo;
    private Retrofit retrofit;
    private ApiHeader mApiHeader;

    @Inject
    public AppApiRepository(Retrofit retrofit, AppPreferencesRepository prefRepo) {
        this.retrofit = retrofit;
        this.prefRepo = prefRepo;
        updateAPIHeaders();
    }

    public void updateAPIHeaders() {
        DataManager.LoggedInMode mode = prefRepo.getLoggedMode() == 1 ? DataManager.LoggedInMode.LOGGED_IN_MODE : DataManager.LoggedInMode.LOGGED_OUT_MODE;

        if (mApiHeader != null) {
            mApiHeader.setProtectedApiHeader(prefRepo.getProtectedApiHeader());
            mApiHeader.setLoggedInMode(mode);
        } else {
            this.mApiHeader = new ApiHeader(prefRepo.getPublicApiHeader(), prefRepo.getSchoolId(), prefRepo.getProtectedApiHeader(), mode);
        }

        mApiHeader.setSchoolId(prefRepo.getSchoolId());
    }

    public ApiHeader getApiHeader() {
        return mApiHeader;
    }

    /**
     * public api
     * provides image data body
     *
     * @param url      image link to download image
     * @param listener to listen the result image {@link ResponseBody}
     */
    public void getImage(String url, ApiListener<ResponseBody> listener) {
        Call<ResponseBody> call = retrofit.create(RestApi.class).getImage(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if (response.isSuccessful()) listener.onSuccess(response.body());
                else listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                listener.onFailed(t.getMessage(), 1001);
            }
        });
    }

    /**
     * public api
     * provides pdf data body
     *
     * @param url      pdf link to download
     * @param listener to listen the result pdf {@link ResponseBody}
     */
    public void getPDF(String url, ApiListener<ResponseBody> listener) {
        Call<ResponseBody> call = retrofit.create(RestApi.class).getPDF(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if (response.isSuccessful()) listener.onSuccess(response.body());
                else listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                listener.onFailed(t.getMessage(), 1001);
            }
        });
    }

    /**
     * public api
     * provides File data body
     *
     * @param url      file link to download
     * @param listener to listen the result File {@link ResponseBody}
     */
    public void getFile(String url, ApiListener<ResponseBody> listener) {
        Call<ResponseBody> call = retrofit.create(RestApi.class).getPDF(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if (response.isSuccessful()) listener.onSuccess(response.body());
                else listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                listener.onFailed(t.getMessage(), 1001);
            }
        });
    }

    /**
     * public api
     * requires apiKey
     * provides school id
     */
    public LiveData<ApiResponse<String>> getSchoolId() {
        MutableLiveData<ApiResponse<String>> result = new MutableLiveData<>();
        result.setValue(ApiResponse.loading(null));

        Call<List<SchoolId>> call = retrofit.create(RestApi.class).getSchoolId(mApiHeader.getPublicApiHeader().getApiKey(), "");

        call.enqueue(new Callback<List<SchoolId>>() {
            @Override
            public void onResponse(@NonNull Call<List<SchoolId>> call, @NonNull Response<List<SchoolId>> response) {
                if (response.isSuccessful()) {
                    Log.e("Success", response.body().toString());
                    result.setValue(ApiResponse.success(response.body().get(0).getSchoolId()));
                } else {
                    Log.e("Error", response.code() + " error " + response.errorBody().toString());
                    result.setValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SchoolId>> call, @NonNull Throwable t) {
                Log.e("error", t.getMessage());
                result.setValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * public api
     * requires ApiKey {@link ApiHeader#getPublicApiHeader()}
     */
    public MediatorLiveData<ApiResponse<List<DashboardItem>>> getMenuItems() {
        MediatorLiveData<ApiResponse<List<DashboardItem>>> result = new MediatorLiveData<>();
        result.setValue(ApiResponse.loading(null));

        Call<List<DashboardItem>> call = retrofit
                .create(RestApi.class)
                .getMenuItems(mApiHeader.getPublicApiHeader().getApiKey(), 0);

        call.enqueue(new Callback<List<DashboardItem>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<DashboardItem>> call,
                    @NonNull Response<List<DashboardItem>> response) {
                if (response.isSuccessful()) {
                    result.setValue(ApiResponse.success(response.body()));
                } else {
                    result.setValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DashboardItem>> call, @NonNull Throwable t) {
                result.setValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * public api
     * requires ApiKey {@link ApiHeader#getPublicApiHeader()}
     */
    public MutableLiveData<ApiResponse<List<Event>>> getCalendarEvents() {
        MutableLiveData<ApiResponse<List<Event>>> result = new MutableLiveData<>();
        result.setValue(ApiResponse.loading(null));

        Call<List<Event>> call = retrofit.create(RestApi.class).getEvents(mApiHeader.getPublicApiHeader().getApiKey(), mApiHeader.getSchoolId());

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(@NonNull Call<List<Event>> call, @NonNull Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    result.setValue(ApiResponse.success(response.body()));
                } else {
                    result.setValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Event>> call, @NonNull Throwable t) {
                result.setValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * public api
     * requires ApiKey {@link ApiHeader#getPublicApiHeader()}
     */
    public MutableLiveData<ApiResponse<List<Grade>>> getGradeList() {
        MutableLiveData<ApiResponse<List<Grade>>> result = new MutableLiveData<>();
        result.setValue(ApiResponse.loading(null));

        Call<List<Grade>> call = retrofit.create(RestApi.class).getGradeList(mApiHeader.getPublicApiHeader().getApiKey(), 0);

        call.enqueue(new Callback<List<Grade>>() {
            @Override
            public void onResponse(@NonNull Call<List<Grade>> call, @NonNull Response<List<Grade>> response) {
                if (response.isSuccessful()) {
                    result.setValue(ApiResponse.success(response.body()));
                } else {
                    result.setValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Grade>> call, @NonNull Throwable t) {
                result.setValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * public api
     * requires ApiKey {@link ApiHeader#getPublicApiHeader()}
     *
     * @param gradeId  of the needed subjects
     * @param listener to listen the result list of {@link DCSubject}
     */
    public void getSubjectList(String gradeId, ApiListener<List<DCSubject>> listener) {
        Call<List<DCSubject>> call = retrofit.create(RestApi.class).getSubjectList(mApiHeader.getPublicApiHeader().getApiKey(), gradeId);

        call.enqueue(new Callback<List<DCSubject>>() {
            @Override
            public void onResponse(@NonNull Call<List<DCSubject>> call, @NonNull Response<List<DCSubject>> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DCSubject>> call, @NonNull Throwable t) {
                listener.onFailed(t.getMessage(), 1001);
            }
        });
    }

    /**
     * public api
     * requires ApiKey {@link ApiHeader#getPublicApiHeader()}
     *
     * @param subjectId of the needed chapters
     * @param listener  to listen the result list of {@link Chapter}
     */
    public void getChapterList(String subjectId, ApiListener<List<Chapter>> listener) {
        Call<List<Chapter>> call = retrofit.create(RestApi.class).getChapterList(mApiHeader.getPublicApiHeader().getApiKey(), subjectId);

        call.enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(@NonNull Call<List<Chapter>> call, @NonNull Response<List<Chapter>> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Chapter>> call, @NonNull Throwable t) {
                listener.onFailed(t.getMessage(), 1001);
            }
        });
    }

    /**
     * public api
     * requires ApiKey for {@link UserType#PARENT} {@link ApiHeader#getPublicApiHeader()}
     * requires ApiKey for {@link UserType#TEACHER} {@link ApiHeader#getProtectedApiHeader()}
     *
     * @param userType
     */
    public LiveData<ApiResponse<List<Notification>>> getNotifications(UserType userType) {
        final MutableLiveData<ApiResponse<List<Notification>>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        Call<List<Notification>> call;

        if (userType == UserType.PARENT) {
            call = retrofit.create(RestApi.class).getNotifications(mApiHeader.getPublicApiHeader().getApiKey(), mApiHeader.getSchoolId());
        } else {
            String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
            String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
            String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
            String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
            long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

            call = retrofit.create(RestApi.class).getNotifications(
                    apiKey,
                    sshKey,
                    sesKey,
                    sesNId,
                    String.valueOf(sesUId),
                    userType.getValue(),
                    mApiHeader.getSchoolId(),
                    String.valueOf(sesUId),
                    prefRepo.getUserType());
        }

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(@NonNull Call<List<Notification>> call, @NonNull Response<List<Notification>> response) {
                if (response.isSuccessful()) {
                    Log.e("Notification data", response.body().toString());
                    result.setValue(ApiResponse.success(response.body()));
                } else {
                    Log.e("Notification ErrorC", response.code() + "");
                    result.setValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), response.body()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Notification>> call, @NonNull Throwable t) {
                Log.e("Notification Error", t.getMessage());
                result.setValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * public api
     * requires ApiKey {@link ApiHeader#getPublicApiHeader()}
     *
     * @param email    email of the user
     * @param password password of the user
     * @param type     user type {@link UserType}
     */
    public MutableLiveData<ApiResponse<User>> loginUser(String email, String password, UserType type, String fcmKey) {
        MutableLiveData<ApiResponse<User>> result = new MutableLiveData<>();

        Call<List<User>> call = retrofit.create(RestApi.class)
                .loginStudent(
                        mApiHeader.getPublicApiHeader().getApiKey(),
                        mApiHeader.getSchoolId(),
                        email,
                        password,
                        type.getValue(),
                        fcmKey);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                Log.e(TAG, response.raw().networkResponse().toString());
                Log.e(TAG, response.headers().toString());
                if (response.isSuccessful()) {
                    Log.e(TAG, response.body().toString());
                    if (response.body() == null || response.body().isEmpty()) {
                        result.postValue(ApiResponse.error(404, "You are not authenticated!! \n try again", null));
                    } else {
                        result.postValue(ApiResponse.success(response.body().get(0)));
                    }
                } else {
                    Log.e(TAG, response.errorBody().toString());
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                LogUtils.errorLog("LoginE", t.getMessage());
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));

            }
        });

        return result;
    }

    /**
     * public api
     * requires ApiKey {@link ApiHeader#getPublicApiHeader()}
     */
    public MediatorLiveData<ApiResponse<List<FeaturesImage>>> getFeaturesImages() {
        MediatorLiveData<ApiResponse<List<FeaturesImage>>> result = new MediatorLiveData<>();
        List<FeaturesImage> items = new ArrayList<>();
        result.setValue(ApiResponse.loading(items));

        Call<List<FeaturesImage>> call = retrofit
                .create(RestApi.class)
                .getFeatureImages(
                        mApiHeader.getPublicApiHeader().getApiKey(),
                        mApiHeader.getSchoolId()
                );

        call.enqueue(new Callback<List<FeaturesImage>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<FeaturesImage>> call,
                    @NonNull Response<List<FeaturesImage>> response
            ) {
                if (response.isSuccessful()) {
                    result.setValue(ApiResponse.success(response.body()));
                } else {
                    result.setValue(
                            ApiResponse.error(
                                    response.code(),
                                    ErrorsUtil.getMessage(response.code()),
                                    null)
                    );
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<FeaturesImage>> call,
                    @NonNull Throwable t
            ) {
                result.setValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * public api
     * requires ApiKey {@link ApiHeader#getPublicApiHeader()}
     *
     * @param listener to listen the result list of {@link SchoolSchedule}
     */
    public void getSchoolSchedule(ApiListener<List<SchoolSchedule>> listener) {
        Call<List<SchoolSchedule>> call = retrofit.create(RestApi.class).getSchoolSchedule(mApiHeader.getPublicApiHeader().getApiKey(), mApiHeader.getSchoolId());

        call.enqueue(new Callback<List<SchoolSchedule>>() {
            @Override
            public void onResponse(@NonNull Call<List<SchoolSchedule>> call, @NonNull Response<List<SchoolSchedule>> response) {
                if (response.isSuccessful()) {
                    Log.e("data", response.body().size() + "");
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailed("School Schedule could not found. \n Please contact to your school!!", 404);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SchoolSchedule>> call, @NonNull Throwable t) {
                listener.onFailed(t.getMessage(), 1001);
            }
        });
    }

    /**
     * public api
     * requires ApiKey {@link ApiHeader#getPublicApiHeader()}
     *
     * @param listener to listen the result list of {@link Bus}
     */
    public void getBusList(ApiListener<List<Bus>> listener) {
        String apiKey = null, sshKey = null, sesKey = null, sesNId = null;
        Long sesUId = null;
        if (mApiHeader.getLoggedInMode() == DataManager.LoggedInMode.LOGGED_IN_MODE) {
             apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
             sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
             sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
             sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
             sesUId = mApiHeader.getProtectedApiHeader().getSESUId();
        }
        Call<List<Bus>> call = retrofit.create(RestApi.class).getBusList(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                sesUId == null ? null : String.valueOf(sesUId),
                sesUId == null ? null : String.valueOf(sesUId),
                mApiHeader.getSchoolId());

        call.enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(@NonNull Call<List<Bus>> call, @NonNull Response<List<Bus>> response) {
                if (response.isSuccessful()) listener.onSuccess(response.body());
                else
                    listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
            }

            @Override
            public void onFailure(@NonNull Call<List<Bus>> call, @NonNull Throwable t) {
                listener.onFailed(t.getMessage(), 1001);
            }
        });
    }

    /**
     * public api
     * requires ApiKey {@link ApiHeader#getPublicApiHeader()}
     *
     * @param busId    to get the particular bus route
     * @param listener to listen the result of {@link BusRoute}
     */
    public void getBusRouteList(String busId, ApiListener<List<BusRoute>> listener) {
        String apiKey = null, sshKey = null, sesKey = null, sesNId = null;
        Long sesUId = null;
        if (mApiHeader.getLoggedInMode() == DataManager.LoggedInMode.LOGGED_IN_MODE) {
            apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
            sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
            sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
            sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
            sesUId = mApiHeader.getProtectedApiHeader().getSESUId();
        }

        Call<List<BusRoute>> call = retrofit.create(RestApi.class).getBusRouteList(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                sesUId == null ? null : String.valueOf(sesUId),
                sesUId == null ? null : String.valueOf(sesUId),
                mApiHeader.getSchoolId(),
                busId);

        call.enqueue(new Callback<List<BusRoute>>() {
            @Override
            public void onResponse(@NonNull Call<List<BusRoute>> call, @NonNull Response<List<BusRoute>> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BusRoute>> call, @NonNull Throwable t) {
                listener.onFailed(t.getMessage(), 1001);
            }
        });
    }

    /**
     * public api
     * requires ApiKey {@link ApiHeader#getPublicApiHeader()}
     *
     * @param listener to listen the result of {@link DressCode}
     */
    public void getDressCode(ApiListener<List<DressCode>> listener) {
        Call<List<DressCode>> call = retrofit.create(RestApi.class).getDressCode(mApiHeader.getPublicApiHeader().getApiKey(), mApiHeader.getSchoolId());

        call.enqueue(new Callback<List<DressCode>>() {
            @Override
            public void onResponse(@NonNull Call<List<DressCode>> call, @NonNull Response<List<DressCode>> response) {
                if (response.isSuccessful()) listener.onSuccess(response.body());
                else
                    listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
            }

            @Override
            public void onFailure(@NonNull Call<List<DressCode>> call, @NonNull Throwable t) {
                listener.onFailed(t.getMessage(), 1001);
            }
        });
    }

    /**
     * public api
     * requires ApiKey {@link ApiHeader#getPublicApiHeader()}
     *
     * @param listener to listen the result of {@link Album}
     */
    public void getImageAlbumList(ApiListener<List<Album>> listener) {
        Call<List<Album>> call = retrofit.create(RestApi.class).getAlbumDetails(mApiHeader.getPublicApiHeader().getApiKey(), mApiHeader.getSchoolId());

        call.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(@NonNull Call<List<Album>> call, @NonNull Response<List<Album>> response) {
                if (response.isSuccessful()) listener.onSuccess(response.body());
                else
                    listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
            }

            @Override
            public void onFailure(@NonNull Call<List<Album>> call, @NonNull Throwable t) {
                listener.onFailed(t.getMessage(), 1001);
            }
        });

    }


    /**
     * public api
     * requires ApiKey {@link ApiHeader#getPublicApiHeader()}
     *
     * @param listener to listen the result of {@link Album}
     */
    public void getVideoAlbums(ApiListener<List<VideoAlbum>> listener) {
        Call<List<VideoAlbum>> call = retrofit.create(RestApi.class).getVideoAlbums(mApiHeader.getPublicApiHeader().getApiKey(), mApiHeader.getSchoolId());

        call.enqueue(new Callback<List<VideoAlbum>>() {
            @Override
            public void onResponse(@NonNull Call<List<VideoAlbum>> call, @NonNull Response<List<VideoAlbum>> response) {
                if (response.isSuccessful()) listener.onSuccess(response.body());
                else listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
            }

            @Override
            public void onFailure(@NonNull Call<List<VideoAlbum>> call, @NonNull Throwable t) {
                listener.onFailed(t.getMessage(), 1001);
            }
        });

    }

    /**
     * public api
     * requires ApiKey {@link ApiHeader#getPublicApiHeader()}
     */
    public MutableLiveData<ApiResponse<SchoolDetails>> getSchoolDetails() {
        MutableLiveData<ApiResponse<SchoolDetails>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        long schoolId = Long.parseLong(prefRepo.getSchoolId());
        Call<List<SchoolDetails>> call = retrofit.create(RestApi.class).getSchoolDetails(mApiHeader.getPublicApiHeader().getApiKey(), mApiHeader.getSchoolId());

        call.enqueue(new Callback<List<SchoolDetails>>() {
            @Override
            public void onResponse(@NonNull Call<List<SchoolDetails>> call, @NonNull Response<List<SchoolDetails>> response) {
                if (response.isSuccessful()) {
                    response.body().get(0).setId(schoolId);
                    result.postValue(ApiResponse.success(response.body().get(0)));
                } else {
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SchoolDetails>> call, @NonNull Throwable t) {
                LogUtils.errorLog("Api call", "School Details Download Failure : " + t.getMessage());
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * private api
     * requires ApiKey {@link ApiHeader#getProtectedApiHeader()} ()}
     *
     * @param listener to listen the result of {@link Book}
     */
    public void getBooks(ApiListener<List<Book>> listener) {
        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<List<Book>> call = retrofit.create(RestApi.class).getBooks(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                mApiHeader.getSchoolId(),
                String.valueOf(sesUId),
                prefRepo.getUserType()
        );

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call, @NonNull Response<List<Book>> response) {

                if (response.isSuccessful()) {
                    if (response.body().isEmpty())
                        listener.onFailed("Book Data could not found. \n Please contact to your school!!", 404);
                    else listener.onSuccess(response.body());
                } else {
                    listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                LogUtils.errorLog("BookError", t.getMessage());
                listener.onFailed(t.getMessage(), 1001);
            }
        });
    }

    /**
     * private api
     * requires ApiKey {@link ApiHeader#getProtectedApiHeader()} ()}
     * returns {@link LiveData<ApiResponse> }
     */
    public LiveData<ApiResponse<Student>> getStudentDetails() {
        MutableLiveData<ApiResponse<Student>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<List<Student>> call = retrofit.create(RestApi.class).getStudentDetails(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                mApiHeader.getSchoolId(),
                String.valueOf(sesUId),
                prefRepo.getUserType()
        );

        call.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(@NonNull Call<List<Student>> call, @NonNull Response<List<Student>> response) {

                Log.e("Student", "Code " + response.code());
                if (response.isSuccessful()) {
                    response.body().get(0).setUserId(sesUId);
                    result.postValue(ApiResponse.success(response.body().get(0)));
                } else {
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Student>> call, @NonNull Throwable t) {
                Log.e("getStudentDetails()", t.getMessage(), t);
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });


        return result;
    }

    public MutableLiveData<ApiResponse<List<ContentFeed>>> getContentFeeds(long gradeId, long subjectId, long chapterId) {
        MutableLiveData<ApiResponse<List<ContentFeed>>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<List<ContentFeed>> call = retrofit.create(RestApi.class).getContentFeeds(
                apiKey,
                gradeId,
                subjectId,
                chapterId);

        call.enqueue(new Callback<List<ContentFeed>>() {
            @Override
            public void onResponse(@NonNull Call<List<ContentFeed>> call, @NonNull Response<List<ContentFeed>> response) {
                Log.e("DC Feed", "Code " + response.code());
                if (response.isSuccessful()) {
                    result.postValue(ApiResponse.success(response.body()));
                } else {
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ContentFeed>> call, @NonNull Throwable t) {
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    public MutableLiveData<ApiResponse<List<VideoFeed>>> getVideoFeeds(long gradeId, long subjectId) {
        MutableLiveData<ApiResponse<List<VideoFeed>>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();
//        String title, String content,String page_number;

        Call<List<VideoFeed>> call = retrofit.create(RestApi.class).getVideoFeeds(
                apiKey,
                gradeId,
                subjectId);

        call.enqueue(new Callback<List<VideoFeed>>() {
            @Override
            public void onResponse(@NonNull Call<List<VideoFeed>> call, @NonNull Response<List<VideoFeed>> response) {
                Log.e("DC Feed", "Code " + response.code());
                if (response.isSuccessful()) {
                    result.postValue(ApiResponse.success(response.body()));
                } else {
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<VideoFeed>> call, @NonNull Throwable t) {
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<ApiResponse<Boolean>> logOutUser() {
        MutableLiveData<ApiResponse<Boolean>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();
        String userType = prefRepo.getUserType();

        Log.e("UserDetails ",
                "\n API: " + apiKey
                        + " \n SSH key: " + sshKey
                        + " \n SES key: " + sesKey
                        + " \n SES NID key: " + sesNId
                        + " \n SES UID key: " + sesUId
                        + " \n UserTYpe key: " + userType);


        Call<JsonElement> call = retrofit.create(RestApi.class).logOut(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                String.valueOf(sesUId),
                userType);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    result.postValue(ApiResponse.success(true));
                } else {
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                result.postValue(ApiResponse.error(1001, t.getMessage(), false));
            }
        });

        return result;
    }

    public void getClassRoutine(ApiListener<List<ClassResponse>> listener) {

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<List<ClassResponse>> call = retrofit.create(RestApi.class).getClassRoutine(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                mApiHeader.getSchoolId(),

                String.valueOf(sesUId),
                prefRepo.getUserType());

        Log.e(TAG, prefRepo.getUserType());

        call.enqueue(new Callback<List<ClassResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ClassResponse>> call, @NonNull Response<List<ClassResponse>> response) {
                Log.e(TAG, response.headers().toString());

                if (response.isSuccessful()) {
                    Log.e(TAG, response.body().toString());
                    Log.e(TAG, "success!! working");
                    listener.onSuccess(response.body());
                } else {
                    Log.e(TAG, response.errorBody().toString());
                    Log.e(TAG, "failed!! not working");
                    listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ClassResponse>> call, @NonNull Throwable t) {
                Log.e("Routine", "failed!! not working " + t.getMessage());
                listener.onFailed(t.getMessage(), 1001);
            }
        });
    }

    public void submitSuggestion(String title, String body, int isAnonymous, ApiListener<String> listener) {

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<JsonElement> call = retrofit.create(RestApi.class).postSuggestion(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),

                mApiHeader.getSchoolId(),
                String.valueOf(sesUId),
                prefRepo.getUserType(),
                title,
                body,
                isAnonymous);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.isSuccessful()) listener.onSuccess("Success!!");
                else listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                listener.onFailed(t.getMessage(), 1001);
            }
        });
    }

    public void submitAppreciation(String title, String body, int isAnonymous, ApiListener<String> listener) {

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<JsonElement> call = retrofit.create(RestApi.class).postAppreciation(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),

                mApiHeader.getSchoolId(),
                String.valueOf(sesUId),
                prefRepo.getUserType(),
                title,
                body,
                isAnonymous);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.isSuccessful()) listener.onSuccess("Success!!");
                else listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                listener.onFailed(t.getMessage(), 1001);
            }
        });
    }

    public LiveData<ApiResponse<List<ClassResponse>>> getClassesAndSections() {
        MutableLiveData<ApiResponse<List<ClassResponse>>> result = new MediatorLiveData<>();
        List<ClassResponse> items = new ArrayList<>();
        result.setValue(ApiResponse.loading(items));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<List<ClassResponse>> call = retrofit.create(RestApi.class).getClassSections(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                prefRepo.getUserType(),

                mApiHeader.getSchoolId(),
                String.valueOf(sesUId)
        );

        call.enqueue(new Callback<List<ClassResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ClassResponse>> call, @NonNull Response<List<ClassResponse>> response) {
                if (response.isSuccessful()) result.setValue(ApiResponse.success(response.body()));
                else
                    result.setValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
            }

            @Override
            public void onFailure(@NonNull Call<List<ClassResponse>> call, @NonNull Throwable t) {
                LogUtils.errorLog("ClassNSubjectE", t.getMessage());
                result.setValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<ApiResponse<AttendanceResponse>> getStudentAttendance(Long classId, Long sectionId) {
        MutableLiveData<ApiResponse<AttendanceResponse>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<List<AttendanceResponse>> call = retrofit.create(RestApi.class).getAttendanceData(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                prefRepo.getUserType(),

                mApiHeader.getSchoolId(),
                String.valueOf(sesUId),
                String.valueOf(classId),
                String.valueOf(sectionId)
        );

        call.enqueue(new Callback<List<AttendanceResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<AttendanceResponse>> call, @NonNull Response<List<AttendanceResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    if (response.body().get(0).getStatus() == 0) {
                        List<StudentAttendance> students = response.body().get(0).getAttendanceStudents();
                        for (StudentAttendance sa : students) {
                            sa.setSchoolId(mApiHeader.getSchoolId());
                            sa.setmClassId(classId);
                            sa.setmSectionId(sectionId);
                            sa.setmAttendantId(sesUId);
                        }
                    }
                    result.postValue(ApiResponse.success(response.body().get(0)));
                } else {
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AttendanceResponse>> call, @NonNull Throwable t) {
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<ApiResponse<List<StudentAttendance>>> getStudentsByClassAndOrSection(Long classId, Long sectionId) {
        MutableLiveData<ApiResponse<List<StudentAttendance>>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<List<StudentAttendance>> call = null;

        if (sectionId == null || sectionId == -1) {
            call = retrofit.create(RestApi.class).getStudentsByClass(
                    apiKey,
                    sshKey,
                    sesKey,
                    sesNId,
                    String.valueOf(sesUId),
                    prefRepo.getUserType(),

                    mApiHeader.getSchoolId(),
                    String.valueOf(sesUId),
                    String.valueOf(classId)
            );

        } else {
            call = retrofit.create(RestApi.class).getStudentsByClassSections(
                    apiKey,
                    sshKey,
                    sesKey,
                    sesNId,
                    String.valueOf(sesUId),
                    prefRepo.getUserType(),

                    mApiHeader.getSchoolId(),
                    String.valueOf(sesUId),
                    String.valueOf(classId),
                    String.valueOf(sectionId)
            );
        }


        call.enqueue(new Callback<List<StudentAttendance>>() {
            @Override
            public void onResponse(@NonNull Call<List<StudentAttendance>> call, @NonNull Response<List<StudentAttendance>> response) {
                if (response.isSuccessful()) {
                    List<StudentAttendance> data = new ArrayList<>();
                    if (response.body() != null) data.addAll(response.body());

                    for (StudentAttendance sa : data) {
                        sa.setSchoolId(mApiHeader.getSchoolId());
                        sa.setmClassId(classId);
                        sa.setmSectionId(sectionId);
                        sa.setmAttendantId(sesUId);
                    }
                    result.postValue(ApiResponse.success(data));
                } else
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
            }

            @Override
            public void onFailure(@NonNull Call<List<StudentAttendance>> call, @NonNull Throwable t) {
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<ApiResponse<Boolean>> submitAttendance(List<StudentAttendance> studentAttendanceData) {
        MutableLiveData<ApiResponse<Boolean>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Log.e(TAG, studentAttendanceData.toString());

        RequestBody schoolIDRB = RequestBody.create(MediaType.parse("text/plain"), mApiHeader.getSchoolId());
        RequestBody userIDRB = RequestBody.create(MediaType.parse("text/plain"), sesUId + "");
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), studentAttendanceData.toString());


        Call<JsonElement> call = retrofit.create(RestApi.class).postStudentAttendance(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                prefRepo.getUserType(),

                schoolIDRB,
                userIDRB,
                body
        );

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                Log.e(TAG, response.headers().toString());
                if (response.isSuccessful()) {
                    Log.e(TAG, response.body().toString());
                    result.postValue(ApiResponse.success(true));
                } else {
                    Log.e(TAG, ErrorsUtil.getMessage(response.code()));
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), false));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
                result.postValue(ApiResponse.error(1001, t.getMessage(), false));
            }
        });
        return result;
    }

    public void submitLeaveApplication(String title, String subject, String body, ApiListener<String> listener) {
        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<JsonElement> call = retrofit.create(RestApi.class).postLeaveApplication(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                prefRepo.getUserType(),

                mApiHeader.getSchoolId(),
                String.valueOf(sesUId),

                title,
                subject,
                body);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                Log.e(TAG, response.headers().toString());
                if (response.isSuccessful()) {
                    Log.e(TAG, response.body().toString());
                    listener.onSuccess("Success!!");
                } else {
                    listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
                listener.onFailed(t.getMessage(), 1001);
            }
        });
    }

    public LiveData<ApiResponse<List<StudentAttendance>>> getMyAttendance() {
        MutableLiveData<ApiResponse<List<StudentAttendance>>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<List<StudentAttendance>> call = retrofit.create(RestApi.class).getMyAttendance(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                prefRepo.getUserType(),

                mApiHeader.getSchoolId(),
                String.valueOf(sesUId)
        );

        call.enqueue(new Callback<List<StudentAttendance>>() {
            @Override
            public void onResponse(@NonNull Call<List<StudentAttendance>> call, @NonNull Response<List<StudentAttendance>> response) {
                if (response.isSuccessful()) {
                    result.postValue(ApiResponse.success(response.body()));
                } else {
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StudentAttendance>> call, @NonNull Throwable t) {
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<ApiResponse<List<MessageModel>>> getInboxMessage() {
        MutableLiveData<ApiResponse<List<MessageModel>>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));


        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();
        String userType = prefRepo.getUserType();

        Call<List<MessageModel>> call;

        if (userType.equalsIgnoreCase("parent")) {

            call = retrofit.create(RestApi.class).getStudentInboxMessage(
                    apiKey,
                    sshKey,
                    sesKey,
                    sesNId,
                    String.valueOf(sesUId),
                    prefRepo.getUserType(),

                    mApiHeader.getSchoolId(),
                    String.valueOf(sesUId)
            );
        } else {
            call = retrofit.create(RestApi.class).getTeacherInboxMessage(
                    apiKey,
                    sshKey,
                    sesKey,
                    sesNId,
                    String.valueOf(sesUId),
                    prefRepo.getUserType(),

                    mApiHeader.getSchoolId(),
                    String.valueOf(sesUId)
            );
        }

        call.enqueue(new Callback<List<MessageModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<MessageModel>> call, @NonNull Response<List<MessageModel>> response) {

                if (response.isSuccessful()) {
                    result.postValue(ApiResponse.success(response.body()));
                } else {
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MessageModel>> call, @NonNull Throwable t) {
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<ApiResponse<List<MessageModel>>> getSentMessage() {
        MutableLiveData<ApiResponse<List<MessageModel>>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();
        String userType = prefRepo.getUserType();

        Call<List<MessageModel>> call;


        if (userType.equalsIgnoreCase("parent")) {

            call = retrofit.create(RestApi.class).getStudentSentMessage(
                    apiKey,
                    sshKey,
                    sesKey,
                    sesNId,
                    String.valueOf(sesUId),
                    prefRepo.getUserType(),

                    mApiHeader.getSchoolId(),
                    String.valueOf(sesUId)
            );
        } else {
            call = retrofit.create(RestApi.class).getTeacherSentMessage(
                    apiKey,
                    sshKey,
                    sesKey,
                    sesNId,
                    String.valueOf(sesUId),
                    prefRepo.getUserType(),

                    mApiHeader.getSchoolId(),
                    String.valueOf(sesUId)
            );
        }

        call.enqueue(new Callback<List<MessageModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<MessageModel>> call, @NonNull Response<List<MessageModel>> response) {
                if (response.isSuccessful()) {
                    result.postValue(ApiResponse.success(response.body()));
                } else {
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<ApiResponse<String>> postMessage(String message, long toUser) {
        MutableLiveData<ApiResponse<String>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();
        String userType = prefRepo.getUserType();

        Call<JsonElement> call;


        if (userType.equalsIgnoreCase("parent")) {

            call = retrofit.create(RestApi.class).postMessageFromStudent(
                    apiKey,
                    sshKey,
                    sesKey,
                    sesNId,
                    String.valueOf(sesUId),
                    prefRepo.getUserType(),

                    mApiHeader.getSchoolId(),
                    String.valueOf(sesUId),
                    message,
                    toUser
            );
        } else {
            call = retrofit.create(RestApi.class).postMessageFromTeacher(
                    apiKey,
                    sshKey,
                    sesKey,
                    sesNId,
                    String.valueOf(sesUId),
                    prefRepo.getUserType(),

                    mApiHeader.getSchoolId(),
                    String.valueOf(sesUId),
                    message,
                    toUser
            );
        }

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    result.postValue(ApiResponse.success(response.body().toString()));
                } else {
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<ApiResponse<List<StudentAttendance>>> getTeacherList() {
        MutableLiveData<ApiResponse<List<StudentAttendance>>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<List<StudentAttendance>> call = retrofit.create(RestApi.class).getTeachersList(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                prefRepo.getUserType(),

                mApiHeader.getSchoolId(),
                String.valueOf(sesUId)
        );

        call.enqueue(new Callback<List<StudentAttendance>>() {
            @Override
            public void onResponse(Call<List<StudentAttendance>> call, Response<List<StudentAttendance>> response) {
                if (response.isSuccessful()) {
                    result.postValue(ApiResponse.success(response.body()));
                } else {
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(Call<List<StudentAttendance>> call, Throwable t) {
                LogUtils.errorLog("AttendanceDataF", t.getMessage());
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<ApiResponse<List<Subject>>> getSchoolSubjectList() {
        MutableLiveData<ApiResponse<List<Subject>>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<List<com.swipecrafts.school.ui.dashboard.assignment.model.Subject>> call = retrofit.create(RestApi.class).getSchoolSubjectsList(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                prefRepo.getUserType(),

                mApiHeader.getSchoolId(),
                String.valueOf(sesUId)
        );

        call.enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(Call<List<com.swipecrafts.school.ui.dashboard.assignment.model.Subject>> call, Response<List<com.swipecrafts.school.ui.dashboard.assignment.model.Subject>> response) {
                if (response.isSuccessful()) {
                    result.postValue(ApiResponse.success(response.body()));
                } else {
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(Call<List<com.swipecrafts.school.ui.dashboard.assignment.model.Subject>> call, Throwable t) {
                LogUtils.errorLog("SubjectE", t.getMessage());
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));

            }
        });

        return result;
    }

    public LiveData<ApiResponse<String>> postAssignment(String title, int classId, int subjectId, File selectedFile) {
        MutableLiveData<ApiResponse<String>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        RequestBody schoolId = RequestBody.create(MediaType.parse("text/plain"), mApiHeader.getSchoolId());
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sesUId));

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), selectedFile);
        Log.e("SelectedFile", selectedFile.getName());
        MultipartBody.Part assignmentDoc = MultipartBody.Part.createFormData("afile", selectedFile.getName(), requestFile);

        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody classBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(classId));
        RequestBody subjectBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(subjectId));
        RequestBody sectionBody = RequestBody.create(MediaType.parse("text/plain"), "");

        Call<JsonElement> call = retrofit.create(RestApi.class).postAssignment(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                prefRepo.getUserType(),

                schoolId,
                userId,
                titleBody,
                assignmentDoc,
                classBody,
                sectionBody,
                subjectBody

        );


        LogUtils.errorLog("Assignment", call.toString());
        Log.e("Assignment", "Uploading");
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                Log.e("Assignment", response.headers().toString() + " code " + response.code());
                if (response.isSuccessful()) {
                    Log.e("Assignment", response.body().toString());
                    result.postValue(ApiResponse.success("assignment successfully posted!!"));
                    try {
                        selectedFile.delete();
                    } catch (SecurityException ignore) {
                    }
                } else {
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Log.e("AssignmentONF", t.getMessage());
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<ApiResponse<List<Assignment>>> getAssignmentList() {
        MutableLiveData<ApiResponse<List<Assignment>>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<List<Assignment>> call = retrofit.create(RestApi.class).getAssignment(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                prefRepo.getUserType(),

                mApiHeader.getSchoolId(),
                String.valueOf(sesUId)
        );

        call.enqueue(new Callback<List<Assignment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Assignment>> call, @NonNull Response<List<Assignment>> response) {
                if (response.isSuccessful()) {
                    result.postValue(ApiResponse.success(response.body()));
                } else {
                    result.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Assignment>> call, @NonNull Throwable t) {
                LogUtils.errorLog("Assignment", t.getMessage());
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    public void getExamRoutine(ApiListener<List<ExamRoutineRes>> listener) {
        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<List<ExamRoutineRes>> call = retrofit.create(RestApi.class).getExamRoutine(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                prefRepo.getUserType(),

                mApiHeader.getSchoolId(),
                String.valueOf(sesUId)
        );

        call.enqueue(new Callback<List<ExamRoutineRes>>() {
            @Override
            public void onResponse(@NonNull Call<List<ExamRoutineRes>> call, @NonNull Response<List<ExamRoutineRes>> response) {
                if (response.isSuccessful()) listener.onSuccess(response.body());
                else listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
            }

            @Override
            public void onFailure(@NonNull Call<List<ExamRoutineRes>> call, @NonNull Throwable t) {
                listener.onFailed(t.getMessage(), 1001);
            }
        });
    }

    public void checkAppVersion(ApiListener<String> listener) {

        Call<List<HashMap<String, String>>> call = retrofit.create(RestApi.class).checkAppVersion(mApiHeader.getPublicApiHeader().getApiKey(), mApiHeader.getSchoolId());

        call.enqueue(new Callback<List<HashMap<String, String>>>() {
            @Override
            public void onResponse(@NonNull Call<List<HashMap<String, String>>> call, @NonNull Response<List<HashMap<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null)
                    listener.onSuccess(response.body().get(0).get("app_version"));
                else listener.onFailed(ErrorsUtil.getMessage(response.code()), response.code());
            }

            @Override
            public void onFailure(@NonNull Call<List<HashMap<String, String>>> call, @NonNull Throwable t) {
                listener.onFailed(t.getMessage(), 1001);
            }
        });
    }

    public LiveData<ApiResponse<List<Teacher>>> getTeacherContactList() {
        MutableLiveData<ApiResponse<List<Teacher>>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();


        Call<List<Teacher>> call = retrofit.create(RestApi.class).getTeacherContactList(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                prefRepo.getUserType(),

                mApiHeader.getSchoolId(),
                String.valueOf(sesUId)
        );

        call.enqueue(new Callback<List<Teacher>>() {
            @Override
            public void onResponse(@NonNull Call<List<Teacher>> call, @NonNull Response<List<Teacher>> response) {
                if (response.isSuccessful()) {
                    result.setValue(ApiResponse.success(response.body()));
                } else {
                    result.setValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), response.body()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Teacher>> call, @NonNull Throwable t) {
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });
        return result;
    }


    public LiveData<ApiResponse<Boolean>> changePassword(String oldPwd, String newPwd, String confPwd) {
        MutableLiveData<ApiResponse<Boolean>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();


        Call<JsonElement> call = retrofit.create(RestApi.class).updatePassword(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                prefRepo.getUserType(),

                mApiHeader.getSchoolId(),
                String.valueOf(sesUId),
                oldPwd,
                newPwd,
                confPwd
        );

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    result.setValue(ApiResponse.success(true));
                } else {
                    result.setValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), false));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                result.postValue(ApiResponse.error(1001, t.getMessage(), false));
            }
        });

        return result;
    }


    public LiveData<ApiResponse<List<LiveBus>>> getLiveMapBusList() {
        MutableLiveData<ApiResponse<List<LiveBus>>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<List<LiveBus>> call = retrofit.create(RestApi.class).getLiveMapBusList(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                prefRepo.getUserType(),

                String.valueOf(sesUId),
                mApiHeader.getSchoolId()
        );

        call.enqueue(new Callback<List<LiveBus>>() {

            @Override
            public void onResponse(@NonNull Call<List<LiveBus>> call, @NonNull Response<List<LiveBus>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.body().toString());
                    result.setValue(ApiResponse.success(response.body()));
                } else {
                    Log.d(TAG, response.errorBody().toString());
                    result.setValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), response.body()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<LiveBus>> call, @NonNull Throwable t) {
                Log.d(TAG, t.getLocalizedMessage());
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<ApiResponse<List<BusDriver>>> getBusDetails() {
        MutableLiveData<ApiResponse<List<BusDriver>>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        String apiKey = mApiHeader.getProtectedApiHeader().getApiKey();
        String sshKey = mApiHeader.getProtectedApiHeader().getSSHKey();
        String sesKey = mApiHeader.getProtectedApiHeader().getSESKey();
        String sesNId = mApiHeader.getProtectedApiHeader().getSESNId();
        long sesUId = mApiHeader.getProtectedApiHeader().getSESUId();

        Call<List<BusDriver>> call = retrofit.create(RestApi.class).getBusDetails(
                apiKey,
                sshKey,
                sesKey,
                sesNId,
                String.valueOf(sesUId),
                prefRepo.getUserType(),

                mApiHeader.getSchoolId(),
                String.valueOf(sesUId)
        );

        call.enqueue(new Callback<List<BusDriver>>() {

            @Override
            public void onResponse(@NonNull Call<List<BusDriver>> call, Response<List<BusDriver>> response) {
                if (response.isSuccessful()) {
                    result.setValue(ApiResponse.success(response.body()));
                } else {
                    result.setValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), response.body()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BusDriver>> call, @NonNull Throwable t) {
                result.postValue(ApiResponse.error(1001, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<ApiResponse<List<HomeModel>>> getHomeItems() {
        MediatorLiveData<ApiResponse<List<HomeModel>>> result = new MediatorLiveData<>();
        List<HomeModel> homeItems = new ArrayList<>();
        result.setValue(ApiResponse.loading(homeItems));

        UserType userType = UserType.from(prefRepo.getUserType());


        AtomicInteger failureRate = new AtomicInteger();
        failureRate.set(0);
        AtomicInteger successRate = new AtomicInteger();
        successRate.set(0);
        final int total = (userType == UserType.TEACHER ? 1 : 0) + 2;

        Log.e("Total Source", total + "");

        if (userType == UserType.TEACHER) {
            LiveData<ApiResponse<List<Notification>>> noticeSource = getNotifications(UserType.TEACHER);
            result.addSource(noticeSource, response -> {
                if (response == null) return;
                if (response.isLoading()) return;

                if (response.isSuccessful()) {
                    result.removeSource(noticeSource);
                    for (Notification item : response.data) {
                        item.setIsPinned(1);
                    }
                    homeItems.addAll(response.data);
                    successRate.set(successRate.get() + 1);
                } else {
                    failureRate.set(failureRate.get() + 1);
                }

                Log.e("Home Item ", (successRate.get() + failureRate.get()) + "");
                if (successRate.get() + failureRate.get() == total) {
                    if (successRate.get() == total) {
                        result.setValue(ApiResponse.success(homeItems));
                    } else {
                        result.setValue(ApiResponse.error(response.status, response.message, homeItems));
                    }
                }
            });
        }

        LiveData<ApiResponse<List<Notification>>> noticeSource = getNotifications(UserType.PARENT);
        result.addSource(noticeSource, response -> {
            if (response == null) return;
            if (response.isLoading()) return;

            if (response.isSuccessful()) {
                result.removeSource(noticeSource);
                for (Notification item : response.data) {
                    item.setIsPinned(0);
                }
                homeItems.addAll(response.data);
                successRate.set(successRate.get() + 1);

            } else {
                result.removeSource(noticeSource);
                failureRate.set(failureRate.get() + 1);
            }

            Log.e("Home Item ", (successRate.get() + failureRate.get()) + "");
            if (successRate.get() + failureRate.get() == total) {
                if (successRate.get() == total) {
                    result.setValue(ApiResponse.success(homeItems));
                } else {
                    result.setValue(ApiResponse.error(response.status, response.message, homeItems));
                }
            }
        });

        LiveData<ApiResponse<List<Event>>> eventSource = getCalendarEvents();
        result.addSource(getCalendarEvents(), response -> {
            if (response == null) return;
            if (response.isLoading()) return;

            if (response.isSuccessful()) {
                result.removeSource(eventSource);
                homeItems.addAll(response.data);
                successRate.set(successRate.get() + 1);

            } else {
                failureRate.set(failureRate.get() + 1);
            }

            Log.e("Home Item ", (successRate.get() + failureRate.get()) + "");
            if (successRate.get() + failureRate.get() == total) {
                if (successRate.get() == total) {
                    result.setValue(ApiResponse.success(homeItems));
                } else {
                    result.setValue(ApiResponse.error(response.status, response.message, homeItems));
                }
            }
        });

        return result;
    }

    public LiveData<ApiResponse<SessionRepository.SessionResources>> getSessionData(boolean loadMenu, boolean loadDC, boolean loadNotice, boolean loadEvent) {
        AtomicInteger successRate = new AtomicInteger();
        AtomicInteger failureRate = new AtomicInteger();
        successRate.set(0);
        failureRate.set(0);
        final int total = (loadMenu ? 1 : 0) + (loadDC ? 1 : 0) + (loadNotice ? 1 : 0) + (loadEvent ? 1 : 0);
        Log.e("Total Size", total + " ");

        MediatorLiveData<ApiResponse<SessionRepository.SessionResources>> result = new MediatorLiveData<>();
        SessionRepository.SessionResources resources = new SessionRepository.SessionResources();
        result.setValue(ApiResponse.loading(resources));

        // observing menus from Remote
        LiveData<ApiResponse<List<DashboardItem>>> menuSource = getMenuItems();
        result.addSource(menuSource, response -> {
            if (response == null) return;
            if (response.isLoading()) return;

            if (response.isSuccessful()) {
                result.removeSource(menuSource);
                resources.setMenuItems(response.data);
                successRate.set(successRate.get() + 1);

            } else {
                failureRate.set(failureRate.get() + 1);
            }

            if (successRate.get() + failureRate.get() == total) {
                if (successRate.get() == total) {
                    result.setValue(ApiResponse.success(resources));
                } else {
                    result.setValue(ApiResponse.error(response.status, response.message, resources));
                }
            }

        });

        // observing grade from Remote
        LiveData<ApiResponse<List<Grade>>> gradeSource = getGradeList();
        result.addSource(gradeSource, response -> {
            if (response == null) return;
            if (response.isLoading()) return;

            if (response.isSuccessful()) {
                result.removeSource(gradeSource);
                resources.setDigitalClassItems(response.data);
                successRate.set(successRate.get() + 1);

            } else {
                failureRate.set(failureRate.get() + 1);
            }

            if (successRate.get() + failureRate.get() == total) {
                if (successRate.get() == total) {
                    result.setValue(ApiResponse.success(resources));
                } else {
                    result.setValue(ApiResponse.error(response.status, response.message, resources));
                }
            }

        });

        // observing notification from Remote
        LiveData<ApiResponse<List<Notification>>> noticeSource = getNotifications(UserType.from(prefRepo.getUserType()));
        result.addSource(noticeSource, response -> {
            if (response == null) return;
            if (response.isLoading()) return;

            if (response.isSuccessful()) {
                result.removeSource(noticeSource);
                resources.setNoticeItems(response.data);
                successRate.set(successRate.get() + 1);

            } else {
                failureRate.set(failureRate.get() + 1);
            }

            if (successRate.get() + failureRate.get() == total) {
                if (successRate.get() == total) {
                    result.setValue(ApiResponse.success(resources));
                } else {
                    result.setValue(ApiResponse.error(response.status, response.message, resources));
                }
            }

        });

        // observing event from Remote
        LiveData<ApiResponse<List<Event>>> eventSource = getCalendarEvents();
        result.addSource(eventSource, response -> {
            if (response == null) return;
            if (response.isLoading()) return;


            if (response.isSuccessful()) {
                result.removeSource(eventSource);
                resources.setEventItems(response.data);
                successRate.set(successRate.get() + 1);
            } else {
                failureRate.set(failureRate.get() + 1);
            }

            if (successRate.get() + failureRate.get() == total) {
                if (successRate.get() == total) {
                    result.setValue(ApiResponse.success(resources));
                } else {
                    result.setValue(ApiResponse.error(response.status, response.message, resources));
                }
            }

        });

        return result;
    }

    public void broadCaseLocation(Location location) {
//        String json = "{\n" +
//                "    \"message\":\n" +
//                "    {\n" +
//                "        \"topic\": \"liveBus\",\n" +
//                "        \"data\":\n" +
//                "        {\n" +
//                "           \"lat\": \"" + location.getLatitude() + "\",\n" +
//                "           \"lng\": \"" + location.getLongitude() + "\",\n" +
//                "        }\n" +
//                "    }\n" +
//                "}";
//        String url = "https://fcm.googleapis.com/fcm/send";
//
//        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(JSON, json);
//
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(url)
//                .header("Authorization", "key=AAAADihaTQ0:APA91bGp4mhOxJEZw7XAYZqlXPi-xsPErB5trR6cNLDp_Sk77TjbAF-KkztGP0I7w5yGOUhX7fBRemRqJRiJQxqVYZvYAZVt80pv-VH9JxyFxRqnvdaZxthDfEpB6xtXvbEvMtqbwBY5")
//                .post(body)
//                .build();
//
//        okhttp3.Call call = client.newCall(request);
//        call.enqueue(new okhttp3.Callback() {
//            @Override
//            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
//
//            }
//        });
    }


}
