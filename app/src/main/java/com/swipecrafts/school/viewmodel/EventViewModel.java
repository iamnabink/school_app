package com.swipecrafts.school.viewmodel;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.IntDef;
import android.util.Pair;

import com.swipecrafts.school.data.model.db.Event;
import com.swipecrafts.school.data.repository.EventRepository;
import com.swipecrafts.school.data.repository.RepositoryFactory;
import com.swipecrafts.library.calendar.core.ADCalendar;
import com.swipecrafts.library.calendar.core.BSCalendar;
import com.swipecrafts.library.calendar.core.CalendarDay;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 2/27/2018.
 */

public class EventViewModel extends ViewModel {

    public static final int DAY = 1;
    public static final int MONTH = 2;
    private final RepositoryFactory repoFactory;
    private final EventRepository eventRepository;
    private final MutableLiveData<Pair<CalendarDay, Integer>> liveDate = new MutableLiveData<>();
    private LiveData<List<Event>> eventLiveData;

    public EventViewModel(RepositoryFactory repo) {
        this.repoFactory = repo;
        this.eventRepository = repoFactory.create(EventRepository.class);

        this.eventLiveData = Transformations.switchMap(liveDate, input -> {
            if (input.second == MONTH) {
                BSCalendar from = (BSCalendar) input.first.getBSCalendar();

                from.setTime(from.getYear(), from.getMonth(), 1, 0, 0, 0);
                BSCalendar to = new BSCalendar(from.getYear(), from.getMonth(), from.getMaximumDaysInMonth(), 23, 59, 59);

                return eventRepository.loadEvents(from.getTime(), to.getTime());
            } else {
                ADCalendar from = (ADCalendar) input.first.getADCalendar();
                from.setTime(from.getYear(), from.getMonth(), from.getDay(), 0, 0, 0);
                ADCalendar to = new ADCalendar(from.getYear(), from.getMonth(), from.getDay(), 23, 59, 59);
                return eventRepository.loadEvents(from.getTime(), to.getTime());
            }
        });
    }

    public void setTime(CalendarDay date, @EventType int type) {
        this.liveDate.postValue(new Pair<>(date, type));
    }

    public LiveData<List<Event>> getEvents() {
        return eventLiveData;
    }

    public LiveData<List<Date>> getEventsDate() {
        return eventRepository.loadAllEventsDate();
    }

    @SuppressLint("UniqueConstants")
    @Retention(RetentionPolicy.RUNTIME)
    @IntDef(flag = true, value = {DAY, MONTH})
    public @interface EventType {
    }
}
