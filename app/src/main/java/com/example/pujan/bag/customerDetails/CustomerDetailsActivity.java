package com.example.pujan.bag.customerDetails;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.pujan.bag.R;

public class CustomerDetailsActivity extends AppCompatActivity {

    LinearLayout addCustomer,viewCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.customersmall);
        actionBar.setTitle(" Customer Details");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        addCustomer = (LinearLayout) findViewById(R.id.addCustomerBtn);
        viewCustomer = (LinearLayout)findViewById(R.id.viewCustomerBtn);

        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),AddCustomerActivity.class);
                i.putExtra("source","");
                i.putExtra("cust_id","");
                startActivity(i);
            }
        });

        viewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),CustomerListActivity.class);
                i.putExtra("method","customerview");
                startActivity(i);
            }
        });
    }
}
