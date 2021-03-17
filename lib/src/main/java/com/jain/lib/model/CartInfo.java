package com.jain.lib.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CartInfo{

	@SerializedName("metaData")
	private MetaData metaData;

	@SerializedName("cartItems")
	private List<MenuItem> cartItems;

	public MetaData getMetaData(){
		return metaData;
	}

	public List<MenuItem> getCartItems(){
		return cartItems;
	}
}