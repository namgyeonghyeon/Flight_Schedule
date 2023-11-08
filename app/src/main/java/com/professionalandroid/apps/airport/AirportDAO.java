package com.professionalandroid.apps.airport;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

@Dao
public interface AirportDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAirports(List<Airport> airports);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAirport(Airport airports);

    @Delete
    public void deleteAirport(Airport airport);

    @Query("SELECT * FROM airport ORDER BY mEstimatedDateTime DESC")
    public LiveData<List<Airport>> loadAllAirports();
    @Query("SELECT mAirport as _id, " +
            "mAirline as suggest_text_1," +
            "mAirport as suggest_intent_data_id " +
            "FROM airport " +
            "WHERE mAirline LIKE :query " +
            "ORDER BY mEstimatedDateTime DESC")
    public Cursor generateSearchSuggestions(String query);

    @Query("SELECT * " +
            "FROM airport " +
            "WHERE mAirline LIKE :query " +
            "ORDER BY mEstimatedDateTime DESC")
    public LiveData<List<Airport>> searchAirports(String query);

    @Query("SELECT * " +
            "FROM airport " +
            "WHERE mAirport = :id " +
            "LIMIT 1")
    public LiveData<Airport> getAirport(String id);

    @Query("SELECT * FROM airport ORDER BY mEstimatedDateTime DESC")
    List<Airport> loadAllAirportsBlocking();
}
