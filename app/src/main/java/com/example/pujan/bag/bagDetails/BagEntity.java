package com.example.pujan.bag.bagDetails;

import java.io.Serializable;

/**
 * Created by Pujan on 1/4/2017.
 */
public class BagEntity implements Serializable{
    private int id;
    private String name;
    private String category;
    private int price;
    private String company;
    private int quantity;
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
