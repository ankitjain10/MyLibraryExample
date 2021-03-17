
package com.jain.lib.orderBook;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jain.lib.TimeUtils;

public class CartItem {
    public CartItem(){}

    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("prepTime")
    @Expose
    private String prepTime;
    @SerializedName("vat")
    @Expose
    private float vat;
    @SerializedName("volume")
    @Expose
    private String volume;
    @SerializedName("kitchenRef")
    @Expose
    private int kitchenRef;
    @SerializedName("itemType")
    @Expose
    private String itemType;
    @SerializedName("itemRef")
    @Expose
    private int itemRef;
    @SerializedName("cost")
    @Expose
    private float cost;
    @SerializedName("originalCost")
    @Expose
    private float originalCost;
    @SerializedName("groupRef")
    @Expose
    private String groupRef;
    @SerializedName("addOns")
    @Expose
    private List<AddOn> addOns = null;
    String startTime;
    String endTime;
    int allotedSlotNo;

    @Override public String toString() {
        return "CartItem{" +
                "id=" + getItemRef() +
                "name=" + getItemName() +
                ", prepTime=" + prepTime +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", allotedSlot=" + allotedSlotNo +
                '}';
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPrepTime() {
        return prepTime;
    }


    public int getPrepTimeInMins(){
        return (int) TimeUtils.getMinutes(getPrepTime());
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public float getVat() {
        return vat;
    }

    public void setVat(float vat) {
        this.vat = vat;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public int getKitchenRef() {
        return kitchenRef;
    }

    public void setKitchenRef(int kitchenRef) {
        this.kitchenRef = kitchenRef;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getItemRef() {
        return itemRef;
    }

    public void setItemRef(int itemRef) {
        this.itemRef = itemRef;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public float getOriginalCost() {
        return originalCost;
    }

    public void setOriginalCost(int originalCost) {
        this.originalCost = originalCost;
    }

    public String getGroupRef() {
        return groupRef;
    }

    public void setGroupRef(String groupRef) {
        this.groupRef = groupRef;
    }

    public List<AddOn> getAddOns() {
        return addOns;
    }

    public void setAddOns(List<AddOn> addOns) {
        this.addOns = addOns;
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
