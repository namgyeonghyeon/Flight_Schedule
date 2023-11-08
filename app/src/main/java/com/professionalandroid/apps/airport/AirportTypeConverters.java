package com.professionalandroid.apps.airport;

import android.arch.persistence.room.TypeConverter;
import android.location.Location;

import java.util.Date;

public class AirportTypeConverters{
    @TypeConverter
    public static Date dateFromTimestamp(Long value) {
        return value == null ? null: new Date(value);
    }

    // date => long
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
