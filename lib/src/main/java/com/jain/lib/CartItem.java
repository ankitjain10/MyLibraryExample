package com.jain.lib;

public class CartItem {

  int id;
  int prepTime;
  String startTime;
  String endTime;
  int allotedSlotNo;

  public CartItem() {
  }

  public CartItem(int id, int prepTime) {
    this.id = id;
    this.prepTime = prepTime;
  }

  @Override public String toString() {
    return "CartItem{" +
        "id=" + id +
        ", prepTime=" + prepTime +
        ", startTime='" + startTime + '\'' +
        ", endTime='" + endTime + '\'' +
        ", allotedSlot=" + allotedSlotNo +
        '}';
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getPrepTime() {
    return prepTime;
  }

  public void setPrepTime(int prepTime) {
    this.prepTime = prepTime;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public int getAllotedSlotNo() {
    return allotedSlotNo;
  }

  public void setAllotedSlotNo(int allotedSlotNo) {
    this.allotedSlotNo = allotedSlotNo;
  }
}
