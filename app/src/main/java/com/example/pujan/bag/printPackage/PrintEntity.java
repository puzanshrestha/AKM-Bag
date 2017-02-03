package com.example.pujan.bag.printPackage;

import java.io.Serializable;

/**
 * Created by Pujan on 1/6/2017.
 */
public class PrintEntity implements Serializable {

    private int id;
    private String products;
    private int quantity;
    private int price;
    private String customer_name;

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String Products) {
        this.products = Products;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
