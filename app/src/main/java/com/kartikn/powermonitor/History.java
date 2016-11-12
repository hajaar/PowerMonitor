package com.kartikn.powermonitor;

/**
 * Created by kartikn on 11-11-2016.
 */

public class History {

    private long id;
    private int capacity;
    private String datetime;
    private int startingcharge;
    private String power;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getStartingcharge() {
        return startingcharge;
    }

    public void setStartingcharge(int startingcharge) {
        this.startingcharge = startingcharge;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }
}
