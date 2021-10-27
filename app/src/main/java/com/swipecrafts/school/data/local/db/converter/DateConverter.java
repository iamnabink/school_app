package com.swipecrafts.school.data.local.db.converter;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by Madhusudan Sapkota on 3/10/2018.
 */

public class DateConverter {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
