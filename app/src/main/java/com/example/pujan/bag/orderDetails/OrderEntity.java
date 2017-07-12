package com.example.pujan.bag.orderDetails;

import com.example.pujan.bag.bagStock.ColorQuantityEntity;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created by Pujan on 1/8/2017.
 */
public class OrderEntity {

    private int bag_id, quantity;
    private Date date;
    private LinkedHashMap<String,Integer> colorQuantity;

    public int getBag_id() {
        return bag_id;
    }

    public void setBag_id(int bag_id) {
        this.bag_id = bag_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LinkedHashMap<String, Integer> getColorQuantity() {
        return colorQuantity;
    }

    public void setColorQuantity(LinkedHashMap<String, Integer> colorQuantity) {
        this.colorQuantity = colorQuantity;
    }
}
