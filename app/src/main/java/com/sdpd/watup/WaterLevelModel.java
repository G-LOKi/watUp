package com.sdpd.watup;

public class WaterLevelModel {
    private int height;
    private long timestamp;

    public WaterLevelModel() {
    }

    public WaterLevelModel(int height, long timestamp) {
        this.height = height;
        this.timestamp = timestamp;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(long timestamp) {
        this.timestamp = timestamp;
    }
}