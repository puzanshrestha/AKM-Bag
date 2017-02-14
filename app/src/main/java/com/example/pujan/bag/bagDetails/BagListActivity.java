package com.example.pujan.bag.bagDetails;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;


import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;

import com.example.pujan.bag.orderDetails.OrderDisplayActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BagListActivity extends AppCompatActivity implements BagViewAdapter.ItemClickCallback, SearchView.OnQueryTextListener {

    RecyclerView recView;
    BagViewAdapter bagViewAdapter;
    String customer_id;
    String source;
    ArrayList<BagEntity> bagData = new ArrayList<>();

    ArrayList<BagColorQuantity> colorQuantities = new ArrayList<>();


    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        if (source.equals("customer")) {
            menuInflater.inflate(R.menu.order_menu_actionbar, menu);
            menuInflater.inflate(R.menu.search_action_bar, menu);
        } else
            menuInflater.inflate(R.menu.search_action_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.searchh);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        SearchViewCompat.setInputType(searchView, InputType.TYPE_CLASS_TEXT);
        searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (source.equals("customer")) {
            switch (item.getItemId()) {
                case R.id.searchh:
                    Toast.makeText(getBaseContext(), "hello :D", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.action_cart:
                    Intent i = new Intent(this, OrderDisplayActivity.class);
                    i.putExtra("cid", customer_id.toString());
                    i.putExtra("recValue", colorQuantities);
                    startActivity(i);
                    return true;
            }
        } else {
            switch (item.getItemId()) {
                case R.id.searchh:
                    Toast.makeText(getBaseContext(), "hello :D", Toast.LENGTH_LONG).show();
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
        if (customer_id != null) {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setLogo(R.drawable.bagsmall);
            actionBar.setTitle(" Order Menu");
            actionBar.setDisplayUseLogoEnabled(true);   // These two are for
            actionBar.setDisplayShowHomeEnabled(true);  // displaying logo in the action bar
            Toast.makeText(this, "Customer id is" + customer_id, Toast.LENGTH_SHORT).show();
        } else {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();

            actionBar.setLogo(R.drawable.bagsmall);
            actionBar.setTitle(" View Bag");
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        recView = (RecyclerView) findViewById(R.id.recView);

        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(10);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        recView.setLayoutManager(lm);
        //recView.setLayoutManager(new ScrollingLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false, duration));


        String response = null;

        try {
            response = new FunctionsThread(this).execute("ViewBag").get();
            JSONObject bagJson = new JSONObject(response);
            JSONArray bagJsonArray = bagJson.getJSONArray("result");
            System.out.println(response);


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


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        bagViewAdapter = new BagViewAdapter(bagData, getIntent().getStringExtra("source"), this);

        recView.setAdapter(bagViewAdapter);
        bagViewAdapter.onItemClickCallback(this);

        bagViewAdapter.notifyDataSetChanged();


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
}
