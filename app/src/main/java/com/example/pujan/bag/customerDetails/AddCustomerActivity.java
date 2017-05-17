package com.example.pujan.bag.customerDetails;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;

public class AddCustomerActivity extends AppCompatActivity implements FunctionsThread.AsyncResponse {


    Button addCustomerBtn;
    EditText nameEditText,addressEditText,phoneEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color=\"#ffffff\">" + "Add New Customer" + "</font>"));
        actionBar.setDisplayHomeAsUpEnabled(true);



        addCustomerBtn = (Button)findViewById(R.id.saveBtn);

        nameEditText = (EditText)findViewById(R.id.nameEditText);
        addressEditText = (EditText)findViewById(R.id.addressEditText);
        phoneEditText = (EditText)findViewById(R.id.phoneEditText);


        addCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FunctionsThread t = new FunctionsThread(AddCustomerActivity.this);
                t.execute("AddCustomer",nameEditText.getText().toString(),addressEditText.getText().toString(),phoneEditText.getText().toString(),"add","0");
                t.trigAsyncResponse(AddCustomerActivity.this);




            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent i = new Intent(getBaseContext(),CustomerListActivity.class);
                startActivity(i);

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(),CustomerListActivity.class);
        startActivity(i);
    }

    @Override
    public void onComplete(String check) {
        if(check.equals("Inserted"))
        {
            Toast.makeText(getBaseContext(),"Added new Customer",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getBaseContext(),CustomerListActivity.class);
            i.putExtra("method","customerview");
            startActivity(i);
        }

    }
}
