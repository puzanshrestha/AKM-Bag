package com.example.pujan.bag.customerDetails;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagListActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomerListActivity extends AppCompatActivity implements CustomerViewAdapter.ItemClickCallback,SearchView.OnQueryTextListener{

    RecyclerView recView;
    CustomerViewAdapter customerViewAdapter;
    String method;
    ArrayList<CustomerEntity> customerData;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();


        method = getIntent().getStringExtra("method");
         if (method.equals("orderView")){
             actionBar.setLogo(R.drawable.customersmall);
             actionBar.setTitle(" Select Customer");
             actionBar.setDisplayUseLogoEnabled(true);
             actionBar.setDisplayShowHomeEnabled(true);
         }
        else {
             actionBar.setLogo(R.drawable.customersmall);
             actionBar.setTitle(" View Customer");
             actionBar.setDisplayUseLogoEnabled(true);
             actionBar.setDisplayShowHomeEnabled(true);
         }

        customerData = new ArrayList<>();
        try{
            String response = new FunctionsThread(this).execute("ViewCustomer").get();
            JSONObject customerJson = new JSONObject(response);
            JSONArray customerJsonArray = customerJson.getJSONArray("result");
            System.out.println(customerJson);
            System.out.println(customerJsonArray);


            for(int i=0;i<customerJsonArray.length();i++)
            {
                JSONObject jObject = customerJsonArray.getJSONObject(i);
                CustomerEntity customerEntity = new CustomerEntity();
                customerEntity.setId(((jObject.getInt("customer_id"))));
                customerEntity.setName(jObject.getString("customer_name"));
                customerEntity.setAddress(jObject.getString("customer_address"));
                customerEntity.setPhone(jObject.getString("customer_phone"));

                customerData.add(customerEntity);


            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        recView = (RecyclerView)findViewById(R.id.recView);
        recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));


        customerViewAdapter = new CustomerViewAdapter(customerData,method,this);
        recView.setAdapter(customerViewAdapter);

        customerViewAdapter.onItemClickCallback(this);






    }

    @Override
    public void onItemClick(int p) {
        if (method.equals("orderView")) {
            System.out.println("this is list activity");
            Toast.makeText(this, "Item " + p + "has been clicked", Toast.LENGTH_SHORT).show();

            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity = customerData.get(p);
            int customer_id = customerEntity.getId();

            Intent i = new Intent(getBaseContext(), BagListActivity.class);
            i.putExtra("customer_id", Integer.toString(customer_id));
            i.putExtra("source", "customer");
            i.putExtra("display", "n");
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

}
