package com.jain.lib.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuItem {

    @SerializedName("volume")
    private String volume;

    @SerializedName("itemRef")
    private int itemRef;

    @SerializedName("itemType")
    private String itemType;

    @SerializedName("itemName")
    private String itemName;

    @SerializedName(value = "cost",alternate = "originalCost")
    private Float cost;

    @SerializedName("addOns")
    private List<AddOnsItem> addOns;

    @SerializedName("count")
    private int count;

    @SerializedName("vat")
    private Float vat;

    @SerializedName("prepTime")
    private String prepTime;

    public String getVolume() {
        return volume;
    }

    public int getItemRef() {
        return itemRef;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public float getCost() {
        if (cost != null) {
            return cost;
        } else
            return 0.0f;
    }

    public List<AddOnsItem> getAddOns() {
        return addOns;
    }


    public float getItemBaseCost() {
        return ((getCost() * 100) / ((100 + getVat())))*(getCount());
    }

    public float getItemVatAmount() {
        return (getVat()* getItemBaseCost())/100;
    }


    public float getAddOnBaseTotal() {
        float totalAddOnValues = 0.00f;

        for (AddOnsItem addOnsItem : addOns) {
            float baseCost = addOnsItem.getAddOnBaseCost();
//            System.out.println("Cost: " + baseCost);
            baseCost = baseCost * getCount();
            totalAddOnValues += baseCost;
        }
        return totalAddOnValues;
    }

    public float getAddOnTax() {
        float totalAddOnValues = 0.00f;

        for (AddOnsItem addOnsItem : addOns) {
            float vatAddOnValues = addOnsItem.getAddOnVatAmount();
//            System.out.println("Cost: " + vatAddOnValues);
            vatAddOnValues = vatAddOnValues * getCount();
            totalAddOnValues += vatAddOnValues;
        }
        return totalAddOnValues;
    }

    public int getCount() {
        return count;
    }

    public float getVat() {
        if (vat != null) {
            return vat;
        } else
            return 0.0f;
    }

    public String getPrepTime() {
        return prepTime;
    }
}