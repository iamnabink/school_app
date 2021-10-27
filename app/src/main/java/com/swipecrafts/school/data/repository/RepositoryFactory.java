package com.swipecrafts.school.data.repository;

import android.support.annotation.NonNull;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.remote.AppApiRepository;

import javax.inject.Inject;

/**
 * Created by Madhusudan Sapkota on 5/20/2018.
 */
public class RepositoryFactory {
    private AppApiRepository webService;
    private AppDbRepository dbService;
    private AppPreferencesRepository prefService;

    @Inject
    public RepositoryFactory(AppApiRepository webService, AppDbRepository dbService, AppPreferencesRepository prefService) {
        this.webService = webService;
        this.dbService = dbService;
        this.prefService = prefService;
    }

    @NonNull
    public <T extends BaseRepository> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserRepository.class)) {
            return (T) new UserRepository(webService, dbService, prefService);
        } else if (modelClass.isAssignableFrom(HomeTypeRepository.class)) {
            return (T) new HomeTypeRepository(webService, dbService, prefService);
        } else if (modelClass.isAssignableFrom(NotificationRepository.class)) {
            return (T) new NotificationRepository(webService, dbService, prefService);
        } else if (modelClass.isAssignableFrom(SessionRepository.class)) {
            return (T) new SessionRepository(webService, dbService, prefService);
        } else if (modelClass.isAssignableFrom(DigitalClassRepository.class)) {
            return (T) new DigitalClassRepository(webService, dbService, prefService);
        }else if (modelClass.isAssignableFrom(UserRepository.class)) {
            return (T) new UserRepository(webService, dbService, prefService);
        }else if (modelClass.isAssignableFrom(TeacherRepository.class)) {
            return (T) new TeacherRepository(webService, dbService, prefService);
        } else if (modelClass.isAssignableFrom(ClassSectionRepository.class)) {
            return (T) new ClassSectionRepository(webService, dbService, prefService);
        }else if (modelClass.isAssignableFrom(AttendanceRepository.class)) {
            return (T) new AttendanceRepository(webService, dbService, prefService);
        }else if (modelClass.isAssignableFrom(EventRepository.class)) {
            return (T) new EventRepository(webService, dbService, prefService);
        }else if (modelClass.isAssignableFrom(SchoolRepository.class)) {
            return (T) new SchoolRepository(webService, dbService, prefService);
        }else if (modelClass.isAssignableFrom(StudentRepository.class)) {
            return (T) new StudentRepository(webService, dbService, prefService);
        }else if (modelClass.isAssignableFrom(SubjectRepository.class)) {
            return (T) new SubjectRepository(webService, dbService, prefService);
        }else if (modelClass.isAssignableFrom(AssignmentRepository.class)) {
            return (T) new AssignmentRepository(webService, dbService, prefService);
        } else if (modelClass.isAssignableFrom(MessageRepository.class)) {
            return (T) new MessageRepository(webService, dbService, prefService);
        } else if (modelClass.isAssignableFrom(LocationRepository.class)) {
            return (T) new LocationRepository(webService, dbService, prefService);
        }else {
            throw new IllegalArgumentException("Repository Not Found");
        }
    }
}
