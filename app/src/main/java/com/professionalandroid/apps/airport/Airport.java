package com.professionalandroid.apps.airport;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity
public class Airport {
    @NonNull
    @PrimaryKey
    private String mAirport; // 출발지 공항
    private String mAirline; // 항공사
    private String mEstimatedDateTime; // 변경 시간
    private String mFlightId; // 항공 편명
    private String mTerminalid; // 터미널 장소
    private String mYoil; // 예정 시간
    private double mWind; // 풍속

    public String  getAirport() { return mAirport;}
    public String getAirline() { return mAirline;}
    public String getEstimatedDateTime() { return mEstimatedDateTime;}
    public String getFlightId() { return mFlightId;}
    public String getTerminalid() { return mTerminalid;}
    public String getYoil() { return mYoil;}
    public double getWind() { return mWind;}

    public Airport(String airport, String airline, String estimatedDateTime, String flightId,
                   String terminalid, String yoil, double wind) {
        mAirport = airport;
        mAirline = airline;
        mEstimatedDateTime = estimatedDateTime;
        mFlightId = flightId;
        mTerminalid = terminalid;
        mYoil = yoil;
        mWind = wind;
    }

    @Override
    public boolean equals( Object obj) {
        if (obj instanceof Airport)
            return (((Airport)obj).getAirport().contentEquals(mAirport));
        else
            return false;
    }
}

