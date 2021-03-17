package com.jain.lib.model;

import com.google.gson.annotations.SerializedName;

public class ServiceInfo{

	@SerializedName("addressRef")
	private int addressRef;

	@SerializedName("selectedTableNo")
	private String selectedTableNo;

	@SerializedName("bookingDate")
	private String bookingDate;

	@SerializedName("type")
	private String type;

	@SerializedName("orderDate")
	private String orderDate;

	public int getAddressRef(){
		return addressRef;
	}

	public String getSelectedTableNo(){
		return selectedTableNo;
	}

	public String getBookingDate(){
		return bookingDate;
	}

	public String getType(){
		return type;
	}

	public String getOrderDate(){
		return orderDate;
	}
}