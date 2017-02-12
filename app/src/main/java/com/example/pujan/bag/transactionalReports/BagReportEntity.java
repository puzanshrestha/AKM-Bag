package com.example.pujan.bag.transactionalReports;

import java.util.Date;

/**
 * Created by Pujan on 11-Feb-17.
 */
public class BagReportEntity {

    private String bag_name;
    private String customer_name;
    private int quantity;
    private Date date;

    public String getBag_name() {
        return bag_name;
    }

    public void setBag_name(String bag_name) {
        this.bag_name = bag_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
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


}
