package com.example.pujan.bag.vendorDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;

public class AddVendorActivity extends AppCompatActivity implements FunctionsThread.AsyncResponse{

    Button addVendorBtn;
    EditText vendorNameEditText, vendorAddressEditText;
    String vendorName, vendorAddress;
    String source;
    String id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color=\"#ffffff\">" + "Add New Vendor" + "</font>"));
        actionBar.setDisplayHomeAsUpEnabled(true);

        addVendorBtn = (Button) findViewById(R.id.saveBtn);
        vendorNameEditText = (EditText) findViewById(R.id.nameEditText);
        vendorAddressEditText = (EditText) findViewById(R.id.addressEditText);

        addVendorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendorName = vendorNameEditText.getText().toString();
                vendorAddress = vendorAddressEditText.getText().toString();


                    FunctionsThread t= new FunctionsThread(AddVendorActivity.this);
                    t.execute("AddVendor", vendorName, vendorAddress, "insert", "0");
                    t.trigAsyncResponse(AddVendorActivity.this);



            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(getBaseContext(), VendorListActivity.class);
                startActivity(i);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), VendorListActivity.class);
        startActivity(i);
    }

    @Override
    public void onComplete(String check) {
        if (check.equals("Inserted")) {
            Toast.makeText(getBaseContext(), "Successfully Added New Vendor", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, VendorListActivity.class);
            startActivity(i);
        }
        else {
            Toast.makeText(getBaseContext(), check, Toast.LENGTH_SHORT).show();
        }
    }
}
