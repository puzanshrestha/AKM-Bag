package com.example.pujan.bag;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.pujan.bag.bagDetails.BagDetailsActivity;
import com.example.pujan.bag.customerDetails.CustomerDetailsActivity;
import com.example.pujan.bag.customerDetails.CustomerListActivity;
import com.example.pujan.bag.customerDetails.ViewCustomerActivity;
import com.example.pujan.bag.vendorDetails.VendorDetailsActivity;

public class ActionListActivity extends AppCompatActivity {

    LinearLayout bagDetailsBtn,customerDetailsBtn,orderDetailsBtn,vendorDetailsBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_list);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setTitle(" Main Menu");
        actionBar.setDisplayUseLogoEnabled(true);   // These two are for
        actionBar.setDisplayShowHomeEnabled(true);  // displaying logo in the action bar




        bagDetailsBtn = (LinearLayout)findViewById(R.id.bagDetailsBtn);
        customerDetailsBtn = (LinearLayout)findViewById(R.id.customerDetailsBtn);
        orderDetailsBtn = (LinearLayout)findViewById(R.id.orderDetailsBtn);
        vendorDetailsBtn = (LinearLayout)findViewById(R.id.vendorDetailsBtn);

        bagDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),BagDetailsActivity.class);
                startActivity(i);
            }
        });

        customerDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),CustomerDetailsActivity.class);
                startActivity(i);

            }
        });

        orderDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),CustomerListActivity.class);
                i.putExtra("method","orderView");
                startActivity(i);
            }
        });

        vendorDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),VendorDetailsActivity.class);
                startActivity(i);

            }
        });




    }
}
