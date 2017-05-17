package com.example.pujan.bag.orderDetailsFragment;

import com.example.pujan.bag.bagDetails.BagColorQuantity;

/**
 * Created by puzan on 12-May-17.
 */
public class FragmentOrderEntity extends BagColorQuantity {
    private String product;

    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
