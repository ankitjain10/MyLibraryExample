package com.jain.lib;

public class Slot {
    int slotNumber;
    String timeStamp;
    long timeStampLong;

    public boolean isAllotted() {
        return isAllotted;
    }

    public void setAllotted(boolean allotted) {
        isAllotted = allotted;
    }

    boolean isAllotted;

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    Integer orderID;


    @Override
    public String toString() {
        return "Slot{" +
                "slotNumber=" + slotNumber +
                ", timeStamp='" + timeStamp + '\'' +
                ", timeStampLong='"+timeStampLong+'\'' +
                '}';
    }

    public long getTimeStampLong() {
        return timeStampLong;
    }

    public void setTimeStampLong(long timeStampLong) {
        this.timeStampLong = timeStampLong;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
