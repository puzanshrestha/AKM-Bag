package com.example.pujan.bag.bagDetails;

/**
 * Created by Pujan on 1/9/2017.
 */
public class RecValue {

    private int bag_id[];
    private int quantity[];
    private String color[];

    public String[] getColor() {
        return color;
    }

    public void setColor(String[] color) {
        this.color = color;
    }

    public int[] getBag_id() {
        return bag_id;
    }

    public void setBag_id(int[] bag_id) {
        this.bag_id = bag_id;
    }

    public int[] getQuantity() {
        return quantity;
    }

    public void setQuantity(int[] quantity) {
        this.quantity = quantity;
    }
}
