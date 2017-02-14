package com.example.pujan.bag.printPackage;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Created by Pujan on 1/6/2017.
 */
public class PrintEntity implements Serializable {

    private int bag_id;
    private String product;
    private int price;
    private LinkedHashMap<String,Integer> colorQuantity;

    public int getBag_id() {
        return bag_id;
    }

    public void setBag_id(int bag_id) {
        this.bag_id = bag_id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String bagname) {
        this.product = bagname;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LinkedHashMap<String, Integer> getColorQuantity() {
        return colorQuantity;
    }

    public void setColorQuantity(LinkedHashMap<String, Integer> colorQuantity) {
        this.colorQuantity = colorQuantity;
    }
}
