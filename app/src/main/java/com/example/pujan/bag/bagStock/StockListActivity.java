package com.example.pujan.bag.bagStock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.example.pujan.bag.bagDetails.BagEntity;
import com.example.pujan.bag.bagDetails.BagViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class StockListActivity extends AppCompatActivity implements FunctionsThread.AsyncResponse, SearchView.OnQueryTextListener {
    RecyclerView recView;
    StockViewAdapter stockViewAdapter;

    ArrayList<BagEntity> bagData = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_action_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.searchh);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
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
        ArrayList<BagEntity> newlist=new ArrayList<>();
        for(BagEntity bag:bagData)
        {
            String name=bag.getName().toLowerCase();
            if(name.contains(newText.toLowerCase())) {
                newlist.add(bag);
            }
        }
        stockViewAdapter.setFilter(newlist);
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bag);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.bagsmall);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);   // These two are for
        actionBar.setDisplayShowHomeEnabled(true);

        recView = (RecyclerView) findViewById(R.id.recView);

        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(10);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        recView.setLayoutManager(lm);



        FunctionsThread t = new FunctionsThread(this);
        t.execute("ViewBag");
        t.trigAsyncResponse(StockListActivity.this);


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,StockDetailsActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, StockDetailsActivity.class);
                startActivity(i);


        }
        return true;
    }

    @Override
    public void onComplete(String response) {

        try {

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


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error connecting with the Server..!",Toast.LENGTH_SHORT).show();
        }


        stockViewAdapter = new StockViewAdapter(bagData, this);

        recView.setAdapter(stockViewAdapter);

        stockViewAdapter.notifyDataSetChanged();

    }
}

