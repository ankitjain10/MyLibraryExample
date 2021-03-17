
package com.jain.lib.orderBook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaData {
    public MetaData(){}

    @SerializedName("bookedBy")
    @Expose
    private String bookedBy;
    @SerializedName("chainRef")
    @Expose
    private int chainRef;
    @SerializedName("venueRef")
    @Expose
    private int venueRef;
    @SerializedName("customerRef")
    @Expose
    private int customerRef;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("serviceType")
    @Expose
    private String serviceType;
    @SerializedName("address_Ref")
    @Expose
    private int addressRef;
    @SerializedName("selectedTable")
    @Expose
    private String selectedTable;
    @SerializedName("orderBookDate")
    @Expose
    private String orderBookDate;
    @SerializedName("orderEtaDate")
    @Expose
    private String orderEtaDate;
    @SerializedName("customerComment")
    @Expose
    private String customerComment;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("tipAmount")
    @Expose
    private int tipAmount;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("paymentUniqueId")
    @Expose
    private String paymentUniqueId;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("totalOrderAmount")
    @Expose
    private float totalOrderAmount;
    @SerializedName("totalItemsAmount")
    @Expose
    private float totalItemsAmount;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("totalVat")
    @Expose
    private float totalVat;

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public int getChainRef() {
        return chainRef;
    }

    public void setChainRef(int chainRef) {
        this.chainRef = chainRef;
    }

    public int getVenueRef() {
        return venueRef;
    }

    public void setVenueRef(int venueRef) {
        this.venueRef = venueRef;
    }

    public int getCustomerRef() {
        return customerRef;
    }

    public void setCustomerRef(int customerRef) {
        this.customerRef = customerRef;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getAddressRef() {
        return addressRef;
    }

    public void setAddressRef(int addressRef) {
        this.addressRef = addressRef;
    }

    public String getSelectedTable() {
        return selectedTable;
    }

    public void setSelectedTable(String selectedTable) {
        this.selectedTable = selectedTable;
    }

    public String getOrderBookDate() {
        return orderBookDate;
    }

    public void setOrderBookDate(String orderBookDate) {
        this.orderBookDate = orderBookDate;
    }

    public String getOrderEtaDate() {
        return orderEtaDate;
    }

    public void setOrderEtaDate(String orderEtaDate) {
        this.orderEtaDate = orderEtaDate;
    }

    public String getCustomerComment() {
        return customerComment;
    }

    public void setCustomerComment(String customerComment) {
        this.customerComment = customerComment;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(int tipAmount) {
        this.tipAmount = tipAmount;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPaymentUniqueId() {
        return paymentUniqueId;
    }

    public void setPaymentUniqueId(String paymentUniqueId) {
        this.paymentUniqueId = paymentUniqueId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public float getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(float totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public float getTotalItemsAmount() {
        return totalItemsAmount;
    }

    public void setTotalItemsAmount(float totalItemsAmount) {
        this.totalItemsAmount = totalItemsAmount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public float getTotalVat() {
        return totalVat;
    }

    public void setTotalVat(float totalVat) {
        this.totalVat = totalVat;
    }

}
