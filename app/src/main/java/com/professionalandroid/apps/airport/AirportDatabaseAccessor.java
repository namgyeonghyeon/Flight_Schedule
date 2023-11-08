package com.professionalandroid.apps.airport;

import android.arch.persistence.room.Room;
import android.content.Context;

public class AirportDatabaseAccessor {

    private static AirportDatabase AirportDatabaseInstance;
    private static final String AIRPORT_DB_NAME = "airport_db";

    private AirportDatabaseAccessor() {}

    public static AirportDatabase getInstance (Context context) {
        if (AirportDatabaseInstance == null) {
            AirportDatabaseInstance = Room.databaseBuilder(context, AirportDatabase.class, AIRPORT_DB_NAME).build();
        }

        return AirportDatabaseInstance;
    }
}
