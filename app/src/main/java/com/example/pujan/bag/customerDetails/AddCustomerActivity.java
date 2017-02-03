package com.example.pujan.bag.customerDetails;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;

public class AddCustomerActivity extends AppCompatActivity {


    Button addCustomerBtn;
    EditText nameEditText,addressEditText,phoneEditText;
    String customerName,customerAddress,customerPhone,source,cid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        source=getIntent().getStringExtra("source");

       android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (source.equals("update")){
            actionBar.setLogo(R.drawable.customersmall);
            actionBar.setTitle(" Update Customer");
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        else{
            actionBar.setLogo(R.drawable.customersmall);
            actionBar.setTitle(" Add Customer");
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        addCustomerBtn = (Button)findViewById(R.id.addCustomerBtn);

        nameEditText = (EditText)findViewById(R.id.customerNameEditText);
        addressEditText = (EditText)findViewById(R.id.customerAddressEditText);
        phoneEditText = (EditText)findViewById(R.id.customerPhoneEditText);

        cid=getIntent().getStringExtra("cust_id");

        if (source.equals("update")){
            nameEditText.setText(getIntent().getStringExtra("cust_name"));
            addressEditText.setText(getIntent().getStringExtra("cust_address"));
            phoneEditText.setText(getIntent().getStringExtra("cust_phone"));
        }



        addCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerName = nameEditText.getText().toString();
                customerAddress = addressEditText.getText().toString();
                customerPhone = phoneEditText.getText().toString();

                try{
                    String check = new FunctionsThread(getBaseContext()).execute("AddCustomer",customerName,customerAddress,customerPhone,source,cid).get();
                    if(check.equals("Inserted"))
                    {
                        Toast.makeText(getBaseContext(),"Added new Customer",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getBaseContext(),CustomerListActivity.class);
                        i.putExtra("method","customerview");
                        startActivity(i);
                    }
                    else if (check.equals("Update")){
                        Toast.makeText(getBaseContext(),"updated new Customer",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getBaseContext(),CustomerListActivity.class);
                        i.putExtra("method","customerview");
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(),check,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }



            }
        });

    }
}
