package com.swipecrafts.school.data.local.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.swipecrafts.school.utils.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Madhusudan Sapkota on 3/10/2018.
 */

public class TimestampConverter {
    static DateFormat df = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT);

    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

}
