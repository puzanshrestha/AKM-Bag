package com.example.pujan.bag.bagDetails;

import com.example.pujan.bag.bagStock.ColorQuantityEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Pujan on 14-Feb-17.
 */
public class BagColorQuantity implements Serializable{
    private int bag_id;
    LinkedHashMap<String, Integer> quantityColor;


    public int getBag_id() {
        return bag_id;
    }

    public void setBag_id(int bag_id) {
        this.bag_id = bag_id;
    }

    public LinkedHashMap<String, Integer> getQuantityColor() {
        return quantityColor;
    }

    public void setQuantityColor(LinkedHashMap<String, Integer> quantityColor) {
        this.quantityColor = quantityColor;
    }
}
