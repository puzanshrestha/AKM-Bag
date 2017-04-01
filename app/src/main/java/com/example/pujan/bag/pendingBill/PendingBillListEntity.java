package com.example.pujan.bag.pendingBill;

import java.util.Date;

/**
 * Created by puzan on 26-Mar-17.
 */
public class PendingBillListEntity {
    int pId;
    Date date;
    String customerName;
    int customerId;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
