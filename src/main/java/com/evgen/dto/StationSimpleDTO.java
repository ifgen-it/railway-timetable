package com.evgen.dto;

import java.io.Serializable;

public class StationSimpleDTO implements Serializable {

    private int stationId;

    private String stationName;

    public StationSimpleDTO() {
    }

    public StationSimpleDTO(int stationId, String stationName) {
        this.stationId = stationId;
        this.stationName = stationName;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public String toString() {
        return "StationSimpleDTO{" +
                "stationId=" + stationId +
                ", stationName='" + stationName + '\'' +
                '}';
    }
}
