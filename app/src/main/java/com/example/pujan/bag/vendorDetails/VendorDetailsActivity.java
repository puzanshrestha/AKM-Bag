package com.example.pujan.bag.vendorDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.pujan.bag.ActionListActivity;
import com.example.pujan.bag.R;

public class VendorDetailsActivity extends AppCompatActivity {

    LinearLayout addVendorBtn, viewVendorBtn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_details);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.vendorsmall);
        actionBar.setTitle(" Vendor Details");
        actionBar.setDisplayUseLogoEnabled(true);   // These two are for
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        addVendorBtn = (LinearLayout) findViewById(R.id.addVendorBtn);
        viewVendorBtn = (LinearLayout) findViewById(R.id.viewVendorBtn);

        addVendorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), AddVendorActivity.class);
                i.putExtra("source", "insert");
                startActivity(i);
            }
        });

        viewVendorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), VendorListActivity.class);
                i.putExtra("getway", "actionlist");
                startActivity(i);

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(getBaseContext(), ActionListActivity.class);
                startActivity(i);

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), ActionListActivity.class);
        startActivity(i);
    }
}
