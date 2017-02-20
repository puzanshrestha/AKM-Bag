package com.example.pujan.bag.bagDetails;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;


import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;

import com.example.pujan.bag.orderDetails.OrderDisplayActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BagListActivity extends AppCompatActivity implements BagViewAdapter.ItemClickCallback, SearchView.OnQueryTextListener,FunctionsThread.AsyncResponse {

    RecyclerView recView;
    BagViewAdapter bagViewAdapter;
    String customer_id;
    String source;
    ArrayList<BagEntity> bagData = new ArrayList<>();

    ArrayList<BagColorQuantity> colorQuantities = new ArrayList<>();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        if (source.equals("customer")) {
            getMenuInflater().inflate(R.menu.order_menu_actionbar, menu);
            getMenuInflater().inflate(R.menu.search_action_bar, menu);
        } else
            getMenuInflater().inflate(R.menu.search_action_bar, menu);

        MenuItem menuItem = menu.findItem(R.id.searchh);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (source.equals("customer")) {
            switch (item.getItemId()) {
                case R.id.searchh:
                    return true;
                case R.id.action_cart:
                    Intent i = new Intent(this, OrderDisplayActivity.class);
                    i.putExtra("cid", customer_id.toString());
                    i.putExtra("recValue", colorQuantities);
                    startActivity(i);
                    return true;
                case android.R.id.home:
                    this.finish();
                    return true;
            }
        } else {
            switch (item.getItemId()) {
                case R.id.searchh:
                    return true;
                case android.R.id.home:
                    this.finish();
                    return true;

            }
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bag);
        source = getIntent().getStringExtra("source");
        customer_id = getIntent().getStringExtra("customer_id");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.bagsmall);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);   // These two are for
        actionBar.setDisplayShowHomeEnabled(true);
        if (customer_id != null) {

            actionBar.setTitle(" Order Menu");
      // displaying logo in the action bar
            Toast.makeText(this, "Customer id is" + customer_id, Toast.LENGTH_SHORT).show();
        } else {

            actionBar.setTitle(" View Bag");

        }

        recView = (RecyclerView) findViewById(R.id.recView);

        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(10);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        recView.setLayoutManager(lm);
        //recView.setLayoutManager(new ScrollingLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false, duration));


        FunctionsThread t = new FunctionsThread(this);
        t.execute("ViewBag");
        t.trigAsyncResponse(this);








    }

    @Override
    public void onItemClick(int p) {

        colorQuantities = bagViewAdapter.getRecValues();


    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<BagEntity> newbag = new ArrayList<>();
        for (BagEntity bagEntity : bagData) {
            String name = bagEntity.getName().toLowerCase();
            if (name.contains(newText)) {
                newbag.add(bagEntity);
            }
        }
        bagViewAdapter.setFilter(newbag);
        return false;
    }

    public void getData(String response){
        try {

            if(response=="Error"){
                Toast.makeText(this,"Connection Error... Please check your connection !",Toast.LENGTH_SHORT).show();
            }
            JSONObject bagJson = new JSONObject(response);
            JSONArray bagJsonArray = bagJson.getJSONArray("result");



            for (int i = 0; i < bagJsonArray.length(); i++) {
                JSONObject jObject = bagJsonArray.getJSONObject(i);
                BagEntity bagEntity = new BagEntity();
                bagEntity.setId(Integer.valueOf(jObject.getString("bag_id")));
                bagEntity.setName(jObject.getString("bag_name"));
                bagEntity.setCategory(jObject.getString("bag_category"));
                bagEntity.setPrice(Integer.valueOf(jObject.getString("bag_price")));
                bagEntity.setCompany(jObject.getString("bag_company"));
                bagEntity.setQuantity(Integer.valueOf(jObject.getString("bag_quantity")));
                bagEntity.setPhoto(jObject.getString("bag_photo"));

                bagData.add(bagEntity);

            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"Error With Database Connection..!!",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onComplete(String output) {

        getData(output);
        bagViewAdapter = new BagViewAdapter(bagData, getIntent().getStringExtra("source"), getBaseContext());

        recView.setAdapter(bagViewAdapter);
        bagViewAdapter.onItemClickCallback(BagListActivity.this);

        bagViewAdapter.notifyDataSetChanged();
    }
}
