package com.swipecrafts.school.ui.home;

import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Madhusudan Sapkota on 4/2/2018.
 */
public abstract class HomeModel implements Comparable<HomeModel> {

    @Ignore
    public static final String EVENT_TYPE = "EVENT_TYPE";
    @Ignore
    public static final String NOTIFICATION_TYPE = "NOTIFICATION_TYPE";
    @Ignore
    protected final String TAG = HomeModel.class.getSimpleName();
    @Ignore
    protected String TYPE = "";

    protected abstract long getId();

    protected abstract String getTitle();

    protected abstract String getDescription();

    protected abstract HomeType getHomeType();

    protected abstract Date getTime();

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "id / " + getId() + " type / " + getHomeType() + " time / " + sdf.format(getTime());
    }

    @Override
    public int compareTo(@NonNull HomeModel o) {
        // if negative  -- this less than   (<)  that(o) --
        // if 0         -- this equals      (==) that(o) --
        // if positive  -- this greater than(>)  that(o) --

        int order;

        // for ascending order
        if (getHomeType().value() < o.getHomeType().value()) {
            order = -1;
        } else if (getHomeType().value() == o.getHomeType().value()) {
            // for descending order
//            if (getHomeType() == HomeType.UPCOMING)
//                order = getTime().compareTo(o.getTime());
//            else
                order = o.getTime().compareTo(getTime());
        } else {
            order = 1;
        }

        return order;
    }
}
