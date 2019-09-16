package com.mob.ibeacontester;

public class Beacon implements Comparable<Beacon> {
    private String uniqueId;
    private Double distance;
    private int batteryPower;

    public Beacon(String uniqueId, Double distance, int batteryPower) {
        this.uniqueId = uniqueId;
        this.distance = distance;
        this.batteryPower = batteryPower;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public int getBatteryPower() {
        return batteryPower;
    }

    public void setBatteryPower(int batteryPower) {
        this.batteryPower = batteryPower;
    }


    @Override
    public int compareTo(Beacon o) {
        if(distance>o.getDistance()){
            return 1;
        }
        else if(distance==o.getDistance()){
            return 0;
        }
        else {
            return -1;
        }
    }
}
