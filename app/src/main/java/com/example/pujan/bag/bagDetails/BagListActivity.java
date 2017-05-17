package com.example.pujan.bag.bagDetails;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pujan.bag.ActionListActivity;
import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BagListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, FunctionsThread.AsyncResponse {

    RecyclerView recView;
    BagViewAdapter bagViewAdapter;

    ArrayList<BagEntity> bagData = new ArrayList<>();

    FloatingActionButton addNewBag;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_action_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.searchh);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchh:
                return true;
            case android.R.id.home:
                Intent i = new Intent(getBaseContext(), ActionListActivity.class);
                startActivity(i);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), ActionListActivity.class);
        startActivity(i);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bag);



        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setTitle("Bag");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recView = (RecyclerView) findViewById(R.id.recView);

        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(10);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        recView.setLayoutManager(lm);
        //recView.setLayoutManager(new ScrollingLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false, duration));

        addNewBag = (FloatingActionButton)findViewById(R.id.addNewBagBtn);

        addNewBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),AddBagActivity.class);
                startActivity(i);
            }
        });

        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy>0|dy<0)
                    addNewBag.hide();


            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                addNewBag.show();
            }
        });


        FunctionsThread t = new FunctionsThread(this);
        t.execute("ViewBag");
        t.trigAsyncResponse(this);


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
            if (name.contains(newText.toLowerCase())) {
                newbag.add(bagEntity);
            }
        }
        bagViewAdapter.setFilter(newbag);
        return false;
    }

    public void getData(String response) {
        try {

            if (response == "Error") {
                Toast.makeText(this, "Connection Error... Please check your connection !", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Error With Database Connection..!!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onComplete(String output) {
        getData(output);
        bagViewAdapter = new BagViewAdapter(bagData, "bag", getBaseContext());
        recView.setAdapter(bagViewAdapter);


    }


}
