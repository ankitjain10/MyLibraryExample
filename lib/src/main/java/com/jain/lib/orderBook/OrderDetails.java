
package com.jain.lib.orderBook;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetails {

    @SerializedName("cartItems")
    @Expose
    private List<CartItem> cartItems = null;
    @SerializedName("metaData")
    @Expose
    private MetaData metaData;

    public OrderDetails(){}

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

}
