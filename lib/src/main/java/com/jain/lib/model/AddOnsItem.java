package com.jain.lib.model;

import com.google.gson.annotations.SerializedName;

public class AddOnsItem {

    @SerializedName("groupName")
    private String groupName;

    @SerializedName("cost")
    private float cost;

    @SerializedName("modifierRef")
    private int modifierRef;

    @SerializedName("modifier")
    private String modifier;

    @SerializedName("vat")
    private float vat;

    @SerializedName("menuType")
    private String menuType;

    @SerializedName("groupRef")
    private int groupRef;

    public String getGroupName() {
        return groupName;
    }

    public float getCost() {
        return cost;
    }

    public int getModifierRef() {
        return modifierRef;
    }

    public String getModifier() {
        return modifier;
    }

    public float getVat() {
        return vat;
    }

    public String getMenuType() {
        return menuType;
    }

    public int getGroupRef() {
        return groupRef;
    }

    public float getAddOnBaseCost() {
    	if(getCost()>0){
    	    float baseCost=(getCost() * 100) / ((100 + getVat()));
            System.out.println(getModifier()+" : OnBaseCost: "+baseCost);
			return baseCost;
		}else{
    		return 0.00f;
		}
    }

	public float getAddOnVatAmount() {
        float addOnVatAmount=(getVat()* getAddOnBaseCost())/100;
        System.out.println(getModifier()+" :addOnVatAmount: "+addOnVatAmount);
		return addOnVatAmount;
	}

}