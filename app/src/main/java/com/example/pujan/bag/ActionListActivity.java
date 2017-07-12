package com.example.pujan.bag;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.pujan.bag.bagDetails.BagListActivity;
import com.example.pujan.bag.bagStock.StockDetailsActivity;
import com.example.pujan.bag.bagStock.StockListActivity;
import com.example.pujan.bag.customerDetails.CustomerDetailsActivity;

import com.example.pujan.bag.customerDetails.CustomerListActivity;
import com.example.pujan.bag.pendingBill.PendingBillList;
import com.example.pujan.bag.transactionalReports.BagReports;
import com.example.pujan.bag.vendorDetails.VendorDetailsActivity;
import com.example.pujan.bag.vendorDetails.VendorListActivity;

public class ActionListActivity extends AppCompatActivity {

    LinearLayout bagDetailsBtn, customerDetailsBtn, orderDetailsBtn, vendorDetailsBtn, bagRecordsBtn, bagStockBtn;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_list);


        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#AAAAAA\">" + "Bag Inventory" + "</font>"));



        bagDetailsBtn = (LinearLayout) findViewById(R.id.bagDetailsBtn);
        customerDetailsBtn = (LinearLayout) findViewById(R.id.customerDetailsBtn);
        orderDetailsBtn = (LinearLayout) findViewById(R.id.orderDetailsBtn);
        vendorDetailsBtn = (LinearLayout) findViewById(R.id.vendorDetailsBtn);
        bagRecordsBtn = (LinearLayout) findViewById(R.id.bagRecordsBtn);
        bagStockBtn = (LinearLayout) findViewById(R.id.bagStockBtn);


        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");
        if (userType.equals("userType#0"))
            bagStockBtn.setVisibility(View.INVISIBLE);


        bagDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), BagListActivity.class);
                startActivity(i);
            }
        });

        customerDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), CustomerListActivity.class);
                startActivity(i);

            }
        });


        orderDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getBaseContext(), PendingBillList.class);
                startActivity(i);

            }
        });

/*
        orderDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

*/

        /*
        orderDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),CustomerListActivity.class);
                i.putExtra("method","orderView");
                startActivity(i);
            }
        });

        */

        vendorDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), VendorListActivity.class);
                startActivity(i);

            }
        });
        bagRecordsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), BagReports.class);
                startActivity(i);


            }
        });
        bagStockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), StockListActivity.class);
                startActivity(i);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.log_out_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logOut) {
            SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();

            this.finish();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);


        }
        return true;
    }


    @Override
    public void onBackPressed() {

    }
}
