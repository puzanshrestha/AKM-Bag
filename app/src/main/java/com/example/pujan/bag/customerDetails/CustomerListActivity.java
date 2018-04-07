package com.example.pujan.bag.customerDetails;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pujan.bag.ActionListActivity;
import com.example.pujan.bag.R;
import com.example.pujan.bag.VolleyFunctions;
import com.example.pujan.bag.bagDetails.BagListActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CustomerListActivity extends AppCompatActivity implements VolleyFunctions.AsyncResponse, CustomerViewAdapter.ItemClickCallback, SearchView.OnQueryTextListener {

    RecyclerView recView;
    CustomerViewAdapter customerViewAdapter;
    String method = "customer";
    ArrayList<CustomerEntity> customerData;
    FloatingActionButton fab;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer);


        Toolbar toolbar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color=\"#ffffff\">" + "Customer" + "</font>"));
        actionBar.setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);


        VolleyFunctions t = new VolleyFunctions(this);
        t.viewCustomer();
        t.trigAsyncResponse(this);


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
    public boolean onCreateOptionsMenu(Menu search) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_action_bar, search);
        MenuItem menuItem = search.findItem(R.id.searchh);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        searchEditText.setHintTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        searchEditText.setBackground(new ColorDrawable(Color.WHITE));
        searchEditText.setHint("Search Customer");
        searchView.setOnQueryTextListener(this);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                Intent i = new Intent(getBaseContext(),ActionListActivity.class);
                startActivity(i);
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(getBaseContext(), ActionListActivity.class);
        startActivity(i);

    }

    @Override

    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override

    public boolean onQueryTextChange(String newText) {
        ArrayList<CustomerEntity> listStartsWith = new ArrayList<>();
        ArrayList<CustomerEntity> listContains = new ArrayList<>();

        for (CustomerEntity customerEntity : customerData) {
            String name = customerEntity.getName().toLowerCase();
            if (name.startsWith(newText.toLowerCase())) {
                listStartsWith.add(customerEntity);
            }
        }
        Collections.sort(listStartsWith, new Comparator<CustomerEntity>() {
            public int compare(CustomerEntity obj1, CustomerEntity obj2) {
                return (obj1.getName()).compareTo(obj2.getName());
            }
        });
        for (CustomerEntity customerEntity : customerData) {
            String name = customerEntity.getName().toLowerCase();
            if ((name.contains(newText.toLowerCase()))&(!listStartsWith.contains(customerEntity))) {
                listContains.add(customerEntity);
            }
        }
        listStartsWith.addAll(listContains);
        customerViewAdapter.setFilter(listStartsWith);
        return false;
    }

    @Override
    public void onComplete(String output) {

        progressBar.setVisibility(View.GONE);
        if (output.equals("ERROR")) {
            Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
        } else {
            customerData = new ArrayList<>();
            if (output.equals("Error")) {
                Toast.makeText(this, "Error in Connection", Toast.LENGTH_SHORT).show();
            } else {
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

                recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (dy > 0 | dy < 0)
                            fab.hide();


                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        fab.show();
                    }
                });


                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getBaseContext(), AddCustomerActivity.class);
                        startActivity(i);
                    }
                });
            }

        }
    }
}
