package com.jain.lib.model;

import com.google.gson.annotations.SerializedName;

public class MetaData{

	@SerializedName("venueRef")
	private int venueRef;

	@SerializedName("customerRef")
	private int customerRef;

	@SerializedName("channel")
	private String channel;

	@SerializedName("serviceInfo")
	private ServiceInfo serviceInfo;

	@SerializedName("chainRef")
	private int chainRef;

	public int getVenueRef(){
		return venueRef;
	}

	public int getCustomerRef(){
		return customerRef;
	}

	public String getChannel(){
		return channel;
	}

	public ServiceInfo getServiceInfo(){
		return serviceInfo;
	}

	public int getChainRef(){
		return chainRef;
	}
}