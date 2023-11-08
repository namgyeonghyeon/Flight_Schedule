package com.professionalandroid.apps.airport;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {Airport.class}, version = 1)
@TypeConverters({AirportTypeConverters.class})
public abstract class AirportDatabase extends RoomDatabase {
    public abstract AirportDAO airportDAO();
}
