
package com.jain.lib.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VenueDetails {

    @SerializedName("venue_ref")
    @Expose
    private int venueRef;
    @SerializedName("chain_ref")
    @Expose
    private int chainRef;
    @SerializedName("venue")
    @Expose
    private String venue;
    @SerializedName("sub_domain")
    @Expose
    private String subDomain;
    @SerializedName("description1")
    @Expose
    private String description1;
    @SerializedName("description2")
    @Expose
    private String description2;
    @SerializedName("description3")
    @Expose
    private String description3;
    @SerializedName("description4")
    @Expose
    private String description4;
    @SerializedName("description5")
    @Expose
    private String description5;
    @SerializedName("account_number")
    @Expose
    private String accountNumber;
    @SerializedName("store_code")
    @Expose
    private String storeCode;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("menu_type")
    @Expose
    private int menuType;
    @SerializedName("table_service")
    @Expose
    private int tableService;
    @SerializedName("delivery_service")
    @Expose
    private int deliveryService;
    @SerializedName("collection_service")
    @Expose
    private int collectionService;
    @SerializedName("kerb_side")
    @Expose
    private int kerbSide;
    @SerializedName("main_description")
    @Expose
    private String mainDescription;
    @SerializedName("logo_img_url")
    @Expose
    private String logoImgUrl;
    @SerializedName("prim_img_url")
    @Expose
    private String primImgUrl;
    @SerializedName("bg_img_url")
    @Expose
    private String bgImgUrl;
    @SerializedName("css_img_url")
    @Expose
    private Object cssImgUrl;
    @SerializedName("table_range")
    @Expose
    private int tableRange;
    @SerializedName("is_bar_open")
    @Expose
    private int isBarOpen;
    @SerializedName("is_kitchen_open")
    @Expose
    private int isKitchenOpen;
    @SerializedName("table_open")
    @Expose
    private int tableOpen;
    @SerializedName("delivery_open")
    @Expose
    private int deliveryOpen;
    @SerializedName("collection_open")
    @Expose
    private int collectionOpen;
    @SerializedName("avg_covers_hour")
    @Expose
    private String avgCoversHour;
    @SerializedName("action_date_time")
    @Expose
    private String actionDateTime;
    @SerializedName("min_order_value")
    @Expose
    private int minOrderValue;
    @SerializedName("collect_order_capcity")
    @Expose
    private int collectOrderCapcity;
    @SerializedName("collect_interval")
    @Expose
    private int collectInterval;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("kitchen_order_capacity")
    @Expose
    private int kitchenOrderCapacity;

    public int getVenueRef() {
        return venueRef;
    }

    public void setVenueRef(int venueRef) {
        this.venueRef = venueRef;
    }

    public int getChainRef() {
        return chainRef;
    }

    public void setChainRef(int chainRef) {
        this.chainRef = chainRef;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getDescription3() {
        return description3;
    }

    public void setDescription3(String description3) {
        this.description3 = description3;
    }

    public String getDescription4() {
        return description4;
    }

    public void setDescription4(String description4) {
        this.description4 = description4;
    }

    public String getDescription5() {
        return description5;
    }

    public void setDescription5(String description5) {
        this.description5 = description5;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getMenuType() {
        return menuType;
    }

    public void setMenuType(int menuType) {
        this.menuType = menuType;
    }

    public int getTableService() {
        return tableService;
    }

    public void setTableService(int tableService) {
        this.tableService = tableService;
    }

    public int getDeliveryService() {
        return deliveryService;
    }

    public void setDeliveryService(int deliveryService) {
        this.deliveryService = deliveryService;
    }

    public int getCollectionService() {
        return collectionService;
    }

    public void setCollectionService(int collectionService) {
        this.collectionService = collectionService;
    }

    public int getKerbSide() {
        return kerbSide;
    }

    public void setKerbSide(int kerbSide) {
        this.kerbSide = kerbSide;
    }

    public String getMainDescription() {
        return mainDescription;
    }

    public void setMainDescription(String mainDescription) {
        this.mainDescription = mainDescription;
    }

    public String getLogoImgUrl() {
        return logoImgUrl;
    }

    public void setLogoImgUrl(String logoImgUrl) {
        this.logoImgUrl = logoImgUrl;
    }

    public String getPrimImgUrl() {
        return primImgUrl;
    }

    public void setPrimImgUrl(String primImgUrl) {
        this.primImgUrl = primImgUrl;
    }

    public String getBgImgUrl() {
        return bgImgUrl;
    }

    public void setBgImgUrl(String bgImgUrl) {
        this.bgImgUrl = bgImgUrl;
    }

    public Object getCssImgUrl() {
        return cssImgUrl;
    }

    public void setCssImgUrl(Object cssImgUrl) {
        this.cssImgUrl = cssImgUrl;
    }

    public int getTableRange() {
        return tableRange;
    }

    public void setTableRange(int tableRange) {
        this.tableRange = tableRange;
    }

    public int getIsBarOpen() {
        return isBarOpen;
    }

    public void setIsBarOpen(int isBarOpen) {
        this.isBarOpen = isBarOpen;
    }

    public int getIsKitchenOpen() {
        return isKitchenOpen;
    }

    public void setIsKitchenOpen(int isKitchenOpen) {
        this.isKitchenOpen = isKitchenOpen;
    }

    public int getTableOpen() {
        return tableOpen;
    }

    public void setTableOpen(int tableOpen) {
        this.tableOpen = tableOpen;
    }

    public int getDeliveryOpen() {
        return deliveryOpen;
    }

    public void setDeliveryOpen(int deliveryOpen) {
        this.deliveryOpen = deliveryOpen;
    }

    public int getCollectionOpen() {
        return collectionOpen;
    }

    public void setCollectionOpen(int collectionOpen) {
        this.collectionOpen = collectionOpen;
    }

    public String getAvgCoversHour() {
        return avgCoversHour;
    }

    public void setAvgCoversHour(String avgCoversHour) {
        this.avgCoversHour = avgCoversHour;
    }

    public String getActionDateTime() {
        return actionDateTime;
    }

    public void setActionDateTime(String actionDateTime) {
        this.actionDateTime = actionDateTime;
    }

    public int getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(int minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public int getCollectOrderCapcity() {
        return collectOrderCapcity;
    }

    public void setCollectOrderCapcity(int collectOrderCapcity) {
        this.collectOrderCapcity = collectOrderCapcity;
    }

    public int getCollectInterval() {
        return collectInterval;
    }

    public void setCollectInterval(int collectInterval) {
        this.collectInterval = collectInterval;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getKitchenOrderCapacity() {
        return kitchenOrderCapacity;
    }

    public void setKitchenOrderCapacity(int kitchenOrderCapacity) {
        this.kitchenOrderCapacity = kitchenOrderCapacity;
    }

}
