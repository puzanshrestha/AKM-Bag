package com.example.pujan.bag.vendorDetails;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.pujan.bag.R;

public class VendorDetailsActivity extends AppCompatActivity {

    LinearLayout addVendorBtn, viewVendorBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_details);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.vendorsmall);
        actionBar.setTitle(" Vendor Details");
        actionBar.setDisplayUseLogoEnabled(true);   // These two are for
        actionBar.setDisplayShowHomeEnabled(true);
        addVendorBtn = (LinearLayout) findViewById(R.id.addVendorBtn);
        viewVendorBtn = (LinearLayout) findViewById(R.id.viewVendorBtn);

        addVendorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), AddVendorActivity.class);
                i.putExtra("source","");
                startActivity(i);
            }
        });

        viewVendorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),VendorListActivity.class);
                i.putExtra("getway","actionlist");
                startActivity(i);

            }
        });



    }
}
