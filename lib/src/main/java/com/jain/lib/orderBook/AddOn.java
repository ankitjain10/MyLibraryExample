
package com.jain.lib.orderBook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddOn {

    public AddOn(){}

    @SerializedName("menuType")
    @Expose
    private String menuType;
    @SerializedName("groupRef")
    @Expose
    private int groupRef;
    @SerializedName("groupName")
    @Expose
    private String groupName;
    @SerializedName("modifierRef")
    @Expose
    private int modifierRef;
    @SerializedName("modifier")
    @Expose
    private String modifier;
    @SerializedName("cost")
    @Expose
    private float cost;
    @SerializedName("vat")
    @Expose
    private int vat;

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public int getGroupRef() {
        return groupRef;
    }

    public void setGroupRef(int groupRef) {
        this.groupRef = groupRef;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getModifierRef() {
        return modifierRef;
    }

    public void setModifierRef(int modifierRef) {
        this.modifierRef = modifierRef;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }

}
