package com.example.pujan.bag.orderDetails;

import java.util.LinkedHashMap;

/**
 * Created by Pujan on 11-Feb-17.
 */
public class AddOrderEntity {
    private int customer_id;
    private int bag_id;
    private LinkedHashMap<String,Integer> colorQuantity;


    public LinkedHashMap<String, Integer> getColorQuantity() {
        return colorQuantity;
    }

    public void setColorQuantity(LinkedHashMap<String, Integer> colorQuantity) {
        this.colorQuantity = colorQuantity;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getBag_id() {
        return bag_id;
    }

    public void setBag_id(int bag_id) {
        this.bag_id = bag_id;
    }


}
