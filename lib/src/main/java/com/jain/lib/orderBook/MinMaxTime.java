package com.jain.lib.orderBook;

public class MinMaxTime {
    String minTime;

    @Override
    public String toString() {
        return "{" +
                "minTime='" + minTime + '\'' +
                ", maxTime='" + maxTime + '\'' +
                '}';
    }

    String maxTime;

    public String getMinTime() {
        return minTime;
    }

    public void setMinTime(String minTime) {
        this.minTime = minTime;
    }

    public String getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }
}
