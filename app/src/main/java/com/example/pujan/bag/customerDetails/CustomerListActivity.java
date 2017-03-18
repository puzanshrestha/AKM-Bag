package com.example.pujan.bag.customerDetails;


import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagListActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomerListActivity extends AppCompatActivity implements FunctionsThread.AsyncResponse, CustomerViewAdapter.ItemClickCallback,SearchView.OnQueryTextListener{

    RecyclerView recView;
    CustomerViewAdapter customerViewAdapter;
    String method;
    ArrayList<CustomerEntity> customerData;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.customersmall);
        actionBar.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);


        method = getIntent().getStringExtra("method");
         if (method.equals("orderView")){

             actionBar.setTitle(" Select Customer");
             actionBar.setDisplayUseLogoEnabled(true);

         }
        else {
             actionBar.setTitle(" View Customer");
             actionBar.setDisplayUseLogoEnabled(true);

         }

        FunctionsThread t = new FunctionsThread(this);
        t.execute("ViewCustomer");
        t.trigAsyncResponse(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(CustomerListActivity.this);
                dialog.setMessage("Enter Customer Name:");

                LinearLayout layout = new LinearLayout(CustomerListActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                TableRow.LayoutParams param = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                param.setMargins(6,6,6,6);
                layout.setLayoutParams(param);
                layout.setPadding(10,10,10,10);
                layout.setBackgroundColor(Color.LTGRAY);


                TextView txt = new TextView(getBaseContext());
                txt.setTextColor(Color.parseColor("#000000"));
                txt.setText("Customer Name");


                final EditText eTxt = new EditText(getBaseContext());
                eTxt.setTextColor(Color.parseColor("#000000"));
                layout.addView(txt);
                layout.addView(eTxt);

                dialog.setView(layout);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getBaseContext(),BagListActivity.class);
                        i.putExtra("source","manual");
                        i.putExtra("customerName",eTxt.getText().toString());
                        startActivity(i);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();



            }
        });




    }

    @Override
    public void onItemClick(int p) {
        if (method.equals("orderView")) {

            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity = customerData.get(p);
            int customer_id = customerEntity.getId();

            Intent i = new Intent(getBaseContext(), BagListActivity.class);
            i.putExtra("customer_id", Integer.toString(customer_id));
            i.putExtra("source", "auto");
            startActivity(i);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu search)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search_action_bar, search);
        MenuItem menuItem=search.findItem(R.id.searchh);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return true;
    }

    @Override

    public boolean onQueryTextSubmit(String query)
    {
return false;
    }

    @Override

    public boolean onQueryTextChange(String newText)
    {
        ArrayList<CustomerEntity> newlist=new ArrayList<>();
        for(CustomerEntity customer:customerData)
        {
            String name=customer.getName();
            if(name.contains(newText)) {
                newlist.add(customer);
            }
        }
        customerViewAdapter.setFilter(newlist);
        return false;
    }

    @Override
    public void onComplete(String output) {

        customerData = new ArrayList<>();
        if(output.equals("Error"))
        {
            Toast.makeText(this,"Error in Connection",Toast.LENGTH_SHORT).show();
        }
        else {
            try {

                JSONObject customerJson = new JSONObject(output);
                JSONArray customerJsonArray = customerJson.getJSONArray("result");
                System.out.println(customerJson);
                System.out.println(customerJsonArray);


                for (int i = 0; i < customerJsonArray.length(); i++) {
                    JSONObject jObject = customerJsonArray.getJSONObject(i);
                    CustomerEntity customerEntity = new CustomerEntity();
                    customerEntity.setId(((jObject.getInt("customer_id"))));
                    customerEntity.setName(jObject.getString("customer_name"));
                    customerEntity.setAddress(jObject.getString("customer_address"));
                    customerEntity.setPhone(jObject.getString("customer_phone"));

                    customerData.add(customerEntity);


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            recView = (RecyclerView) findViewById(R.id.recView);
            recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


            customerViewAdapter = new CustomerViewAdapter(customerData, method, this);
            recView.setAdapter(customerViewAdapter);

            customerViewAdapter.onItemClickCallback(this);
        }

    }
}
