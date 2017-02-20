package com.example.pujan.bag.vendorDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.vendorsmall);
        actionBar.setTitle(" Add Vendor");
        actionBar.setDisplayUseLogoEnabled(true);   // These two are for
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        addVendorBtn = (Button) findViewById(R.id.addVendorBtn);
        vendorNameEditText = (EditText) findViewById(R.id.vendorNameEditText);
        vendorAddressEditText = (EditText) findViewById(R.id.vendorAddressEditText);
        source = getIntent().getStringExtra("source");
        if (source.equals("update")) {
            vendorNameEditText.setText(getIntent().getStringExtra("name"));
            vendorAddressEditText.setText(getIntent().getStringExtra("address"));
            id = getIntent().getStringExtra("id");
        }

        addVendorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendorName = vendorNameEditText.getText().toString();
                vendorAddress = vendorAddressEditText.getText().toString();


                    FunctionsThread t= new FunctionsThread(AddVendorActivity.this);
                    t.execute("AddVendor", vendorName, vendorAddress, source, id);
                    t.trigAsyncResponse(AddVendorActivity.this);



            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
        }
        return true;
    }

    @Override
    public void onComplete(String check) {
        if (check.equals("Inserted")) {
            Toast.makeText(getBaseContext(), "Successfully Added new Vendor", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getBaseContext(), VendorDetailsActivity.class);
            i.putExtra("getway","actionlist");
            startActivity(i);
        } else if (check.equals("Updated")) {
            Toast.makeText(getBaseContext(), "Successfully updated new Vendor", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getBaseContext(), VendorListActivity.class);
            i.putExtra("getway","actionlist");
            startActivity(i);
        } else {
            Toast.makeText(getBaseContext(), check, Toast.LENGTH_SHORT).show();
        }
    }
}
